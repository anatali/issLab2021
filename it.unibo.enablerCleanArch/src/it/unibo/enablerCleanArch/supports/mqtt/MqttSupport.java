package it.unibo.enablerCleanArch.supports.mqtt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.eclipse.paho.client.mqttv3.*;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandlerMqtt;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.IContextMsgHandlerMqtt;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
 

 
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
public class MqttSupport implements Interaction2021 {
 	
protected MqttClient client;
public static final String topicInput = "topicCtxMqtt";
protected static MqttSupport mqttSup ;  //to realize a singleton

protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
protected String clientid;
protected IContextMsgHandlerMqtt handler;
protected String brokerAddr;
protected boolean isConnected   = false;


	public static synchronized MqttSupport getSupport( ) {
		//if( mqttSup == null  ) mqttSup = new MqttSupport("mqttSupport", MqttSupport.topicOut);
		return mqttSup;
	}
	public static synchronized MqttSupport createSupport(String clientName, String topicToSubscribe) {
		if( mqttSup == null  ) mqttSup = new MqttSupport(clientName,topicToSubscribe);
		return mqttSup;
	}
	
    protected MqttSupport(String clientName, String topicToSubscribe) {
    	connectToBroker(clientName, RadarSystemConfig.mqttBrokerAddr);	   	
		handler = new ContextMqttMsgHandler( "ctxH"  );
    	subscribe(topicToSubscribe, handler);
    }
     
    public BlockingQueue<String> getQueue() {
    	return blockingQueue;
    }
    
    public IContextMsgHandlerMqtt getHandler() {
    	return handler;
    }
    
    
    
    public void connectToBroker(String clientid,  String brokerAddr) {
    	if( isConnected ) return;
		try {
			ColorsOut.out("MqttSupport | connectToBroker clientid " + clientid  + " to broker " + brokerAddr );
			this.brokerAddr = brokerAddr;
			client          = new MqttClient(brokerAddr, clientid);
			ColorsOut.out("MqttSupport | connectToBroker clientSupportId=" + client.getClientId() );
			MqttConnectOptions connOpts = new MqttConnectOptions();
			
		    connOpts.setCleanSession(true);
		    //connOpts.setUserName(userName);
		    //connOpts.setPassword(passWord.toCharArray());
		    /* 
		     * This value, measured in seconds, defines the maximum time interval the client  
 			 *  will wait for the network connection to the MQTT server to be established
		    */
		    connOpts.setConnectionTimeout(60); 
		    /* 
		     * This value, measured in seconds, defines the maximum time interval between 
		     * messages sent or received
		     */
		    connOpts.setKeepAliveInterval(30); 
		    connOpts.setAutomaticReconnect(true);			
			
//			options.setKeepAliveInterval(480);
//			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			client.connect(connOpts);
			this.clientid   = clientid; //client.getClientId();
			isConnected = true;
 			
			ColorsOut.out("MqttSupport | connected client " + client.getClientId() + " to broker " + brokerAddr );
		} catch (MqttException e) {
			isConnected = false;
			ColorsOut.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}    	
    }
 
   
	protected void connect(String clientid, String topic, String brokerAddr) {
		if( isConnected ) return;
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
			ColorsOut.out("MqttSupport | connected " + clientid + " to " + topic, ColorsOut.CYAN);
		} catch (MqttException e) {
			isConnected = false;
			ColorsOut.outerr("MqttSupport  | connect Error:" + e.getMessage());
		}
	}
	
