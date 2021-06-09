
##############################################################
# clientToWenv.py
# sendToVirtualRobot : sends a command in output
# read               : acquires data from input
##############################################################
import socket
import time
import json

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
virtualRobotPort = 8999
sep = ';'   #required by the Soffritti virtual robot 'language'

goForwardMsg  = '{"type": "moveForward",   "arg": -1  }'
goBackwardMsg = '{"type": "moveBackward",  "arg": -1  }'
turnLeftMsg   = '{"type": "turnLeft",      "arg": 400 }'
turnRightMsg  = '{"type": "turnRight",     "arg": 400 }'
haltMsg       = '{"type": "alarm",         "arg": 0   }'

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED WITH the virtual robot" , server_address)

def sendToVirtualRobot( message ) :
    print("forward ", message)
    msgJson = json.loads( message )
    msg     = sep+ json.dumps(msgJson)+ sep 
    byt=msg.encode()    #required in Python3
    sock.send( byt )

def work() :
    sendToVirtualRobot( goForwardMsg ) 
    time.sleep(1)
    sendToVirtualRobot( haltMsg ) 

def read() :
    BUFFER_SIZE = 1024
    data = sock.recv(BUFFER_SIZE)
    print( "received data:", data )

def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(virtualRobotPort)
work()
read()
terminate()
