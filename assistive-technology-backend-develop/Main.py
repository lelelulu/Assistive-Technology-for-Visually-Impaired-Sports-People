from flask import Flask, request, jsonify
from objects.Session import Session
from objects.Guide import Guide
from objects.VIP import VIP
from objects.IDGenerator import IDGenerator
app = Flask(__name__)

sessions = {}
idGen = IDGenerator()


@app.route('/api/vip/joinSession', methods=["POST"])
def joinSession():
    content = request.json
    longitude = content['longitude']
    latitude = content['latitude']
    sessionId = content['sessionId']
    name = content['name']
    existedSession = sessions[sessionId]
    vip_id = existedSession.addVip(name, latitude, longitude)
    print("Recieved json:",latitude,longitude)
    return jsonify({
        'success':True,
        'vipId': vip_id
    })


@app.route('/users/<int:id>/location/', methods=["GET"])
def get_user_location(id):
    content = request.json
    longitude = 0.0
    latitude = 0.0
    return jsonify({
        'longitude': longitude,
        'latitude': latitude,
    })

@app.route('/updateLocation/', methods=["POST"])
def updateLocation():
    content = request.json
    longitude = content['longitude']
    latitude = content['latitude']
    print("Recieved json:",latitude,longitude)
    return jsonify({
        'success':True
    })

''' 
the id in the method parameter means the session id
according to the session id, we can find all the VIP & 
Guide stored in session, then get everyone's locations
return a list of list
[
    [id1, name1, latitude1, longitude1],
    [id2, name2, latitude2, longitude2],
    .....
]
'''

@app.route('/getAllLocation/', methods=["GET"])
def get_all_location():
    context = request.json
    location_list = []
    initial_list = [0]*4
    current_session = sessions[context["id"]]

    '''get the guide location'''
    for guideId in current_session.guides:
        Guide = current_session.guides[guideId]
        initial_list[0] = Guide.GUIDEID  
        initial_list[1] = Guide.name
        initial_list[2] = Guide.latitude
        initial_list[3] = Guide.longitude  
        location_list.append(initial_list)

    '''get the VIP location'''
    for vipId in current_session.vips:
        VIP = current_session.vips[vipId]
        initial_list[0] = VIP.VIPID 
        initial_list[1] = VIP.name
        initial_list[2] = VIP.latitude
        initial_list[3] = VIP.longitude 
        location_list.append(initial_list)
    
    return jsonify({
        'list of locaton': location_list
    })


@app.route("/api/guide/createSession", methods=["POST"])
def guideCreateSession():
    try:
        content = request.json

        guideName = content["name"]
        guideLat = content["latitude"]
        guideLong = content["longitude"]

        newSessionId = idGen.generateID()
        newSession = Session(newSessionId)
        sessions[newSessionId] = newSession

        guideId = newSession.addGuide(guideName, guideLat, guideLong)
        return jsonify({
            "guideId":guideId,
            "sessionId": newSessionId
        })

    except Exception as e:
        print(e)
        return jsonify({
            'success':False
        })

    


if __name__ == '__main__':
    app.run()
