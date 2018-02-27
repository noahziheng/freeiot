"""
    File Adapter Module
"""
import time
import uuid
from flask import request, jsonify
from ..base import BaseAdapter

class FileAdapter(BaseAdapter):
    """ File Adapter Class """
    def create_routes(self, app, mongo):
        """ Routes Creator """
        @app.route('/api/upload', methods=['POST'])
        def save_upload():
            """ File Upload POST Route """
            filename = uuid.uuid3(uuid.NAMESPACE_DNS, request.files['file'].filename.split('.')[0] + str(time.time())).hex + "." + request.files['file'].filename.split('.')[1]
            mongo.save_file(filename, request.files['file'], base = "datas")
            return jsonify(filename)

        @app.route('/api/upload/<path:filename>')
        def get_upload(filename):
            """ File Upload GET Route """
            response = mongo.send_file(filename, base = "datas")
            response.cache_control.public = False
            response.cache_control.max_age = 0
            response.cache_control.no_cache = True
            return response

    def run(self):
        """ Main Entry for Adapter """
        self.create_routes(self.app, self.scope["mongo"])
        print('Hello from FileAdapter')
