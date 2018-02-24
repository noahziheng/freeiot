"""
    FreeIOT Parse Package Initial Script

    Author: Noah Gao
    Updated at: 2018-02-23
"""
import os
from pymongo import MongoClient

mongo = MongoClient()
mongo.db = mongo[os.environ.get("MONGO_DBNAME") or "test"]
