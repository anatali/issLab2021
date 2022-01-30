package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class MqttAnswerHandler extends ApplMsgHandler{
private static int n = 0; 
	private BlockingQueue<String> blockingQueue = null;

	public MqttAnswerHandler( BlockingQueue<String> blockingQueue ) {
		super("mqttAnswHandler"+ n++);
 		this.blockingQueue = blockingQueue;
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message)   {
 		Colors.outappl(name + " | messageArrived:" + message + " on topic="+topic, Colors.ANSI_PURPLE );
 		Colors.outappl(name + " | msgId=" + 
 				message.getId() + "  Qos="+ message.getQos() + " isDuplicate=" 
 				+ message.isDuplicate() + " payload=" + message.getPayload().length, 
 				Colors.ANSI_PURPLE );
 		if( message.getPayload().length == 1 ) {
 			elaborate("sorry", MqttSupport.getSupport() );
 			return;  //perchè RICEVO 0 ???
 		}
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			elaborate(msgInput, MqttSupport.getSupport() );
		}catch( Exception e) {
			Colors.out(name + " ApplMsgHandler | messageArrived WARNING:"+ e.getMessage(), Colors.ANSI_YELLOW );
 		}
	}
 		@Override
		public void elaborate(ApplMessage message, Interaction2021 conn) {
			Colors.out(name + " | elaborate " + message);
			//Invio sulla coda
			try {
				blockingQueue.put(message.toString());
			} catch (InterruptedException e) {
				Colors.outerr(name + " | elaborate ERROR " + e.getMessage());
			}			
		}

		@Override
		public void elaborate(String message, Interaction2021 conn) {
			Colors.out(name + " | elaborate String: " + message);
			try {
				blockingQueue.put(message.toString());
			} catch (InterruptedException e) {
				Colors.outerr(name + " | elaborate ERROR " + e.getMessage());
			}			
			
		}
		@Override
		public void deliveryComplete(IMqttDeliveryToken token){
			Colors.out(name + " ApplMsgHandler | deliveryComplete token=" + token.getMessageId(), Colors.ANSI_YELLOW);
		}		
}
