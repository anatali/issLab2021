package it.unibo.supports2021.usage.prodCons;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;


public class ActorProducer extends ActorBasicJava {
    private IJavaActor consumer;

    public ActorProducer(String name, IJavaActor consumer){
        super(name);
        this.consumer = consumer;
        //doJob();      //WRONG here!
    }
    @Override
    protected void handleInput(String info) {
        //System.out.println( myname + " | " + info + " --- " + aboutThreads() );
        if( info.equals("application_start")) doJob();
    }

    protected void doJob(){
        for( int i=0; i<3; i++){
            String info = "hello_"+i;
            System.out.println( myname + " | produces " + info + " --- " + aboutThreads() );
            updateObservers(info);
            consumer.send(info);
            //ActorBasicJava.delay(i*200);
        }
        consumer.send("end");
        updateObservers("end");
        this.terminate();
    }

}
