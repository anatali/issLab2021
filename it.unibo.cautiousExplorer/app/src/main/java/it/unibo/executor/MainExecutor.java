package it.unibo.executor;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;

public class MainExecutor {

    public static void main(String args[]){
        System.out.println("================================================================");
        System.out.println("MainExecutor | main " + ActorBasicJava.aboutThreads() );
        System.out.println("================================================================");
    //Configure the system
        IJavaActor master = new WalkStrategyActor("master" );
    //Activate the system
         master.send(ApplMsgs.activateMsg  );
        //ActorBasicJava.delay(5000);
    }
}
