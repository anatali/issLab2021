.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/


Nella versione attuale, ogni enabler *tipo server* attiva un ``TCPServer`` su una propria porta.

.. image::  ./_static/img/Radar/EnablerAsServerSonarLed.PNG
  :align: center 
  :width: 20%


Una ottimizzazione delle risorse può essere ottenuta introducendo :blue:`un solo TCPServer` per ogni nodo
computazionale. Questo server (che denominiamo ``TcpContextServer``) 
verrebbe a costituire una sorta di ``Facade`` comune a tutti gli 
``ApplMessageHandler`` disponibili su quel nodo.


.. *enabler-server* attivati nello stesso :blue:`contesto` rappresentato da quel  nodo.

.. image::  ./_static/img/Radar/TcpContextServerSonarLed.PNG
  :align: center 
  :width: 50%

 
Per realizzare questa ottimizzazione, il ``TcpContextServer`` deve essere capace di sapere per quale
componente è destinato un messaggio, per poi invocarne l'appropriato ``ApplMessageHandler``.

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


La classe ``ApplMessage`` fornisce metodi per la costruzione e la gestione di messaggi organizzati
nel modo descritto. La classe si avvale del supporto del tuProlog_.

 .. code:: java

  enum ApplMessageType{
      event, dispatch, request, reply, invitation
  }   
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

-------------------------------------------------------
Il TcpContextServer
-------------------------------------------------------

Quando una stringa di forma ``msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )`` viene ricevuta
dal  ``TcpContextServer``, questi attiva un gestore di sistema dei messaggi (``ContextMsgHandler``)
capace di invocare l'``ApplMessageHandler`` relativo al componente destinatario registrato presso
di esso.

 .. code:: java

  public class TcpContextServer extends TcpServer{
  private static boolean activated = false;
  private ContextMsgHandler ctxMsgHandler;

    public TcpContextServer(String name, int port ) {
      super(name, port, new ContextMsgHandler("ctxH"));
      this.ctxMsgHandler = (ContextMsgHandler) userDefHandler;
    } 

    @Override
    public void activate() {
      if( stopped ) {
        stopped = false;
        if( ! activated ) {		//SINGLETON
          activated = true;
          this.start();
        }			
      }
    }
	  public void addComponent( String name, IApplMsgHandler h) {
      ctxMsgHandler.addComponent(name,h);
	  }
    public void removeComponent( String name ) {
      ctxMsgHandler.removeComponent(name );
    }
  }

-------------------------------------------------------
Il gestore di sistema dei messaggi
-------------------------------------------------------

 .. code:: java

  public class ContextMsgHandler extends ApplMessageHandler{
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


:remark:`I componenti IApplMsgHandler acquisiscono la capacità di interazione dal contesto`

:remark:`I componenti IApplMsgHandler sono semplici gestori di messaggi`


-------------------------------------------------------
Un esempio
-------------------------------------------------------

Avvaledoci dei componenti introdotti in precedenza, costruiamo un sistema su PC che abbia tre componenti:

- un Sonar di classe ``SonarAdapterEnablerAsServer`` che riceve valori di distanza inviati via rete
- un Led  di classe ``LedEnablerAsServer`` che riceve comandi di accensione-spegnimento inviati via rete
- un ``TcpContextServer`` che riceve messaggi da client remoti e invoca (usando un ``ContextMsgHandler``) 
  il metodo ``elaborate`` del Sonar e del Led.

Ricordiamo che gli enabler *tipo-server* sono tutti specializzazioni della classe ``ApplMessageHandler``
che definisce il metodo ``elaborate`` per l'elaborazione dei messaggi a livello applicativo. 
Inoltre essi non attivano alcun server se il tipo di protocollo
specificato nel costruttore è ``null``.

 
++++++++++++++++++++++++++++++++++++++++++
Struttura del programma 
++++++++++++++++++++++++++++++++++++++++++

La struttura del programma di esempio comprende un metodo di configurazione del sistema e un
metodo di esecuzione.

 .. code:: java

    public class TcpContextServerExampleMain {
      private TcpContextServer contextServer;
      private ISonar sonar;
      private Interaction2021 conn; 
      //Definizione dei Messaggi
      ...
      //Definizione di un metodo di configurazione
      public void configureTheSystem() { ... }
      
      //Definizione di un metodo di esecuzione
      public void execute() throws Exception{ ... }

      public static void main( String[] args) throws Exception {
        TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
        sys.configureTheSystem();
        sys.execute();
      }
    }

++++++++++++++++++++++++++++++++++++++++++
Definizione dei messaggi
++++++++++++++++++++++++++++++++++++++++++
I messaggi per aggiornare il Sonar e per comandare il Led sono ``dispatch``:

 .. code:: java

  //Definizione dei Messaggi
  ApplMessage fardistance  = 
    new ApplMessage("msg( distance, dispatch, main, sonar, 36, 0 )");
  ApplMessage neardistance = 
    new ApplMessage("msg( distance, dispatch, main, sonar, 10, 1 )");
  ApplMessage turnOnLed    = 
    new ApplMessage("msg( turn, dispatch, main, led, on, 2 )");
  ApplMessage turnOffLed   = 
    new ApplMessage("msg( turn, dispatch, main, led, off, 3 )");


++++++++++++++++++++++++++++++++++++++++++
Definizione del configuratore
++++++++++++++++++++++++++++++++++++++++++

Il metodo di configurazione definisce i parametri e crea i componenti:

 .. code:: java

  public void configureTheSystem() {
    RadarSystemConfig.simulation        = true;    
    RadarSystemConfig.testing           = true;    		
    RadarSystemConfig.ControllerRemote  = false;    		
    RadarSystemConfig.LedRemote         = false;    		
    RadarSystemConfig.SonareRemote      = false;    		
    RadarSystemConfig.RadarGuieRemote   = false;    	
    RadarSystemConfig.pcHostAddr        = "localhost";
    RadarSystemConfig.ctxServerPort     = 8048;
    RadarSystemConfig.sonarDelay        = 1500;
     
 
    //Creazione del server di contesto
    contextServer  = 
      new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
		
    //Creazione del Sonar e del Led
 		sonar = DeviceFactory.createSonar();
		led   = DeviceFactory.createLed();

    //Registrazione dei componenti presso il contesto	
    IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
    IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);
    IApplMsgHandler radarHandler = new RadarApplHandler("radarH");
    contextServer.addComponent("sonar", sonarHandler);
    contextServer.addComponent("led",   ledHandler);	
    contextServer.addComponent("radar", radarHandler);	
  }//configureTheSystem


