package it.unibo.robotWithActorJava;

import it.unibo.supports2021.ActorBasicJava;

public class NaiveObserverActor extends ActorBasicJava {
    public NaiveObserverActor(String name) {
        super(name);
    }

    @Override
    protected void handleInput(String msgJson) {
        System.out.println( "---------------------------------------------");
        System.out.println( myname + " | " + msgJson);
        System.out.println( "---------------------------------------------");
    }
}
