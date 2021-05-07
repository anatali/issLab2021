package mapRoomKotlin

data class Box  (
			var isObstacle : Boolean = false,
            var notExplored: Boolean = true,
            var isRobot    : Boolean = false) : java.io.Serializable {

	
	
//@JvmOverloads
//constructor(var isObstacle: Boolean = false,
//            private var notExplored: Boolean = true,
//            var isRobot: Boolean = false) : Serializable {

//    fun notExplored(): Boolean {
//        return notExplored
//    }
//
//    fun setNotExplored(notExplored: Boolean) {
//        this.notExplored = notExplored
//    }
//
//    companion object {
//        /**
//         *
//         */
//        private const val serialVersionUID = 1L
//    }

}