++++++++++++++++++++++++++++++++++++++++++
Definizione di un client di trasmissione
++++++++++++++++++++++++++++++++++++++++++
Il client per trasmettere messaggi al ``TcpContextServer`` del nodo è una semplice specializzazione 
di ``ProxyAsClient``:

 .. code:: java

  public class ACallerClient  extends ProxyAsClient{
    public ACallerClient(String name, String host, String entry ) {
      super(name, host, entry, ProtocolType.tcp);
    }
  }


++++++++++++++++++++++++++++++++++++++++++
Esecuzione
++++++++++++++++++++++++++++++++++++++++++
Il metodo di esecuzione utilizza il client per trasmettere al ``TcpContextServer`` 
dapprima messaggi che riguardano il Sonar e successivamente messaggi che riguardano il Led.

.. invia prima un valore ``d>DLIMIT`` e poi un valore ``d<DLIMIT``


.. code:: java
 
  public void execute() throws Exception{
    sonar.activate();
    contextServer.activate();
    //simulateDistance(   );
    simulateController();
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
simulateDistance
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'operazione ``simulateDistance`` usa la connessione in modo diretto: è un modo da evitare:

.. code:: java

  protected void simulateDistance(  ) throws Exception {
    ACallerClient serverCaller = 
      new ACallerClient("client","localhost", ""+RadarSystemConfig.ctxServerPort);
    conn = serverCaller.getConn();
    conn.forward( fardistance.toString() );  
    conn.forward( neardistance.toString() );  
  }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
simulateController
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'operazione ``simulateController`` usa la connessione in modo diretto: è un modo da evitare:

.. code:: java
 
	protected void simulateController(    )  {
    RadarSystemConfig.sonarDelay        = 50;
    RadarSystemConfig.DLIMIT            = 40;
		
    ACallerClient sonarCaller  = 
      new ACallerClient("sonarCaller", "localhost",  ""+RadarSystemConfig.ctxServerPort);
    ACallerClient ledCaller    = 
      new ACallerClient("ledCaller",   "localhost",  ""+RadarSystemConfig.ctxServerPort);
    RadarGuiClient radarCaller = 
      new RadarGuiClient("radarCaller","localhost",  ""+RadarSystemConfig.ctxServerPort, 
        ProtocolType.tcp);
		
    //Activate the sonar
    sonarCaller.sendCommandOnConnection(sonarActivate.toString());

    for( int i=1; i<= 10; i++) {
      String answer = sonarCaller.sendRequestOnConnection(getSonarval.toString());
      int v = Integer.parseInt(answer);
      radarCaller.sendCommandOnConnection(radarUpdate.toString().replace("DISTANCE",answer));
      if( v < RadarSystemConfig.DLIMIT ) 
        ledCaller.sendCommandOnConnection(turnOnLed.toString());
      else ledCaller.sendCommandOnConnection(turnOffLed.toString());  
      String ledState = ledCaller.sendRequestOnConnection(getLedState.toString());
      System.out.println("simulateController ledState=" + ledState + " for distance=" + v);
      Utils.delay(1000);
		}
	}


++++++++++++++++++++++++++++++++++++++++
Problemi ancora aperti  
++++++++++++++++++++++++++++++++++++++++

- Un handler lento o che si blocca rallenta o blocca la gestione dei messaggi da parte del
  ``ContextMsgHandler`` e quindi del ``TcpContextServer``
- Nel caso di componenti con stato utlizzabili da più clients, vi possono essere problemi
  di concorrenza.

  L'esempio:

  - ``SharedCounterExampleMain`` 
  - ``CounterWithDelay``
  - ``EnablerCounter``
  - ``CounterClient``
  - ``msg( dec, dispatch, main, counter, dec(10), 1 )``


.. image:: ./_static/img/Radar/CounterWithDelay.PNG
   :align: center  
   :width: 60%

 