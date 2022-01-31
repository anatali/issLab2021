package it.unibo.enablerCleanArch.supports.mqtt;

 
import java.util.Observable;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class SonarObserverHandlerNaive implements IApplMsgHandler{
	private String name;
  
	public SonarObserverHandlerNaive(String name ) {  
		this.name    = name;
 	}
  	@Override
	public void connectionLost(Throwable cause) {
  		ColorsOut.out(name + " | connectionLost " + cause);
		
	}
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		ColorsOut.out(name + " | messageArrived " + message);
		
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		ColorsOut.out(name + " | deliveryComplete " + token);
		
	}
	@Override
	public String getName() {
 		return name;
	}
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message);
		
	}
	@Override
	public void elaborate(ApplMessage message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate ApplMessage:" + message);
		
	}
	@Override
	public void sendMsgToClient(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | sendMsgToClient:" + message);
		
	}
	@Override
	public void sendAnswerToClient(String message) {
		ColorsOut.out(name + " | sendAnswerToClient:" + message);
	}
}