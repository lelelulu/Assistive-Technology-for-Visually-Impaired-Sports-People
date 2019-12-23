#Assistive Tech Group 2019
# 
# __init__(self, id)
# updateLocation(self, id, latitude)
# addGuide(self, id, lat, long)
# removeGuide(self, id)
# addVip(self, id, lat, long)
# removeVip(self, id)
# addPath(self, path)
from objects.VIP import VIP
from objects.Guide import Guide
from objects.IDGenerator import IDGenerator


class Session:
    def __init__(self, id):
        self.id = id
        self.path = None
        self.guides = {}
        self.vips = {}
        self.idGen = IDGenerator()

    # Updates location for VIP and guide
    # Input:  id, latitude and longitutde
    # Return: true if updating successful
    #         false if updating fail
    def updateLocation(self, id, latitude, longitude):
        if id in self.vips:
            self.vips[id].updateLocation(latitude, longitude)
            return True
            
        elif id in self.guides:
            self.guides[id].updateLocation(latitude, longitude)
            return True
        else:
            return False
            
    # Adds a guide 
    # Input: name, latitude, langitude
    # Return: guide id
    def addGuide(self, name, latitude, longitude):
        i = self.idGen.generateID()
        g = Guide(i, name, latitude,longitude)
        self.guides[i] = g
        return i

    # Removes a guide 
    # Input: id
    # Return: true if removing successful
    #         false if removing fail
    def removeGuide(self, id):
        try:
            del self.guides[id]
            return True

        except Exception:
            return False
    
    # Adds a vip 
    # Input: name, latitude, langitude
    # Return: vip id
    def addVip(self, name, latitude, longitude):
        i = self.idGen.generateID()
        v = VIP(i, name, latitude,longitude)
        self.vips[i] = v
        return i

    # Removes a vip 
    # Input: id
    # Return: true if removing successful
    #         false if removing fail
    def removeVip(self, id):
        try:
            del self.vips[id]
            return True

        except Exception:
            return False
            
    # Adds a path 
    # Input: path
    # Return: true
    def addPath(self, path):
        self.path = path
        return True
