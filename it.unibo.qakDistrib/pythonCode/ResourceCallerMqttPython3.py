##############################################################
# ResourceCallerMqttPython3.py
##############################################################
import time
import paho.mqtt.client as paho
 
brokerAddr     = "broker.hivemq.com"
topic          = "resource/input"
 
msgresourcealone   = "msg(cmd,request,python,resource,cmd(python),5)"
alarm              = "msg(alarm,event,python,none,alarm(firepython),3)" 

def sendMqtt( message ) :
    print("sendMqtt ", message)
    msg = message + "\n"
    #byt=msg.encode()    #required in Python3
    client.publish(topic, msg)

def on_message(client, userdata, message) :
    print("message received " ,str(message.payload.decode("utf-8")))
    ## print("message topic=",message.topic)
    ## print("message qos=",message.qos)
    ## print("message retain flag=",message.retain)
    

def work() :
    client.subscribe(topic, qos=0)      #subscribe before publish
    client.on_message=on_message        #attach function to callback
    ##sendMqtt( msgresourcealone )
    ##time.sleep( 2 )
    print("EMIT ALARM ..." )
    sendMqtt( alarm )
    client.loop_start()  #check for messages

def terminate() :
    client.disconnect()
    print("BYE")
#################################################   
client= paho.Client("senderPython")      
client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
work()
time.sleep( 3 )
print("BYE")
##terminate()