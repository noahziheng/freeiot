"""
    Main Entry for FreeIOT

    Author: Noah Gao
    Updated at: 2018-02-23
"""
import os
from libfreeiot.core import create_app

def create_flask_app():
    """ Function for create flask application instance """
    return create_app(os.getenv('FLASK_CONFIG') or 'default')

def run(port = int(os.environ.get("PORT")), host = "0.0.0.0", adapters = None, app = None):
    """ Main Method for running application """
    if app is None:
        app = create_flask_app() # Create Flask Application
    scope = dict()

    # 代入数据库句柄到 Adapter 作用域
    from .core import mongo
    scope["mongo"] = mongo

    if adapters is None:
        adapters = [] # Create adapter group if not provided
    for adapter in adapters:
        adapter.init(app, scope) # Initialize all adapters
        adapter.run() # Run all adapters

    app.run(debug=os.environ.get("DEBUG") == "true", port=port) # Start API services
