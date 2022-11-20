.. role:: red 
.. role:: blue 
.. role:: remark

===================================================
Il RadarSystem basato su CoAP
===================================================

Il nostro interesse su CoAP si concentra , per ora, sui seguenti aspetti:

#. CoAP fornisce un modello di interazione ancora punto-a-punto ma, essendo di tipo ``REST``, il suo utilizzo
   implica schemi di progettazione molto simili a quelli di applicazioni Web basate su HTTP;
#. l'uso di CoAP modifica il modello concettuale di riferimento per le interazioni, in quanto propone
   l'idea di accesso in lettura (GET) o modifica (PUT) a :blue:`risorse` identificate da ``URI`` attraverso un 
   unico :blue:`CoapServer`.

    
   .. image:: ./_static/img/Architectures/CoapResources.png 
     :align: center
     :width: 40%

   Da questo punto di vista, il modello è simile a quanto proposto in  :doc:`ContextServer`, ma con
   una forte forma di :blue:`standardizzazione` sia a livello di 'verbi' di interazione (GET/PUT) sia a livello di 
   organizzazione del codice applicativo (gerarchia di risorse);
#. l'adozione del protocollo CoAP come supporto alle interazioni potrebbe indurci a modificare radicalmente 
   il software di livello applicativo già sviluppato usando TCP. Oviamente sarebbe opportuno poter 
   adottare il nuovo protocollo modificando il meno possibile quanto già prodotto.

In questa sezione vediamo come affrontare il terzo punto con riferimento al RadarSystem.

.. Vediamo subito il risultato.


------------------------------------------------
Organizzazione delle risorse
------------------------------------------------
Le risorse CoAP del nostro dominio applicativo  saranno organizzate come nella figura che segue:

.. image:: ./_static/img/Radar/CoapRadarResources.png 
    :align: center
    :width: 60%

- Il codice applicativo di gestione del Sonar viene incapsulato in una risorsa il cui URI è
  ``devices/input/sonar``
- Il codice applicativo di gestione del Led viene viene incapsulato in una risorsa di URI è
  ``devices/output/led``

Le risorse del dominio sono introdotte come specializzazioni di una classe-base.

.. _CoapDeviceResource:

++++++++++++++++++++++++++++++++++++++++
La risorsa-base CoapDeviceResource
++++++++++++++++++++++++++++++++++++++++

La classe astratta ``CoapDeviceResource`` è una  ``CoapResource`` che realizza la gestione delle richieste GET e PUT 
demandandole rispettivamente ai metodi ``elaborateGet`` ed  ``elaboratePut`` delle classi specializzate.

.. code:: Java

  public abstract class CoapDeviceResource extends CoapResource {
     protected abstract String elaborateGet(String req);
     protected abstract void elaboratePut(String req);	
    @Override
    public void handleGET(CoapExchange exchange) {
       String answer = elaborateGet( exchange.getQueryParameter("q") );
       exchange.respond(answer);
    }
    @Override
    public void handlePUT(CoapExchange exchange) {
       String arg = exchange.getRequestText() ;
       elaboratePut( arg );
	   changed();  //IMPORTANT to notify the observers
       exchange.respond(CHANGED);
    }
    @Override
    public void handleDELETE(CoapExchange exchange) {
       delete();
       exchange.respond(DELETED);
    }
    @Override
    public void handlePOST(CoapExchange exchange) {}
  }

La risorsa viene creata come :blue:`risorsa osservabile` da un costruttore che provvede ad  
aggiungerla al server CoAP (il singleton `CoapApplServer`_), attivandolo - se già non lo fosse.


.. code:: Java

   public CoapDeviceResource(String name, DeviceType dtype)  {
     super(name);
     setObservable(true); //La risorsa è osservabile
     CoapApplServer coapServer = CoapApplServer.getServer(); //SINGLETION
     if( dtype==DeviceType.input )        
       coapServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
     else if( dtype==DeviceType.output )  
       coapServer.addCoapResource( this, CoapApplServer.outputDeviceUri);
    }


.. _CoapApplServer:

