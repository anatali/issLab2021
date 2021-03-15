/*
===============================================================
RobotBoundaryLogic.java
implements the business logic  

===============================================================
*/
package it.unibo.wenv;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports.IssCommSupport;
import mapRoomKotlin.mapUtil;

public class RobotBoundaryLogic {
    private IssCommSupport rs ;

private int stepNum              = 1;
private String journey           = "";
private boolean boundaryWalkDone = false ;
private boolean usearil          = false;
private int moveInterval         = 1000;
private boolean  doMap           = false;
    //public enum robotLang {cril, aril}    //todo

    public RobotBoundaryLogic(IssCommSupport support, boolean usearil, boolean doMap){
        rs = support;
        this.usearil = usearil;
        this.doMap   = doMap;
        showRobotMovesRepresentation();
    }
    
    public synchronized String doBoundaryStart(){
        System.out.println("RobotBoundaryLogic | doBoundary rs=" + rs + " usearil=" + usearil);
        rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg  );
        //The reply to the request is sent by WEnv after the wtime defined in issRobotConfig.txt  
        delay(moveInterval ); //to reduce the robot move rate
        while( ! boundaryWalkDone ) {
            try {
                wait();
                //System.out.println("RobotBoundaryLogic | RESUMES " );
                rs.close();
             } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return getMovesRepresentationAndClean();
    }

    protected String getMovesRepresentationAndClean(  ){
        if( doMap ) return mapUtil.getMapAndClean();
        else {
            String answer = journey;
            journey       = "";
            return answer;
        }
    }
    protected void updateRobotMovesRepresentation(String move ){
        if( doMap )  mapUtil.doMove( move );
        else journey = journey + move;
    }
    protected void showRobotMovesRepresentation(  ){
        if( doMap ) mapUtil.showMap();
        else System.out.println( "journey=" + journey );
    }
    

 //Business logic in RobotBoundaryLogic
    protected synchronized void boundary( String move, boolean obstacle ){
         if (stepNum <= 4) {
            if( move.equals("turnLeft") ){
                //journey = journey + "l";
                updateRobotMovesRepresentation("l");
                showRobotMovesRepresentation();
                if (stepNum == 4) {
                    boundaryWalkDone=true;
                    notify(); //to resume the main
                    return;
                }
                stepNum++;
                rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg );
                delay(moveInterval ); //to reduce the robot move rate
                return;
            }
            //the move is moveForward
            if( obstacle ){
                rs.request( usearil ? MsgRobotUtil.lMsg : MsgRobotUtil.turnLeftMsg   );
            }
            if( ! obstacle ){
                //journey = journey + "w";
                updateRobotMovesRepresentation("w");
                rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg );
            }
            //showRobotMovesRepresentation();
            delay(moveInterval ); //to reduce the robot move rate
        }else{ //stepNum > 4
            System.out.println("RobotBoundaryLogic | boundary ENDS"  );
        }
    }

    protected void delay( int dt ){
        try { Thread.sleep(dt); } catch (InterruptedException e) { e.printStackTrace(); }
    }

}
