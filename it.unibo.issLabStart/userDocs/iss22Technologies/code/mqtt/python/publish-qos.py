#import paho.mqtt.testclient as mqtt
import testclient as mqtt
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
mqtt.Client.suppress_puback_flag=False
mqtt.Client.suppress_pubrel_flag=False
client = mqtt.Client("python1")             #create new instance 
client.on_log=on_log
client.on_connect = on_connect
client.on_disconnect = on_disconnect
client.on_publish = on_publish
client.connect(broker,port)           #establish connection
#client.loop_start()
while not client.connected_flag: #wait in loop
   client.loop()
   #print("In wait loop")
   time.sleep(1)

print("\npublishing qos of 1")
client.loop()
print("outgoing messages queued ",len(client._out_messages))
ret=client.publish("house/bulb1","Test message 0",1)    #publish
print("outgoing messages queued ",len(client._out_messages))
#print("published return=",ret)
client.loop()
print("outgoing messages queued ",len(client._out_messages))
time.sleep(2)
client.loop()
print ("\nSuppress puback")
client.suppress_puback_flag=True
ret=client.publish("house/bulb1","Test message 1",1)    #publish
#print("published return=",ret)
#ret=client.publish("house/bulb1","Test message 2",2)    #publish
#print("published return=",ret)
client.loop()
time.sleep(3)
#client.loop()
time.sleep(3)

while True:
   client.loop()
   time.sleep(1)
   count+=1
   #print("count =",count,"  flag", client.suppress_puback_flag)
   print("count =",count)
   print("outgoing messages queued ",len(client._out_messages))
   if count==3:
      print("\npublishing qos of 0")
      ret=client.publish("house/bulb0","Test message 2",0)    #publish
      print("\npublishing qos of 1")
      ret=client.publish("house/bulb0","Test message 3",1)    #publish
   if count==11:
      print("\ndisconnecting")
      client.disconnect()
   if count==13:
      print("\nRe-connecting")
      client.connect(broker,port)           #establish connection
   if count==16:
      print ("\nun-Suppress puback")
      client.suppress_puback_flag=False

   if count==30:
      break         

client.disconnect()


