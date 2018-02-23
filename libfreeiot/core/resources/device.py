from flask import request, abort
from flask_restful import Resource, reqparse, fields, marshal
import random
import string
from flask_jwt_simple import jwt_required
from api.datas import r
import json

class Device(Resource):
    @jwt_required
    def get(self, device_id=None):
      if(device_id!=None):
        res = r.hgetall('device-' + device_id)
        return abort(404) if res==None else res
      list = []
      for item in r.keys('device-*'):
        list.append(r.hgetall(item))
      return list

    @jwt_required
    def post(self, device_id=None):
      parser = reqparse.RequestParser()
      parser.add_argument('remark', type=str, help='Remark of the device')
      parser.add_argument('map', type=str, help='Running map of the device', location='json')
      args = parser.parse_args()
      if(device_id==None):
        device_id = ''.join(random.choice(string.ascii_lowercase + string.digits) for _ in range(7))
        data = args
        data['id'] = device_id
      else:
        data = r.hgetall('device-' + device_id)
        data["remark"] = args["remark"]
        data["map"] = args["map"]
      r.hmset('device-' + device_id, data)
      return data