package it.unibo.enablerCleanArch.supports.mqtt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.eclipse.paho.client.mqttv3.*;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ApplMessageType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.ContextMsgHandler;

 
/*
 * MqttSupportIn implementa Interaction2021 e quindi realizza il concetto di connessione nel caso di MQTT.
 * Nel caso di MQTT, una connessione è realizata usando due topic, la prima di nome t1 
 * e la seconda di nome t1answer.
 * Un EnablerServer fissa il nome t1  (ad es LedServerMqtt)
 * Un proxyclient c1 usa il nome t1 per inviare comandi e richieste al server 
 * e fa una subscribe su t1answer (o meglio su t1c1answer) per ricevere le risposte alle sue request.
 * L'operazione request del proxyclient usa la sua istanza di MqttSupportIn (mqttProxy) per fare la 
 * publish della richiesta su t1. 
 * Per ricevere la risposta dal server il proxyclient fa un polling (poll)
 * sulla blockingqueue di mqttProxy in modo da permettere la esecuzione 
 * di messageArrived del ClientApplHandlerMqtt che fa l'operazione di put sulla blockingqueue di mqttProxy.
 */
public class MqttSupportIn implements Interaction2021{
//	private static MqttSupportIn singletonMqttSupportIn = null;
//	
//	public static MqttSupportIn getTheSupport() {
//		if( singletonMqttSupportIn == null ) 
//			singletonMqttSupportIn = new MqttSupportIn();
//		return singletonMqttSupportIn;
//	}

	
protected MqttClient client;
protected boolean isConnected = false;
protected String topic;
protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
protected String clientid;
protected IApplMsgHandler handler;
protected String brokerAddr;
protected static MqttSupportIn mqttSup ;
public static final String topicOut = "topicCtxMqtt";

	public static MqttSupportIn getSupport() {
		if( mqttSup == null  ) mqttSup = new MqttSupportIn();
		if( ! mqttSup.isConnected ) {
			IContextMsgHandler  ctxH = new ContextMqttMsgHandler ( "ctxH" );
			//mqttSup.connectMqtt("CtxServerMqtt", myTopic , ctxH); 
			mqttSup.connect("MqttSupportIn", topicOut, RadarSystemConfig.mqttBrokerAddr);
 		}
		return mqttSup;
	}
	
    protected MqttSupportIn() {
    }
     
    public BlockingQueue<String> getQueue() {
    	return blockingQueue;
    }
    
    public IApplMsgHandler getHandler() {
    	return handler;
    }
    
