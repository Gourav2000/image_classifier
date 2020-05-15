import pyrebase

config={
    "apiKey": "AIzaSyC53JSNokmKEuGpbdjbp-a-4ZSSIhHedSU",
    "authDomain": "chuater-da353.firebaseapp.com",
    "databaseURL": "https://chuater-da353.firebaseio.com",
    "projectId": "chuater-da353",
    "storageBucket": "chuater-da353.appspot.com",
    "messagingSenderId": "842759559701",
    "appId": "1:842759559701:web:9c1cf756984c9970528c1c",
    "measurementId": "G-WWGQ8ZY4TS"
}

firebase=pyrebase.initialize_app(config)
storage=firebase.storage()

path_cloud="uploads/1587160233387.jpg"
storage.child(path_cloud).download("69.jpg")
