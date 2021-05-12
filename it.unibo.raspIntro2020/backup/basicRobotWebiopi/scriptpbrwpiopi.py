import webiopi
import datetime
import time
import socket

GPIO = webiopi.GPIO

LIGHT = 17       # GPIO pin using BCM numbering

robotName      = "basicrobot"  
hostAdress     = 'localhost'
basicRobotPort = 8018  

msgTemplate   = "msg(cmd,dispatch,python,"+ robotName +",cmd(CMDVAL),1)"
eventTemplate = "msg(userCmd,event,python,none,userCmd(CMDVAL),1)"
sock          = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
topic         = "unibo/qak/events" 
 
# setup function is automatically called at WebIOPi startup
def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(LIGHT, GPIO.OUT)
    # retrieve current datetime
    now = datetime.datetime.now()
    print("			script0 | SETUP at:" + str(now) )       
    state = True
    for x in range(0, 6):   
	    GPIO.digitalWrite(LIGHT, state)
	    state = not state
	    # gives CPU some time before looping again
	    webiopi.sleep(0.5)
    print("CONNECTING ... ")
    connect(basicRobotPort)
    sock.settimeout(60000)

'''
# Looped by WebIOPi
def loop():
	# Toggle LED each 5 seconds
	webiopi.sleep(5)        
	value = not GPIO.digitalRead(LIGHT)
	GPIO.digitalWrite(LIGHT, value)
'''

# A macro which says hello
@webiopi.macro
def HelloWorld(first, last):
	webiopi.debug("HelloWorld(%s, %s)" % (first, last))
	return "Hello %s %s !!!!" % (first, last)

# A macro without args which return nothing
@webiopi.macro
def PrintTime():
	webiopi.debug("PrintTime: " + time.asctime())


# destroy function is called at WebIOPi shutdown
def destroy():
    GPIO.digitalWrite(LIGHT, GPIO.LOW)

# ==================================================================
# Towards custom commands
# ==================================================================
@webiopi.macro
def BasicCommand( cmd ):
	#webiopi.debug("BasicCommand(%s )" % ( cmd ))
	doCmd( cmd )
	return "Cmd %s done " % ( cmd  )
 	
#-------------------------------------------------------------------
def connect(port) :
    server_address = (hostAdress, port)
    sock.connect(server_address)    
    print("CONNECTED WITH ", server_address)

def terminate() :
    sock.close()    #qak infrastr receives a msg null
    print("BYE")

def read() :
    BUFFER_SIZE = 1024
    data = sock.recv(BUFFER_SIZE)
    print( "received data:", data )

def forward( cmd ) :
    message = msgTemplate.replace("CMDVAL", cmd)
    print("forward ", message)
    msg = message + "\n"
    byt=msg.encode()    #required in Python3
    sock.send(byt)

def emit( cmd ) :
	message = eventTemplate.replace("CMDVAL", cmd)
	print("emit ", message)
	msg = message + "\n"
	byt=msg.encode()    #required in Python3
	sock.send(byt)
    
def doCmd( cmd ) :  
	print("doCmd  cmd=" ,  cmd   )
	forward( cmd )
	#emit( cmd )
    