------------------------------------------------
Il Server delle risorse applicative
------------------------------------------------
 
Il server ``CoapApplServer`` è una estensione di ``org.eclipse.californium.core.CoapServer`` 
che realizza un singleton capace 
di accogliere nuove risorse del dominio, ciascuna come un dispositivo, o di input o di output.


.. code:: Java

    public class CoapApplServer extends CoapServer{
    public final static String outputDeviceUri = "devices/output";
    public final static String lightsDeviceUri = outputDeviceUri+"/lights";
    public final static String inputDeviceUri  = "devices/input";
	
    private static CoapResource root      = new CoapResource("devices");
    private static CoapApplServer server  = null;
		
    public static CoapApplServer getServer() {
         if( server == null ) server = new CoapApplServer();
         return server;
    }	
        private CoapApplServer(){
           CoapResource outputRes= new CoapResource("output");
           outputRes.add( new CoapResource("lights"));
           root.add(outputRes);
           root.add(new CoapResource("input"));
           add( root );
           start();
        }
        public  void stopServer() { stop(); }

Il metodo statico ``getServer`` è un factory method che restituisce il singleton, creandolo ed
attivandlo, se già non lo fosse.

Il metodo per aggiungere risorse è così definito:

.. code:: Java		

	public  void addCoapResource( CoapResource resource, String fatherUri  )   {
         Resource res = getResource("/"+fatherUri);
         if( res != null ) res.add( resource );
	}

Il metodo statico ``getResource`` restituisce (il riferimento a) una risorsa, dato il suo URI, 
avvalendosi di una ricerca *depth-first* nell'aòbero delle risorse:

.. code:: Java	

    public static Resource getResource( String uri ) {
      return getResource( root, uri );
    }

    private static Resource getResource(Resource root, String uri) {
      if( root != null ) {
        Collection<Resource> rootChilds = root.getChildren();
        Iterator<Resource> iter         = rootChilds.iterator();
            while( iter.hasNext() ) {
                Resource curRes = iter.next();
                String curUri   = curRes.getURI();
                if( curUri.equals(uri) ){ return  curRes;
                }else {  //explore sons
                    Resource subRes = getResource(curRes,uri); 
                    if( subRes != null ) return subRes;					               
                 }
            }//while
      }
      return null;			
    }


------------------------------------------------
Una risorsa per il Led
------------------------------------------------
La risorsa CoAP  per il Led è una specializzazione di `CoapDeviceResource`_ che 
incorpora un Led e ridirige a questo Led le richieste GET e PUT.

.. code:: Java

   public class LedResourceCoap extends CoapDeviceResource {
   private ILed led; 
     public LedResourceCoap(String name, ILed led ) {
       super(name, DeviceType.output);
       this.led = led;
     }
     @Override
     protected String elaborateGet(String req) { return ""+led.getState(); }
     
     @Override
     protected void elaboratePut(String req) {
      if( req.equals( "on") ) led.turnOn();
      else if( req.equals("off") ) led.turnOff();		
     }  
   }

------------------------------------------------
Il Led accessibile via CoAP (o TCP)
------------------------------------------------

Impostiamo un programma (di testing) che procede nelle seguenti fasi, ciscuna realizzata da una 
specifica operazione:

#. definisce i parameteri di configurazione tramite lettura di un file o 
   mediante assegnamenti diretti alle variabili della classe ``RadarSystemConfig``;
#. configura un sistema costuito da un solo Led remoto cui si
   accede utilizzando il protocollo (Tcp o CoAP) specificato nel file di configurazione;
#. esegue almeno una volta tutte operazioni rese disponibili dalla interfaccia ``ILed``;
#. effettua la terminazione del sistema disattivando i server creati.

Il programma si presewnta dunque come segue:

.. code:: Java

    public class LedUsageMain  {
    private EnablerAsServer ledServer;
    private ILed ledClient1, ledClient2;
    private ILed led;

    public static void main( String[] args)  {
        LedUsageMain  sys = new LedUsageMain();	
        sys.setup(null);
        sys.configure();
        sys.execute();
        Utils.delay(2500);
        sys.terminate();
    }

Alle diverse fasi corrispondono altrettante oeprazioni:

.. code:: Java

	public void setup( String fName) { 
		if( fName != null )  RadarSystemConfig
		else{
			RadarSystemConfig.protcolType = ProtocolType.coap; //Protocol.tcp
			RadarSystemConfig.ledPort     = 8015;
			...
		}
	}
	public void configure() { 
 		configureTheLedEnablerServer();
 		configureTheLedProxyClient();
	}
 	public void execute() { ... }
	public void terminate() { ... }



++++++++++++++++++++++++++++++++++++
Configurazione di LedUsageMain
++++++++++++++++++++++++++++++++++++

La fase di configurazione viene divisa in due parti:

- la costruzione di un enabler tipo-server per il Led;
- la costruzione di (almeno) un proxy tipo-client per il Led.

La costruzione del proxy può avvenire creando una istanza di ``LedProxyAsClient`` avendo  cura 
di specificare il paranetro ``entry`` in funzione del protocollo selezionato:

.. code:: Java

   protected void configureTheLedProxyClient() {		 
     String host           = RadarSystemConfig.pcHostAddr;
     ProtocolType protocol = RadarSystemConfig.protcolType;
     String portLedTcp     = ""+RadarSystemConfig.ledPort;

     String nameUri  = CoapApplServer.outputDeviceUri+"/led";
     String entry    = protocol==ProtocolType.coap ? nameUri : portLedTcp;
     ledClient1      = new LedProxyAsClient("client1", host, entry, protocol );
     ledClient2      = new LedProxyAsClient("client2", host, entry, protocol );	
   }

La costruzione dell'enabler tipo-server per il Led avviene in due modi diversi:

- se si usa TCP, si crea una istanza di ``EnablerAsServer`` specificando come ultimo patrametro
  del costruttore un oggetto di gestione dei messaggi appliocativi, come  ``LedApplHandler``;
- se si usa CoAP, si crea una ``LedResourceCoap`` di nome **led**, che potrà essere indentificata mediante
  l'URI ``devices/output/led``.

.. image:: ./_static/img/Radar/LedUsage.png 
    :align: center
    :width: 60%

.. code:: Java

   protected void configureTheLedEnablerServer() {
      led = DeviceFactory.createLed();
      if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
         ledServer = new EnablerAsServer("LedServer",RadarSystemConfig.ledPort, 
               RadarSystemConfig.protcolType, new LedApplHandler("ledH",led) );
         ledServer.activate();
      }else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
         new LedResourceCoap("led", led);
      } 
    }

