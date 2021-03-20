/**
 * HelloNaive
 * @author AN - DISI - Unibo
===============================================================
Just to show the initial number of threads is 2 and does not change
===============================================================
 */
package it.unibo.demo
class HelloNaive(){
    fun doJob(){
        println("HelloNaive | doJob n_Threads=" + Thread.activeCount());
    }
}
    fun main() {
        println("==============================================")
        println("HelloNaive | main START n_Threads=" + Thread.activeCount());
        println("==============================================")
        HelloNaive().doJob();
        println("==============================================")
        println("HelloNaive | main END n_Threads=" + Thread.activeCount());
        println("==============================================")
    }
