##############################################################
# basicrobotconsole.py
##############################################################
import paho.mqtt.client as paho

### brokerAddr ="broker.hivemq.com"
brokerAddr ="localhost"

#define callback
def on_message(client, userdata, message):
    print("received message =",str(message.payload.decode("utf-8")))

def requestStep(  ):
    msg = "msg(step,request,py,basicrobot,step(350),1)"
    ##print("requestMsg  requestMsg= :" , msg  )
    client.publish("unibo/qak/basicrobot",msg) 

def forwardMsg( move ):
    msg = "msg(cmd,dispatch,py,basicrobot,cmd("+str(move)+"),1)"
    print("forwardMsg  forwardMsg= :" , msg  )
    client.publish("unibo/qak/basicrobot",msg) 

def console() :  
    print("console  STARTS :"   )
    cmd =  str( input() )
    while( cmd != "q"  ) :
        #print( cmd )
        if( cmd == "p" ) :
            requestStep()
        else :
            forwardMsg( cmd )
        cmd = str(input() )
##################################################################

client= paho.Client("basicrobotconsole")  
client.on_message=on_message            # Bind function to callback    

client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)

print("subscribing to unibo/polar")
client.subscribe("unibo/polar")      #subscribe

print("starting the client loop to receive sonar data" )
client.loop_start()                  #start loop to process received messages
 
console() 