/*	
	protected void connectMqtt(String clientid, String topic, IApplMsgHandlerMqtt handler) {
		connect( clientid, topic, RadarSystemConfig.mqttBrokerAddr);
		this.handler = handler;
		subscribe(clientid, topic, handler);    
		ColorsOut.out(clientid + " | CREATED MqttSupport handler="+handler + " subscribed to " + topic, ColorsOut.CYAN);
	}
*/	
	public void disconnect() {
		try {
			client.disconnect();
			client.close();
			isConnected = false;
			ColorsOut.out(clientid + " | disconnect ", ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupport  | disconnect Error:" + e.getMessage());
		}
	}
	
	public void subscribe(String clientid, String topic) {
		subscribe( clientid, topic, new MqttSupportCallback(client.getClientId() , blockingQueue));
	}
	
	public void unsubscribe( String topic ) {
		try {
			client.unsubscribe(topic);
			ColorsOut.out("unsubscribed " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupport  | unsubscribe Error:" + e.getMessage());
		}
	}
	
	public void subscribe ( String topic, IApplMsgHandlerMqtt handler) {
		//this.handler = handler;
		subscribe(clientid, topic, handler);    
		ColorsOut.out(clientid + " | MqttSupport handler="+handler.getName() + " subscribed to " + topic, ColorsOut.CYAN);		
	}
	
	protected void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
			//Colors.out("subscribe " + client.getClientId() + " topic=" + topic  , , Colors.CYAN);
			client.setCallback( callback );	
			client.subscribe(topic);			
			//Colors.out("subscribed " + clientid + " topic=" + topic + " blockingQueue=" + blockingQueue, , Colors.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupport  | subscribe Error:" + e.getMessage());
		}
	}
	
 
	
	public void publish(String topic, String msg, int qos, boolean retain) {
		MqttMessage message = new MqttMessage();
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos);
		}
		try {
			ColorsOut.out("MqttSupport  | publish topic=" + topic + " msg=" + msg + " client=" + client, ColorsOut.CYAN);
			message.setPayload(msg.toString().getBytes());		 
			client.publish(topic, message);
			//Colors.out("MqttSupport  | publish-DONE on topic=" + topic, Colors.CYAN );
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupport  | publish Error "  + e.getMessage());
		}
	}
	
