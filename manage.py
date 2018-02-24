#!/usr/bin/env python3
"""
    Sample script for create a FreeIOT Application
"""
import os
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter

if __name__ == '__main__':
    app.run(os.environ.get("PORT"), adapters = [ MQTTAdapter() ])
