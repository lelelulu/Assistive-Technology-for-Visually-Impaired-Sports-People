#Assistive Tech Group 2019
# __init__(self, id, lat, lon)
# updateLocation(self,latitude, longitude)



class Guide:
    def __init__(self, GUIDEID, name, latitude, longitude):# Guide(GUIDEID, latitude, longitude)
        self.GUIDEID = GUIDEID
        self.latitude = latitude
        self.longitude = longitude
        self.name = name
    
    def updateLocation(self,latitude,longitude):
        self.latitude = latitude
        self.longitude = longitude
