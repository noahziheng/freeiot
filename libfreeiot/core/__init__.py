"""
    FreeIOT Core Package Initial Script

    Author: Noah Gao
    Updated at: 2018-02-23
"""
from flask import Flask
from libfreeiot.config import config
from libfreeiot.core.routes import create_routes

def create_app(config_name):
    """
        Function for create Flask App instance
    """
    app = Flask(__name__,
        static_folder = config[config_name].TEMPLATE_DIR + "/static",
        template_folder = config[config_name].TEMPLATE_DIR) # Initialize app

    # Import project config
    app.config.from_object(config[config_name])
    config[config_name].init_app(app)

    app, api = create_routes(app) # Initialize api services with Routes definition

    return app