La costruzione della ``LedResourceCoap`` provoca la attivazione di una versione specializzate
del ``CoAPServer`` (un singleton di classe ``CoAPApplServer``),  se non già avvenuta in precedenza. 

++++++++++++++++++++++++++++++++++++
Esecuzione di LedUsageMain
++++++++++++++++++++++++++++++++++++
La fase di esecuzione 

.. code:: Java

	public void execute() {
		ledClient1.turnOn();	
		boolean curLedstate = ledClient2.getState();
 		System.out.println("LedProxyAsClientMain | ledState=" + curLedstate);
		assertTrue( curLedstate);
		Utils.delay(1500);	//give time to look at the Led
		ledClient2.turnOff();
		curLedstate = ledClient1.getState();
		System.out.println("LedProxyAsClientMain | ledState=" + curLedstate);
		assertTrue( ! curLedstate);
	}

Notiamo che 
- usiamo i client in modo intercambiabile per accedere al Led;
- inseriamo asserzioni all'interno di execute, anticipando la scrittura di una TestUnit.

++++++++++++++++++++++++++++++++++++
Terminazione 
++++++++++++++++++++++++++++++++++++

.. code:: Java

    public void terminate() {
     if( led instanceof LedMockWithGui ) { 
         ((LedMockWithGui) led).destroyLedGui(  ); 
     }
     if( RadarSystemConfig.protcolType == ProtocolType.tcp) 
        ledServer.deactivate();
     else {
        CoapApplServer.getServer().stop();
        CoapApplServer.getServer().destroy();
    }
  }



