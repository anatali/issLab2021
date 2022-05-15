package robotMbot

import jssc.SerialPort
import jssc.SerialPortEventListener
import kotlin.Throws
import jssc.SerialPortEvent
import jssc.SerialPortException
import okhttp3.internal.notifyAll
import java.lang.Exception
import java.util.ArrayList
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
 

class SerialPortConnSupport( val serialPort: SerialPort ) : ISerialPortInteraction, SerialPortEventListener {
    //private lateinit var list: MutableList<String>
    private var curString   = ""
	private val dataChannel = Channel<String>(5)
	
    init {
		println("SerialPortConnSupport init serialPort=$serialPort"  )
        try {
            //list = mutableListOf<String>()
            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR)
         } catch (e: Exception) {
            println("SerialPortConnSupport ERROR=$e"  );
        }
    }

    @Throws(Exception::class)
    override fun sendALine(msg: String) {
        //msg = msg+"\n";
        //println("SerialPortConnSupport sendALine ... " + msg);
        serialPort!!.writeBytes(msg.toByteArray())
        //println("SerialPortConnSupport has sent   " + msg);
    }

    //EXTENSION for mBot
    @Throws(Exception::class)
    fun sendCmd(msg: String) {
        println("SerialPortConnSupport sendCmd ... $msg")
        serialPort!!.writeBytes(msg.toByteArray())
    }

    @Throws(Exception::class)
    fun sendCmd(cmd: ByteArray?) {
        serialPort!!.writeBytes(cmd)
    }

    @Throws(Exception::class)
    override fun sendALine(msg: String, isAnswer: Boolean) {
        sendALine(msg)
    }

    //@Synchronized
    //@Throws(Exception::class)
    override  fun receiveALine(): String {
		var result = "0"
		runBlocking  {
	  		//println("SerialPortConnSupport receiveALine   " );	        
			//GlobalScope.launch{
				result = dataChannel.receive()
				//println(" receiveALine result=$result ")
			//} 
		}//runBlocking	    
		return result
    }

    @Throws(Exception::class)
    override fun closeConnection() {
        if (serialPort != null) {
            serialPort.removeEventListener()
            serialPort.closePort()
        }
    }

    override fun serialEvent(event: SerialPortEvent) {
        if (event.isRXCHAR && event.eventValue > 0) {
            try {
                var data = serialPort.readString(event.eventValue)
                val ds = data.split("\n".toRegex()).toTypedArray()
                data = if (ds.size > 0) ds[0] else return
                //println("SerialPortConnSupport serialEvent data=" + data );                                           
//	            this.notifyTheObservers(data);  //an observer can see the received data as it is
                updateLines( "$data".trimIndent() )
            } catch (ex: SerialPortException) {
                println("Error in receiving string from Arduino: $ex")
            }
        }
    }

    //@Synchronized
    protected fun updateLines(data: String) {
        //println("SerialPortConnSupport updateLines $data ${data.endsWith("\n")} "  );    
        if (data.length > 0) {
            curString = curString + data
            //if (data.endsWith("\n")) {
                //println("updateLines curString= " +  curString + " / " + curString.length());
 				runBlocking{
					dataChannel.send(curString)
					curString = ""
				}               
           // }
        }
    }

    companion object {
        const val SPACE_ASCII = 32
        const val DASH_ASCII = 45
        const val NEW_LINE_ASCII = 10
        const val CR_ASCII = 13
    }


}