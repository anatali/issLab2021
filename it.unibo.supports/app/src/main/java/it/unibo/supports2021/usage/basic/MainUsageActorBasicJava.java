package it.unibo.supports2021.usage.basic;

import it.unibo.actor0.sysUtil;
import it.unibo.supports2021.ActorBasicJava;

import java.util.Vector;

public class MainUsageActorBasicJava {
    public static final int numOfActors = 20;

    public static void main(String[] args) {
        Long startTime  = sysUtil.getCurrentTime( );
        int numOfActors = 40;
        System.out.println("==============================================");
        System.out.println("MainActor0Demo0 | START " + sysUtil.aboutSystem("applmain"));
        System.out.println("==============================================");

        Vector<NaiveActorBasicJava> a = new Vector<NaiveActorBasicJava>();

        for( int i=0; i<numOfActors; i++){
            NaiveActorBasicJava aa = new NaiveActorBasicJava("a"+i);
            a.add( aa );
            aa.send("start");
        }
        //MsgUtil.sendMsg("start", "start", a0);

        ActorBasicJava.delay(1000);

        for( int i=0; i<numOfActors; i++){
             a.get(i).send("end");
        }

        Long endTime = sysUtil.getDuration(startTime);
        System.out.println("==============================================");
        System.out.println("MainActor0Demo0 | END TIME=" + endTime + " " + sysUtil.aboutThreads("applmain"));
        System.out.println("==============================================");
    }
}
