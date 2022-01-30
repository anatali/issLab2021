#!python3
import paho.mqtt.client as mqtt
import time
import logging
import sys
broker="192.168.1.184"
port =1883
logging.basicConfig(level=logging.INFO)
#use DEBUG,INFO,WARNING

def on_log(client, userdata, level, buf):
   logging.info(buf) 
def on_connect(client, userdata, flags, rc):
    if rc==0:
        client.connected_flag=True #set flag
        logging.info("connected OK")
    else:
        logging.info("Bad connection Returned code="+str(rc))
        client.loop_stop()  
def on_disconnect(client, userdata, rc):
   logging.info("client disconnected ok")
def on_publish(client, userdata, mid):
    logging.info("In on_pub callback mid= "  + str(mid))
def on_subscribe(client,userdata,mid,granted_qos):
    logging.info("in on subscribe callback result "+str(mid))
    for t in topic_ack:
        if t[1]==mid:
            t[2]=1 #set acknowledged flag
            logging.info("subscription acknowledged  "+t[0])
            client.suback_flag=True
def on_message(client, userdata, message):
    topic=message.topic
    msgr=str(message.payload.decode("utf-8"))
    msgr="Message Received topic="+topic+ " message ="+msgr
    logging.info(msgr)
def check_subs():
    #returns flase if have an unacknowledged subsription
    for t in topic_ack:
        if t[2]==0:
            logging.info("subscription to "+t[0] +" not acknowledged")
            return(False)
    return True 

mqtt.Client.connected_flag=False#create flag in class

client = mqtt.Client("python-sub")             #create new instance 
client.on_log=on_log
client.on_connect = on_connect
client.on_disconnect = on_disconnect
client.on_publish = on_publish
client.on_subscribe = on_subscribe
client.on_message = on_message
topics=[("house/bulb0",2),("house/bulb1",2),("house/bulb2",2)]
topic_ack=[]
print("Connecting to broker ",broker)

try:
    client.connect(broker)      #connect to broker
except:
    print("can't connect")
    sys.exit(1)
client.loop_start()  #Start loop 
while not client.connected_flag: #wait in loop
    print("In wait loop")
    time.sleep(1)
####

print("subscribing  " + str(topics))
for t in topics:
    try:
        r=client.subscribe(t)
        if r[0]==0:
            logging.info("subscribed to topic "+str(t[0])+" return code" +str(r))
            topic_ack.append([t[0],r[1],0]) #keep track of subscription
        else:
            logging.info("error on subscribing "+str(r))
            client.loop_stop()    #Stop loop 
            sys.exit(1)
    except Exception as e:
        logging.info("error on subscribe"+str(e))
        client.loop_stop()    #Stop loop 
        sys.exit(1)
print("waiting for all subs")
while not check_subs():
    time.sleep(1)
#####

time.sleep(60)
client.loop_stop()    #Stop loop 
client.disconnect() # disconnect



