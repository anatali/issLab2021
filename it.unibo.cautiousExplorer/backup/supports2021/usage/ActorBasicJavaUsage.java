package it.unibo.supports2021.usage;

import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import it.unibo.supports2021.usage.NaiveActorObserver;

public class ActorBasicJavaUsage {

    public void testActors(){
        IssWsHttpJavaSupport support   = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        NaiveActorObserver[] observers = new NaiveActorObserver[5];

        for( int i = 0; i<5; i++){
            observers[i] = new NaiveActorObserver("a"+i, i);
            support.registerActor( observers[i] );
         }

        support.forward( MsgRobotUtil.turnLeftMsg );
        ActorBasicJava.delay(1000);

        for( int i = 0; i<5; i++){
            support.removeActor(observers[i]);
            observers[i].terminate();
        }
        support.close();
    }


    public static void main(String[] args) throws Exception{
        System.out.println("ActorBasicJavaUsage | BEGIN " + ActorBasicJava.aboutThreads() );
        it.unibo.supports2021.usage.ActorBasicJavaUsage appl = new it.unibo.supports2021.usage.ActorBasicJavaUsage();
        //appl.testActors();
        System.out.println("ActorBasicJavaUsage | END " + ActorBasicJava.aboutThreads() );

    }
}
