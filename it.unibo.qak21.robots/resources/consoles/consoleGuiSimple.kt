package consoles

import connQak.ConnectionType
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import connQak.connQakBase
import consolegui.ButtonAsGui
import it.unibo.kactor.MsgUtil
import consolegui.Utils
 

class consoleGuiSimple( val connType : ConnectionType,
						val hostIP : String,   val port : String,
						val destName : String ) : IObserver {
lateinit var connQakSupport : connQakBase
val buttonLabels = arrayOf("e","w", "s", "l", "r", "z", "x", "b", "p", "h")
	
    init{
		create( connType )
	} 
	
    fun create( connType : ConnectionType){
		connQakSupport = connQakBase.create(connType, hostIP, port,destName )
		connQakSupport.createConnection()
		createTheGui()
		Utils.showSystemInfo("after create")
    }

	
	fun createTheGui(   ){
			 var guiName = ""
			 when( connType ){
				 ConnectionType.COAP -> guiName="GUI-COAP"
				 ConnectionType.MQTT -> guiName="GUI-MQTT"
				 ConnectionType.TCP  -> guiName="GUI-TCP"
 				 ConnectionType.HTTP -> guiName="GUI-HTTP"
			 }
 		val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
 	    concreteButton.addObserver( this )
	}
	
	override fun update(o: Observable, arg: Any) {
    	  var move = arg as String
		  if( move == "p" ){
			  val msg = MsgUtil.buildRequest("console", "step", "step(600)", destName )
			  connQakSupport.request( msg )
		  } 
		  else if( move == "e" ){
			  val msg = MsgUtil.buildEvent("console","alarm","alarm(fire)" )
			  connQakSupport.emit( msg )
		  }
 		  else{
			  val msg = MsgUtil.buildDispatch("console", "cmd", "cmd($move)", destName )
			  connQakSupport.forward( msg )
		  }
	}
}

fun main(){
	println("consoleGuiSimple")
	val appl = consoleGuiSimple( ConnectionType.COAP, hostAddr, port, qakdestination)
}