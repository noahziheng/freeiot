/* 
 FreeIOT Agent ESP8266 Firmware
 This firmware suport FreeIOT Sevices
 A Open-source IOT Platform
 */
#include "FS.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ESP8266httpUpdate.h>   // Ota

const char* Version_num = "1.0";
const char* Version_tag = "preview";
const char* Version_build = "20170123A2TJ";

boolean initF = false;
boolean product_initF = false;
boolean device_initF = false;
boolean ap_initF = false;
boolean wifi_initF = false;
boolean wifid_initF = false;

boolean socket_flag = false;
boolean mqtt_flag = false;

String product[2];
String device[2];

String S_SSID;
String S_PASSWORD;
char C_SSID[20];
char C_PASSWORD[50];
char AP_SSID[20];
char AP_PASSWORD[50];

WiFiServer server(3000);
WiFiClient wclient;

String apiurl = "api.iot.noahgao.net";
PubSubClient mclient(wclient, apiurl);

// Init Function
void setup() {
  // Serial Init and Welcome Msg
  Serial.begin(115200);
  Serial.println("");
  Serial.println("FreeIOT Firmware ESP8266 by Noah Gao");
  Serial.print("Version: V");
  Serial.print(Version_num);
  Serial.print(" ");
  Serial.println(Version_tag);
  Serial.print("Build: ");
  Serial.print(Version_build);
  Serial.println("");
  Serial.print("Chip Id:");
  Serial.println(ESP.getChipId());
  Serial.print("Power VIN:");
  Serial.print(ESP.getVcc() /10000);
  Serial.print(".");
  Serial.print(ESP.getVcc() / 100 %100);
  Serial.println("V");
  Serial.print("Free Heap:");
  Serial.println(ESP.getFreeHeap());

  SPIFFS.begin();
  loadConfig();
  Serial.print("Device Id:");
  Serial.println(device[0]);
  Serial.print("Device Secret:");
  Serial.println(device[1]);
  
  // Wifi/AP Init
  WiFi.mode(WIFI_AP_STA);

}

void loop() {
  if (!ap_initF && product_initF) handleAPInit();
  if (!wifi_initF && wifid_initF) handleWIFIInit();
  if (!initF && device_initF) handleMQTTInit();

  handleTCPServer();
  
  // Serial Data parse
  String get = Serial.readStringUntil(';');
  get = strParse(get);
  String tag = get.substring(0,1);
  if(mqtt_flag && tag != "P" && tag != "D" && tag != "W") mclient.publish("uplink", get);
  if (mclient.connected()) mclient.loop();
}

void handleTCPServer () {
  WiFiClient client = server.available(); 
  if (client)
  {
    while (client.connected())
    {
      if (client.available())
      {
        if (!socket_flag) {
          client.print("V:");
          client.print(Version_num);
          client.print(",");
          client.print(Version_tag);
          client.print(",");
          client.print(Version_build);
          client.print(",");
          if(ESP.getFreeHeap() <= 307200) client.print("0");
          else client.print("1");
          client.print(";");
          client.print("P:");
          client.print(product[0]);
          client.print(",");
          client.print(product[1]);
          client.print(";");
          socket_flag = true;
        }
        if (initF && !socket_flag) Serial.print("+K;");
        String line = client.readStringUntil(';');
        line = strParse(line);
        String tag = line.substring(0,1);
        if (line.length() >= 1)
        {
          if (tag == "I") socket_flag = false;
          else if (tag == "W") client.print("+C;");
          else if (tag == "D") client.print("+C;");
          else if (tag == "U") client.print("+C;");
          else {
            if (initF) Serial.print("+O;");
            break;
          }
        }
      }
    }
    delay(1);
    client.stop();
  }
}

void handleAPInit() {
  String t_ap = "FIOT_" + product[0];
  t_ap.toCharArray(AP_SSID, 20);
  t_ap = product[1];
  t_ap.toCharArray(AP_PASSWORD, 50);
  WiFi.softAP(AP_SSID, AP_PASSWORD);
  server.begin();
  ap_initF = true;
}

