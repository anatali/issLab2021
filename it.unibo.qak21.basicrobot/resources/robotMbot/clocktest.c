#include <time.h>
#include <stdio.h>

//g++  clocktest.c  -o  clocktest
//docker cp clocktest.c 4ef9b200e277:/it.unibo.qak21.basicrobot-1.0/bin/clocktest.c
//gcc  clocktest.c  -std=c11 -o  clocktest

int main() {
   clock_t start_t, end_t, total_t;
 
	double sum = 0;
	double add = 1;
	int iterations = 1000 * 1000 * 10 ;  //*10=0.701 seconds

   start_t = clock();
    
   printf("Going to run a BIG LOOP, start_t = %ld\n", start_t);
	for (int i = 0; i < iterations; i++) {
		sum += add;
		add /= 2.0;
	}
	
   end_t = clock();
   printf("End of the big loop, end_t = %ld\n", end_t);
   
   double delta = (double)(end_t - start_t);
   printf("DELTA= %f\n", delta);
   
   //total_t = (double)(end_t - start_t) / CLOCKS_PER_SEC;
   total_t = delta / CLOCKS_PER_SEC;

   printf("Total time taken by CPU: %f \n",  total_t  );
   printf("Exiting of the program...\n");

   return(0);
}