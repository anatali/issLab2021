package itunibo.planner

import aima.core.agent.Action
import aima.core.search.framework.problem.ActionsFunction
import mapRoomKotlin.RoomMap.Companion.getRoomMap
import aima.core.search.framework.problem.ResultFunction
import aima.core.search.framework.problem.StepCostFunction
import aima.core.search.framework.problem.GoalTest
import mapRoomKotlin.RobotState
import java.lang.IllegalArgumentException
import java.util.HashSet

class Functions : ActionsFunction, ResultFunction, StepCostFunction, GoalTest {
    companion object {
        const val MOVECOST = 1.0
        const val TURNCOST = 1.0
    }
    override fun c(arg0: Any, arg1: Action, arg2: Any): Double {
        val action = arg1 as RobotAction
        return if (action.action == RobotAction.FORWARD ||
            action.action == RobotAction.BACKWARD) MOVECOST else TURNCOST
    }

    override fun result(arg0: Any, arg1: Action): Any {
        val state = arg0 as RobotState
        val action = arg1 as RobotAction
        val result: RobotState
        result = when (action.action) {
            RobotAction.FORWARD -> state.forward()
            RobotAction.BACKWARD -> state.backward()
            RobotAction.TURNLEFT -> state.turnLeft()
            RobotAction.TURNRIGHT -> state.turnRight()
            else -> throw IllegalArgumentException("Not a valid RobotAction")
        }
        return result
    }

    override fun actions(arg0: Any): Set<Action> {
        val state = arg0 as RobotState
        val result: MutableSet<Action> = HashSet()
        result.add(RobotAction(RobotAction.TURNLEFT))
        result.add(RobotAction(RobotAction.TURNRIGHT))
        if (getRoomMap().canMove(state.x, state.y, state.direction)) result.add(RobotAction(RobotAction.FORWARD))
        return result
    }

    override fun isGoalState(arg0: Any): Boolean {
        //plannerUtil.colorPrint("Functions - isGoalState $arg0")
        val state = arg0 as RobotState
        return if (getRoomMap().isDirty(state.x, state.y) &&
            ! getRoomMap().isObstacle(state.x, state.y)
        ) true else false
    }


}