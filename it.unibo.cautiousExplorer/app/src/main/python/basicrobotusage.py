##############################################################
# basicrobotusage.py
# sendDispatch       : sends a command in output
# read               : acquires data from input
##############################################################
import socket
import time

basicRobotPort = 8020
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

goForwardMsg  = "msg(cmd,dispatch,python,basicrobot,cmd(w),1)"
goBackwardMsg = "msg(cmd,dispatch,python,basicrobot,cmd(s),1)" 
turnLeftMsg   = "msg(cmd,dispatch,python,basicrobot,cmd(a),1)"  
turnRightMsg  = "msg(cmd,dispatch,python,basicrobot,cmd(d),1)"  
haltMsg       = "msg(cmd,dispatch,python,basicrobot,cmd(h),1)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED ... WITH basicrobot" , server_address)

def sendDispatch( message ) :
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    sendDispatch( goForwardMsg ) 
    time.sleep(1)
    sendDispatch( haltMsg )

def read() :
    BUFFER_SIZE = 1024
    data = sock.recv(BUFFER_SIZE)
    print( "received data:", data )

def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(basicRobotPort)
work()
##read()
terminate()  
