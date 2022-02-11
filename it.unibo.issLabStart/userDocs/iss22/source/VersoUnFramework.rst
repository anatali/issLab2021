.. role:: red 
.. role:: blue 
.. role:: remark
  
.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/

.. _californium: https://www.eclipse.org/californium/

.. _paho: https://www.eclipse.org/paho/

==================================================
Verso un framework
==================================================

I primi SPRINT dello sviluppo hanno seguito un processo bottom-up, che ha fatto riferimento
a TCP come protocollo per le comunicazioni di rete.

Abbiamo anche costruito un  :ref:`prototipo<primoPrototipo>` di una versione distribuita del sistema, 
la cui architettura è schematizzata nella figura che segue:

.. image:: ./_static/img/Radar/sysDistr1.PNG
   :align: center 
   :width: 70%

Con maggior dettaglio, questa architettura si basa sugli elementi costitutivi di figura:

.. image:: ./_static/img/Architectures/framework0.PNG
   :align: center  
   :width: 70%


- Un oggetto (POJO) di interfaccia ``Ixxx`` che definisce il comportamento di un dispositivo reale o simulato.   
- Un oggetto di interfaccia :ref:`IApplIntepreter<IApplIntepreterEsteso>` che trasforma messaggi (di comando e richieste
  di informazione)   in chiamate a metodi di ``Ixxx``.
- Un oggetto di interfaccia :ref:`IApplMsgHandler<IApplMsgHandlerEsteso>` che definisce il codice di gestione
  dei messaggi di livello applicativo indirizzati a un particolare dispositivo.
- Un oggetto di tipo :ref:`ContextMsgHandler<ContextMsgHandler>` che realizza un gestore dei sistema dei messaggi 
  li reindirizza (dispatching) agli opportuni handler applicativi.
- Un (unico) :ref:`TcpContextServer<TcpContextServer>` attivato su un nodo di elaborazione ``A`` (ad esempio un Raspberry) che 
  permette a componenti :ref:`proxy<ProxyAsClient>` allocati su nodi esterni  (ad esempio un PC)
  di interagire con i dispositivi allocati su ``A``. Questo componente è un :ref:`TcpServer<TcpServer>` che crea un 
  :ref:`TcpApplMessageHandler` per ogni connessione, il quale riceve i messaggi e chiama il 
  :ref:`ContextMsgHandler<ContextMsgHandler>`.

La domanda che ci poniamo ora è se questa organizzazione possa essere riusata nel caso in cui si voglia sostituire
al protocolllo TCP un altro protocollo, tra quelli indicati in :ref:`ProtocolType`.

- Il caso ``UDP``: la possibilità di sostituire TCP con UDP è  resa possibile dalla libreria  ``unibonoawtsupports.jar`` sviluppata
  in anni passati. Il compito non si è rivelato troppo difficle, visto la relativa vicinanza operazionale tra le
  librerie dei due protocolli.
- Il caso ``HTTP``: affronteremo l'uso di questo protocollo più avanti, in relazione alla costruzione di un componente  
  Web GUI (se veda IssHttpSupport).
 
Più arduo sembra invece il caso di un protocollo di tipo publish-subscribe come MQTT o di un protocollo REST come CoAP
che cambiano l'impostazione logica della interazione. 

In ogni caso, dovremo costruire le nostre astrazioni utilizzando le librerie disponibili.

------------------------------------
Liberie di riferimento
------------------------------------

Come librerie di riferimento useremo le seguenti:

- per MQTT: la libreria `paho`_
- per CoAP: la libreria `californium`_


---------------------------------------------------------
I supporti per :ref:`Interaction2021<Interaction2021>`
---------------------------------------------------------

Il :ref:`tcpsupportClient` crea l'implemetazione TCP di :ref:`Interaction2021<Interaction2021>` 
introdotta a suo tempo, come oggetto di classe :ref:`TcpConnection<TcpConnection>`.

La creazione di analoghi supporti per MQTT e CoAP  parte dalle seguenti osservazioni:

- per MQTT si tratta di creare una connessione con un broker che media la interazione tra mittente
  e destinatario
- per CoAP si tratta di utilizzare un oggetto  
  di classe ``CoapClient`` di `californium`_, che richiede come argomento l'``URL`` della risorsa
  a cui ci si vuole connettere, che ha la forma:

  .. code:: 

    "coap://"+host + ":5683/"+ entry

.. _MqttConnection:

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
``MqttConnection`` implementa :ref:`Interaction2021<Interaction2021>`
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Come :ref:`TcpConnection`, la classe ``MqttConnection`` implementa :ref:`Interaction2021` 
e quindi realizza il concetto di connessione tenendo conto delle seguenti caratteristiche del protocollo
MQTT e della libreiria `paho`_:

