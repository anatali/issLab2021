##############################################################
# pathexecCaller.py
##############################################################
import socket
import time

port       = 8032
sock       = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
calledName = "pathexec"

PathTodo    = "'{\"path\":\"wwlw\" , \"owner\":\"pathexecCaller\"}'"
requestMsg  = "msg(dopath,request,python,RECEIVER,PAYLOAD,1)".replace("RECEIVER",calledName).replace("PAYLOAD",PathTodo)
alarm = "msg(alarm,event,python,none,alarm(fire),3)"

def connect(port) :
    server_address = ('localhost', port)
    sock.connect(server_address)    
    print("CONNECTED " , server_address)

def forward( message ) :
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def emit( event ) :
    print("emit ", event)
    msg = event + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def work() :
    forward( requestMsg ) 
    time.sleep(1)
 


def terminate() :
    sock.close()
    print("BYE")

###########################################    
connect(port)
work()
##terminate()  
