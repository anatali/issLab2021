package it.unibo.supports2021.usage;

import it.unibo.supports2021.ActorBasicJava;


public class NaiveActorObserver extends ActorBasicJava {

    public NaiveActorObserver(String name){
        super(name);
    }
    @Override
    protected void handleInput(String info) {
        System.out.println( myname + " | " + info + " " + aboutThreads() );
    }

}