- non vi è più (come in TCP)  una connessione punto-a-punto con il nodo destinatario ma una connessione punto-a-punto
  con un Broker (il cui indirizzo sarà nel parametro di configurazione ``RadarSystemConfig.mqttBrokerAddr``);
- la connessione col Broker viene effettuata da un client  di classe ``org.eclipse.paho.client.mqttv3.MqttClient``
  che deve avere un preciso ``clientId`` (di tipo ``String``). Il Broker accetta una sola connessione per volta
  da un dato ``clientId`` e dunque la ``MqttConnection`` fornisce un singleton.

.. code:: java

  public class MqttConnection implements Interaction2021
  public static final String topicInput = "topicCtxMqtt";   
  protected static MqttConnection mqttSup ;  //for singleton

  protected BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>(10);
  protected String clientid;

    //Factory method
    public static synchronized MqttConnection getSupport( ){ ... }
    //Get the singleton
    public static MqttConnection getSupport() { return mqttSup; }
    
    //Hidden costructor
    protected MqttConnection( String clientName ) {
      connectToBroker(clientName, RadarSystemConfig.mqttBrokerAddr);	   	
    }
    public void connectToBroker(String clientid,  String brokerAddr) { 
      ... 
      client  = new MqttClient(brokerAddr, clientid);
    }


Il costruttore del singleton  ``MqttConnection``crea un ``MqttClient`` con ``clientId``, 
il quale si connette al Broker.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
publish e subscribe
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

- ``MqttConnection`` realizza l'invio di un messaggio invocando l'operazione ``publish`` su una topic;
- la ricezione di un messaggio si realizza attraverso la ``subscribe`` ad una topic; i messaggi pubblicati su
  questa topic posssono essere gestiti associando al client (col metodo ``setCallback``) un oggetto di classe 
  ``org.eclipse.paho.client.mqttv3.MqttCallback`` 

 
.. code:: java 

    public void publish(String topic, String msg, int qos, boolean retain) {
		MqttMessage message = new MqttMessage();
      if (qos == 0 || qos == 1 || qos == 2) {
        //qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
        message.setQos(qos);
      }
      try {
        message.setPayload(msg.toString().getBytes());		 
        client.publish(topic, message);
      } catch (MqttException e) { ...  }
	  }

    //To receive and handle a messagge (command or request)
    public void subscribe ( String topic, IApplMsgHandlerMqtt handler) {
      subscribe(clientid, topic, handler);    
    }
    protected void subscribe(String clientid, String topic, MqttCallback callback) {
      try {
        client.setCallback( callback );	
        client.subscribe( topic );			
      } catch (MqttException e) { ...		}
	  }

Poichè la gestione di un messaggio è competenza del livello applicativo, l'handler passato alla
``subscribe`` deve rispettare un contratto imposto sia dal nostro framework sia dalla libreria.
Per questo l'oggetto di callback deve implementare una interfaccia che estende :ref:`IApplMsgHandler`.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
IApplMsgHandlerMqtt
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

   public interface IApplMsgHandlerMqtt extends IApplMsgHandler, MqttCallback{}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il metodo forward  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

  @Override
  public void forward(String msg) throws Exception {
		try{
      new ApplMessage(msg); //no exception => we can publish
    }catch( Exception e ) { //The message is not structured
      ApplMessage msgAppl = Utils.buildDispatch("mqtt", "cmd", msg, "unknown");
    }				
    publish(topicInput, msg, 2, false);	
	}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Connessione come coppia di topic
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Una connessione di tipo :ref:`Interaction2021` viene da noi realizzata usando due topic: 
una per ricevere messaggi e una per inviare risposte relative ai messaggi di richiesta. 

Se la topic di ricezione ha nome ``t1``, la topic per le risposte deve avere il nome ``t1CXanswer`` 
ove ``CX`` è il nome del client che ha inviato una richiesta su ``t1``. 
Ad esempio, un proxyclient di nome ``ledPxy`` che usa la topic ``ctxEntry`` per inviare comandi e richieste al 
ContextServer,  fa una subscribe su ``ctxEntryledPxyanswer`` per ricevere le risposte.

.. code:: java

  //To receive and handle an answer
  public void subscribe(String clientid, String answertopic) {
    subscribe( clientid, answertopic, 
      new MqttConnectionCallback(client.getClientId(), blockingQueue)
    );
  }

