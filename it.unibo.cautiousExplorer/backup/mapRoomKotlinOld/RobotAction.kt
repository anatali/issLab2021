package mapRoomKotlin

import aima.core.agent.Action
import mapRoomKotlin.RobotAction
import java.lang.IllegalArgumentException

class RobotAction(action: Int) : Action {
    val action: Int
    override fun isNoOp(): Boolean {
        return false
    }

    init {
        require(!(action < FORWARD || action > TURNLEFT))
        this.action = action
    }
    override fun toString(): String {
        return when (action) {
            FORWARD -> "w" //"forward";
            BACKWARD -> "s" //"backward";
            TURNRIGHT -> "r" //"turnRight";
            TURNLEFT -> "l" //"turnLeft";
            else -> throw IllegalArgumentException("Not a valid action")
        }
    }

    companion object {
        const val FORWARD = 0
        const val TURNRIGHT = 1
        const val BACKWARD = 2
        const val TURNLEFT = 3
        val wAction = RobotAction(FORWARD)
        val sAction = RobotAction(BACKWARD)
        val lAction = RobotAction(TURNLEFT)
        val rAction = RobotAction(TURNRIGHT)
    }


}