	protected void connect(String clientid, String topic, String brokerAddr) {
		if( ! isConnected )
		try {
			this.clientid   = clientid;
			this.topic      = topic;
			this.brokerAddr = brokerAddr;
			client          = new MqttClient(brokerAddr, clientid);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(options);
			isConnected = true;
			ColorsOut.out("MqttSupportIn | connected " + clientid + " to " + topic, ColorsOut.BgYellow);
		} catch (MqttException e) {
			isConnected = false;
			ColorsOut.outerr("MqttSupportIn  | connect Error:" + e.getMessage());
		}
	}
	
 	
	public void connectMqtt(String clientid, String topic, IApplMsgHandler handler) {
		connect( clientid, topic, RadarSystemConfig.mqttBrokerAddr);
		this.handler = handler;
		subscribe(clientid, topic, handler);    
		ColorsOut.out(clientid + " | CREATED MqttSupportIn handler="+handler + " subscribed to " + topic);
	}
	
	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupportIn  | disconnect Error:" + e.getMessage());
		}
	}
	
	public void subscribe(String clientid, String topic) {
		subscribe( clientid, topic, new MqttSupportCallback(client.getClientId() , blockingQueue));
	}
	
	public void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
			client.setCallback( callback );	
			client.subscribe(topic);			
			ColorsOut.out("subscribe " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, ColorsOut.BLUE);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupportIn  | subscribe Error:" + e.getMessage());
		}
	}
	
	private int nattempts = 0;
	public void publish(String topic, String msg, int qos, boolean retain) {
		MqttMessage message = new MqttMessage();
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos);
		}
		try {
			ColorsOut.out("MqttSupportIn  | publish topic=" + topic + " msg=" + msg + " client=" + client);
			message.setPayload(msg.getBytes());		 
			client.publish(topic, message);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupportIn  | publish Error " + nattempts + ":" + e.getMessage());
			if( nattempts++ > 3 ) { 
				ColorsOut.outerr("MqttSupportIn  | publish NO MORE POSSIBLE"  );
				System.exit(10);
				//connectMqtt( clientid,  topic,  handler);
				//publish(  topic,   msg,   qos,   retain);
			} 
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		ColorsOut.out("forward topic=" + topic + " msg=" + msg);
		try{
			new ApplMessage(msg); //no exception => we can publish
			publish(topic, msg, 0, false);	
		}catch( Exception e ) { //The message is not structured
			ApplMessage msgAppl = Utils.buildDispatch("mqtt", "cmd", msg, "unknown");
			publish(topic, msgAppl.toString(), 0, false);
		}				
	}

	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		ColorsOut.out("... request " + msg + " by clientid=" + clientid + " support=" + this);
		ColorsOut.out("... request  handler=" + handler);
        
		//String answerTopic = topic+clientid+"answer"; //UNDERSCORE NOT ALLOWED  topic=topicLedServer
		//subscribe( answerTopic );  //Before publish 
 		//BlockingQueue<String> answerQueue = new LinkedBlockingDeque<String>(1);
		//subscribe(topic, new MqttSupportInCallback(answerQueue) );
		
		//INVIO RICHIESTA su topic
		MqttAnswerHandler ah = null;
		try{
			ApplMessage mmm = new ApplMessage(msg); //no exception => we can publish
			publish(topic, msg, 0, false);	
			ah = new MqttAnswerHandler( blockingQueue );
			//Aggiungo il gestore della risposta con il nome del sender
			((ContextMsgHandler)handler).addComponent(mmm.msgSender(), ah );				
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outerr("MqttSupportIn | request ERROR:" + e.getMessage());
			ApplMessage msgAppl = Utils.buildRequest("mqtt", "request", msg, "unknown");
			publish(topic, msgAppl.toString(), 0, false); //????
		}	
 		//ATTESA RISPOSTA su answerTopic (subscribe done by ClientApplHandlerMqtt)
		String answer = null;
		while( answer== null ) {
			answer=blockingQueue.poll() ;
			ColorsOut.out("MqttSupportIn | blockingQueue poll answer=" + answer  );
			Utils.delay(500); //il client ApplMsgHandler dovrebbe andare ...
		}
		ColorsOut.out("MqttSupportIn | request answer=" + answer + " blockingQueue=" + blockingQueue);
 		try {
 			ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
 			answer = msgAnswer.msgContent(); 		
 			if( ah != null) ((ContextMsgHandler)handler).removeComponent(ah.getName());
 		}catch(Exception e) {
 			ColorsOut.out("MqttSupportIn | receiveMsg2 " + answer + " not structured"   ); 			
 		}
		return answer;
 	}
	 
	public void reply(String msg) throws Exception {
		try {
			ApplMessage m = new ApplMessage(msg);
			ColorsOut.out("MqttSupportIn | reply topic=" + topic  + " msg="+msg);
			String dest = m.msgReceiver();
			publish(topic,msg,0,false);
 		}catch(Exception e) {
			ColorsOut.outerr("MqttSupportIn | reply msg not structured " + msg);
			//publish(topic+"Answer",msg,0,false);
		}
	}
	
	protected String receiveMsg(String topic, BlockingQueue<String> bq) throws Exception{
		ColorsOut.out("MqttSupportIn | receiveMsg2 topic=" + topic + " blockingQueue=" + bq);
  		String answer = bq.take();
		ColorsOut.out("MqttSupportIn | receiveMsg2 answer=" + answer + " blockingQueue=" + bq);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			ColorsOut.out("MqttSupportIn | receiveMsg2 " + answer + " not structured"   ); 
  		}
		client.unsubscribe(topic);
		return answer;		 
	}
	
	protected String receiveMsg(String topic) throws Exception{
		ColorsOut.out("MqttSupportIn | receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue);
		//subscribe(topic);
 		String answer = blockingQueue.take();
		ColorsOut.out("MqttSupportIn | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			ColorsOut.out("MqttSupportIn | receiveMsg " + answer + " not structured"   ); 			
 		}
		client.unsubscribe(topic);
		return answer;		 
	}

	@Override
	public String receiveMsg() throws Exception {		
		ColorsOut.out("MqttSupportIn | receiveMsg subscribes topic=" + topic + " blockingQueue=" + blockingQueue);
		//subscribe(topic);
 		String answer = blockingQueue.take();
		ColorsOut.out("MqttSupportIn | receiveMsg subscribes answer=" + answer + " blockingQueue=" + blockingQueue);
 		client.unsubscribe(topic);
		return answer;
	}

	@Override
	public void close() throws Exception {
		try {
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupportIn  | close Error:" + e.getMessage());
 		}
	}
	
}
