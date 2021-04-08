package it.unibo.supports2021.usage.timer;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.ActorMsgs;
import it.unibo.supports2021.TimerActor;
import it.unibo.supports2021.usage.prodCons.ActorNaiveObserver;

public class MainTimer {
    public static void main(String[] args) {
        System.out.println("%%% MainTimer | START " + ActorBasicJava.aboutThreads());
        ActorNaiveObserver obs = new ActorNaiveObserver("obs");
        IJavaActor timer       = new TimerActor("t0", obs);
        timer.send(ActorMsgs.startTimerMsg.replace("TIME", "500") );

    }
}
