package it.unibo.actor0robot

import mapRoomKotlin.mapUtil.showMap
import mapRoomKotlin.mapUtil.getMapAndClean
import mapRoomKotlin.mapUtil.getMapRep
import mapRoomKotlin.mapUtil.doMove

class RobotMovesInfo(val doMap: Boolean) {

    private var journey = ""
    fun showRobotMovesRepresentation() {
        if (doMap) showMap() else println("journey=$journey")
    }

    fun cleanMovesRepresentation(): String {
        return if (doMap) getMapAndClean() //getCleanMap();
        else {
            val answer = journey
            journey = ""
            answer
        }
    }

    val movesRepresentation: String
        get() = if (doMap) getMapRep() else journey

    fun updateMovesRep(move: String) {
        if (doMap) doMove(move) else journey = journey + move
    }


}