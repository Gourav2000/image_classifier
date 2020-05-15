from firebase import firebase

firebase=firebase.FirebaseApplication('https://chuater-da353.firebaseio.com/')
result= firebase.get('Users',None)
for i in result:
    for j in i:
        print(j[4])
        
