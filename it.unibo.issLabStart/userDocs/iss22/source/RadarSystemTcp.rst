.. role:: red 
.. role:: blue 
.. role:: remark
  
.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/

==================================================
Il RadarSystem con TCP
==================================================

-------------------------------------------------------
Ridefinizione degli handler
-------------------------------------------------------

All'interno di ogni handler applicativo, occorre ora definire il codice del metodo `elaborate` 
quando il messggio di input è di tipo :ref:`ApplMessage`.

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


++++++++++++++++++++++++++++++++++++++++++++++
Metodo elaborate di :ref:`SonarApplHandler` 
++++++++++++++++++++++++++++++++++++++++++++++

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

Introduciamo un nuovo parametro di configurazione per indicare l'uso del  :ref:`TcpContextServer`:

.. code:: java
  
  RadarSystemConfig.withContext = true;

Ridefiniamo i client definiti in precedenza (come ad esempio :ref:`SonarProxyAsClient<SonarProxyAsClientNoContext>`)
in modo  da inviare messaggi di tipo :ref:`ApplMessage`, quando la configurazione 
*RadarSystemConfig.withContext* specifica che usiamo il  :ref:`TcpContextServer`:

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


.. _primoPrototipo: 

------------------------------------------
Architettura del primo prototipo
------------------------------------------


Avvaledoci dei componenti introdotti in precedenza, costruiamo un sistema che abbia il Controller (e il radar) su PC
e i dispositivi sul Raspberry, secondo l'architettura mostrata in figura:


.. image:: ./_static/img/Radar/sysDistr1.PNG
   :align: center 
   :width: 70%

I dispositivi sul Raspberry sono incspsulati in  handler che gestiscono i :ref:`Messaggi applicativi<messaggiAppl>` inviati 
loro dal :ref:`TcpContextServer<TcpContextServer>`.

Si veda:

- ``RadarSystemMainDevsCtxOnRasp`` : da attivare sul Raspberry 
- ``RadarSystemMainWithCtxOnPc`` : da attivare sul PC
 
--------------------------------------------
Deployoment del primo prototipo
--------------------------------------------

.. code:: 

  gradle build jar -x test

Crea il file `build\distributions\it.unibo.enablerCleanArch-1.0.zip` che contiene la directory bin  

 
.. Test funzionale
.. Si veda :doc:`ContextServer`.

La distribuzione del *RadarSystem* assume due forme:

- la forma di una libreria di nome ``it.unibo.enablerCleanArch-1.0.jar`` prodotta dal progetto it.unibo.enablerCleanArch_
- la forma di una applicazione web (che utiliza la libreria precedente) prodotta dal progetto ``it.unibo.msenabler``


.. _enablerCleanArch:

++++++++++++++++++++++++++++++++++++
it.unibo.enablerCleanArch
++++++++++++++++++++++++++++++++++++

Il progetto *it.unibo.enablerCleanArch* è sviluppato in ``Java8`` e fornisce il programma
``AllMainRadarLed`` che permette di selezionare ed eseguire diverse configurazioni applicative.

.. code:: 

  1    LedUsageMain 
  a    RadarSystemDevicesOnRaspMqtt
  A    RadarSystemMainOnPcMqtt
  2    SonarUsageMainWithEnablerTcp
  3    SonarUsageMainWithContextTcp 
  4    SonarUsageMainWithContextMqtt
  5    SonarUsageMainCoap
  6    RadarSystemAllOnPc
  7    RadarSystemDevicesOnRasp
  8    RadaSystemMainCoap
  9    RadarSystemMainOnPcCoap

Selezionando **a** si esegue la parte di applicazione che attiva i dispositivi Led e Sonar sul Raspberry.
A queta parte corrisponde la parte di applicazione  **A**, da eseguire sul PC per inviare comandi ai dispositivi remoti 
e per ricevere informazioni sul loro stato.
Le due parti interagiscono via MQTT usando il broker di indirizzo ``tcp://broker.hivemq.com``.

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 8 uso di Coap - sistema tutto su Raspberry
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il sistema su Raspberry attiva un unico server (**CoapApplServer**) e aggiunge come risorse 
il Led ( devices/output/lights/led ) e il Sonar (devices/input/sonar). 

.. code:: 

   "simulation"       : "false",
   "ControllerRemote" : "false",
   "LedRemote"        : "false",
   "SonareRemote"     : "false",
   "RadarGuiRemote"   : "false",
   "protocolType"     : "coap",
   "withContext"      : "false",
   "sonarDelay"       : "200",
   ........................................
   "pcHostAddr"       : "192.168.1.9",
   "raspHostAddr"     : "192.168.1.24",
   "radarGuiPort"     : "8014",
   "ledPort"          : "8010",
   "ledGui"           : "true",
   "sonarPort"        : "8012",
   "sonarObservable"  : "false",
   "controllerPort"   : "8016",
   "serverTimeOut"    : "600000",
   "applStartdelay"   : "3000",
   "sonarDistanceMax" : "150",
   "DLIMIT"           : "12",
   "ctxServerPort"    : "8018",
   "mqttBrokerAddr"   : "tcp://broker.hivemq.com",
   "testing"          : "false"

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 8 uso di Coap - dispositivi su Raspberry 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Condifguriamo il sistema su Raspberry specificando che il controller è remoto.

.. code:: 

   "simulation"       : "false",
   "ControllerRemote" : "true",
   ...


A questo punto attiviamo il programma 9 su PC

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 9 uso di Coap - Controller su PC
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Questo programma nasce per usare CoAP e quindi fissa in modo diretto i parametri di configurazione 
che gli interessano:

.. code:: 

   	RadarSystemConfig.raspHostAddr = "192.168.1.xxx";
		RadarSystemConfig.DLIMIT       = 12;
		RadarSystemConfig.simulation   = false;
		RadarSystemConfig.withContext  = false;
		RadarSystemConfig.sonarDelay   = 200;     //come quello del Raspberry

Il programma può operare anche definendo il Controller come un observer della risorsa Sonar,
ponendo 

.. code:: 

   useProxyClient = false

In caso contrario, il Controller opera con un convenzionale ciclo **read-eval-print**.


-----------------------------------------------
Problemi ancora aperti  
-----------------------------------------------

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
 


