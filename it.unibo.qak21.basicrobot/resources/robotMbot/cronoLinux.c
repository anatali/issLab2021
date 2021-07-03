#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif

//#include "pch.h"
//#include <chrono>
//#include <thread>
#include <stdio.h>
#include <time.h>

//g++  cronoLinux.c  -o  cronoLinux
//using namespace std::this_thread; // sleep_for, sleep_until
//using namespace std::chrono;     // nanoseconds, system_clock, seconds
//gcc  cronoLinux.c  -std=c11 -o  cronoLinux 

int cronoLinux(double delay) {
	//std::cout << "crono \n";
	printf("START CLOCKS_PER_SEC=%d delay=%f\n", CLOCKS_PER_SEC, delay);

	// Start measuring time
	clock_t start = clock();

	//sleep_for(nanoseconds(100000));
	
	//sleep_until(system_clock::now() + seconds(1));
	
	//std::this_thread::sleep_for(std::chrono::milliseconds( delay  ));
	
	/*
	double sum = 0;
	double add = 1;
	int iterations = 1000 * 1000 * 100;  //*10=0.701 seconds
	for (int i = 0; i < iterations; i++) {
		sum += add;
		add /= 2.0;
	}
	*/

	usleep( delay );
	
	// Stop measuring time and calculate the elapsed time
	printf("STOP  \n" );
	clock_t end = clock();	//CPU time on Linux, wall time on Windows
	
	int delta = ( end -  start) ;
	 
	double elapsed = (double)delta  ; //    / CLOCKS_PER_SEC
	printf("Time measured: %.6f seconds delay=%f \n", elapsed, delay);

	return 0;
}

int main(){
//for (int i = 1; i <= 3; i++) {cronoLinux(i*0.5);}
cronoLinux( 350000 );
}