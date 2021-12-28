package it.unibo.enablerCleanArch.supports.mqtt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.paho.client.mqttv3.*;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ApplMessageType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;

 
/*
 * Singleton for a specific broker
 */
public class MqttSupport implements Interaction2021{
	private static int msgNum=0;	
	private static MqttSupport singletonMqttsupport = null;
	public static MqttSupport getTheSupport() {
		if( singletonMqttsupport == null ) singletonMqttsupport = new MqttSupport();
		return singletonMqttsupport;
	}
	
	
	//String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT, String SEQNUM

		public static ApplMessage buildDispatch(String sender, String msgId, String payload, String dest) {
			try {
				return new ApplMessage(msgId, ApplMessageType.dispatch.toString(),sender,dest,payload,""+(msgNum++));
			} catch (Exception e) {
				Colors.outerr("buildDispatch ERROR:"+ e.getMessage());
				return null;
			}
		}
		
		public static ApplMessage buildRequest(String sender, String msgId, String payload, String dest) {
			try {
				return new ApplMessage(msgId, ApplMessageType.request.toString(),sender,dest,payload,""+(msgNum++));
			} catch (Exception e) {
				Colors.outerr("buildRequest ERROR:"+ e.getMessage());
				return null;
			}
		}
		public static ApplMessage buildReply(String sender, String msgId, String payload, String dest) {
			try {
				return new ApplMessage(msgId, ApplMessageType.reply.toString(),sender,dest,payload,""+(msgNum++));
			} catch (Exception e) {
				Colors.outerr("buildRequest ERROR:"+ e.getMessage());
				return null;
			}
		}
	
	
	
	
	
protected MqttClient client;
protected boolean isConnected = false;
protected String topic;
protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);

 	
	public void connect(String clientid, String topic, String brokerAddr) {
		try {
			this.topic = topic;
			client = new MqttClient(brokerAddr, clientid);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(options);
			isConnected = true;
			Colors.out("connected " + clientid);
		} catch (MqttException e) {
			isConnected = false;
			Colors.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}
	}
	
	public void connectAsEnabler(String name, MqttCallback handler) {
		connect("enabler"+name, "topic"+name, RadarSystemConfig.mqttBrokerAddr);
		subscribe("topic"+name, handler);
	}
	
	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | disconnect Error:" + e.getMessage());
		}
	}
	
	public void subscribe(String topic) {
		subscribe( topic, new MqttSupportCallback(client.getClientId() , blockingQueue));
	}
	
	public void subscribe(String topic, MqttCallback callback) {
		try {
			client.setCallback( callback );	
			client.subscribe(topic);			
			//Colors.out("subscribe topic=" + topic + " blockingQueue=" + blockingQueue);
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | subscribe Error:" + e.getMessage());
		}
	}
	
	public void publish(String topic, String msg, int qos, boolean retain) {
		MqttMessage message = new MqttMessage();
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos);
		}
		try {
			//Colors.out("publish topic=" + topic + " msg=" + msg);
			message.setPayload(msg.getBytes());		 
			client.publish(topic, message);
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | publish Error:" + e.getMessage());
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		publish(topic, msg, 0, false);		
	}

	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		String answerTopic = topic+"answer";
		subscribe( answerTopic );
		publish(topic, msg, 0, false);
		//Utils.delay(500);
		String answer = receiveMsg( answerTopic ); //UNDERSCORE NOT ALLOWED  
		return answer;
	}
	 
	public void reply(String msg) throws Exception {
		publish(topic+"answer",msg,0,false);
	}
	
	protected String receiveMsg(String topic) throws Exception{
		Colors.out("receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue);
		//subscribe(topic);
 		String answer = blockingQueue.take();
 		client.unsubscribe(topic);
		return answer;		 
	}

	@Override
	public String receiveMsg() throws Exception {		
		subscribe(topic);
 		String answer = blockingQueue.take();
 		client.unsubscribe(topic);
		return answer;
	}

	@Override
	public void close() throws Exception {
		try {
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | close Error:" + e.getMessage());
 		}
	}
	
}
