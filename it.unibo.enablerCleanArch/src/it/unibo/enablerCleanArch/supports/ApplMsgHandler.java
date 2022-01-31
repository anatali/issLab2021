package it.unibo.enablerCleanArch.supports;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
 
/*
 * TODO: omettere la parte MqttCallback che viene realizzata da ContextMqttMsgHandler
 */
public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
		//Colors.out(name + " | CREATING ... ", Colors.BLUE );
	}
 	
 	public String getName() {
		return name;
	}	 
   	
 	//Warning: le risposte sono messaggi 'unstructured'
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			ColorsOut.out(name + " | ApplMsgHandler sendMsgToClient message=" + message + " conn=" + conn, ColorsOut.BLUE);
			conn.forward( message );
		} catch (Exception e) {
 			ColorsOut.outerr(name + " | ApplMsgHandler sendMsgToClient ERROR " + e.getMessage());;
		}
 	} 
 	@Override
 	public void sendAnswerToClient( String reply  ) {
		ColorsOut.out(name + " | ApplMsgHandler sendAnswerToClient reply=" + reply, ColorsOut.BLUE);
		try {
			MqttSupport.getSupport().reply(reply);
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient ERROR " + e.getMessage());
 		}
  	}
 	public void sendMsgToClient( ApplMessage message, Interaction2021 conn  ) {
 		sendMsgToClient( message.toString(), conn );
 	}
 	
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
 	
    protected ApplMessage prepareReply(ApplMessage message, String answer) {
		String payload = message.msgContent();
		String sender  = message.msgSender();
		String receiver= message.msgReceiver();
		String reqId   = message.msgId();
		ApplMessage reply = null;
		if( message.isRequest() ) {
			//The msgId of the reply must be the id of the request !!!!
 			reply = Utils.buildReply(receiver, reqId, answer, message.msgSender()) ;
		}else { //DEFENSIVE
			ColorsOut.outerr(name + " | ApplMsgHandler prepareReply ERROR: message not a request");
		}
		return reply;
    }
 
	@Override
	public void connectionLost(Throwable cause) {
		ColorsOut.outerr(name + " ApplMsgHandler | connectionLost not implemented" );
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)  {
		ColorsOut.outerr(name + " ApplMsgHandler | messageArrived not yet implemented");
 	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token){
		ColorsOut.outerr(name + " ApplMsgHandler | deliveryComplete not implemented");
	}
 	
}
