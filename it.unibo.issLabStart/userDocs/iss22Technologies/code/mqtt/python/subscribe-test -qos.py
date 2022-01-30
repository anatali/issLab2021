import paho.mqtt.client as paho
import time
broker="192.168.1.186"
port=1883

def on_subscribe(client, userdata, mid, granted_qos):   #create function for callback
   print("subscribed with qos",granted_qos," ",mid, "\n")
   pass
def on_message(client, userdata, msg):
    topic=msg.topic
    m_decode=str(msg.payload.decode("utf-8","ignore"))
    print(m_decode,topic)
def on_disconnect(client, userdata, rc):
   print("client disconnected ok")
def on_connect(client, userdata, flags, rc):
    m="Connected flags"+str(flags)+"result code "\
    +str(rc)
    print(m)
topic1 ="house/#"
topic2 =("house/bulb2",1)
topic3 =[("house/bulb3",2),("house/bulb4",1),("house/bulb5",0)]
client= paho.Client("Python5",False)       #create client object
client.on_subscribe = on_subscribe   #assign function to callback
client.on_disconnect = on_disconnect #assign function to callback
client.on_connect = on_connect #assign function to callback
client.on_message = on_message #assign function to callback
client.connect(broker,port,keepalive=30)           #establish connection
time.sleep(1)
client.loop_start()
time.sleep(2)
print("Subscribing to topics ",topic1)
r=client.subscribe(topic1,2)    #subscribe single topic
print ("subsribed return = ",r)
count=0
while True:
   time.sleep(1)
   count+=1
   if count==90:
      break
"""
print ("subsribed return = ",r)
time.sleep(6)
print("Subscribing to several topics ",topic3)
r=client.subscribe(topic3)    #subscribe several topic
print ("subscribed return = ",r)
time.sleep(6)
"""
client.disconnect()
client.loop_stop()
