package utils

import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.*
import it.unibo.kactor.ActorBasic
 

var mqtttraceOn = false

class MqttUtils(val owner: String )  {
 	protected var eventId: String? = "mqtt"
	protected var eventMsg: String? = ""
	protected lateinit var client: MqttClient
 	protected lateinit var workActor : ActorBasic
	protected val RETAIN      = false
	protected var isConnected = false
			
	
	fun trace( msg : String ){
		if( mqtttraceOn ) println("$msg")
	}
	
	fun connect(clientid: String, brokerAddr: String ): Boolean {
		try {
  			trace("MqttUtils $owner | doing connect for $clientid to $brokerAddr "  );
			client = MqttClient(brokerAddr, clientid)
            //trace("connect $brokerAddr client = $client" )
			val options = MqttConnectOptions()
			options.setKeepAliveInterval(480)
			options.setWill("unibo/clienterrors", "crashed".toByteArray(), 0, false)
			client.connect(options)
			println("MqttUtils $owner | connect DONE $clientid to $brokerAddr " )//+ " " + client
			isConnected = true
		} catch (e: Exception) {
			println("for $clientid connect $e for: $brokerAddr" ) //
			isConnected = false
		}
 			return isConnected
	}
	
	fun connectDone() : Boolean{
		return isConnected
	}

	fun disconnect() {
		try {
			//trace("disconnect "  )
			client.disconnect()
		} catch (e: Exception) {
			//trace("disconnect ERROR ${e}")
		}
	}


	fun subscribe(  actor: ActorBasic, topic: String ) {
		//trace("${actor.name} subscribe to topic=$topic client=$client "  )
		try {
			this.workActor = actor
			//client.setCallback(this)
			client.setCallback(actor)
			client.subscribe(topic)
		}catch( e: Exception ){
			trace("MqttUtils $owner |  ${actor.name} subscribe topic=$topic ERROR=$e "  )
		}
	}

  
	/*
         * sends to a tpoic a content of the form
         * 	 msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
     */
	@Throws(MqttException::class)
	fun publish( topic: String, msg: String?, qos: Int=2, retain: Boolean=false) {
		val message = MqttMessage()
		message.setRetained(retain)
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos)
		}
		message.setPayload(msg!!.toByteArray())
		try {
			client.publish(topic, message)
 			trace("publish $message topic=$topic"  )
		} catch (e:Exception) {
			println("MqttUtils $owner | publish ERROR $e topic=$topic msg=$msg"  )
 		}
	}


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
//	fun sendMsgToWorkActor( msg: String ){
//		workActor.scope.launch{
//			val m = AppMsg.create( msg )
//			workActor.actor.send( m )
// 		}
//	}
	
	fun clearTopic( topic : String )  {
  		trace("clearTopic $topic" );
		publish( topic, "", 1, true);	//send a retained message !!
	}

 
}