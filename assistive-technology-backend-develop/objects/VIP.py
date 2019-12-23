# Assistive Tech Group 2019
# 
# This file includes VIP data structure.

class VIP:
    # Constructs a VIP 
    # Input: id, name, latitude and longitude
    def __init__(self,VIPID, name, latitude, longitude):  
        self.VIPID = VIPID
        self.latitude = latitude
        self.longitude = longitude
        self.name = name
    
    # Updates location
    # Input: latitude and longitude
    def updateLocation(self, latitude, longitude): 
        self.latitude = latitude
        self.longitude = longitude
