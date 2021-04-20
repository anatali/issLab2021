package mapRoomKotlin

import mapRoomKotlin.mapUtil.getMapRep

class TripInfo( ) {

    private var journey = ""
    var map   = RoomMap.getRoomMap()

    fun updateMovesRep(move: String) {
        mapUtil.doMove(move)
        journey = journey + move
    }

    fun setObstacle() {
        mapUtil.setObstacle()
    }

    fun getJourney(): String {
        return journey
    }

    fun getMap(): String {
        return getMapRep()
    }

    fun getDirection(): String {
        return mapUtil.getDirection()
    }

    fun showMap() {
        mapUtil.showMap()
    }

    fun showJourney() {
        println(journey)
    }


}