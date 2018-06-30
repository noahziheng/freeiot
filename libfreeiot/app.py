"""
    Main Entry for FreeIOT

    Author: Noah Gao
    Updated at: 2018-02-23
"""

# Load environment
import os
from dotenv import load_dotenv, find_dotenv
from libfreeiot.core import create_app
from libfreeiot import version

load_dotenv(find_dotenv(usecwd=True), override=True)

print('FreeIOT Version: %s' % version.__version__)
print('Web Service: %s:%s' % (os.environ.get('APP_HOST'), os.environ.get('APP_PORT')))
print('MongoDB Service: %s:%s/%s' % (os.environ.get('MONGO_HOST'), os.environ.get('MONGO_PORT'), os.environ.get('MONGO_DBNAME')))

scope = dict()

def create_flask_app():
    """ Function for create flask application instance """
    global scope
    return create_app(os.getenv('FLASK_CONFIG') or 'default', scope)

def run(port = int(os.environ.get("APP_PORT")),
    host = os.environ.get('APP_HOST', '127.0.0.1'),
    adapters = None,
    app = None):
    """ Main Method for running application """
    global scope

    if app is None:
        app = create_flask_app() # Create Flask Application

    # 代入数据库句柄到 Adapter 作用域
    from .core import mongo
    scope["mongo"] = mongo

    if adapters is None:
        adapters = [] # Create adapter group if not provided
    for adapter in adapters:
        adapter.init(app, scope) # Initialize all adapters
        adapter.run() # Run all adapters

    app.run(debug=os.environ.get("APP_DEBUG") == "true", port=port, host=host, threaded=True) # Start API services
