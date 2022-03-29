import socket
import time
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("-H", "--host", help="raspi host address", default="localhost", required=False)
parser.add_argument("-P", "--port", help="raspi led port", default=8040, type=int, required=False)
parser.add_argument("--udp", help="use udp", action="store_true")
args = parser.parse_args()

hostAdress = args.host
port   = args.port
ledOn  = "on"
ledOff = "off"
msg = ''

sock = None
if args.udp:
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
else: #default: TCP
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def connect(port) :
    server_address = (hostAdress, port)
    sock.connect(server_address)    
    print("CONNECTED WITH ", server_address)

def terminate() :
    sock.close()    #qak infrastr receives a msg null
    print("BYE")

def forward( message ) :
    print("forward ", message)
    msg = message + "\n" 		##### \n solo per TCP, by AN
    sock.send(msg.encode())

def console() :  
    print("console  STARTS :"   )
    cmd =  str( input() ).strip()
    print("console  D= :" , cmd  )
    while( cmd != "q"  ) :
        msg = cmd
        print( msg )
        forward( msg )
        cmd =  str(input())
     
##################################################
print("STARTING ... ")
connect(port)
console()

### USAGE: 
### 	python LedUsageUdpLenzi.py -h
### 	python LedUsageUdpLenzi.py -H localhost -P 8015 --udp