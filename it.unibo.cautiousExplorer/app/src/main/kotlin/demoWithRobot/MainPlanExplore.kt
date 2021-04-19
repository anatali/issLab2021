package demoWithRobot

import itunibo.planner.plannerUtil

//import mapRoomKotlin.plannerUtil

object mainPlanRobotDemo {
    fun explore() {
         try {
            plannerUtil.startTimer()

            plannerUtil.initAI()
            println("===== initial map")
            plannerUtil.showMap()


                       //plannerUtil.cell0DirtyForHome()
            plannerUtil.setGoal(1, 1);
            plannerUtil.doPlan()
                       executeMoves( )
                       println("===== map after plan for home")
            plannerUtil.showMap()

            plannerUtil.getDuration()

		} catch (e: Exception) {
            e.printStackTrace()
        }

    }

@Throws(Exception::class)
    internal fun doSomeMOve() {
    plannerUtil.doMove("w")
    plannerUtil.doMove("l")
    plannerUtil.doMove("w")
    plannerUtil.doMove("w")
    plannerUtil.doMove("r")
    plannerUtil.doMove("w")
    plannerUtil.doMove("l")
        //mapUtil.doMove("obstacleOnRight")
    }
 

    @Throws(Exception::class)
    internal fun executeMoves( ) {
        var move = plannerUtil.getNextPlannedMove()
        while ( move.length > 0 ) {
            plannerUtil.doMove(move)
			move = plannerUtil.getNextPlannedMove()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        explore()
    }

}
