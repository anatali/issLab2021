package robotMbot

import jssc.SerialPort
import jssc.SerialPortEventListener
import kotlin.Throws
import jssc.SerialPortEvent
import jssc.SerialPortException
import okhttp3.internal.notifyAll
import java.lang.Exception
import java.util.ArrayList

class SerialPortConnSupport(private val serialPort: SerialPort?) : ISerialPortInteraction, SerialPortEventListener {
    private var list: MutableList<String>? = null
    private var curString = ""
    protected fun init() {
        try {
            serialPort!!.addEventListener(this, SerialPort.MASK_RXCHAR)
            list = ArrayList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    override fun sendALine(msg: String) {
        //msg = msg+"\n";
        //System.out.println("SerialPortConnSupport sendALine ... " + msg);
        serialPort!!.writeBytes(msg.toByteArray())

        //		System.out.println("SerialPortConnSupport has sent   " + msg);
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

    @Synchronized
    @Throws(Exception::class)
    override fun receiveALine(): String {
// 		System.out.println(" SerialPortConnSupport receiveALine   " );
        var result = "no data"
        while (list!!.size == 0) {
// 			println("list empty. I'll wait" );
            //wait()
        }
        result = list!!.removeAt(0)
        //		System.out.println(" SerialPortConnSupport receiveALine  result= " + result);
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
                var data = serialPort!!.readString(event.eventValue)
                val ds = data.split("\n".toRegex()).toTypedArray()
                data = if (ds.size > 0) ds[0] else return
                //println("SerialPortConnSupport serialEvent ... " + data );                                           
//	            this.notifyTheObservers(data);  //an observer can see the received data as it is
                updateLines(
                    """
    $data
    
    """.trimIndent()
                )
            } catch (ex: SerialPortException) {
                println("Error in receiving string from COM-port: $ex")
            }
        }
    }

    @Synchronized
    protected fun updateLines(data: String) {
        //System.out.println("SerialPortConnSupport updateLines ... " + data.endsWith("\n") );    
        if (data.length > 0) {
            curString = curString + data
            if (data.endsWith("\n")) {
                //System.out.println("updateLines curString= " +  curString + " / " + curString.length());
                list!!.add(curString)
                //this.notifyAll()
                curString = ""
            }
        }
    }

    companion object {
        const val SPACE_ASCII = 32
        const val DASH_ASCII = 45
        const val NEW_LINE_ASCII = 10
        const val CR_ASCII = 13
    }

    init {
        init()
    }
}