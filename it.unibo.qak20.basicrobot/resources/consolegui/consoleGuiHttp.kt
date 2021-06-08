package consolegui
 
object consoleGuiHttp{
	fun create(  hostIP : String,     port : String,     destName : String) {
		consoleGuiSimple( connQak.ConnectionType.HTTP, hostIP, port, destName)
	}
} 
fun main(){
	consoleGuiHttp.create( "localhost", "8080", "basicrobot")
}
 