package it.unibo.enablerCleanArch.supports;

import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface IApplMsgHandler extends MqttCallback{
	public String getName(); 
	public void elaborate( String message, Interaction2021 conn ) ;
	public void sendMsgToClient( String message, Interaction2021 conn  );
}
