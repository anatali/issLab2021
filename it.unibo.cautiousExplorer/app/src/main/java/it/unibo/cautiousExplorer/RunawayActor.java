package it.unibo.cautiousExplorer;

import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

/*
The robot cannot be stopped.
The returnPath is obstacle-free
 */
public class RunawayActor extends AbstractRobotActor {

    private enum State {start, moving, end };

    private State curState       = State.start ;
    //protected RobotMovesInfo map   ;
    protected IJavaActor mainActor ;
    protected String returnPath    = "";

    public RunawayActor(String name, String path, IJavaActor mainActor ) {
        super(name );
        //returnPath = returnPath.replace("l","r");
        returnPath = reverse( path.replace("l","r")  );
        //this.map        = map;
        this.mainActor  = mainActor;
    }
    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
    protected void updateTripInfo(String move){
        //map.updateMovesRep(move);
        mapUtil.doMove(move);
    }
    protected void trun180(){
        doMove('l');
        mapUtil.doMove("l");
        doMove('l');
        mapUtil.doMove("l");
    }

    protected void doMove(char moveStep){
        System.out.println("RunawayActor | doMove ... " + moveStep + " returnPath="+returnPath);
        if( moveStep == 'w') doStep();
        else if( moveStep == 'l') turnLeft();
        else if( moveStep == 'r') turnRight();
        else if( moveStep == 's') doBackStep();
    }

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | fsm state=" +
                curState +  " move=" + move + " endmove=" + endmove + " returnPath="+returnPath );
        switch (curState) {
            case start: {
                //if( move.equals("goback") && returnPath.length() > 0){
                System.out.println("=%=%==%=%=%==%=%=%==%=%=%==%=%=%==%=%=%==%=%=%==%=%=%==% ");

                if( returnPath.length() > 0){
                    //map.showRobotMovesRepresentation();
                    trun180();
                    mapUtil.showMap();
                    waitUser("start ");
                    doMove( returnPath.charAt(0) );
                    curState = State.moving;
                 }else curState = State.end;

                break;
            }
            case moving: {
                String moveInfo = MoveNameShort.get(move);

                //if ( endmove.equals("false")) { //ignore the obstacle
                //    System.out.println("RunawayActor |  OUT OF HYPOTHESIS in " + curState);
                //}
                //if ( endmove.equals("true")) {

                updateTripInfo(moveInfo);
                mapUtil.showMap();
                    //map.showRobotMovesRepresentation();
                //}

                returnPath = returnPath.substring(1);   //a back-move has been done
                System.out.println(myname + " | moveInfo " + moveInfo + " returnPath=" + returnPath);
                if( returnPath.length() > 0){
                        //map.showRobotMovesRepresentation();
                        doMove( returnPath.charAt(0) );
                        curState = State.moving;
                    }else{ //returnPath.length() == 0
                        //It does not arrive in end, since no more moves
                        microStep();
                        curState = State.end;
                    }
                //}
                break;
            }//moving
            case end: {
                System.out.println(myname + " | END ---------------- "  );
                //map.showRobotMovesRepresentation();
                reactivate(mainActor);
                terminate();
                break;
            }//end
            default: {
                System.out.println(myname + " | error - curState = " + curState);
            }
        }

    }


/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject infoJson){
        System.out.println("RunawayActor | infoJson:" + infoJson);
        if( infoJson.has("goBack") )    fsm("", "");
        if( infoJson.has("endmove") )   fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        //else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        //else if( infoJson.has("collision") ) handleCollision(infoJson);
    }



}
