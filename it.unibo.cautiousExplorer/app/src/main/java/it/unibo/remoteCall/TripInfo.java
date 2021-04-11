/*
============================================================
TripInfo

============================================================
 */
package it.unibo.remoteCall;

import mapRoomKotlin.RoomMap;
import mapRoomKotlin.mapUtil;

public class TripInfo {
    private RoomMap map = RoomMap.Companion.getRoomMap();   //init
    private String journey = "";

    public void updateMovesRep(String move){
        mapUtil.doMove( move );
        journey = journey + move;
    }

    public void setObstacle(){ mapUtil.setObstacle(); }
    public String getJourney(){ return journey; }
    public String getMap(){ return mapUtil.getMapRep(); }
    public String getDirection(){ return mapUtil.getDirection(); }
    public void showMap(){ mapUtil.showMap(); }
    public void showJourney(){ System.out.println(journey); }

}
