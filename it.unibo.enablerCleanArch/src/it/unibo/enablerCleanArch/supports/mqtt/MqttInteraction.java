package it.unibo.enablerCleanArch.supports.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;




public class MqttInteraction implements Interaction2021{
	private MqttSupport mqtt;
	private String topic;
	
	public MqttInteraction(MqttSupport mqtt, String topic) {
		this.mqtt  = mqtt;
		this.topic = topic;
	}

	@Override
	public void forward(String msg) throws Exception {
		mqtt.publish(topic, msg, 0, false);
		
	}

	@Override
	public String request(String msg) throws Exception {
		mqtt.publish(topic, msg, 0, false);
		
		return null;
	}

	@Override
	public String receiveMsg() throws Exception {
		throw new Exception("receiveMsg not allowed for MQTT");
		//return null;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
