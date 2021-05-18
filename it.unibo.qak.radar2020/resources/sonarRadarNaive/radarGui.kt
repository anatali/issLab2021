package sonarRadarNaive
/*
 radarGui.kt
 ------------------------------------------------------------------------------------------
Solution expressed in a main program: no structure. Interaction based on  java.net.
Behavior expressed as a control-flow with comments to mark logical states.
 
Code refactoring required 
------------------------------------------------------------------------------------------
*/
import java.net.ServerSocket
import java.io.DataOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
//STATE: init
	radarPojo.radarSupport.setUpRadarGui()
	val serverSocket  = ServerSocket( 8010 )
	val socket        = serverSocket.accept()
	val outStream 	  = socket.getOutputStream()
	val inStream  	  = socket.getInputStream()
	val outputChannel = DataOutputStream(outStream);
	val inputChannel  = BufferedReader( InputStreamReader( inStream ) );
//STATE: work	
	while( true ){
//STATE: read the request
		try{
			val	DistanceStr   = inputChannel.readLine()
//STATE: elaborate the request
			radarPojo.radarSupport.update( DistanceStr, "0")
//STATE: send the answer
			Thread.sleep(1000 ) //simulate some work to do ...
			outputChannel.writeBytes("done\n" );	
			outputChannel.flush();
		}catch( e: Exception){
			println("radarGui | error $e")
			break //exit form while or redo init
		}
	}
	println("radarGui | END")
}