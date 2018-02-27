#!/usr/bin/env python3
"""
    Sample script for create a FreeIOT Application
"""
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter
from libfreeiot.adapters.file import FileAdapter

if __name__ == '__main__':
    app.run(adapters = [ MQTTAdapter(), FileAdapter() ])
