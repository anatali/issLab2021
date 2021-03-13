/*
===============================================================
RobotControllerBoundary.java
implements the business logic by handling messages received on the cmdsocket-8091

===============================================================
*/
package it.unibo.wenv;
import it.unibo.supports.IssCommSupport;
import it.unibo.interaction.IssObserver;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports.RobotSupport;
import org.json.JSONObject;

public class RobotControllerBoundary implements IssObserver {
private int stepNum              = 1;
private String journey           = "";
private boolean boundaryWalkDone = false ;
private RobotSupport rs ;

    public RobotControllerBoundary(IssCommSupport support){
        rs = new RobotSupport(support);
     }

    public synchronized String doBoundary(){
        rs.request( MsgRobotUtil.forwardMsg  );
        while( ! boundaryWalkDone ) {
            try {
                wait();
                //System.out.println("RobotControllerBoundary | RESUMES - final journey=" + journey);
                rs.close();
                return journey;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return journey;
    }

    @Override
    public void handleInfo(String infoJson) {
        handleInfo( new JSONObject(infoJson) );
    }
    @Override
    public void handleInfo(JSONObject infoJson) {
        if( infoJson.has("endmove") ) boundary(infoJson);
    }
//Business logic in RobotControllerBoundary
    protected synchronized void boundary( JSONObject mv ){
        String answer = (String) mv.get("endmove");
        String move   = (String) mv.get("move");
        System.out.println("RobotControllerBoundary | boundary stepNum:" + stepNum + " " + journey);
        if (stepNum <= 4) {
            if( move.equals("turnLeft") ){
                journey = journey + "l";
                if (stepNum == 4) { boundaryWalkDone=true; notify(); return; }
                stepNum++;
                rs.request(  MsgRobotUtil.forwardMsg );
                return;
            }
            if( move.equals("moveForward") && answer.equals("true") ){
                journey = journey + "w";
                rs.request( MsgRobotUtil.forwardMsg );
            }else{ //collision
                rs.request(MsgRobotUtil.turnLeftMsg );
            }
        }else{ //steps ended
            System.out.println("RobotControllerBoundary | boundary journey:" + journey);
        }
    }

}
