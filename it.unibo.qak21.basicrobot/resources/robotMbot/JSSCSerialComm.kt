package robotMbot

import java.util.ArrayList
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import jssc.SerialPort
import jssc.SerialPortEvent
import jssc.SerialPortException
import jssc.SerialPortList

class JSSCSerialComm {
	private lateinit var list: MutableList<String>  
	private lateinit var  `object`: Lock 
	private lateinit var  dataAvailable: Semaphore 
	private lateinit var  serialPort: SerialPort 
	private lateinit var  portNames: Array<String> 

	init {
 		list = ArrayList<String>()
		dataAvailable = Semaphore(0)
		`object`      = ReentrantLock()
		portNames     = SerialPortList.getPortNames()
		if (portNames.size == 0) {
			println("JSSCSerialComm: There are no serial-ports")
		} else {
			println("FOUND " + portNames.size + " serial-ports")
			for (i in portNames.indices) {
				println("JSSCSerialComm: FOUND " + portNames[i] + " PORT")
			}
		}
	}


	fun connect(commPort: String) : SerialPortConnSupport? { 
		println("JSSCSerialComm: CONNECT TO " + commPort + " ports num=" + portNames!!.size)
		var commPortName = commPort.replace("'", "")
		//var serialPort = null
		for (i in portNames.indices) {
			if (portNames[i].equals(commPortName)) {
			    //commPortName = portNames[i]
				println("JSSCSerialComm: CONNECTING TO $commPortName"  )
				serialPort = SerialPort(commPortName)
				serialPort.openPort()
				serialPort.setParams(
					SerialPort.BAUDRATE_115200,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE
				)
//				serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
				return SerialPortConnSupport(serialPort)
			}
		}
		return null
	}

	//	@Override
	fun close() {
		try {
			serialPort!!.removeEventListener()
			serialPort!!.closePort()
		} catch (e: SerialPortException) {
			System.out.println("JSSCSerialComm: could not close the port")
		}
	}

	//	@Override
	fun readData(): String? {
		var result = ""
		try {
			dataAvailable!!.acquire()
			`object`!!.lock()
			result = list.removeAt(0)
			`object`.unlock()
		} catch (e: Exception) {
			System.out.println("JSSCSerialComm: could not read from port")
		}
		return result
	}

	//	@Override
	fun writeData(data: String?) {
		try {
			serialPort!!.writeString(data)
		} catch (e: SerialPortException) {
			System.out.println("JSSCSerialComm: could not write to port")
		}
	}

	fun serialEvent(event: SerialPortEvent?) {
		if (event!!.isRXCHAR() && event!!.getEventValue() > 0) {
			try {
				val receivedData = serialPort!!.readString(event!!.getEventValue())
				`object`!!.lock()
				list!!.add(receivedData)
				System.out.println("serialEvent: " + receivedData!!)
				`object`!!.unlock()
				dataAvailable!!.release()
			} catch (ex: SerialPortException) {
				System.out.println("Error in receiving string from COM-port: " + ex!!)
			}
		}
	}
}