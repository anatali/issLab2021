/*
mainPlanDemo
shows the behavior of a planner
*/

package itunibo.planner

object mainPlanDemo {
    fun demo() {
        try {
			plannerUtil.startTimer()

			plannerUtil.initAI()
            //plannerUtil.cleanQa()
            println("===== initial map")
            plannerUtil.showMap()
            doSomeMOve() //w l w w r w l

            println("===== map after some move")
            plannerUtil.showMap()


            val actions = plannerUtil.doPlan()  //no goal
            println("===== plan actions: ${actions!!} "   )
            executeMoves( )			
            println("===== map after plan no goal")
            plannerUtil.showMap()
                       //plannerUtil.cell0DirtyForHome()
                       plannerUtil.setGoal(0,0);
                       plannerUtil.doPlan()
                       executeMoves( )
                       println("===== map after plan for home")
                       plannerUtil.showMap()

			plannerUtil.getDuration()
            plannerUtil.saveRoomMap("demoMap")
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
            plannerUtil.doMove( move )
			move = plannerUtil.getNextPlannedMove()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        demo()
    }

}
