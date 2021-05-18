package it.unibo.radar

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope

class RadarGuiFsm(name: String, scope: CoroutineScope) : ActorBasicFsm(name, scope) {

	override fun getInitialState(): String {
		return "s0"
	}

	override fun getBody(): (ActorBasicFsm.() -> Unit) {
		return {
			//this:ActionBasciFsm
			state("s0") {
				//this:State
				action {
					//it:State
					println("radar start")
					radarPojo.radarSupport.setUpRadarGui()
				}
				transition(edgeName = "goto", targetState = "waitForDataToShow", cond = doswitch())
			}
			state("waitForDataToShow") {
				//this:State
				action {
					//it:State
				}
				transition(edgeName = "t00", targetState = "showSpot", cond = whenDispatch("polar"))
			}
			state("showSpot") {
				//this:State
				action {
					//it:State
					if (checkMsgContent(
							Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"),
							currentMsg.msgContent()
						)
					) { //set msgArgList
						radarPojo.radarSupport.update(payloadArg(0), payloadArg(1))
					}
				}
				transition(edgeName = "goto", targetState = "waitForDataToShow", cond = doswitch())
			}
		}
	}
}

fun main() = runBlocking {
	println("RadarGuiFsm | START")
	val radar = RadarGuiFsm("radarguifsm", this) //this is a CoroutineScope
//	delay(2000)
//	MsgUtil.sendMsg("main", "polar", "polar(60,0)", radar)
//	delay(2000)
//	MsgUtil.sendMsg("main", "polar", "polar(80,0)", radar)
}