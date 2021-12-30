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
 * MqttSupport implementa Interaction2021 e quindi realizza il concetto di connessione nel caso di MQTT.
 * Nel caso di MQTT, una connessione è realizata usando due topic, la prima di nome t1 e la seconda di nome t1answer
 * Un EnablerServer fissa il nome t1  (ad es LedServerMqtt)
 * Un proxyclient c1 usa il nome t1 per inviare comandi e richieste al server 
 * e fa una subscribe su t1answer (o meglio su t1c1answer) per ricevere le risposte alle sue request.
 * L'operazione request del proxyclient usa la sua istanza di MqttSupport (mqttProxy) per fare la 
 * publish della richiesta su t1. 
 * Per ricevere la risposta dal server il proxyclient fa un polling (poll)
 * sulla blockingqueue di mqttProxy in modo da permettere la esecuzione 
 * di messageArrived del ClientApplHandlerMqtt che fa l'operazione di put sulla blockingqueue di mqttProxy.
 */
public class MqttSupport implements Interaction2021{
//	private static MqttSupport singletonMqttsupport = null;
//	
//	public static MqttSupport getTheSupport() {
//		if( singletonMqttsupport == null ) 
//			singletonMqttsupport = new MqttSupport();
//		return singletonMqttsupport;
//	}

	
protected MqttClient client;
protected boolean isConnected = false;
protected String topic;
protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
protected String clientid;
protected MqttCallback handler;
 	
    public MqttSupport() {
    	
    }
    
    public BlockingQueue<String> getQueue() {
    	return blockingQueue;
    }
    
	public void connect(String clientid, String topic, String brokerAddr) {
		try {
			this.clientid = clientid;
			this.topic    = topic;
			client        = new MqttClient(brokerAddr, clientid);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(options);
			isConnected = true;
			Colors.out("MqttSupport | connected " + clientid + " to " + topic);
		} catch (MqttException e) {
			isConnected = false;
			Colors.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}
	}
	
 	
	public void connectMqtt(String clientid, String topic, MqttCallback handler) {
		connect( clientid, topic, RadarSystemConfig.mqttBrokerAddr);
		this.handler = handler;
		subscribe(clientid, topic, handler);    
	}
	
	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | disconnect Error:" + e.getMessage());
		}
	}
	
	public void subscribe(String clientid, String topic) {
		subscribe( clientid, topic, new MqttSupportCallback(client.getClientId() , blockingQueue));
	}
	
	public void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
			client.setCallback( callback );	
			client.subscribe(topic);			
			Colors.out("subscribe " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, Colors.ANSI_YELLOW);
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
			//Colors.out("MqttSupport  | publish topic=" + topic + " msg=" + msg);
			message.setPayload(msg.getBytes());		 
			client.publish(topic, message);
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | publish Error:" + e.getMessage());
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		Colors.out("forward topic=" + topic + " msg=" + msg);
		try{
			new ApplMessage(msg); //no exception => we can pubish
			publish(topic, msg, 0, false);	
		}catch( Exception e ) { //The message is not structured
			ApplMessage msgAppl = Utils.buildDispatch("mqtt", "cmd", msg, "unknown");
			publish(topic, msgAppl.toString(), 0, false);
		}				
	}

	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		Colors.out(".......... request " + msg + " by clientid=" + clientid + " support=" + this);
        
		String answerTopic = topic+clientid+"answer"; //UNDERSCORE NOT ALLOWED  topic=topicLedServer
		//subscribe( answerTopic );  //Before publish 
 		//BlockingQueue<String> answerQueue = new LinkedBlockingDeque<String>(1);
		//subscribe(topic, new MqttSupportCallback(answerQueue) );
		
		//INVIO RICHIESTA su topic
		try{
			new ApplMessage(msg); //no exception => we can publish
			publish(topic, msg, 0, false);	
		}catch( Exception e ) { //The message is not structured
			ApplMessage msgAppl = Utils.buildRequest("mqtt", "request", msg, "unknown");
			publish(topic, msgAppl.toString(), 0, false);
		}	
		Colors.out("......................................................");
		//ATTESA RISPOSTA su answerTopic (subscribe done by ClientApplHandlerMqtt)
		String answer = null;
		while( answer== null ) {
			answer=blockingQueue.poll() ;
			Colors.out("MqttSupport | blockingQueue empty ..."  );
			Utils.delay(500); //il client ApplMsgHandler dovrebbe andare ...
		}
		Colors.out("MqttSupport | request answer=" + answer + " blockingQueue=" + blockingQueue);
 		try {
 			ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
 			answer = msgAnswer.msgContent(); 			
 		}catch(Exception e) {
 			Colors.out("MqttSupport | receiveMsg2 " + answer + " not structured"   ); 			
 		}
		return answer;
 	}
	 
	public void reply(String msg) throws Exception {
		Colors.out("MqttSupport | reply topic=" + topic  + " msg="+msg);
		publish(topic+"",msg,0,false);
		//forward(msg);
	}
	
	protected String receiveMsg(String topic, BlockingQueue<String> bq) throws Exception{
		Colors.out("MqttSupport | receiveMsg2 topic=" + topic + " blockingQueue=" + bq);
  		String answer = bq.take();
		Colors.out("MqttSupport | receiveMsg2 answer=" + answer + " blockingQueue=" + bq);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			Colors.out("MqttSupport | receiveMsg2 " + answer + " not structured"   ); 
  		}
		client.unsubscribe(topic);
		return answer;		 
	}
	
	protected String receiveMsg(String topic) throws Exception{
		Colors.out("MqttSupport | receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue);
		//subscribe(topic);
 		String answer = blockingQueue.take();
		Colors.out("MqttSupport | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			Colors.out("MqttSupport | receiveMsg " + answer + " not structured"   ); 			
 		}
		client.unsubscribe(topic);
		return answer;		 
	}

	@Override
	public String receiveMsg() throws Exception {		
		Colors.out("MqttSupport | receiveMsg subscribes topic=" + topic + " blockingQueue=" + blockingQueue);
		//subscribe(topic);
 		String answer = blockingQueue.take();
		Colors.out("MqttSupport | receiveMsg subscribes answer=" + answer + " blockingQueue=" + blockingQueue);
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
