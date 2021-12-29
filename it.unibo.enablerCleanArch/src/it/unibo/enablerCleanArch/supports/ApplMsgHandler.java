package it.unibo.enablerCleanArch.supports;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

/*
 * 
 */
public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
		//Colors.out(name + " | CREATING ... ", Colors.ANSI_YELLOW );
	}
   	
 	//Warning: le risposte sono messaggi 'plain'
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			Colors.out(name + " | sendMsgToClient " + message, Colors.ANSI_YELLOW);
			conn.forward( message );
		} catch (Exception e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
 	public String getName() {
		return name;
	}	
 	
//MQTT part 	 MqttCallback
	@Override
	public void connectionLost(Throwable cause) {
		Colors.outerr(name + " ApplMsgHandler | connectionLost cause="+cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Colors.outappl(name + " ApplMsgHandler | messageArrived:" + message, Colors.ANSI_PURPLE );
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			elaborate(  msgInput.msgContent(),   MqttSupport.getTheSupport()) ;
		}catch( Exception e) {
			elaborate(  message.toString(),   MqttSupport.getTheSupport()) ;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
//			Colors.outappl("MqttSupportCallback | deliveryComplete token=" 
//		       + token.getMessage() + " client=" + token.getClient().getClientId() , Colors.ANSI_YELLOW);
		} catch (Exception e) {
			Colors.outerr(name + " ApplMsgHandler | deliveryComplete Error:"+e.getMessage());		
		}
	}
 	
}
