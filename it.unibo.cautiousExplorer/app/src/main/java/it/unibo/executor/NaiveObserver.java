package it.unibo.executor;

import it.unibo.interaction.IUniboActor;
import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;


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

    @Override
    public void registerActor(@NotNull IUniboActor iUniboActor) {

    }

    @Override
    public void removeActor(@NotNull IUniboActor iUniboActor) {

    }
}
