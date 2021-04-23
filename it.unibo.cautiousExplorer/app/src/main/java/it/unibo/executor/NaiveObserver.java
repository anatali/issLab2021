package it.unibo.executor;

import it.unibo.supports2021.ActorBasicJava;


public class NaiveObserver extends ActorBasicJava {

    public NaiveObserver(String name ){
        super(name);

    }
    @Override
    protected void handleInput(String info) {
        //ActorBasicJava.delay(count*1000);
        System.out.println( myname + " | " + info + " " + aboutThreads() );

        this.terminate();
     }

}
