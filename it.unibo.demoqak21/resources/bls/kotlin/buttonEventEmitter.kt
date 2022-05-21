package resources.bls.kotlin

import it.unibo.bls.interfaces.IObserver
import it.unibo.kactor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import it.unibo.bls.devices.gui.ButtonAsGui

class buttonEventEmitter( val a : ActorBasic, val name : String ) : IObserver {
	
	companion object{
		
		fun create(   ){
			val concreteButton = ButtonAsGui.createButton( "click" )
			val a = sysUtil.getActor("button")
            concreteButton.addObserver( buttonEventEmitter( a!!, "click") )		
		}
		fun create( a : ActorBasic, name : String ){
			val concreteButton = ButtonAsGui.createButton(name)
            concreteButton.addObserver( buttonEventEmitter(a,name) )		
		}
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi

     override fun update(o: Observable?, arg: Any?) {
 	        GlobalScope.launch{
	            //MsgUtil.sendMsg("click", "clicked", a)
				val event = MsgUtil.buildEvent("buttonEventEmitter","local_buttonCmd", "local_buttonCmd($name)")
				println("buttonEventEmitter click => emits local_buttonCmd")
				a.emit(event, avatar=true)				
	        }
    }
}

 