#include<stdio.h>
#include<time.h>


/*
#include "pch.h"
#include <chrono>
#include <thread>
#include <unistd.h>
*/

//for gcc
/*
Most of the C standard library, including stdio for example, is not included in the gcc distribution. 
Instead, gcc depends on whatever runtime library is provided by the operating system.

include <threads>	

gcc -std=c11 -g -Wall -pthread crono.c -lpthread -o crono
-g asks for debug information, -Wall asks for all warnings, -pthread asks for POSIX threads support.
*/

//use option -std=c99, -std=gnu99, -std=c11 or -std=gnu11 to compile your code
 
//gcc  crono.c  -std=c11 -o  crono
//g++  crono.c  -o  crono

int crono() {
	//using namespace std::this_thread; // sleep_for, sleep_until
	//using namespace std::chrono;     // nanoseconds, system_clock, seconds
	//std::cout << "crono \n";
	printf("START CLOCKS_PER_SEC=%d\n", CLOCKS_PER_SEC);
	// Start measuring time
enum {SECS_TO_SLEEP = 1, NSEC_TO_SLEEP = 0};	
struct timespec remaining, request = {SECS_TO_SLEEP, NSEC_TO_SLEEP};

	clock_t start = clock();
    printf("START start=%ld\n", start);
 
	//sleep_for(nanoseconds(1000));
	//sleep_until(system_clock::now() + seconds(2));

	//nanosleep(&request, &remaining);
	
	
	double sum = 0;
	double add = 1;
	int iterations = 1000 * 1000 * 10;  //*10=0.701 seconds
	for (int i = 0; i < iterations; i++) {
		sum += add;
		add /= 2.0;
	}
	
	
	// Stop measuring time and calculate the elapsed time
	clock_t end = clock();
	printf("ENDS end=%ld\n", end);
	
	//double elapsed = (1.0*end - 1.0*start) ;  //  / CLOCKS_PER_SEC
	//printf("ELAPSED: %.3f seconds.\n", elapsed);
	int elapsed = ( end -  start) ;
	printf("ELAPSED: %ld seconds.\n", elapsed);

	return 0;
}

int main(){
    
   crono();
  //cout << "crono \n" ;
  //int CLOCKS_PER_SEC = 10 ;
  //printf("START CLOCKS_PER_SEC=%d\n", CLOCKS_PER_SEC );
  
}


// docker cp crono.c 4ef9b200e277:/it.unibo.qak21.basicrobot-1.0/bin/crono.c
