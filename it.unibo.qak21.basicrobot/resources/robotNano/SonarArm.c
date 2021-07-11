#include <wiringPi.h>
#include <unistd.h>
#include<stdio.h>
#include <time.h>

/*
 * in the directory: of SonarAlone.c:
1)  [ sudo ../../pi-blaster/pi-blaster ] if servo
2)  g++  SonarAlone.c -l wiringPi -o  SonarAlone
    gcc SonarArm.c -std=gnu11 -l wiringPi -o  SonarAlone  
    sudo ./SonarAlone
 */

void mydelay(unsigned int milliseconds){
    clock_t start = clock();
    while((clock() - start) * 1000 / CLOCKS_PER_SEC < milliseconds);
}

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
	//printf("SonarArm SETUP\n" );
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
		//printf("SonarArm calling SETUP\r\n" );
        setup();
        while(1) {
                cm = getCM();
                //cout <<  cm; //<<   endl;
                printf("%d\n", cm);
                fflush(stdout);		//OTHERWISE RECEIVE ONLY AT THE END
                //putchar(10);putchar(13);
                delay(300);
        }
        return 0;
}