Per permettere al livello applicativo di ricevere una risposta, l'handler di callback associato alla 
answertopic (``MqttConnectionCallback``) provvede a inserire il messaggio nella ``blockingQueue`` del supporto.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il metodo request  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il metodo ``request`` di :ref:`Interaction2021<Interaction2021>` viene implementato facendo una ``publish`` sulla entry-topic
del nodo destinatario per poi far attendere la risposta a un nuovo client temporaneo appositamente creato per 
sottosrviversi sulla answertopic.

.. code:: java

  //To send a request and wait for the answer
  @Override
  public String request(String msg) throws Exception { 
    ApplMessage requestMsg = new ApplMessage(msg);

    //Preparo per ricevere la risposta
    String sender = requestMsg.msgSender();
    String reqid  = requestMsg.msgId();
    String answerTopicName = "answ_"+reqid+"_"+sender;
    MqttClient clientAnswer = setupConnectionFroAnswer(answerTopicName);

    //publish(topicInput, requestMsg.toString(), 2, false); //qos=2 !
    forward( requestMsg.toString() );
		String answer = receiveMsg();
		clientAnswer.disconnect();
		clientAnswer.close();
  }
 
Il metodo ``waitFroAnswerBlocking`` attende la risposta sulla  ``blockingQueue`` e, quando questa
arriva, disattiva il client temporaneo.

