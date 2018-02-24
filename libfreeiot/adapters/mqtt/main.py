"""
    MQTT Adapter Module
"""
import json
import paho.mqtt.client as mqtt
from ..base import BaseAdapter
from .Parse import main as Parse

class MQTTAdapter(BaseAdapter):
    """ Base Adapter Class """
    client_id = "mqtt-adapter"
    server_address = "localhost"
    client = mqtt.Client(client_id)

    def run(self):
        """ Main Entry for Adapter """
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.connect(self.server_address, 1883, 60) # 连接服务器（TCP）
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
        """ Callback while received message """
        Parse.main(client, msg.topic.split('/'), json.loads(msg.payload.decode()))
