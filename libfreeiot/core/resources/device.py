"""
    The RESTFul resource of device

    Author: Noah Gao
    Updated at: 2018-02-23
"""
from flask import Response
from flask_restful import Resource, reqparse
from flask_jwt_simple import jwt_required
from bson import json_util, ObjectId
from libfreeiot.core import mongo

class Device(Resource):
    """
        The RESTFul resource class of device
    """
    @jwt_required
    def get(self, device_id=None):
        """
            RESTFul GET Method
        """
        if device_id!=None:
            res = mongo.db.devices.find_one_or_404({'_id': ObjectId(device_id)})
        else:
            res = mongo.db.devices.find()
        return Response(
            json_util.dumps(res),
            mimetype='application/json'
        )

    @jwt_required
    def post(self, device_id=None):
        """
            RESTFul POST Method
        """
        parser = reqparse.RequestParser()
        parser.add_argument('remark', type=str, help='Remark of the device')
        parser.add_argument('status', type=int, help='Status of the device')
        parser.add_argument('version', type=str, help='Version of the device')
        args = parser.parse_args()
        if device_id is None:
            data = args
            data['_id'] = ObjectId()
        else:
            data["remark"] = args["remark"]
            data["status"] = args["status"]
            data["version"] = args["version"]
        res = mongo.db.devices.save(data)
        return Response(
            json_util.dumps(data),
            mimetype='application/json'
        )

    @jwt_required
    def delete(self, device_id):
        """
            RESTFul DELETE Method
        """
        res = mongo.db.devices.delete_one({'_id': ObjectId(device_id)})
        return Response(
            json_util.dumps(res.raw_result),
            mimetype='application/json'
        )
