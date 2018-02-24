"""
    FreeIOT Core Package Initial Script

    Author: Noah Gao
    Updated at: 2018-02-23
"""
from flask import Flask
from flask_pymongo import PyMongo
from libfreeiot.config import CONFIG

mongo = PyMongo()

def create_app(config_name):
    """
        Function for create Flask App instance
    """
    app = Flask(__name__) # Initialize app

    # Import project config
    app.config.from_object(CONFIG[config_name])
    CONFIG[config_name].init_app(app)

    # Init MongoDB Ext
    mongo.init_app(app)

    from libfreeiot.core.routes import create_routes
    app, api = create_routes(app) # Initialize api services with Routes definition
    return app
