/**
 * ClientBoundaryUsingWebsock
 */

package it.unibo.wenv;
import it.unibo.annotations.*;
import it.unibo.interaction.IssOperations;
import it.unibo.supports.RobotApplicationStarter;
import it.unibo.supports.RobotSupport;

@ArilRobotSpec
public class ClientBoundaryUsingWebsock {
    private IssOperations rs;	//robot support

    public ClientBoundaryUsingWebsock(IssOperations support){  //Injected by UniboRobotApplicationStarter
        this.rs = support;
    }
/*
BUSINESS LOGIC
 */
protected String doBoundary( ) {
    return RobotSupport.doBoundary( 1, "", rs);
}

 /*
MAIN
 */
    public static void main(String[] args) {
        Object appl = RobotApplicationStarter.createInstance(ClientBoundaryUsingWebsock.class);

        if( appl != null ) {
            String trip =  ((ClientBoundaryUsingWebsock)appl).doBoundary();
            System.out.println("trip=" + trip);
        }
    }

}