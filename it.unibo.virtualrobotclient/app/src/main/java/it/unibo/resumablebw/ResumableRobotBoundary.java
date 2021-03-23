/*
===============================================================
ClientBoundaryWebsockArilAsynch.java
Use the aril language and the support specified in the
configuration file IssProtocolConfig.txt

The business logic is defined in RobotControllerArilBoundary
that is 'message-driven'
===============================================================
*/
package it.unibo.resumablebw;
import it.unibo.annotations.ArilRobotSpec;
import it.unibo.consolegui.ConsoleGui;
import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;
import it.unibo.supports.IssCommSupport;
import it.unibo.supports.RobotApplicationStarter;


@ArilRobotSpec
public class ResumableRobotBoundary {
    private RobotApplInputController controller;

    //Constructor
    public ResumableRobotBoundary(IssOperations rs){
        IssCommSupport rsComm = (IssCommSupport)rs;
        controller            = new RobotApplInputController(rsComm, true, true );
        rsComm.registerObserver( controller );

        IssObserver obs = new AnotherObserver();
        rsComm.registerObserver( obs );

        System.out.println("RobotBoundaryArilAsynch | CREATED with rsComm=" + rsComm);
        new ConsoleGui(  controller );
    }


    public static void main(String args[]){
        try {
            System.out.println("RobotBoundaryArilAsynch | main start n_Threads=" + Thread.activeCount());
            Object appl = RobotApplicationStarter.createInstance(ResumableRobotBoundary.class);
            System.out.println("RobotBoundaryArilAsynch  | appl n_Threads=" + Thread.activeCount());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
