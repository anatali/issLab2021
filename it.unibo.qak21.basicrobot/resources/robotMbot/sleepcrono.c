/*
sleepcrono.c
*/
#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif

#include<stdio.h>
#include<time.h>
 
//use option -std=c99, -std=gnu99, -std=c11 or -std=gnu11 to compile your code
 
//gcc  sleepcrono.c  -std=gnu11 -o  sleepcrono
//g++  sleepcrono.c  -o  sleepcrono

/*
SLEEP? NO, GRAZIE!
https://italiancoders.it/sleep-no-grazie-considerazioni-sul-perche-non-usare-la-sleep-in-c-e-c-pt-1/

What Is sleep() function and How To Use It In C Program?
https://www.poftut.com/what-is-sleep-function-and-how-to-use-it-in-c-program/
*/

int crono( double delay ) {
    printf("-----------------------");
 	printf("START CLOCKS_PER_SEC=%d\n", CLOCKS_PER_SEC);
	// Start measuring time

enum {SECS_TO_SLEEP = 3, NSEC_TO_SLEEP = 0};	
struct timespec remaining, request = {SECS_TO_SLEEP, NSEC_TO_SLEEP};

	clock_t start = clock();
    printf("START start=%ld\n", start);
 
 	//sleep_until(system_clock::now() + seconds(delay));
 	sleep( delay );
	//nanosleep(&request, &remaining);

	// Stop measuring time and calculate the elapsed time
	clock_t end = clock();
	printf("ENDS end=%ld\n", end);
	
	int elapsed = ( end -  start) ;
	double delta = (double)(end - start);
	printf("ELAPSED=%ld \n", elapsed);
	printf("DELTA=%f \n", delta);

	return 0;
}

int main(){
   for (int i = 0; i < 3; i++) {
   		crono( i );
   }
}


// docker cp sleepcrono.c 4ef9b200e277:/it.unibo.qak21.basicrobot-1.0/bin/sleepcrono.c
