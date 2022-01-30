package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class MqttSupportDemoEssential {
private String topic      = MqttSupport.topicOut;
private String brokerAddr = RadarSystemConfig.mqttBrokerAddr; // : 1883  OPTIONAL

//String sender, String msgId, String payload, String dest
private String helloMsg  = Utils.buildDispatch("demo", "cmd",   "hello",    "anyone").toString();
private String aRequest  = Utils.buildRequest("demo",  "query", "getTime",  "anyone").toString();
private String aReply    = Utils.buildReply("demo",  "answer", "ANSWER",    "anyone").toString();

//private BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);


public void simulateReceiver(String name) {
	new Thread() {
		public void run() {
		try {
			MqttSupport mqtt = MqttSupport.getSupport();	
			Colors.outappl("simulateReceiver STARTS with " + mqtt, Colors.GREEN);
			String inputMNsg = mqtt.receiveMsg();
			Colors.outappl("RECEIVED:"+ inputMNsg, Colors.BLACK);
			
			
 		} catch (Exception e) {
			Colors.outerr("receiver  | Error:" + e.getMessage());
	 	}
		}//run
	}.start();
}


public void doSendReceive() {
 	MqttSupport mqtt    = MqttSupport.getSupport();
	Colors.outappl("doSendReceive with " + mqtt, Colors.GREEN);

	simulateReceiver("r1");
	
	
	MqttAnswerHandler h = new MqttAnswerHandler( mqtt.getQueue() );
	mqtt.connectToBroker("demo", RadarSystemConfig.mqttBrokerAddr);

	mqtt.subscribe(topic, h);
	
	try {
		mqtt.forward(helloMsg);	//OK
 	} catch (Exception e) {
		e.printStackTrace();
	}
}



public void simulateCalled(String name) {
	new Thread() {
		public void run() {
		try {
			MqttSupport mqtt     = new MqttSupport();	
			MqttAnswerHandler h = new MqttAnswerHandler( mqtt.getQueue() );
			mqtt.subscribe(topic, h);
			
			String input         = mqtt.receiveMsg(); 
			
			ApplMessage msgInput = new ApplMessage(input);
			Colors.outappl(name + " | input=" + input + " topic="+topic, Colors.GREEN);
 			String answer = aReply.replace("ANSWER", ""+java.time.LocalTime.now() );
 			mqtt.reply(answer);
 		} catch (Exception e) {
				Colors.outerr("simulateCalled  | Error:" + e.getMessage());
	 	}
		}//run 
	}.start(); 
}

public void doRequestRespond() {
 	MqttSupport mqtt    = MqttSupport.getSupport();
	Colors.outappl("doRequestRespond with " + mqtt, Colors.GREEN);

	simulateCalled("r1");
	mqtt.connectToBroker("demo", RadarSystemConfig.mqttBrokerAddr);
	
	try {
		mqtt.request(helloMsg);	 
 	} catch (Exception e) {
		e.printStackTrace();
	}
}
 
	public static void main(String[] args) throws Exception  {
		MqttSupportDemoEssential sys = new MqttSupportDemoEssential();	
 		//sys.doSendReceive();
  		sys.doRequestRespond();
 	}

}
