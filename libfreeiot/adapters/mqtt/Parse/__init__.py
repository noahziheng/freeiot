"""
    FreeIOT Parse Package Initial Script

    Author: Noah Gao
    Updated at: 2018-02-23
"""
import os
from pymongo import MongoClient

mongo = MongoClient(host=os.environ.get("MONGO_HOST") or "localhost", port=int(os.environ.get("MONGO_PORT")) or 27017)
mongo.db = mongo[os.environ.get("MONGO_DBNAME") or "test"]
