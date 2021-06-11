package connQak

import connQak.ConnectionType

val mqtthostAddr    = "localhost"	//broker.hivemq.com
val mqttport		= "1883"
val mqtttopic       = "unibo/basicrobot"
val hostAddr 		= "192.168.1.72" //   172.17.0.2 "192.168.1.5" "localhost"
val port     		= "8020"
val qakdestination 	= "basicrobot"
val ctxqakdest      = "ctxbasicrobot"
val connprotocol    = ConnectionType.MQTT //TCP COAP HTTP MQTT

//fun main(){ println("consoles") }