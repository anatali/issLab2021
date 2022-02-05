.. role:: red 
.. role:: blue 
.. role:: remark
  
.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/

==================================================
Il concetto di contesto
==================================================

Nella versione attuale, ogni enabler *tipo server* attiva un ``TCPServer`` su una propria porta.

.. image::  ./_static/img/Radar/EnablersLedSonar.PNG
  :align: center 
  :width: 20%


Una ottimizzazione delle risorse può essere ottenuta introducendo :blue:`un solo TCPServer` per ogni nodo
computazionale. Questo server (che denominiamo ``TcpContextServer``) 
verrebbe a costituire una sorta di ``Facade`` comune a tutti gli :ref:`ApplMsgHandler<IApplMsgHandler>` 
disponibili su quel nodo.

.. *enabler-server* attivati nello stesso :blue:`contesto` rappresentato da quel  nodo.

.. image::  ./_static/img/Radar/TcpContextServerSonarLed.PNG
  :align: center 
  :width: 50%

 
Per realizzare questa ottimizzazione, il ``TcpContextServer`` deve essere capace di sapere per quale
componente è destinato un messaggio, per poi invocarne l'appropriato :ref:`IApplMsgHandler<IApplMsgHandler>`
(come :ref:`LedApplHandler` e :ref:`SonarApplHandler`).

  
-------------------------------------------------------
Struttura dei messaggi applicativi
-------------------------------------------------------

Introduciamo dunque una  estensione sulla struttura dei messaggi, che ci darà d'ora in poi anche uno 
:blue:`standard interno` sulla struttura delle informazioni scambiate via rete:

 .. code:: java

    msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )

  - MSGID:    identificativo del messaggio
  - MSGTYPE:  tipo del message (Dispatch, Invitation,Request,Reply,Event)  
  - SENDER:   nome del componente che invia il messaggio
  - CONTENT:  contenuto applicativo del messaggio (detto anche payload)
  - RECEIVER: nome del componente chi riceve il messaggio 
  - SEQNUM:   numero di sequenza del messaggio

I messaggi scambiati sono logicamente suddivisi in diverse categorie:

.. list-table:: 
  :widths: 70,30
  :width: 100%

  * - - :blue:`dispatch`: un messaggio inviato a un preciso destinatario senza attesa  di una risposta 
        (in modo detto anche  `fire-and-forget`);
      - :blue:`invitation`: un messaggio inviato a un preciso destinatario aspettandosi un 'ack' da parte di questi;
      - :blue:`request`: un messaggio inviato a un preciso destinatario aspettandosi da parte di questi una 
        :blue:`response/reply` logicamente correlata alla richiesta;
      - :blue:`event`: un messaggio inviato a chiunque sia in grado di elaborarlo.

    - .. image:: ./_static/img/Architectures/legendMessages.PNG
        :align: center
        :width: 80%

.. code:: java

  enum ApplMessageType{
      event, dispatch, request, reply, invitation
  }   
   

.. _ApplMessage:

++++++++++++++++++++++++++++++++++++++++++++++++
La classe ApplMessage
++++++++++++++++++++++++++++++++++++++++++++++++

La classe ``ApplMessage`` fornisce metodi per la costruzione e la gestione di messaggi organizzati
nel modo descritto. La classe si avvale del supporto del tuProlog_.

 .. code:: java

   public class ApplMessage {
    protected String msgId       = "";
    protected String msgType     = null;
    protected String msgSender   = "";
    protected String msgReceiver = "";
    protected String msgContent  = "";
    protected int msgNum         = 0;

    public ApplMessage( String MSGID, String MSGTYPE,  
          String SENDER, String RECEIVER, String CONTENT, String SEQNUM ) {
      ...
    }

    public ApplMessage( String msg ) {
      Struct msgStruct = (Struct) Term.createTerm(msg);
      setFields(msgStruct);
    }  

    public String msgId() {   return msgId; }
    public String msgType() { return msgType; }
    public String msgSender() { return msgSender; }
    public String msgReceiver() { return msgReceiver;  }
    public String msgContent() { return msgContent;  }
    public String msgNum() { return "" + msgNum; }

    public boolean isEvent(){ 
      return msgType == ApplMessageType.event.toString(); }
    ...
    public String toString() { ... }
  }


.. _TcpContextServer:

-------------------------------------------------------
Il TcpContextServer
-------------------------------------------------------

