# -*- coding: utf-8 -*-
"""
Created on Sat May  2 09:50:45 2020

@author: goura
"""

import pyrebase

config={
   "apiKey": "AIzaSyDD8KVx-8SWl1C3ZVkn_sqSkOqLxrdd_LA",
    "authDomain": "test-423ed.firebaseapp.com",
    "databaseURL": "https://test-423ed.firebaseio.com",
    "projectId": "test-423ed",
    "storageBucket": "test-423ed.appspot.com",
    "messagingSenderId": "226218019533",
    "appId": "1:226218019533:web:174573d452b4cf63433784",
    "measurementId": "G-6Q60N72YK1"
}

firebase=pyrebase.initialize_app(config)
db = firebase.database()
string="C8W9K2ZZK9"
string2="dog"
db.child("requests").child(string).update({"result": string2})