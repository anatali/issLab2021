package it.unibo.enablerCleanArch.supports;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
/*
 * TODO: omettere la oarte MqttCallback che viene realizzata da ContextMqttMsgHandler
 */
public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
		//Colors.out(name + " | CREATING ... ", Colors.ANSI_YELLOW );
	}
   	
 	//Warning: le risposte sono messaggi 'unstructured'
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			Colors.out(name + " | ApplMsgHandler sendMsgToClient message=" + message + " conn=" + conn, Colors.ANSI_YELLOW);
// 			if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
// 				String reply = Utils.buildReply("sender", "msgid", message, "dest").toString();
// 				conn.forward( reply );
// 			}else 
 				conn.forward( message );
		} catch (Exception e) {
 			Colors.outerr(name + " | ApplMsgHandler sendMsgToClient ERROR " + e.getMessage());;
		}
 	} 
 	public void sendMsgToClient( ApplMessage message, Interaction2021 conn  ) {
 		sendMsgToClient( message.toString(), conn );
 	}
 	
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
 	public String getName() {
		return name;
	}	
 
 /*
//MQTT part 	 MqttCallback
	@Override
	public void connectionLost(Throwable cause) {
		Colors.outerr(name + " ApplMsgHandler | connectionLost cause="+cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Colors.outappl(name + " ApplMsgHandler | messageArrived:" + message + " topic="+topic, Colors.ANSI_PURPLE );
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			Colors.outappl(name + " ApplMsgHandler | msgInput:" + msgInput.msgContent() , Colors.ANSI_PURPLE );
			if( msgInput.isRequest() ) {
				MqttSupport conn = new MqttSupport();
				conn.connect(name+"Answer", topic+"answer", RadarSystemConfig.mqttBrokerAddr);  //Serve solo per spedire
				if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
					//the object working is ContextMsgHandler  
					elaborate(  msgInput.toString(),   conn) ;
				}else elaborate(  msgInput.msgContent(),   conn) ;
			}else if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
						//the object working is ContextMsgHandler  
						elaborate(  msgInput.toString(),   null) ;
				 }else elaborate(  msgInput.msgContent(),   null) ;
		}catch( Exception e) {
			Colors.outerr(name + " ApplMsgHandler | messageArrived WARNING:"+ e.getMessage() );
			//if( RadarSystemConfig.protcolType != ProtocolType.mqtt)  
				//elaborate(  message.toString(),   null) ;
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
*/ 
	@Override
	public void connectionLost(Throwable cause) {
		Colors.outerr(name + " ApplMsgHandler | connectionLost not implemented" );
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)  {
		Colors.outerr(name + " ApplMsgHandler | messageArrived not implemented");
 	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token){
		Colors.outerr(name + " ApplMsgHandler | deliveryComplete not implemented");
	}
 	
}
