#!/usr/bin/env python3
"""
    Sample script for create a FreeIOT Application
"""
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter

if __name__ == '__main__':
    app.run(3000, adapters = [ MQTTAdapter() ])
