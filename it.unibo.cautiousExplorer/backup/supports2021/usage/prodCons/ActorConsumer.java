package it.unibo.supports2021.usage.prodCons;

import it.unibo.supports2021.ActorBasicJava;


public class ActorConsumer extends ActorBasicJava {

    public ActorConsumer(String name){
        super(name);
    }

    @Override
    protected void handleInput(String info) {
        System.out.println( myname + " | " + info + " --- " + aboutThreads() );
        if( info.equals("end")) this.terminate();
    }


}