------------------------------------------------
Una risorsa per il Sonar
------------------------------------------------
La risorsa CoAP  per il Sonar è una specializzazione di `CoapDeviceResource`_ che 
incorpora un Sonar, a cui ridirige le richieste GET di lettura  e i comandi 
PUT di attivazione/disativazione.

.. _SonarResourceCoap:

.. code:: Java

  public class SonarResourceCoap extends CoapDeviceResource  {
  ISonar sonar;
  String curVal="";
    public SonarResourceCoap(String name, ISonar sonar) {
      super(name, DeviceType.input);
      this.sonar = sonar;
    }

    @Override
    protected String elaborateGet(String req) {
      if( req == null || req.equals("getDistance")) {
          String answer = curVal;
          return answer;
      }  
      if( req != null && req.isEmpty()) return curVal; //for the observers
      if( req != null && req.equals("isActive")) return ""+sonar.isActive();
      else return "SonarResourceCoap: request notUnderstood";
    }

    @Override
    protected void elaboratePut(String arg) {
      if( arg.equals("activate")) getSonarValues();
      else if( arg.equals("deactivate")) sonar.deactivate(); 	
    }

La richiesta PUT di (de)attivazione provoca la (de)attivazione del Sonar.
In quanto produttore di dati, il Sonar modifica (``elaborateAndNotify``) il valore corrente 
``curVal`` della distanza misurata e notifica tutti gli observer.

.. code:: Java			
			
    private void getSonarValues() {
      new Thread() {
        public void run() {
          sonar.activate();
          while( sonar.isActive() ) {
            int v = sonar.getDistance().getVal();
            elaborateAndNotify(  v );
          }
        }
      }.start();
    }
    protected void elaborateAndNotify(int arg) {
       curVal= ""+arg;
	     changed();	// notify all CoAP observers
    }		
  }

------------------------------------------------
Il Sonar accessibile via CoAP (o TCP) 
------------------------------------------------

Come già fatto per il Led, impostiamo un programma che prima configura il sistema e poi effettua operazioni relative al Sonar.

A differenza del caso del Led, l'uso di un dispositivo di input quale il Sonar si presta ad impostare un maggior 
numero di configurazioni, a partire dal Sonar stesso, che può essere:

- un oggetto (POJO) che implementa l'interfaccia  `ISonar`_ ; 
- un POJO osservabile che implementa l'interfaccia `ISonarObservable`_.


Il Sonar (semplice od osservabile) può essere reso utilizzabile da remoto: 

- con un enabler tipo-server TCP (``EnablerSonarAsServer``) che invia messaggi 'semplici' al gestore 
  applicativo `SonarApplHandler`_;
- attraverso un ``TcpContextServer``, che ridirige **il payload** di messaggi di tipo ``ApplMessage``
  al gestore applicativo `SonarApplHandler`_;
- come parte di una ``CoapResource`` ( come `SonarResourceCoap`_ che estende `CoapDeviceResource`_)
  con URI= ``devices/ouput/sonar``.

I paranetri di configurazione sono espressi dalle seguenti variabili:

.. code:: Java

  RadarSystemConfig.protcolType
  boolean withContext   //usata se RadarSystemConfig.protcolType=ProtocolType.tcp
  

Al Sonar può essere associato un observer (o più):

- realizzato come POJO che implementa la interfaccia `IObserver`_ (ad esempio `SonarObserverFortesting`_);
- realizzato come componente CoAP che implementa l'interfaccia ``CoapHandler`` (ad esempio `CoapApplObserver`_).

Per accedere al Sonar si possono usare: 

- clienti di tipo ``ProxyAsClient`` (come `SonarProxyAsClient`_) che implementano l'interfaccia ``ISonar``. 
  Questi client inviano al server cui sono connessi messaggi (semplici o di tipo ``ApplMessage``)
  secondo la configurazione selezionata;
- supporti di tipo ``CoapSupport`` che implementano l'interfaccia `Interaction2021`_ inviando richieste GET/PUT.


