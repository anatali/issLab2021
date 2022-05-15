package robotMbot
//import gnu.io.SerialPort;
import jssc.SerialPort
import it.unibo.`is`.interfaces.IObservable
import it.unibo.`is`.interfaces.IObserver

interface ISerialPortConnection : IObserver, IObservable {
	//var port: SerialPort

	
	//fun connect(portName: String, userClass: Class<T,Any>): SerialPort


	fun connect(portName: String, name: String): SerialPort
	fun connect(portName: String): SerialPort
	fun closeConnection(portName: String)
}