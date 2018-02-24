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

def run(port, host = "0.0.0.0", adapters = None, app = None):
    """ Main Method for running application """
    if app is None:
        app = create_flask_app() # Create Flask Application
    if adapters is None:
        adapters = [] # Create adapter group if not provided
    for adapter in adapters:
        adapter.run() # Run all adapters
    app.run(debug=False) # Start API services
