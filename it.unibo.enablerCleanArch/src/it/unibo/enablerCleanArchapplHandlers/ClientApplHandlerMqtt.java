package it.unibo.enablerCleanArchapplHandlers;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

public class ClientApplHandlerMqtt extends ApplMsgHandler {
private String myTopic;
private Interaction2021 conn;

	public ClientApplHandlerMqtt(String name, Interaction2021 conn ) {
		super( name );
		this.conn = conn;
 	}
	
//	@Override  //MqttCallback
//	public void messageArrived(String topic, MqttMessage message) throws Exception {
//		//Colors.outappl(name + " ApplMsgHandler | messageArrived:" + message, Colors.GREEN );
//		myTopic = topic;
//		try { //Perhaps we receive a structured message
//			ApplMessage msgInput = new ApplMessage(message.toString());
//			if( msgInput.isRequest() ) {
//				Colors.outappl(name + " ApplMsgHandler | REQUEST:" + message + " conn="+conn, Colors.RED );
//				elaborate(  msgInput.msgContent(),   conn);				
//			}else{ 
//				elaborate(  msgInput.msgContent(),   conn) ;
//			}
//		}catch( Exception e) {
//			elaborate(  message.toString(),  conn) ;
//		}
//	}	
	
	public void connectionLost(Throwable cause) {
		ColorsOut.outerr(name + " | connectionLost cause="+cause);
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
//			Colors.outappl("MqttSupportCallback | deliveryComplete token=" 
//		       + token.getMessage() + " client=" + token.getClient().getClientId() , Colors.ANSI_YELLOW);
		} catch (Exception e) {
			ColorsOut.outerr(name + " | deliveryComplete Error:"+e.getMessage());		
		}
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message)   {
		ColorsOut.outappl(name + " ApplMsgHandler | messageArrived:" + message + " topic="+topic, ColorsOut.ANSI_PURPLE );
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			ColorsOut.outappl(name + " ApplMsgHandler | msgInput:" + msgInput.msgContent() , ColorsOut.ANSI_PURPLE );
			if( msgInput.isRequest() ) {
				MqttSupport conn = MqttSupport.getSupport();
				//conn.connect(name+"Answer", topic+name+"answer", RadarSystemConfig.mqttBrokerAddr);  //Serve solo per spedire
 				elaborate(  msgInput.toString(),   conn) ;   //answer TODO
 			}else  
 				elaborate(   msgInput.toString() ,   conn) ;
 		}catch( Exception e) {
			ColorsOut.outerr(name + " ApplMsgHandler | messageArrived WARNING:"+ e.getMessage() );
			//if( RadarSystemConfig.protcolType != ProtocolType.mqtt)  
				//elaborate(  message.toString(),   null) ;
		}
	}
	
	 
	@Override  
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
		ColorsOut.outerr(name + " | WARNING: sendMsgToClient for a client for answer should never been used");
 	} 
 
	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate message=" + message + " conn=" + conn, ColorsOut.RED);
 		try {
			ApplMessage msg = new ApplMessage(message);
//			if( msg.isReply() ) {
//				Colors.out(name + " | put in queue message=" + msg.msgContent() + " conn=" + conn, Colors.RED);
//				((MqttSupport)conn).getQueue().put(msg.msgContent());
//			}else {
				ColorsOut.out(name + " | elaborate message put in queue  conn=" + conn, ColorsOut.RED);
				((MqttSupport)conn).getQueue().put(message);
//			}
		}catch(Exception e) { //not a structured
			ColorsOut.out(name + " | " +e.getMessage() + " elaborate plain message put in queue  conn=" + conn, ColorsOut.RED);
			try {
				((MqttSupport)conn).getQueue().put(message);
			} catch (InterruptedException e1) {
				ColorsOut.outerr(name + " | elaborate ERROR:" + e.getMessage());
			}
		}
	}
	
 	
}
