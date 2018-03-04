"""
Message Parse Module SYS topic
Author: Noah Gao
Updated at: 2018-2-2
"""
import os
import json
from bson import ObjectId
from .. import Constant
from . import mongo

def online(client, device):
    """ Register Method of device online """
    d_id = ObjectId(dict(device).get("id"))
    mongo.db.devices.update_one({"_id": d_id},{
        "$set": {
            "status":  Constant.Status.STATUS_WAIT,
            "version": dict(device).get("version")
        }
    })
    client.publish(dict(device).get("id") + "/status/d", Constant.Status.STATUS_WAIT)
    if "TOPICS_NEED" in os.environ:
        topics_need = json.loads(os.environ.get("TOPICS_NEED"))
        for item in topics_need:
            client.subscribe(dict(device).get("id") + "/" + item + "/u")
            client.subscribe(dict(device).get("id") + "/" + item + "/d")
            print("Auto subscribing", dict(device).get("id") + "/" + item + "/(u|d)")
    print(dict(device).get("id") + " Online")

def offline(client, device):
    """ Register Method of device offline """
    d_id = dict(device).get("id")
    mongo.db.devices.update_one(
        {"_id": ObjectId(d_id)},
        { "$set": { "status":  Constant.Status.STATUS_UNKNOWN }
        })
    client.publish(dict(device).get("id") + "/status/d", Constant.Status.STATUS_UNKNOWN)
    if "TOPICS_NEED" in os.environ:
        topics_need = json.loads(os.environ.get("TOPICS_NEED"))
        print(topics_need)
        for item in topics_need:
            client.unsubscribe(dict(device).get("id") + "/" + item + "/u")
            client.unsubscribe(dict(device).get("id") + "/" + item + "/d")
    print(dict(device).get("id") + " Offline")
