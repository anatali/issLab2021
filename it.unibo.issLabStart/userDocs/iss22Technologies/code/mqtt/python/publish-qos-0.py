import paho.mqtt.client as mqtt
import time
broker="192.168.1.158"
#broker="192.168.1.186"
#broker="broker.hivemq.com"
#broker="iot.eclipse.org"
port =1883

def on_log(client, userdata, level, buf):
   print(buf) 
def on_connect(client, userdata, flags, rc):
    if rc==0:
        client.connected_flag=True #set flag
        print("connected OK")
    else:
        print("Bad connection Returned code=",rc)
        client.loop_stop()  
def on_disconnect(client, userdata, rc):
   print("client disconnected ok",rc)

def on_publish(client, userdata, mid):
    print("In on_pub callback mid= "  ,mid)
count=0
mqtt.Client.connected_flag=False#create flag in class
client = mqtt.Client("python1")             #create new instance 
client.on_log=on_log
client.on_connect = on_connect
client.on_disconnect = on_disconnect
client.on_publish = on_publish
client.connect(broker,port)           #establish connection
#client.loop_start()
while not client.connected_flag: #wait in loop
   client.loop()
   print("In wait loop")
   time.sleep(1)

print("\npublishing qos of 0")
client.loop()
ret=client.publish("house/bulb0","Test message 1",0)    #publish
print("published return=",ret)
client.loop()
time.sleep(2)
client.loop()
print("\npublishing qos of 1")
ret=client.publish("house/bulb1","Test message 2",1)    #publish
print("published return=",ret)
time.sleep(2)
client.loop()
print("\npublishing qos of 2")
ret=client.publish("house/bulb2","Test message 3",2)    #publish
print("published return=",ret)
client.loop()
time.sleep(2)
client.loop()
time.sleep(2)
client.loop()
#client.loop()
time.sleep(20)
client.disconnect()