.. code:: java

  @Override
  public String receiveMsg() throws Exception {		
    String answer = blockingQueue.take();
    ApplMessage msgAnswer = new ApplMessage(answer); //answer is structured
    answer = msgAnswer.msgContent(); 		
    return answer;
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il metodo reply  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il metodo reply viene implementato facendo una public sulla answertopic, il cui nome viene costruito
ai partire dai dati contenuti nel messaggio

.. code:: java

	public void reply(String msg) throws Exception {
		try {
			ApplMessage m = new ApplMessage(msg);
			String dest   = m.msgReceiver();
			String reqid  = m.msgId();
			String answerTopicName = "answ_"+reqid+"_"+dest;
			publish(answerTopicName,msg,2,false); //"xxx"
 		}catch(Exception e) { ... 		}
	}




.. image:: ./_static/img/Radar/MqttConn.PNG
   :align: center  
   :width: 70%



---------------------------------
I ContextServer
---------------------------------

Come primo passo per la definizione di un nostro framework di supporto alle applicazioni distribuite, 
introduciamo un contratto per il concetto di ContextServer che imponga metodi per attivare/disattivare il server e per
aggiungere/rimuovere componenti di tipo :ref:`IApplMsgHandler<IApplMsgHandler>`:


++++++++++++++++++++++++++++++++++++++++++++++
IContext
++++++++++++++++++++++++++++++++++++++++++++++

.. code:: java

  public interface IContext {
    public void addComponent( String name, IApplMsgHandler h);
    public void removeComponent( String name );
    public void activate();
    public void deactivate();
  }

Questo contratto è già rispettato da :ref:`TcpContextServer`, così che possiamo estendere la sua definizione come segue: 

  public class TcpContextServer extends TcpServer **implements IContext**

Dobbiamo ora introdurre un ContextServer per MQTT (si veda :ref:`MqttContextServer`) 
e per CoAP (si veda :ref:`CoapContextServer`).

.. Individuare i punti in cui occorre tenere conto dello specifico protocollo per definire i parametri delle *operazioni astratte*

Al solito, è opportuno definire  una Factory per la creazione di un ContextServer in funzione del protocolllo:


++++++++++++++++++++++++++++++++++++++++++++++
Context2021
++++++++++++++++++++++++++++++++++++++++++++++

.. code:: java

  public class Context2021 {

    public static IContext create(String id, String entry ) {
    IContext ctx = null;
    ProtocolType protocol = RadarSystemConfig.protcolType;
      switch( protocol ) {
      case tcp : {
        ctx=new TcpContextServer(id, entry);
        ctx.activate();
        break;
      }
      case mqtt : {
        ctx= new MqttContextServer( id, entry);
        ctx.activate();
        break;
      }
      case coap : {
        ctx = new CoapContextServer( );
        ctx.activate();
        break;
      }
      default:
        break;
      }
      return ctx;
    }//create  

 


I parametri ``id`` ed ``entry`` da specificare nel costruttore nei vari casi sono:

===========================   ===========================    =========================== 
        Server                            id                        entry
---------------------------   ---------------------------    ---------------------------
:ref:`TcpContextServer`               nome dell'host                  port
:ref:`MqttContextServer`              id del client              nome di una topic     
:ref:`CoapContextServer`                    -                      -
===========================   ===========================    ===========================   


Il :ref:`CoapContextServer` non ha bisogno di parametri in quanto per attivarlo occore conoscere
l'indirizzo del broker (unico per tutti i componenti del sistema), definito nel parametro di configurazione:
``RadarSystemConfig.mqttBrokerAddr``.
 


+++++++++++++++++++++++++++++++
IContextMsgHandler
+++++++++++++++++++++++++++++++

Ogni ContextServer necessita di un gestore di sistema dei messaggi  come il 
:ref:`ContextMsgHandler<ContextMsgHandler>` già definito per il TCP.

Introduciamo anche per questo gestore un contratto che imponga la implementazione di metodi per
aggiungere/rimuovere oggetti applicativi di tipo :ref:`IApplMsgHandler<IApplMsgHandler>`
(cosa che :ref:`ContextMsgHandler<ContextMsgHandler>` fa già).

  .. code:: java

    public interface IContextMsgHandler extends IApplMsgHandler{
      public void addComponent( String name, IApplMsgHandler h);
      public void removeComponent( String name );
      public IApplMsgHandler getHandler( String name );
    } 

L'operazione ``getHandler`` (che va ora aggiunta a :ref:`ContextMsgHandler<ContextMsgHandler>`) 
permette di ottenere il riferimento a un oggetto applicativo 'registrato' nel contesto, 
dato il nome dell'oggetto.

La situazione, generalizzata con le interfacce, si presenta ora come segue:


.. image:: ./_static/img/Architectures/framework1.PNG
   :align: center  
   :width: 70%

Osserviamo che il framework:

:remark:`realizza una infrastruttura di comunicazione`

:remark:`permette di creare componenti applicativi capaci di intergire in rete`

:remark:`impone che ogni componente applicativo abbia un nome univoco`

 
Il lavoro che ora dobbiamo compiere ora consiste nel definire un Proxy client e un ContextServer relativo a ogni protocollo. 

.. _libreriaProtocolli:



.. _ProxyAsClientEsteso:

--------------------------------------------------------
Estensione della classe :ref:`ProxyAsClient`
--------------------------------------------------------
 
Il metodo ``setConnection`` invocato dal costruttore crea un supporto diverso per ogni protocollo, ma 
ciasuno realizza l'idea di connessione, implementando l'interf:ref:`Interaction2021<Interaction2021>`.


.. code:: java

  public class ProxyAsClient {
  private Interaction2021 conn; 
    public ProxyAsClient( 
      String name, String host, String entry, ProtocolType protocol ){ 
        ... 
        setConnection(host, entry, protocol);
      }  
   ....
  protected void setConnection( String host, String entry, 
                ProtocolType protocol  ) throws Exception {
    switch( protocol ) {
    case tcp : {
      int port = Integer.parseInt(entry);
      int numOfAttempts = 10;
      conn = TcpClientSupport.connect(host,port,numOfAttempts);  
      break;
    }
    case coap : {
      conn = new CoapSupport("CoapSupport_"+name, host,entry);//entry is uri path
      break;
    }
    case mqtt : {
      conn = MqttConnection.getSupport();					
      break;
    }	
    default :{
      ColorsOut.outerr(name + " | Protocol unknown");
    }
  }



---------------------------------------
I nuovi ContextServer
---------------------------------------

Abbiamo già introdotto :ref:`TcpContextServer` come implementazione di :ref:`IContext`
che utilizza librerie per la gestione di *Socket*.

La creazione di analoghi ContextServer per MQTT e CoAP parte dalla disponibilità 
delle citate :ref:`librerie<libreriaProtocolli>`.

+++++++++++++++++++++++++++++++++++++++
ContextServer per MQTT
+++++++++++++++++++++++++++++++++++++++

Un ContextServer per MQTT richiede che un client di classe ``org.eclipse.paho.client.mqttv3.MqttClient`` 
si connetta al nodo facendo una subscribe alla  *topic* specificata dal parametro ``entry``.

Il server crea, al momento della costruzione:

- un oggetto (``ctxMsgHandler``) per la gestione dei messaggi di sistema
- un oggetto (``mqtt`` di tipo :ref:`MqttConnection` ) che realizza l'astrazione connessione implementando 
  :ref:`Interaction2021`.


.. image:: ./_static/img/Architectures/frameworkMqtt.PNG
   :align: center  
   :width: 70%

La libreria `paho`_ (come molte altre) permette di gestire i messaggi inviati su questa entry-topic 
mediamte un oggetto che implementa l'interfaccia  ``org.eclipse.paho.client.mqttv3.MqttCallback``.
Il metodo che contiene il codice di gestione dei messaggi ha la signature che segue:

.. code:: java

    @Override  //from MqttCallback
    public void messageArrived(String topic, MqttMessage message)   {
      ...
    }

La rappresentazione in forma di String del messaggio ricevuto di tipo ``org.eclipse.paho.client.mqttv3.MqttMessage``
deve avere la struttura introdotta in :ref:`msgApplicativi`:

.. code:: java

  msg(MSGID,MSGTYPE,SENDER,RECEIVER,CONTENT,SEQNUM)

Pertanto deve essere possibile eseguire il mapping in un oggetto di tipo :ref:`ApplMessage` senza generare eccezioni:

.. code:: java

  ApplMessage msgInput = new ApplMessage(message.toString());

Queste trasformazioni sono quindi di pertinenza del gestore di sistema dei messaggi in arrivo
(``ctxMsgHandler``) che viene definito in modo da fungere anche da callBack MQTT in quanto implementa
una interfaccia :ref:`IContextMsgHandlerMqtt` che estende  :ref:`IContextMsgHandler<IContextMsgHandler>`.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
IContextMsgHandlerMqtt
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

  public interface IContextMsgHandlerMqtt 
      extends IContextMsgHandler, IApplMsgHandlerMqtt{}

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
ContextMqttMsgHandler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

  public class ContextMqttMsgHandler extends ApplMsgHandler 
                              implements IContextMsgHandlerMqtt{
    ...
  @Override
  public void addComponent( String devName, IApplMsgHandler h) { ... }

  @Override
  public void elaborate( ApplMessage msg, Interaction2021 conn ) { ... }

  @Override
  public void elaborate(String message, Interaction2021 conn) { ... }

  @Override
  public void messageArrived(String topic, MqttMessage message){ ... }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
MqttContextServer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

    public class MqttContextServer implements IContext{
    private MqttConnection mqtt ; //Singleton
    private IContextMsgHandlerMqtt ctxMsgHandler;

    public MqttContextServer(String clientId, String topic) {
      ...
      ctxMsgHandler = new ContextMqttMsgHandler("ctxH");
    }
      ...
	  @Override
	  public void activate() {
		  mqtt = MqttConnection.createSupport( clientId, topic );
		  mqtt.connectToBroker(clientId,  RadarSystemConfig.mqttBrokerAddr);
      mqtt.subscribe( topic, ctxMsgHandler );	
   	}
	  @Override
	  public void addComponent(String name, IApplMsgHandler h) {
		  ctxMsgHandler.addComponent(name, h);	
	  }
      ...

Il metodo ``activate`` del ContextServer per MQTT, crea il singleton ``MqttConnection`` 
e lo utilizza per fare una subscribe sulla ``topic``.
Il ``ctxMsgHandler`` come 
gestore dei messaggi in arrivo.

Infatti la libreria `paho`_ (come molte altre) gestisce i messaggi ricevuti attraverso una callback
associata al 

.. code:: java

	public void subscribe(String clientid, String topic, MqttCallback callback) {
		try {
 			client.setCallback( callback );	
			client.subscribe(topic);			
 		} catch (MqttException e) {
			ColorsOut.outerr("MqttConnection  | subscribe Error:" + e.getMessage());
		}
	}



 


 








+++++++++++++++++++++++++++++++++++++++
CoapContextServer
+++++++++++++++++++++++++++++++++++++++

CoAP mira a modellizzare
tutte le interazioni client/server come uno scambio di rappresentazioni di risorse. L'obiettivo
è quello di realizzare una infrastruttura di gestione delle risorse remote tramite alcune semplici
funzioni di accesso e interazione come quelle di HTTP: PUT, POST, GET, DELETE.

Si tratta quindi di utilizzare un oggetto di `californium`_ (libreria di riferimento) di classe ``CoapServer``
  in cui si siano aggiunte tutte le risorse che corrispondono ai componenti destinatari di messaggi (ad
  esempio, una risorsa per il Led e una per il Sonar)

La libreria ``org.eclipse.californium`` offre ``CoapServer`` che viene decorato da ``CoapApplServer``.

- ``CoapApplServer`` extends CoapServer implements :ref:`IContext`
- class ``CoapSupport`` implements :ref:`Interaction2021`
- abstract class ``ApplResourceCoap`` extends CoapResource implements :ref:`IApplMsgHandler`


La classe ``CoapResource`` viene decorata da ``ApplResourceCoap`` per implementare ``IApplMsgHandler``.
In questo modo una specializzazione come ``LedResourceCoap`` può operare come componente da aggiungere 
al sistema tramite ``CoapApplServer`` che la ``Context2021.create()`` riduce a ``CoapServer`` in cui 
sono registrate le risorse.




