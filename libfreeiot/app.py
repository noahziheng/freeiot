"""
    Main Entry for FreeIOT

    Author: Noah Gao
    Updated at: 2018-02-23
"""
import os
from dotenv import load_dotenv, find_dotenv
from libfreeiot.core import create_app

load_dotenv(find_dotenv(), override=True)

def run(port, host = "0.0.0.0", adapters = None):
    """
        Main Method for running application
    """
    if adapters is None:
        adapters = []
    app = create_app(os.getenv('FLASK_CONFIG') or 'default')
    for adapter in adapters:
        adapter.run()
    app.run(debug=False) # Start API services
