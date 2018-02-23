"""
Routes Module
Author: Noah Gao
Updated at: 2018-2-2
"""
import os
import datetime
from flask import request, jsonify
from flask_restful import Api
from flask_jwt_simple import JWTManager, create_jwt
from libfreeiot.core.resources.device import Device
from libfreeiot.core.resources.image import Image

JWT_EXPIRES = 7 * 24 * 3600

def create_routes(app):
    '''
      Function for create routes
    '''
    app.config['JWT_SECRET_KEY'] = 'super-secret'  # Change this!
    app.config['JWT_EXPIRES'] = datetime.timedelta(7)
    app.config['UPLOAD_FOLDER'] = os.path.join(os.getcwd(), '/images')
    app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024
    jwt = JWTManager(app)

    @app.route('/')
    def catch_all():
        '''
          Index Route
        '''
        return jsonify({"msg": "Hello World!"})

    @app.route('/api/auth', methods=['POST'])
    def auth():
        '''
          JWT Auth Route
        '''
        username = request.json.get('username', None)
        password = request.json.get('password', None)
        if not username:
            return jsonify({"msg": "Missing username parameter"}), 400
        if not password:
            return jsonify({"msg": "Missing password parameter"}), 400
        if username != 'admin' or password != 'admin':
            return jsonify({"msg": "Bad username or password"}), 401

        return jsonify({'jwt': create_jwt(identity=username)}), 200

    # RESTFul API Routes definition
    api = Api(app)
    api.add_resource(Image, '/images/<string:device_id>/<string:image_id>.<string:image_type>', '/api/image/<string:device_id>/<int:camera_id>/<int:position>')
    api.add_resource(Device, '/api/device', '/api/device/<string:device_id>')

    return (app, api)
