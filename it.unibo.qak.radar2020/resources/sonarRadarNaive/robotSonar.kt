package sonarRadarNaive
/*
 robotSonar.kt
 ------------------------------------------------------------------------------------------
Solution expressed in a main program: no structure. Interaction based on  java.net.
Behavior expressed as a control-flow with comments to mark logical states.
 
Code refactoring required 
------------------------------------------------------------------------------------------
*/

import java.net.Socket
import java.io.DataOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader

//enum class State{ INIT,WORK,SEND_DATA }


fun main() {
//STATE: init	
	val socket 		  =  Socket( "localhost", 8010 )
	val outStream 	  =  socket.getOutputStream()
	val inStream  	  =  socket.getInputStream()
	val outputChannel =  DataOutputStream(outStream)
	val inputChannel  =  BufferedReader( InputStreamReader( inStream ) )
	
	for( i in 1..3 ){
//STATE: send request
		val v = i*20
		println(  "robotSonar | doing request: $v")
		outputChannel.writeBytes("${v}\n" )
		outputChannel.flush();
	
//STATE: wait anwser	
		val answer = inputChannel.readLine()  //blocking
		println(  "robotSonar | answer=$answer")
	}
}