//----------------------------------------------------	
	@Override
	public void forward(String msg) throws Exception {
		//Colors.out("forward topic=" + topicOut + " msg=" + msg);
		try{
			new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			ApplMessage msgAppl = Utils.buildDispatch("mqtt", "cmd", msg, "unknown");
		}				
		publish(topicInput, msg, 2, false);	
	}

	
	protected MqttClient setupConnectionFroAnswer(String answerTopicName) {
		try{
			MqttClient clientAnswer    = new MqttClient(brokerAddr, "clientAnswer");
			MqttConnectOptions options = new MqttConnectOptions();
			options.setKeepAliveInterval(480);
			options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);  
			clientAnswer.connect(options);
	 		//Colors.out("MqttSupport | connected clientAnswer to " + brokerAddr  , Colors.CYAN);
	 		MqttAnswerHandler ah = new MqttAnswerHandler( "replyH", blockingQueue );	
	 		clientAnswer.setCallback(ah);
	 		clientAnswer.subscribe(answerTopicName);		
	 		return clientAnswer;
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outerr("MqttSupport | setupConnectionFroAnswer ERROR:" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public String request(String msg) throws Exception { //msg should contain the name of the sender
		ColorsOut.out("... request " + msg + " by clientid=" + clientid + " support=" + this, ColorsOut.CYAN);		
		//INVIO RICHIESTA su topic
//		MqttAnswerHandler ah = null;
		ApplMessage requestMsg;
		try{
			 requestMsg = new ApplMessage(msg); //no exception => we can publish
		}catch( Exception e ) { //The message is not structured
			ColorsOut.outerr("MqttSupport | request warning:" + e.getMessage());
			requestMsg = Utils.buildRequest("mqtt", "request", msg, "unknown");
		}			
		
//Preparo per ricevere la risposta
		String sender = requestMsg.msgSender();
		String reqid  = requestMsg.msgId();
		String answerTopicName = "answ_"+reqid+"_"+sender;
		ColorsOut.out("request answerTopicName="+answerTopicName, ColorsOut.RED);
//		ah = new MqttAnswerHandler( "replyH", blockingQueue );	
		MqttClient clientAnswer    = setupConnectionFroAnswer(answerTopicName);

//Invio la richiesta 		
		publish(topicInput, requestMsg.toString(), 2, false);	
 		
 		//ATTESA RISPOSTA su answerTopic (subscribe done by ClientApplHandlerMqtt)
		String answer = null;
		while( answer== null ) {
			answer=blockingQueue.poll() ;
			ColorsOut.out("MqttSupport | blockingQueue-poll answer=" + answer, ColorsOut.CYAN  );
			Utils.delay(200); //il client ApplMsgHandler dovrebbe andare ...
		}	
		ColorsOut.out("MqttSupport | request-answer=" + answer + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
 		try {
 			ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
 			answer = msgAnswer.msgContent(); 		
 			//Disconnect ancd close the answer client
 			clientAnswer.disconnect();
 			clientAnswer.close();
   		}catch(Exception e) {
 			ColorsOut.outerr("MqttSupport | request-answer ERROR: " + e.getMessage()   ); 			
 		}
		return answer;
 	}
	 
	public void reply(String msg) throws Exception {
		try {
			ApplMessage m = new ApplMessage(msg);
			//Colors.out("MqttSupport | reply  msg="+msg, Colors.CYAN);
			//TODO: Si potrebbe tenere traccia della richiesta e del caller
			String dest  = m.msgReceiver();
			String reqid = m.msgId();
			String answerTopicName = "answ_"+reqid+"_"+dest;
			publish(answerTopicName,msg,2,false); //"xxx"
			//Colors.out("MqttSupport | reply  DONE", Colors.CYAN );
 		}catch(Exception e) {
			ColorsOut.outerr("MqttSupport | reply msg not structured " + msg);
			//publish(topic+"Answer",msg,0,false);
		}
	}
	
	protected String receiveMsg(String topic, BlockingQueue<String> bq) throws Exception{
		ColorsOut.out("MqttSupport | receiveMsg2 topic=" + topic + " blockingQueue=" + bq, ColorsOut.CYAN);
  		String answer = bq.take();
		ColorsOut.out("MqttSupport | receiveMsg2 answer=" + answer + " blockingQueue=" + bq, ColorsOut.CYAN);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			ColorsOut.outerr("MqttSupport | receiveMsg2 " + answer + " not structured"   ); 
  		}
		client.unsubscribe(topic);
		return answer;		 
	}
	
	protected String receiveMsg(String topic) throws Exception{
		ColorsOut.out("MqttSupport | receiveMsg topic=" + topic + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
		//subscribe("mqttsupport",topic);
 		String answer = blockingQueue.take();
		//Colors.out("MqttSupport | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue, Colors.CYAN);
 		try {
 			ApplMessage msg = new ApplMessage(answer); //answer is structured
 			answer = msg.msgContent(); 			
 		}catch(Exception e) {
 			ColorsOut.outerr("MqttSupport | receiveMsg " + answer + " not structured"   ); 			
 		}
		client.unsubscribe(topic);
		return answer;		 
	}

	@Override
	public String receiveMsg() throws Exception {		
		ColorsOut.out("MqttSupport | receiveMsg ... blockingQueue=" + blockingQueue, ColorsOut.CYAN);
  		String answer = blockingQueue.take();
		ColorsOut.out("MqttSupport | receiveMsg answer=" + answer + " blockingQueue=" + blockingQueue, ColorsOut.CYAN);
		return answer;
	}

	@Override
	public void close()   {
		try {
			client.disconnect();
			client.close();
			ColorsOut.outappl("MqttSupport | client disconnected and closed ", ColorsOut.CYAN);
		} catch (MqttException e) {
			ColorsOut.outerr("MqttSupport  | close Error:" + e.getMessage());
 		}
	}
	
}
