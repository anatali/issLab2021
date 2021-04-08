package it.unibo.supports2021.usage;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.usage.RobotActorController;

public class MainIssWsHttpJavaSupportUsage {

    public static void main(String[] args) {
        System.out.println("WebSocketJavaSupportUsage | BEGIN " + ActorBasicJava.aboutThreads() );
        IJavaActor controller = new RobotActorController("rctrl");

        //controller.send("testHttp");
        //controller.send("testWs");

        controller.send("testObservers");
        System.out.println("WebSocketJavaSupportUsage | END " + ActorBasicJava.aboutThreads() );

    }
}