Il ``TcpContextServer`` è una specializzazione del :ref:`TcpServer<TcpServer>` che lega il campo ``userDefHandler`` 
a un gestore di messaggi (il `ContextMsgHandler`_ ) che ha il compito
di reindirizzare il messaggio ricevuto di forma ``msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )``
al gestore applicativo, sulla base dell'attributo  ``RECEIVER``.

.. image:: ./_static/img/Architectures/MessageHandlers.PNG
   :align: center 
   :width: 50%

Per ottenere questo scopo, il ``TcpContextServer``
definisce metodi per aggiungere al  (ed eliminare dal)  `ContextMsgHandler`_  oggetti di tipo :ref:`IApplMsgHandler<IApplMsgHandler>` 
che realizzano la gestione di livello applicativo dei messaggi di tipo `ApplMessage`_.

 
.. code:: java

  public class TcpContextServer extends TcpServer{
  private static boolean activated = false;
  private ContextMsgHandler ctxMsgHandler;

    public TcpContextServer(String name, int port ) {
      super(name, port, new ContextMsgHandler("ctxH"));
      this.ctxMsgHandler = (ContextMsgHandler) userDefHandler;
    } 

    public void addComponent( String name, IApplMsgHandler h) {
      ctxMsgHandler.addComponent(name,h);
    }
    public void removeComponent( String name ) {
      ctxMsgHandler.removeComponent(name );
    }
  }
 
.. _ContextMsgHandler:

-------------------------------------------------------
Il gestore di sistema dei messaggi
-------------------------------------------------------

Il gestore dei sistema dei messaggi attua il reindirizzamento (dispatching) consultando una mappa
interna che associa un :blue:`identificativo univoco` (il nome del destinatario) a un handler.


 .. code:: java

  public class ContextMsgHandler extends IApplMsgHandler{
  //MAPPA
  private HashMap<String,IApplMsgHandler> handlerMap = 
                           new HashMap<String,IApplMsgHandler>();

    public ContextMsgHandler(String name) { super(name); }

    @Override
    public void elaborate(String message) {
      //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
      ApplMessage msg   = new ApplMessage(message);
      String dest       = msg.msgReceiver();
      IApplMsgHandler h = handlerMap.get( dest );
      if( dest != null ) h.elaborate(msg.msgContent(), conn);
    }
    public void addComponent( String name, IApplMsgHandler h) {
      handlerMap.put(name, h);
    }
    public void removeComponent( String name ) {
      handlerMap.remove( name );
    }
  }

.. image:: ./_static/img/Architectures/ContextServer.PNG
   :align: center 
   :width: 80%


:remark:`I componenti IApplMsgHandler sono semplici gestori di messaggi`

:remark:`I componenti IApplMsgHandler acquisiscono dal contesto la capacità di interazione`


-------------------------------------------------------
Ridefinizione degli handler
-------------------------------------------------------

All'interno di ogni handler applicativo, occorre ora definire il codice del metodo `elaborate` 
quando il messggio di input è di tipo `ApplMessage`_.

++++++++++++++++++++++++++++++++++++++++++
elaborate di :ref:`LedApplHandler` 
++++++++++++++++++++++++++++++++++++++++++

.. code:: java

  public void elaborate( ApplMessage message, Interaction2021 conn ) {
    String payload = message.msgContent();
		if( message.isRequest() ) {
			if(payload.equals("getState") ) {
 				String ledstate = ""+led.getState();
        ApplMessage reply = prepareReply( message, ledstate);
				sendAnswerToClient(reply.toString());
			}
		}else if( message.isReply() ) {
			
		}else elaborate(payload, conn); //call to previous version
  }


++++++++++++++++++++++++++++++++++++++++++
elaborate di :ref:`SonarApplHandler` 
++++++++++++++++++++++++++++++++++++++++++

.. code:: java

  public void elaborate( ApplMessage message, Interaction2021 conn ) {
  String payload = message.msgContent();
			if( message.isRequest() ) {
				if(payload.equals("getDistance") ) {
					String vs = ""+sonar.getDistance().getVal();
					ApplMessage reply = prepareReply( message, vs);   
					sendAnswerToClient(reply.toString());

				}else if(payload.equals("isActive") ) {
 					String sonarState = ""+sonar.isActive();
					ApplMessage reply = prepareReply( message, sonarState); //Utils.buildReply("sonar", "sonarstate", sonarState, message.msgSender()) ;
  					sendAnswerToClient(reply.toString());
				}
			}else elaborate(payload, conn);			
  }

++++++++++++++++++++++++++++++++++++++++++
Il metodo prepareReply
++++++++++++++++++++++++++++++++++++++++++

Il metodo ``prepareReply`` viene introdotto in :ref:`ApplMsgHandler<ApplMsgHandler>` in modo che:

