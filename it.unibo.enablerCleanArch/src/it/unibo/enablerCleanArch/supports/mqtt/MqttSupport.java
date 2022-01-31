package it.unibo.enablerCleanArch.supports.mqtt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.eclipse.paho.client.mqttv3.*;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ApplMessageType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.ContextMsgHandler;

 
/*
 * MqttSupport implementa Interaction2021 e quindi realizza il concetto di connessione nel caso di MQTT.
 * Nel caso di MQTT, una connessione è realizata usando due topic, la prima di nome t1 
 * e la seconda di nome t1answer.
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
//protected String topic;
protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
protected String clientid;
protected IApplMsgHandler handler;
protected String brokerAddr;
protected static MqttSupport mqttSup ;
public static final String topicOut = "topicCtxMqtt";

	public static synchronized MqttSupport getSupport() {
		if( mqttSup == null  ) mqttSup = new MqttSupport();
//		if( ! mqttSup.isConnected ) {
//			IContextMsgHandler  ctxH = new ContextMqttMsgHandler ( "ctxH" );
//			//mqttSup.connectMqtt("CtxServerMqtt", myTopic , ctxH); 
//			mqttSup.connect("mqttSupport", topicOut, RadarSystemConfig.mqttBrokerAddr);
// 		}
		return mqttSup;
	}
	
    protected MqttSupport() {
    }
     
    public BlockingQueue<String> getQueue() {
    	return blockingQueue;
    }
    
    public IApplMsgHandler getHandler() {
    	return handler;
    }
    
 
    
    public void connectToBroker(String clientid,  String brokerAddr) {
		try {
 			this.brokerAddr = brokerAddr;
			client          = new MqttClient(brokerAddr, clientid);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(options);
			this.clientid   = clientid; //client.getClientId();
			isConnected = true;
			Colors.out("MqttSupport | connected client " + client.getClientId() + " to broker " + brokerAddr, Colors.CYAN);
		} catch (MqttException e) {
			isConnected = false;
			Colors.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}    	
    }
 
   
	protected void connect(String clientid, String topic, String brokerAddr) {
		if( ! isConnected )
		try {
			this.clientid   = clientid;
			//this.topic      = topic;
			this.brokerAddr = brokerAddr;
			client          = new MqttClient(brokerAddr, clientid);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(options);
			isConnected = true;
			Colors.out("MqttSupport | connected " + clientid + " to " + topic, Colors.CYAN);
		} catch (MqttException e) {
			isConnected = false;
			Colors.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}
	}
	
	
	protected void connectMqtt(String clientid, String topic, IApplMsgHandler handler) {
		connect( clientid, topic, RadarSystemConfig.mqttBrokerAddr);
		this.handler = handler;
		subscribe(clientid, topic, handler);    
		Colors.out(clientid + " | CREATED MqttSupport handler="+handler + " subscribed to " + topic, Colors.CYAN);
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
	
	public void unsubscribe( String topic ) {
		try {
			client.unsubscribe(topic);
			Colors.out("unsubscribed " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, Colors.CYAN);
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | unsubscribe Error:" + e.getMessage());
		}
	}
	
	public void subscribe ( String topic, IApplMsgHandler handler) {
		//this.handler = handler;
		subscribe(clientid, topic, handler);    
		Colors.out(clientid + " | MqttSupport handler="+handler.getName() + " subscribed to " + topic, Colors.CYAN);		
	}
	
	protected void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
			//Colors.out("subscribe " + client.getClientId() + " topic=" + topic  , , Colors.CYAN);
			client.setCallback( callback );	
			client.subscribe(topic);			
			//Colors.out("subscribed " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, , Colors.CYAN);
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
			Colors.out("MqttSupport  | publish topic=" + topic + " msg=" + msg + " client=" + client, Colors.CYAN);
			message.setPayload(msg.toString().getBytes());		 
			client.publish(topic, message);
			//Colors.out("MqttSupport  | publish-DONE on topic=" + topic, Colors.CYAN );
		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | publish Error "  + e.getMessage());
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		Colors.out("forward topic=" + topicOut + " msg=" + msg);
		try{
			new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			ApplMessage msgAppl = Utils.buildDispatch("mqtt", "cmd", msg, "unknown");
		}				
		publish(topicOut, msg, 2, false);	
	}

	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		Colors.out("... request " + msg + " by clientid=" + clientid + " support=" + this, Colors.CYAN);		
		//INVIO RICHIESTA su topic
		MqttAnswerHandler ah = null;
		ApplMessage mmm;
		try{
			 mmm = new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			Colors.outerr("MqttSupport | request warning:" + e.getMessage());
			mmm = Utils.buildRequest("mqtt", "request", msg, "unknown");
		}			
		
//Preparo per ricevere la risposta
		String sender = mmm.msgSender();
		String reqid  = mmm.msgId();
		String answerTopicName = "answ_"+reqid+"_"+sender;
		Colors.out("request answerTopicName="+answerTopicName, Colors.RED);
		ah = new MqttAnswerHandler( "replyH", blockingQueue );
		//Aggiungo il gestore della risposta con il nome del sender
		//if( handler != null ) ((ContextMsgHandler)handler).addComponent(mmm.msgSender(), ah );	
		
		MqttClient clientAnswer    = new MqttClient(brokerAddr, "clientAnswer");
		MqttConnectOptions options = new MqttConnectOptions();
		options.setKeepAliveInterval(480);
		options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
		clientAnswer.connect(options);
 		Colors.out("MqttSupport | connected clientAnswer "  , Colors.CYAN);
 		clientAnswer.setCallback(ah);
 		clientAnswer.subscribe(answerTopicName);
		
		//subscribe(answerTopicName,ah); //"xxx"

		publish(topicOut, mmm.toString(), 0, false);	
		
 		
 		
 		//ATTESA RISPOSTA su answerTopic (subscribe done by ClientApplHandlerMqtt)
		String answer = null;
		while( answer== null ) {
			answer=blockingQueue.poll() ;
			Colors.out("MqttSupport | blockingQueue-poll answer=" + answer, Colors.CYAN  );
			Utils.delay(500); //il client ApplMsgHandler dovrebbe andare ...
		}
		client.unsubscribe(answerTopicName);
		Colors.out("MqttSupport | request-answer=" + answer + " blockingQueue=" + blockingQueue, Colors.CYAN);
 		try {
 			ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
 			answer = msgAnswer.msgContent(); 		
 			//if( ah != null && handler != null) ((ContextMsgHandler)handler).removeComponent(ah.getName());
 		}catch(Exception e) {
 			Colors.outerr("MqttSupport | receiveMsg2 " + answer + " not structured"   ); 			
 		}
		return answer;
 	}
	 
	public void reply(String msg) throws Exception {
		try {
			ApplMessage m = new ApplMessage(msg);
			Colors.out("MqttSupport | reply  msg="+msg, Colors.CYAN);
			//TODO: Bisogna tenere traccia della richiesta e del caller
			String dest  = m.msgReceiver();
			String reqid = m.msgId();
			String answerTopicName = "answ_"+reqid+"_"+dest;
			publish(answerTopicName,msg,2,false); //"xxx"
			//Colors.out("MqttSupport | reply  DONE", Colors.CYAN );
 		}catch(Exception e) {
			Colors.outerr("MqttSupport | reply msg not structured " + msg);
			//publish(topic+"Answer",msg,0,false);
		}
	}
	
	protected String receiveMsg(String topic, BlockingQueue<String> bq) throws Exception{
		Colors.out("MqttSupport | receiveMsg2 topic=" + topic + " blockingQueue=" + bq, Colors.CYAN);
  		String answer = bq.take();
		Colors.out("MqttSupport | receiveMsg2 answer=" + answer + " blockingQueue=" + bq, Colors.CYAN);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			Colors.outerr("MqttSupport | receiveMsg2 " + answer + " not structured"   ); 
  		}
		client.unsubscribe(topic);
		return answer;		 
	}
	
	protected String receiveMsg(String topic) throws Exception{
		Colors.out("MqttSupport | receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue, Colors.CYAN);
		//subscribe("mqttsupport",topic);
 		String answer = blockingQueue.take();
		//Colors.out("MqttSupport | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue, Colors.CYAN);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			Colors.outerr("MqttSupport | receiveMsg " + answer + " not structured"   ); 			
 		}
		client.unsubscribe(topic);
		return answer;		 
	}

	@Override
	public String receiveMsg() throws Exception {		
		Colors.out("MqttSupport | receiveMsg ... blockingQueue=" + blockingQueue, Colors.CYAN);
  		String answer = blockingQueue.take();
		Colors.out("MqttSupport | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue, Colors.CYAN);
		return answer;
	}

	@Override
	public void close()   {
		try {
			client.disconnect();
			Colors.outappl("MqttSupport | client disconnected ", Colors.CYAN);
			client.close();
 		} catch (MqttException e) {
			Colors.outerr("MqttSupport  | close Error:" + e.getMessage());
 		}
	}
	
}
