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
			
		}
		
}
