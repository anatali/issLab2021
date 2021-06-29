#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif

//#include "pch.h"
#include <chrono>
#include <thread>
#include <stdio.h>
#include <time.h>

//g++  cronoLinux.c  -o  cronoLinux
		using namespace std::this_thread; // sleep_for, sleep_until
		using namespace std::chrono;     // nanoseconds, system_clock, seconds
//gcc  cronoLinux.c  -std=c11 -o  cronoLinux 

int cronoLinux(int msec) {
	//std::cout << "crono \n";
	printf("START CLOCKS_PER_SEC=%d %d\n", CLOCKS_PER_SEC, msec);

	// Start measuring time
	clock_t start = clock();

	//sleep_for(nanoseconds(100000));
	
	//sleep_until(system_clock::now() + seconds(1));
	
	std::this_thread::sleep_for(std::chrono::milliseconds( msec  ));
	
	/*
	double sum = 0;
	double add = 1;
	int iterations = 1000 * 1000 * 100;  //*10=0.701 seconds
	for (int i = 0; i < iterations; i++) {
		sum += add;
		add /= 2.0;
	}
	*/

	// Stop measuring time and calculate the elapsed time
	printf("STOP  \n" );
	clock_t end = clock();	//CPU time on Linux, wall time on Windows
	
	double elapsed = double(end - start) / CLOCKS_PER_SEC  ; //    / CLOCKS_PER_SEC

	printf("Time measured: %.6f seconds vs. %d msec\n", elapsed, msec);

	return 0;
}

int main()
{
//for (int i = 0; i < 5; i++) {
 	cronoLinux(350);
//}
}