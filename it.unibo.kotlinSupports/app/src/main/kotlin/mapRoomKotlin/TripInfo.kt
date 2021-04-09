package mapRoomKotlin


class TripInfo( ) {

    private var journey = ""
    var map   = RoomMap.getRoomMap()

    fun updateMovesRep(move: String) {
        mapUtil.doMove(move)
        journey = journey + move
    }

    fun getJourney(): String {
        return journey
    }
    fun getMap(): String{
        return mapUtil.getMapRep()
    }

}