package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IApplMsgHandlerMqtt;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class MqttAnswerHandler extends ApplMsgHandler implements IApplMsgHandlerMqtt{
private static int n = 0; 
	private BlockingQueue<String> blockingQueue = null;

	public MqttAnswerHandler( String name, BlockingQueue<String> blockingQueue ) {
		super( name );
 		this.blockingQueue = blockingQueue;
 		//Colors.out(name+" | MqttAnswerHandler CREATED blockingQueue=" + blockingQueue, Colors.ANSI_PURPLE);		
	}

	public MqttAnswerHandler( BlockingQueue<String> blockingQueue ) {
		this("mqttAnswHandler"+ n++, blockingQueue);
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message)   {
// 		Colors.out(name + " | messageArrived:" + message + " on topic="+topic, Colors.ANSI_PURPLE );
// 		Colors.out(name + " | msgId=" + 
// 				message.getId() + "  Qos="+ message.getQos() + " isDuplicate=" 
// 				+ message.isDuplicate() + " payload=" + message.getPayload().length, 
// 				Colors.ANSI_PURPLE );
 		if( message.getPayload().length == 1 ) {
 			elaborate("sorry", MqttConnection.getSupport() );
 			return;  //perchè RICEVO 0 ???
 		}
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			elaborate(msgInput, MqttConnection.getSupport() );
		}catch( Exception e) {
			ColorsOut.out(name + " ApplMsgHandler | messageArrived WARNING:"+ e.getMessage(), ColorsOut.ANSI_YELLOW );
 		}
	}
 		@Override
		public void elaborate(ApplMessage message, Interaction2021 conn) {
			//Invio sulla coda
			try {
				blockingQueue.put(message.toString());
				ColorsOut.out(name + " | elaborate " + message + " blockingQueue=" + blockingQueue, ColorsOut.ANSI_PURPLE);
			} catch (InterruptedException e) {
				ColorsOut.outerr(name + " | elaborate ERROR " + e.getMessage());
			}			
		}

		@Override
		public void elaborate(String message, Interaction2021 conn) {
			ColorsOut.out(name + " | elaborate String: " + message);
			try {
				blockingQueue.put(message.toString());
			} catch (InterruptedException e) {
				ColorsOut.outerr(name + " | elaborate ERROR " + e.getMessage());
			}			
			
		}
		@Override
		public void deliveryComplete(IMqttDeliveryToken token){
			ColorsOut.out(name + " ApplMsgHandler | deliveryComplete token=" + token.getMessageId(), ColorsOut.ANSI_YELLOW);
		}

		@Override
		public void connectionLost(Throwable cause) {
			// TODO Auto-generated method stub
			
		}		
}
