# Generates id for VIPs and guider
class IDGenerator:
    def __init__(self): 
        self.idGen = 0
        
    # Generates id 
    # Return: new id
    def generateID(self): 
        self.idGen += 1
        return self.idGen