- l'identificativo del messaggio di risposta coincida con quello della richiesta
- l'identificativo del destinatario sia il mittente della richiesta

.. code:: java

    protected ApplMessage prepareReply(ApplMessage message, String answer) {
		String payload = message.msgContent();
		String sender  = message.msgSender();
		String receiver= message.msgReceiver();
		String reqId   = message.msgId();
		ApplMessage reply = null;
		if( message.isRequest() ) {
			//The msgId of the reply must be the id of the request !!!!
 			reply = Utils.buildReply(receiver, reqId, answer, message.msgSender()) ;
		}else { //DEFENSIVE
			ColorsOut.outerr(name + " | ApplMsgHandler prepareReply ERROR: message not a request");
		}
		return reply;
    }

-------------------------------------------------------
Ridefinizione dei client Proxy
-------------------------------------------------------

Introduciamo un nuovo parametro di configurazione per indicare l'uso del  `TcpContextServer`_:

.. code:: java
  
  RadarSystemConfig.withContext = true;

Ridefiniamo i client definiti in precedenza (come ad esempio :ref:`SonarProxyAsClient<SonarProxyAsClientNoContext>`)
in modo  da inviare messaggi di tipo `ApplMessage`_, quando la configurazione 
*RadarSystemConfig.withContext* specifica che usiamo il  `TcpContextServer`_:

Ad esempio, ridefiniamo il Proxy del Led (il caso del Sonar è analogo) tenendo anche conto 
dei protocolli CoAP e MQTT :

.. _LedProxyAsClient:

.. code::   java

  public class LedProxyAsClient extends ProxyAsClient implements ILed {
    public LedProxyAsClient( String name, String host, String entry,
                       ProtocolType protocol  ) {
      super(name,host,entry, protocol);
    }

    @Override
    public void turnOn() { 
        if( RadarSystemConfig.protcolType == ProtocolType.tcp 
            && RadarSystemConfig.withContext ) {
        sendCommandOnConnection(Utils.turnOnLed.toString());
      }
      else if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
        sendCommandOnConnection(Utils.turnOnLed.toString());
      }
      else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
        sendCommandOnConnection( "on" );
      }else //CASO DI DEFAULT
        sendCommandOnConnection( "on" );
    }

    @Override
    public void turnOff() {   
      if( RadarSystemConfig.protcolType == ProtocolType.tcp 
          && RadarSystemConfig.withContext ) {
        sendCommandOnConnection(Utils.turnOffLed.toString());
      }
      else if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
        sendCommandOnConnection(Utils.turnOffLed.toString());
      }
      else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
        sendCommandOnConnection( "off" );
      } else  //CASO DI DEFAULT
        sendCommandOnConnection( "off" );
    }

    @Override
    public boolean getState() {   
      String answer="";
      if( RadarSystemConfig.protcolType == ProtocolType.tcp 
          && RadarSystemConfig.withContext ) {
        answer = sendRequestOnConnection(
          Utils.buildRequest(name, "query", "getState", "led").toString()) ;
      }
        else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
          answer = sendRequestOnConnection(
            Utils.buildRequest(name, "query", "getState", "led").toString());
      else { //CASO DI DEFAULT
        answer = sendRequestOnConnection( "getState" );
      }
      return answer.equals("true");
    }
  }

I metodi ``sendCommandOnConnection`` e ``sendRequestOnConnection`` sono definiti in :ref:`ProxyAsClient`.


.. _messaggiAppl:

+++++++++++++++++++++++++++++++++++++++++++++
Definizione dei messaggi come ``ApplMessage``
+++++++++++++++++++++++++++++++++++++++++++++
 

La classe ``Utils`` fornisce metodi per la creazione dei messaggi usati dagli handler del Led e dal Sonar
usando un ``dispatch`` per i comandi e un  ``request`` per le richieste
di informazione.

 .. code:: java

  //Definizione dei Messaggi
  ApplMessage turnOnLed    = 
    new ApplMessage("msg( turn, dispatch, system, led, on, 2 )");
  ApplMessage turnOffLed   = 
    new ApplMessage("msg( turn, dispatch, system, led, off, 3 )");
  ApplMessage sonarActivate =  
    new ApplMessage("msg( sonarcmd, dispatch,system,sonar, activate,4)");
  ApplMessage getDistance  = 
    new ApplMessage("msg(sonarcmd,request,system,sonar, getDistance,5)");
  ApplMessage getLedState  = 
    new ApplMessage("msg(ledcmd,request,system,led,getState, 6)");
  //For simulation:
  ApplMessage fardistance  =
    new ApplMessage("msg( distance, dispatch, system, sonar, 36, 0 )");
  ApplMessage neardistance =
    new ApplMessage("msg( distance, dispatch, system, sonar, 10, 1 )");




