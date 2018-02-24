"""
    The RESTFul resource of device

    Author: Noah Gao
    Updated at: 2018-02-23
"""
import json
from flask import Response
from flask_restful import Resource, reqparse
from flask_jwt_simple import jwt_required
from bson import json_util, ObjectId, DBRef
from libfreeiot.core import mongo

class Data(Resource):
    """
        The RESTFul resource class of data
    """

    def parse_args(self):
        """ Message Parse Method """
        parser = reqparse.RequestParser()
        parser.add_argument('device', type=str, help='ObjectId(str) of the message\'s original device')
        parser.add_argument('topic', type=str, help='The message\'s topic')
        parser.add_argument('flag', type=str, help='The message\'s flag')
        parser.add_argument('content', type=json.loads, help='The message\'s content of JSON Encode')
        args = parser.parse_args()
        args["device"] = DBRef("devices", args["device"])
        return args

    @jwt_required
    def get(self, data_id=None):
        """
            RESTFul GET Method
        """
        if data_id!=None:
            res = mongo.db.datas.find_one_or_404({'_id': ObjectId(data_id)})
        else:
            res = mongo.db.datas.find()
        return Response(
            json_util.dumps(res),
            mimetype='application/json'
        )

    @jwt_required
    def post(self):
        """
            RESTFul POST Method
        """
        args = self.parse_args()
        res = mongo.db.datas.insert_one(args)
        return Response(
            json_util.dumps(res.inserted_id),
            mimetype='application/json'
        )

    @jwt_required
    def put(self, data_id):
        """
            RESTFul PUT Method
        """
        args = self.parse_args()
        res = mongo.db.datas.update_one({"_id": ObjectId(data_id)}, {"$set": args})
        return Response(
            json_util.dumps(res.raw_result),
            mimetype='application/json'
        )

    @jwt_required
    def delete(self, data_id):
        """
            RESTFul DELETE Method
        """
        res = mongo.db.datas.delete_one({'_id': ObjectId(data_id)})
        return Response(
            json_util.dumps(res.raw_result),
            mimetype='application/json'
        )
