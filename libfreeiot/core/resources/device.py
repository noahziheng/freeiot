"""
    The RESTFul resource of device

    Author: Noah Gao
    Updated at: 2018-02-23
"""
from flask import Response, abort
from flask_restful import Resource, reqparse
from flask_jwt_simple import jwt_required
from bson import json_util, ObjectId
from libfreeiot.core import mongo
from libfreeiot.app import scope

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
            res = mongo.db.devices.insert_one(data)
        else:
            data = {}
            if not args["remark"] is None:
                data["remark"] = args["remark"]
            if not args["status"] is None:
                data["status"] = args["status"]
            if not args["version"] is None:
                data["version"] = args["version"]
            if not ObjectId.is_valid(device_id):
                return abort(400)
            if(len(data) < 1):
                return abort(400)
            mongo.db.devices.update_one({"_id": ObjectId(device_id)},{
                "$set": data
            })
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
