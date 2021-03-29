package it.unibo.supports2021.usage;

import it.unibo.supports2021.ActorBasicJava;


public class NaiveActorObserver extends ActorBasicJava {
private int count;
    public NaiveActorObserver(String name, int i){
        super(name);
        count = i;
    }
    @Override
    protected void handleInput(String info) {
        //ActorBasicJava.delay(count*1000);
        System.out.println( myname + " | " + info + " " + aboutThreads() );
     }

}
