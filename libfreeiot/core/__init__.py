"""
    FreeIOT Core Package Initial Script

    Author: Noah Gao
    Updated at: 2018-02-23
"""
from flask import Flask
from flask_pymongo import PyMongo
from libfreeiot.config import CONFIG

mongo = None

def create_app(config_name, scope = None):
    """
        Function for create Flask App instance
    """
    global mongo
    if scope is None:
        scope = dict()
    app = Flask(__name__) # Initialize app

    # Import project config
    app.config.from_object(CONFIG[config_name])
    CONFIG[config_name].init_app(app)

    app.config["MONGO_URI"] = "mongodb://" + app.config["MONGO_HOST"] + ":" + str(app.config["MONGO_PORT"]) + "/" + app.config["MONGO_DBNAME"]
    print(app.config["MONGO_URI"])

    # Init MongoDB Ext
    mongo = PyMongo(app)

    from libfreeiot.core.routes import create_routes
    app, api = create_routes(app, scope) # Initialize api services with Routes definition
    return app
