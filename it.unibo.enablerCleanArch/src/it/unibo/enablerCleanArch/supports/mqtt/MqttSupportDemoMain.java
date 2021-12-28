package it.unibo.enablerCleanArch.supports.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class MqttSupportDemoMain {
private String topic      = "unibodemo";
private String brokerAddr = "tcp://broker.hivemq.com"; // : 1883  OPTIONAL

	public void simulateSender() {
		new Thread() {
			public void run() {
				MqttSupport mqtt = new MqttSupport();	
				mqtt.connect("sender", topic, brokerAddr);
				Colors.out("forward");
				try {
					mqtt.forward("hello");
				} catch (Exception e) {
					Colors.outerr("sender  | Error:" + e.getMessage());
 				}
				
			}
		}.start();		
	}
	
	public void simulateReceiver(String name) {
		new Thread() {
			public void run() {
			try {
				MqttSupport mqtt = new MqttSupport();	
				mqtt.connect(name, topic, brokerAddr);
				String input = mqtt.receiveMsg();
				Colors.out(name + " | input=" + input);
				
//				mqtt.connect("receiver", topic+"_answer", brokerAddr);
//				Colors.out("subscribe");
//				mqtt.subscribe(topic); //Nel connect ??				
			} catch (Exception e) {
					Colors.outerr("receiver  | Error:" + e.getMessage());
		 	}
			}//run
		}.start();
	}
	
	public void simulateCalled(String name) {
		new Thread() {
			public void run() {
			try {
				MqttSupport mqtt = new MqttSupport();	
				mqtt.connect(name, topic, brokerAddr);
				String input = mqtt.receiveMsg(); 
				Colors.out(name + " | input=" + input + " topic="+topic);
				mqtt.publish(topic+"answer","answerTo"+input,0,false);				
 			} catch (Exception e) {
					Colors.outerr("receiver  | Error:" + e.getMessage());
		 	}
			}//run
		}.start();
	}
	
	public void simulateCaller(String name) {
		new Thread() {
			public void run() {
			try {
				MqttSupport mqtt = new MqttSupport();	
				mqtt.connect(name, topic, brokerAddr);
				String answer = mqtt.request("helloRequest");
				Colors.out(name + " | answer=" + answer);
 			} catch (Exception e) {
					Colors.outerr("receiver  | Error:" + e.getMessage());
		 	}
			}//run
		}.start();
	}
	
	public static void main(String[] args) throws Exception  {
		MqttSupportDemoMain sys = new MqttSupportDemoMain();
		
//		sys.simulateReceiver("receiver1");
//		Utils.delay(1000);
//		sys.simulateReceiver("receiver2");
//		Utils.delay(1000);
		sys.simulateCalled("called1");
		Utils.delay(1000);
		sys.simulateCaller("caller1");
//		sys.simulateSender();
 	}

}
