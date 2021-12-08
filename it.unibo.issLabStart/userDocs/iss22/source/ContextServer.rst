.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

==================================================
Il concetto di contesto
==================================================

Nella versione attuale, ogni enabler *tipo server* attiva un ``TCPServer`` su una propria porta.

Una ottimizzazione delle risorse può essere ottenuta introducendo :blue:`un solo TCPServer` per ogni nodo
computazionale. Questo server (che denominiamo ``TcpContextServer``) 
verrebbe a costituire una sorta di ``Facade`` comune a tutti gli *enabler-server*
attivati nello stesso :blue:`contesto` rappresentato da quel  nodo.

Per realizzare questa ottimizzazione, il ``TcpContextServer`` deve essere capace di sapere per quale
*enabler-server* è destinato un messaggio, per poi invocarne l'appropriato ``ApplMessageHandler``
definito dall'application designer.

-------------------------------------------------------
Struttura dei messaggi applicativi
-------------------------------------------------------

Introduciamo dunque una  estensione sulla struttura dei messaggi, che ci fornirà d'ora in poi anche uno 
:blue:`standard` sulla struttura delle informazioni scambiate via rete:

 .. code:: java

    msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )

  - MSGID:    identificativo del messaggio
  - MSGTYPE:  tipo del message (Dispatch, Invitation,Request,Reply,Event)  
  - SENDER:   nome del componente che invia il messaggio
  - CONTENT:  contenuto applicativo del messaggio (detto anche payload)
  - RECEIVER: nome del componente chi riceve il messaggio 
  - SEQNUM:   numero di sequenza del messaggio

I messaggi scambiati verranno logicamente suddivisi in diverse categorie:

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
nel modo descritto. La classe si avvale del supporto del TuProlog.

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
dal  ``TcpContextServer``, questi attiva un gestore di sistema dei messaggi (``SysMessageHandler``)
capace di invocare l'``ApplMessageHandler`` relativo al componente destinatario registrato presso
di esso.

 .. code:: java

  public class TcpContextServer extends TcpServer{
  private SysMessageHandler sysMsgHandler;
    public TcpContextServer(String name, int port ) {
      super(name, port, new SysMessageHandler("sysHandler"));
      sysMsgHandler = getHandler();
    }   
    public SysMessageHandler getHandler() {
      return (SysMessageHandler) applHandler;
    }
	  public void addComponent( String name, ApplMessageHandler h) {
      sysMsgHandler.registerHandler(name,h);
	  }
    public void removeComponent( String name, ApplMessageHandler h) {
      sysMsgHandler.unregisterHandler(name );
    }
  }

-------------------------------------------------------
Il gestore di sistema dei messaggi
-------------------------------------------------------

 .. code:: java

  public class SysMessageHandler extends ApplMessageHandler{
  private HashMap<String,ApplMessageHandler> handlerMap = 
                           new HashMap<String,ApplMessageHandler>();

    public SysMessageHandler(String name) { super(name); }

    @Override
    public void elaborate(String message) {
      //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
      ApplMessage msg = new ApplMessage(message);
      ApplMessageHandler h = handlerMap.get(msg.msgReceiver());
      if( h != null ) h.elaborate(message);
    }
    public void registerHandler(String name, ApplMessageHandler h) {
      handlerMap.put(name, h);
    }
    public void unregisterHandler( String name ) {
      handlerMap.remove( name );
    }
  }
 
-------------------------------------------------------
Un esempio
-------------------------------------------------------

Avvaledoci dei componenti introdotti in precedenza, costruiamo un sistema su PC che abbia tre componenti:
- un Sonar di classe ``SonarAdapterEnablerAsServer`` che riceve valori di distanza inviati via rete
- un Led  di classe ``LedEnablerAsServer`` che riceve comandi di accensione-spegnimento inviati via rete
- un ``TcpContextServer`` che riceve messaggi da client remoti e invoca opportuni metodi del Sonar e del Led

Ricordiamo che gli enabler *tipo-server* sono tutti specializzazioni della classe ApplMessageHandler
che definisce il metodo di elaborazione dei messaggi di livello applicativo che dovrà essere posto 
in esecuzione dal ``TcpContextServer``.


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
I messaggi per aggiornare il Sonar e per comandare il Led sono definiti di tipo ``dispatch``:

 .. code:: java

  //Definizione dei Messaggi
  ApplMessage fardistance  = new ApplMessage("msg( distance, dispatch, main, sonar, 36, 0 )");
  ApplMessage neardistance = new ApplMessage("msg( distance, dispatch, main, sonar, 10, 1 )");
  ApplMessage turnOnLed    = new ApplMessage("msg( turn, dispatch, main, led, on, 2 )");
  ApplMessage turnOffLed   = new ApplMessage("msg( turn, dispatch, main, led, off, 3 )");


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
		RadarSystemConfig.ledPort	          = 8010;		
		RadarSystemConfig.sonarPort         = 8012;		
		RadarSystemConfig.ctxServerPort     = 8048;
		
    //Creazione del server di contesto
		contextServer  = new TcpContextServer("TcpApplServer", RadarSystemConfig.ctxServerPort);
		
    //Creazione del sonar
		sonar = new SonarAdapterEnablerAsServer("sonar",  RadarSystemConfig.sonarPort, ProtocolType.tcp);
		
    //Creazione del Led
    ILed led = DeviceFactory.createLed();		
		LedEnablerAsServer ledServer = 
				new LedEnablerAsServer(  "led", RadarSystemConfig.ledPort, ProtocolType.tcp, led  );
 		
    //Registrazione dei componenti presso il server
		contextServer.addComponent("sonar",(ApplMessageHandler) sonar);
		contextServer.addComponent("led",ledServer);	
	}

++++++++++++++++++++++++++++++++++++++++++
Esecuzione
++++++++++++++++++++++++++++++++++++++++++
Il metodo di esecuzione utilizza un client per trasmettere al ``TcpContextServer`` 
dapprima messaggi che riguardano il Sonar e successivamente messaggi che riguardano il Led

.. invia prima un valore ``d>DLIMIT`` e poi un valore ``d<DLIMIT``


.. code:: java
 
 	public void execute() throws Exception{
		contextServer.activate();
		ACallerClient client = new ACallerClient("client","localhost",RadarSystemConfig.ctxServerPort);
		conn = client.getConn();
		simulateDistance( true );
		simulateDistance( false );
 	}
	
	protected void simulateDistance( boolean far ) throws Exception {
		if( far ) conn.forward( fardistance.toString() );  
		else  conn.forward( neardistance.toString() );  
		// client --> contextServer --> sonar.valueUpdated( ) --> produced=true
		int v = sonar.getVal();
		if( v < RadarSystemConfig.DLIMIT ) conn.forward(turnOnLed.toString());  
		else conn.forward(turnOffLed.toString());  		
	}


++++++++++++++++++++++++++++++++++++++++++
Definizione di un client di trasmissione
++++++++++++++++++++++++++++++++++++++++++
Il client per trasmettere messaggi al ``TcpContextServer`` del nodo è una semplice specializzazione 
di ``EnablerAsClient``:

 .. code:: java

  public class ACallerClient  extends EnablerAsClient{
    public ACallerClient(String name, String host, int port ) {
      super(name, host, port, ProtocolType.tcp);
    }
    @Override
    protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {}
  }




  

