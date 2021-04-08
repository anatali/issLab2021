package it.unibo.executor;

import it.unibo.supports2021.ActorBasicJava;


public class NaiveObserver extends ActorBasicJava {
private int count;
    public NaiveObserver(String name, int i){
        super(name);
        count = i;
    }
    @Override
    protected void handleInput(String info) {
        //ActorBasicJava.delay(count*1000);
        System.out.println( myname + " | " + info + " " + aboutThreads() );

        this.terminate();
     }

}