-------------------------------------------------------
Un esempio
-------------------------------------------------------

Avvaledoci dei componenti introdotti in precedenza, costruiamo un sistema che abbia il Controller (e il radar) su PC
e i dispositivi sul Raspberry, secondo l'architettura mostrata in figura:


.. image:: ./_static/img/Radar/sysDistr1.PNG
   :align: center 
   :width: 60%

I dispositivi sul Raspberry sono incspsulati in  handler che gestiscono i :ref:`Messaggi applicativi<messaggiAppl>` inviati 
loro dal :ref:`TcpContextServer<TcpContextServer>`.

Si veda:

- ``RadarSystemMainDevsCtxOnRasp`` : da attivare sul Raspberry 
- ``RadarSystemMainWithCtxOnPc`` : da attivare sul PC
 


++++++++++++++++++++++++++++++++++++++++
Problemi ancora aperti  
++++++++++++++++++++++++++++++++++++++++

- Un handler lento o che si blocca, rallenta o blocca la gestione dei messaggi da parte del
  ``ContextMsgHandler`` e quindi del :ref:`TcpContextServer<TcpContextServer>`.
- Nel caso di componenti con stato utlizzabili da più clienti, vi possono essere problemi di concorrenza.
  
Per un esempio, si consideri un contatore (POJO) che effettua una operazione di decremento rilasciando il controllo 
prima del completamento della operazione. 
  
.. code:: java

  public class CounterWithDelay {
    private int n = 2;
    public void inc() { n = n + 1; }
    public void dec(int dt) {	//synchronized required BUT other clients delayed
      int v = n;
      v = v - 1;
      ColorsOut.delay(dt);  //the control is given to another client
      ColorsOut.out("Counter resumes v= " + v);
      n = v;
      ColorsOut.out("Counter new value after dec= " + n);
    }
  }
  
.. image:: ./_static/img/Radar/CounterWithDelay.PNG
   :align: center  
   :width: 60%


L'handler che


.. code:: java

  public class CounterHandler extends ApplMsgHandler {
  private CounterWithDelay c = new CounterWithDelay();

  public CounterHandler( String name ) { super(name); }

	@Override
	public void elaborate(String message, Interaction2021 conn) {
    try {
      ApplMessage msg = new ApplMessage(message);
      String cmd      = msg.msgContent();
			Struct cmdT     = (Struct) Term.createTerm(cmd);
			String cmdName  = cmdT.getName();
			if( cmdName.equals("dec")) {
				elaborateDec(cmdT);	
				if( msg.isRequest() ) {
					String reply = "answer_from_" + name;
	 				ColorsOut.out(name + " | reply="+reply );					
					//sendMsgToClient( msg.msgId(),   "replyToDec", msg.msgSender(), reply);
	 				sendMsgToClient(  reply, conn ) ;
				}
		}
		}catch( Exception e) {
			Struct cmdT     = (Struct) Term.createTerm(message);
			elaborateDec(   cmdT );
		}	
 	} 
	public void elaborate( ApplMessage message, Interaction2021 conn ) {
  }
	
	protected void elaborateDec( Struct cmdT ) {
		int delay = Integer.parseInt(cmdT.getArg(0).toString());
		ColorsOut.out(name + " | dec delay="+delay);
		c.dec(delay);			
	}

	@Override
	public void sendAnswerToClient(String message) {
		// TODO Auto-generated method stub
		
	}

 

}


La chiamata al contatore può essere effettuata da un Proxy che invia un messaggio ``msg( cmd, dispatch, main, counter, dec(DELAY), 1)``
con ``DELAY`` fissato a un certo valore.
Ad esempio:

.. code:: java

  String delay = "500"; 
  ApplMessage msgDec = new ApplMessage(
      "msg( cmd, dispatch, main, DEST, dec(DELAY), 1 )"
      .replace("DEST", resourceName).replace("DELAY", delay));

  new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp).
      sendCommandOnConnection(msgDec.toString());

Il programma ``SharedCounterExampleMain`` crea due chiamate di questo tipo una di seguito all'alltra. 
Con delay basso (ad esempio ``delay = "50"``) il comportamento è corretto (e il contatore va a 0), 
ma con ``delay = "500"`` si vede che il decremento non avviene (il contatore si fissa a 1).
 




 