In relazione alle diverse possibilità introdiuciamo diversi programmi di esempio, partendo da una classe 
astratta, che si occupa della creazione del sonar come POJO e che definisce alcuni metodi di uso comune, 
lasciandone altri non specificati:


.. _SonarUsageAbstractMain:

++++++++++++++++++++++++++++++++++++++++++++++++++++
La classe astratta SonarUsageAbstractMain
++++++++++++++++++++++++++++++++++++++++++++++++++++

.. code:: Java

  public class SonarUsageAbstractMain  {
  protected ISonar   sonar;

  public void configure() {
    setConfiguration();
    createTheSonar();
    createObservers();
    configureTheServer();
	}
  public void setConfiguration() {
    RadarSystemConfig.pcHostAddr         = "localhost";
    RadarSystemConfig.sonarDelay         = 100;		
    RadarSystemConfig.sonarObservable    = true;		
  }
  
  public abstract void execute();

	protected void createTheSonar() {
		sonar = DeviceFactory.createSonar(RadarSystemConfig.sonarObservable);		
	}
  protected abstract void configureTheServer();
  protected  abstract void createObservers(); 
  }


++++++++++++++++++++++++++++++++++++++++++++++++++++
Esempi di configurazioni applicative
++++++++++++++++++++++++++++++++++++++++++++++++++++

A partire dalla classe astratta `SonarUsageAbstractMain`_ defininiamo i programmi:

- ``SonarUsageMainWithEnablerTcp``: rende accessibile il Sonar attraverso il server TCP `EnablerSonarAsServer`_ .
- ``SonarUsageMainWithContextTcp``: rende accessibile il Sonar attraverso il server di contesto 
  `TcpContextServer`_, aggiungendovi il componente `SonarApplHandler`_.
- ``SonarUsageMainCoap``: rende accessibile il Sonar attraverso la risorsa CoAP `SonarResourceCoap`_ che si aggiunge
  al `CoapApplServer`_ con *URI-Path=devices/input/sonar*. Si permette anche la creazione di un observer CoAP 
  di tipo `CoapApplObserver`_ che visualizza la distanza corrente sulla RadarGui,  attraverso un `SonarDistanceHandler`_

Tutti i programmi:

- utilizzano il Sonar remoto attraverso un client di tipo `SonarProxyAsClient`_, 
  che invia messaggi (semplici o di tipo ``ApplMessage``), secondo la configurazione selezionata
- permettono di associare al Sonar POJO un osservatore  `SonarObserverFortesting`_.


.. _CoapApplObserver:

.. %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.. CoapApplObserver
.. %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

  public class CoapApplObserver implements CoapHandler{
    protected IApplMsgHandler applHandler;
    
    public CoapApplObserver(String hostAddr, String resourceUri, IApplMsgHandler applHandler) {
      this.applHandler = applHandler;
    }
    @Override
    public void onLoad(CoapResponse response) {
      applHandler.elaborate(response.getResponseText(), null);
    }
    @Override
    public void onError() { //If a request timeouts or the server rejects it
      Colors.outerr("CoapApplObserver | ERROR " );	      
    }
  }

.. _SonarDistanceHandler:

.. %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.. SonarDistanceHandler
.. %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

  public class SonarDistanceHandler extends ApplMsgHandler{
    private IRadarDisplay radar;	
    public SonarDistanceHandler(String name) {
      super(name);
      radar = RadarDisplay.getRadarDisplay();
    }

    @Override
    public void elaborate(String message, Interaction2021 conn) {
      showDataOnGui( message );
    }
    
    public  void showDataOnGui( String msg ){
      try {  //Normally we handle structured message strings
        ApplMessage m   = new ApplMessage( msg );
        String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
        radar.update(distance, "0");
      }catch( Exception e ){ //Otherwise we handle simple integers
        if( msg.length() > 0 ) radar.update(msg, "0");
        else Colors.outerr("showDataOnGui ERROR: empty String");
      }
    }

  }

------------------------------------------------
TODO
------------------------------------------------

- LedUsageMain
- SonarUsageMain
- RadarSystemMainOnPcCoap
- RadarSystemMainOnPcLikeRaspCoap

gradle build jar -x test