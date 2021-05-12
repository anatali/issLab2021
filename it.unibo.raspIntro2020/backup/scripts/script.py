# imports
import webiopi
import time
from webiopi import deviceInstance
#L298N port define
ena = 8
enb = 13
in1 = 9
in2 = 10
in3 = 11
in4 = 12
# Enable debug output
webiopi.setDebug()
#instantiate device
pca = webiopi.deviceInstance("pwm0")
pca.pwmCount()
max = pca.pwmMaximum()
speed = 3095

#set the speed of two motors
def set_speed(motorspeed):
    pca.pwmWrite(ena,motorspeed)

    pca.pwmWrite(enb,motorspeed)
#robot go ahead
@webiopi.macro
def go_forward():
        set_speed(speed)
	
        pca.pwmWrite(in1,max) #IN1
        pca.pwmWrite(in2,0)   #IN2
		
        pca.pwmWrite(in3,max) #IN3
        pca.pwmWrite(in4,0)   #IN4
        #print(speed)

#robot stop
@webiopi.macro
def stop():
        set_speed(0)

#robot backwards
@webiopi.macro
def go_backward():
        set_speed(speed)
        
        pca.pwmWrite(in1,0)   #IN1
        pca.pwmWrite(in2,max) #IN2
        
        pca.pwmWrite(in3,0)   #IN3
        pca.pwmWrite(in4,max) #IN4
        #print(speed)

#robot turn left
@webiopi.macro
def turn_left():
        set_speed(1500)
        
        pca.pwmWrite(in1,max) #IN1
        pca.pwmWrite(in2,0)   #IN2
        
        pca.pwmWrite(in3,0)   #IN3
        pca.pwmWrite(in4,max) #IN4
        #print(speed)

#robot turn right
@webiopi.macro
def turn_right():
        set_speed(1500)
        
        pca.pwmWrite(in1,0)   #IN1
        pca.pwmWrite(in2,max) #IN2
        
        pca.pwmWrite(in3,max) #IN3
        pca.pwmWrite(in4,0)   #IN4
        #print(speed)

#reduce speed
@webiopi.macro
def down_speed():
        global speed
        if speed > 3095:
            speed -= 100
        else:
            speed = 3095

        print("Current speed:%d" %speed)
        
#increase speed        
@webiopi.macro
def add_speed():
        global speed
        if speed < max:
            speed += 100
        else :
            speed = max
        print("Current speed:%d" %speed)

