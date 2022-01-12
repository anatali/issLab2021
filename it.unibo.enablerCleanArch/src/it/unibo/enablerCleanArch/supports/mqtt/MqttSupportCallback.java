package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;

public class MqttSupportCallback implements MqttCallback{
//	private String clientName;
	private BlockingQueue<String> blockingQueue = null;
	private IApplMsgHandler handler = null;

	public MqttSupportCallback( IApplMsgHandler handler ) {
 		this.handler       = handler;
	}
	public MqttSupportCallback( BlockingQueue<String> blockingQueue ) {
 		this.blockingQueue = blockingQueue;
	}

		public MqttSupportCallback(String clientName, BlockingQueue<String> blockingQueue ) {
			//this.clientName    = clientName;
			this.blockingQueue = blockingQueue;
		}
		@Override
		public void connectionLost(Throwable cause) {
			Colors.outerr("MqttSupportCallback | connectionLost cause="+cause);
	 	}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			Colors.outappl("MqttSupportCallback | messageArrived:" + message, Colors.ANSI_PURPLE );
			if( blockingQueue != null ) blockingQueue.put( message.toString() );	
			else if( handler != null ) handler.elaborate(message.toString(), null);  //TODO 
//			Colors.outappl("MqttSupportCallback | messageArrived:"+message + " for " + clientName 
//					+ " topic=" + topic + " bqsize="+blockingQueue.size(), Colors.ANSI_PURPLE);
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			try {
//				Colors.outappl("MqttSupportCallback | deliveryComplete token=" 
//			       + token.getMessage() + " client=" + token.getClient().getClientId() , Colors.ANSI_YELLOW);
			} catch (Exception e) {
				Colors.outerr("MqttSupportCallback | deliveryComplete Error:"+e.getMessage());		
			}
	 	}
		
}
