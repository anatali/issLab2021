package resources.bls.kotlin

import it.unibo.bls.interfaces.ILed
import org.json.JSONObject
import java.io.File

object ledsupport{
	lateinit var ledtype  :  String
	lateinit var ledgpio  :  String

	//TODO
	fun createfromFile(configFileName: String) : ILed{
		val config = File("${configFileName}").readText(Charsets.UTF_8)
		//println( "		--- robotSupport | config=$config" )
		val jsonObject   = JSONObject( config )
		ledtype   = jsonObject.getString("ledconcrete") 
		ledgpio   = jsonObject.getString("gpio") 
		println( "		--- led | CREATED for $ledtype ledgpio=$ledgpio" )
		return it.unibo.bls.devices.gui.LedAsGui.createLed()  //TODO
	}
	fun create(ledType: String) : ILed{
		when( ledType ){
			"ledgui" -> return it.unibo.bls.devices.gui.LedAsGui.createLed()
			 else -> return it.unibo.bls.devices.mock.LedMock.createLed()
		}
 	}
}