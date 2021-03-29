/*
===============================================================
MainRobotActorJava.java
Use the aril language and the support specified in the
configuration file IssProtocolConfig.txt


===============================================================
*/
package it.unibo.resumablebwWithActor;
import it.unibo.consolegui.ConsoleGuiActor;
import it.unibo.robotWithActorJava.BoundaryWalkerActor;
import it.unibo.robotWithActorJava.NaiveObserverActor;
import it.unibo.supports2021.IssWsHttpJavaSupport;

public class MainResumableRobotActorJava {

    //Constructor
    public MainResumableRobotActorJava( ){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        //while( ! support.isOpen() ) ActorBasicJava.delay(100);

        ResumableBoundaryWalkerActor ra = new ResumableBoundaryWalkerActor("rwa", support);
        support.registerActor(ra);

        ConsoleGuiActor console = new ConsoleGuiActor();
        console.registerActor(ra);
        //console.registerActor(new NaiveObserverActor("naiveObs") );

        //ra.send("startApp");

        System.out.println("MainRobotActorJava | CREATED  n_Threads=" + Thread.activeCount());
    }


    public static void main(String args[]){
        try {
            System.out.println("MainRobotActorJava | main start n_Threads=" + Thread.activeCount());
            new MainResumableRobotActorJava();
            //System.out.println("MainRobotActorJava  | appl n_Threads=" + Thread.activeCount());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
