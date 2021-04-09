package it.unibo.executor;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;

public class MainStepRobotActor {

    public static void main(String args[]){
        System.out.println("================================================================");
        System.out.println("MainStepRobotActor | main " + ActorBasicJava.aboutThreads() );
        System.out.println("================================================================");
    //Configure the system
        NaiveObserver obs  = new NaiveObserver("obs" );
        IJavaActor stepper = new StepRobotActor("stepper", obs );
    //Activate the system
         for( int i =1; i<=5; i++ ) {
             ActorBasicJava.delay(1000);
             stepper.send(ApplMsgs.stepMsg.replace("TIME", "350"));
         }

    }
}