void handleWIFIInit() {
  Serial.println(C_SSID);
  Serial.println(C_PASSWORD);
  if(WiFi.isConnected()) WiFi.disconnect();
  WiFi.begin(C_SSID, C_PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  wifi_initF = true;
}

void handleMQTTCallback(const MQTT::Publish& pub) {
  Serial.print("+");
  if (pub.has_stream()) {
    uint8_t buf[100];
    int read;
    while (read = pub.payload_stream()->read(buf, 100)) {
      Serial.write(buf, read);
    }
    pub.payload_stream()->stop();
  } else
    Serial.print(pub.payload_string());
  Serial.print(";");
}

void handleMQTTInit() {
  if (WiFi.status() == WL_CONNECTED) {
    MQTT::Connect con("iot/" + device[0] + "/");
    con.set_will("logout", device[0] + "/" + device[1]);
    if (mclient.connect(con)) {
      mclient.subscribe(device[0] + "-d");
      Serial.println(device[0] + "-d");
      mclient.set_callback(handleMQTTCallback);
      initF = true;
      mqtt_flag = true;
      Serial.print("+O;");
    }
  }
}

String strParse (String get) {
  String tag;
  if (get.indexOf('+') != -1) {
    get = get.substring(get.indexOf('+') + 1);
    tag = get.substring(0,1);
    if (tag == "P") {
      product[0] = get.substring(2, get.indexOf(','));
      product[1] = get.substring(get.indexOf(',') + 1);
      product_initF = true;
    } else if (tag == "D") {
      device[0] = get.substring(2, get.indexOf(','));
      device[1] = get.substring(get.indexOf(',') + 1);
      saveConfig();
      device_initF = true;
    } else if (tag == "W") {
      S_SSID = get.substring(2, get.indexOf(','));
      S_PASSWORD = get.substring(get.indexOf(',') + 1);
      S_SSID.toCharArray(C_SSID, 20);
      S_PASSWORD.toCharArray(C_PASSWORD, 50);
      wifi_initF = false;
      wifid_initF = true;
    } else if (tag == "U") {
      String name = get.substring(2);
      Serial.println("Updateing...Version: " + name);
      t_httpUpdate_return ret = ESPhttpUpdate.update("http://ota.iot.noahgao.net:5000/firmware/esp8266/" + name + ".bin");
      switch(ret) {
        case HTTP_UPDATE_FAILED:
          Serial.printf("HTTP_UPDATE_FAILD Error (%d): %s", ESPhttpUpdate.getLastError(), ESPhttpUpdate.getLastErrorString().c_str());
          break;
        case HTTP_UPDATE_NO_UPDATES:
          Serial.println("HTTP_UPDATE_NO_UPDATES");
          break;
        case HTTP_UPDATE_OK:
          Serial.println("HTTP_UPDATE_OK");
          break;
      }
      Serial.println("Updated!Ready to restart!");
      ESP.restart();
    }
  }
  return get;
}

bool loadConfig() {
  File configFile = SPIFFS.open("/config.txt", "r");
  if (!configFile) {
    return false;
  }
  size_t size = configFile.size();
  if (size > 1024) {
    return false;
  }
  String config_t = configFile.readString();
  if (config_t.indexOf(',') != -1) {
    device[0] = config_t.substring(0, config_t.indexOf(','));
    device[1] = config_t.substring(config_t.indexOf(',') + 1);
    device_initF = true ;
  }
  return true;
}

bool saveConfig() {
  String config_t = device[0] + "," + device[1];
  if (SPIFFS.exists("/config.txt")) SPIFFS.remove("/config.txt");
  File configFile = SPIFFS.open("/config.txt", "w");
  if (!configFile) {
    return false;
  }
  configFile.print(config_t);
  return true;
}

