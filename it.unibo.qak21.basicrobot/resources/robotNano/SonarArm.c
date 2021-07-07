#include <wiringPi.h>
#include <unistd.h>
#include<stdio.h>


/*
 * in the directory: of SonarAlone.c:
1)  [ sudo ../../pi-blaster/pi-blaster ] if servo
2)  g++  SonarAlone.c -l wiringPi -o  SonarAlone
    gcc SonarArm.c -std=gnu11 -l wiringPi -o  SonarAlone
    sudo ./SonarAlone
 */

//#define TRUE 1
//Wiring Pi numbers for radar with stepper
#define TRIG 0  //4
#define ECHO 2  //5
//using namespace std;

void setup() {
	wiringPiSetup();
	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, INPUT);

	//TRIG pin must start LOW
	digitalWrite(TRIG, LOW);
	delay(30);
}

int getCM() {
	//Send trig pulse
	digitalWrite(TRIG, HIGH);
	delayMicroseconds(20);
	digitalWrite(TRIG, LOW);

	//Wait for echo start
	while(digitalRead(ECHO) == LOW);

	//Wait for echo end
	long startTime = micros();
	while(digitalRead(ECHO) == HIGH);
	long travelTime = micros() - startTime;

	//Get distance in cm
	int distance = travelTime / 58;

 	return distance;
}

int main(void) {
        int cm ;
        setup();
        while(1) {
                cm = getCM();
                //cout <<  cm; //<<   endl;
                printf("%d", cm);
                putchar('\n');
                delay(300);
        }
        return 0;
}
