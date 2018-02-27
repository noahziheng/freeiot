"""
    MQTT Adapter Module
"""
import os
import json
import paho.mqtt.client as mqtt
from ..base import BaseAdapter
from .Parse import main as Parse

class MQTTAdapter(BaseAdapter):
    """ Base Adapter Class """
    client_id = os.environ.get("MQTT_CLIENTID") or "mqtt-adapter"
    server_address = os.environ.get("MQTT_HOST") or "localhost"
    server_port = int(os.environ.get("MQTT_PORT")) or 1883
    client = mqtt.Client(client_id)
    parse_driver = os.environ.get("MQTT_PARSE_DRIVER") or "json"

    def run(self):
        """ Main Entry for Adapter """
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.connect(self.server_address, self.server_port, 60) # 连接服务器（TCP）
        self.scope["mqttClient"] = self.client # 将 client 代入 Adapter 作用域
        self.client.loop_start()
        print('Hello from MQTTAdapter')

    def on_connect(self, client, userdata, flags, rc):
        """ Callback while conntected """
        print("MQTT Broker connected with result code "+str(rc))
        # Subscribing in on_connect() means that if we lose the connection and
        # reconnect then subscriptions will be renewed.
        self.client.subscribe("SYS/online")
        self.client.subscribe("SYS/will")

    # The callback for when a PUBLISH message is received from the server.
    def on_message(self, client, userdata, msg):
        """ Callback while received messageA """
        Parse.main(client, msg.topic.split('/'), self.parse_driver_select(msg.payload.decode()))

    def parse_driver_select(self, data):
        """ Select a driver to parse data """
        if self.parse_driver == 'msgpack':
            raise OSError("Parse driver 'msgpack' under development.")
        elif self.parse_driver == 'json':
            return json.loads(data)
        else:
            raise OSError("Parse driver '" + self.parse_driver + "' under development.")
