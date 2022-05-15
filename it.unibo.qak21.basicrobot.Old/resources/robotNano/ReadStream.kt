package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.io.InputStream
import kotlinx.coroutines.runBlocking

/* 
I guess the issue is that you are only reading InputStream and not reading ErrorStream.
 
 You also have to take care that both the streams are read in parallel.
 It may so happen that currently the data piped from the output stream fills up
 the OS buffer, your exec command will be automatically be suspended to give
 your reader a chance to empty the buffer.
 But the program will still be waiting for the output to process.
 Hence, the hang occurs.

You can create a separate class to handle both the Input and Error Stream as follows,
 */





@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class ReadStream ( val name : String, val  inps : InputStream, val a : ActorBasic ) : Runnable{ //ActorBasic( name ) {
	//lateinit var reader : BufferedReader
	//lateinit var thread : Thread
	
	fun activate() {
		val thread = Thread(this)
		thread.start()
		//println("ReadStream $name | ACTIVATED "   )
	}
	
	override fun run(){
			try{				 
 				//println("ReadStream $name | inps = $inps"   )
				val inpsreader = InputStreamReader(inps)
				val reader     = BufferedReader( inpsreader )
				//println("ReadStream $name | reader = $reader"   )
 				var line : String? = reader.readLine()				
				while ( line  != null && line.length > 0 ){
					    println( line )
						try{
							val v = line.toInt()
							if( v > 0 && v <= 150 ){	//A first filter ...
								val m1 = "sonar( $v )"
								val event = MsgUtil.buildEvent( name,"sonarRobot",m1)
								//emit( event )
								if( a != null )
								runBlocking{
									a.emitLocalStreamEvent( event )		//not propagated to remote actors
								    println("$name | emit event: $event "   )
									delay( 300 )
								}
							}
						}catch(e: Exception){
							println("sonarHCSR04SupportActor readLine ERROR $e"   )
						}
					    line = reader.readLine()	
				}
				println("------------------------- ReadStream	$name | line is null")
				inps.close()
 			}catch( e : Exception){
				println("ReadStream $name | ERROR:  $e")
			}		
	}
 	
}
/*
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
	var p : Process? = null
	try{
		val cmd = "cmd /C si viewhistory --fields=revision --project=" 
		p       = Runtime.getRuntime().exec(cmd) ;  
		val s1 =  ReadStream("stdin", p.getInputStream ());
		val s2 =  ReadStream("stderr", p.getErrorStream ());
		s1.activate();
		s2.activate();
		p.waitFor();        
	} catch ( e : Exception ) {  
		e.printStackTrace();  
	} finally {
	    if(p != null)
	        p.destroy();
	}
}
*/