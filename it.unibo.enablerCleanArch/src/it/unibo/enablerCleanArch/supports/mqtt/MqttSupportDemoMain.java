package it.unibo.enablerCleanArch.supports.mqtt;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class MqttSupportDemoMain {
private String topic      = MqttSupport.topicOut;
private String brokerAddr = RadarSystemConfig.mqttBrokerAddr; // : 1883  OPTIONAL

//String sender, String msgId, String payload, String dest
private String helloMsg  = Utils.buildDispatch("demo", "cmd",   "hello",    "anyone").toString();
private String aRequest  = Utils.buildRequest("demo",  "query", "getTime",  "anyone").toString();
private String aReply    = Utils.buildReply("demo",  "answer", "ANSWER",    "anyone").toString();

//The reply should be related to a request
	public void simulateSender() {
		new Thread() {
			public void run() {
				MqttSupport mqtt = new MqttSupport();	
				mqtt.connect("sender", topic, brokerAddr);
				Colors.out("forwarding ...");
				try {
					mqtt.forward(helloMsg);
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
				MqttSupport mqtt     = new MqttSupport();	
				mqtt.connect(name, topic, brokerAddr);
				String input         = mqtt.receiveMsg();
				ApplMessage msgInput = new ApplMessage(input);
				Colors.out(name + " | msgInput=" + msgInput + " topic="+topic);
				
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
				MqttSupport mqtt     = new MqttSupport();	
				mqtt.connect(name, topic, brokerAddr);
				String input         = mqtt.receiveMsg(); 
				ApplMessage msgInput = new ApplMessage(input);
				Colors.outappl(name + " | input=" + input + " topic="+topic, Colors.GREEN);
				if( msgInput.msgContent().equals("getTime")) {
					String answer = aReply.replace("ANSWER", ""+java.time.LocalTime.now() );
					//mqtt.publish(topic+"answer",answer,0,false);				//forward? NO topic
					mqtt.reply(answer);
				}else mqtt.publish(topic+"answer","notUnderstand",0,false);
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
				String answer = mqtt.request(aRequest);
				Colors.outappl(name + " | answer=" + answer, Colors.GREEN);
 			} catch (Exception e) {
					Colors.outerr("receiver  | Error:" + e.getMessage());
		 	}
			}//run
		}.start();
	} 
	
	
	public void doSendReceive() {
 		simulateReceiver("receiver1");
 		Utils.delay(1000);
// 		simulateReceiver("receiver2");
// 		Utils.delay(1000);
 		simulateSender();		
	}
	public void doRequest() {
 		simulateCalled("called1");
		Utils.delay(1000);
		simulateCaller("caller1");
	}

	public static void main(String[] args) throws Exception  {
		MqttSupportDemoMain sys = new MqttSupportDemoMain();	
 		sys.doSendReceive();
  		//sys.doRequest();
 	}

}
