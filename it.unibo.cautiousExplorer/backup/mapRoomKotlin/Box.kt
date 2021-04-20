package mapRoomKotlin

import java.io.Serializable

class Box @JvmOverloads constructor(
    var isObstacle: Boolean = false,
    var notExplored: Boolean = true,
    var isRobot: Boolean = false
) : Serializable {

    companion object {
         private const val serialVersionUID = 1L
    }
}