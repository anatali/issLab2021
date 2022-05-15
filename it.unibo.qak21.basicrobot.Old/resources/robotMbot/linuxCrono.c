#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif

#include <stdio.h>
#include <time.h>
#include <iostream>
#include <fstream>
//For sleep
#include <chrono>
#include <thread>

//g++  linuxCrono.c  -o  linuxCrono
//gcc  linuxCrono.c  -std=c11 -o  linuxCrono 
int crono (int msec) {
	//cout << "crono \n";
	//printf("START CLOCKS_PER_SEC=%d %d\n", CLOCKS_PER_SEC, msec);

	// Start measuring time
    struct timespec begin, end; 
    clock_gettime(CLOCK_REALTIME, &begin);	
    	//CLOCK_REALTIME 				measures wall time
    	//CLOCK_PROCESS_CPUTIME_ID		measures CPU time 

	//sleep_for(nanoseconds(100000));	
	//sleep_until(system_clock::now() + seconds(1));
	
	std::this_thread::sleep_for(std::chrono::milliseconds( msec  ));
 
     // Stop measuring time and calculate the elapsed time
    clock_gettime(CLOCK_REALTIME, &end);
    long seconds     = end.tv_sec  - begin.tv_sec;
    long nanoseconds = end.tv_nsec - begin.tv_nsec;
    double elapsed   = seconds + nanoseconds*1e-9;
    
     
    //printf("Time measured: %.3f seconds.\n", elapsed);
 	cout <<  elapsed << endl;
	return 0;
}

int main()
{
for (int i = 0; i < 5; i++) {
 	crono(350);
}
}