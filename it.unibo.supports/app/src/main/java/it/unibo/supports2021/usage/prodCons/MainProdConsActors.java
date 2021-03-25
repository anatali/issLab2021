package it.unibo.supports2021.usage.prodCons;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;

public class MainProdConsActors {

    public static void main(String[] args){
        System.out.println("%%% MainProdConsActors | START " + ActorBasicJava.aboutThreads() );
        IJavaActor consumer = new ActorConsumer("cons");
        IJavaActor producer = new ActorProducer("prod", consumer);
        IJavaActor observer = new ActorNaiveObserver("prodObs");
        producer.registerActor(observer);
        /*
        Now all the actors are ready to receive messages
         */
        producer.send("application_start");

        ActorBasicJava.delay(1000);
        System.out.println("%%% MainProdConsActors | END " + ActorBasicJava.aboutThreads() );
    }
}
