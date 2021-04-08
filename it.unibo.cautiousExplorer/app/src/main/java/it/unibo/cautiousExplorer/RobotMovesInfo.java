package it.unibo.cautiousExplorer;

import mapRoomKotlin.mapUtil;

import static mapRoomKotlin.mapUtil.*;
import static mapRoomKotlin.mapUtil.getMapRep;

public class RobotMovesInfo {
    private boolean  doMap = false;
    private String journey = "";

    public RobotMovesInfo(boolean doMap){
        this.doMap = doMap;
    }
    public void showRobotMovesRepresentation(  ){
        if( doMap ) showMap();
        else System.out.println( "journey=" + journey );
    }

    public String cleanMovesRepresentation(  ){
        if( doMap ) return getMapAndClean(); //getCleanMap();
        else {
            String answer = journey;
            journey       = "";
            return answer;
        }
    }

    public String getMovesRepresentation(  ){
        if( doMap ) return getMapRep();
        else return journey;
    }

    public void updateMovesRep(String move ){
        if( doMap )  doMove( move );
        else journey = journey + move;
    }


}
