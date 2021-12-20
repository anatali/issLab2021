.. role:: red 
.. role:: blue 
.. role:: remark

.. _Californium: https://www.eclipse.org/californium/

 
CoAP  ( :blue:`Constrained Application Protocol`) è un protocollo aperto e leggero per dispositivi IoT.
CoAP è simile ad HTTP, ma è stato specificato (in IETF RFC 7252 e approvato nel 2014) 
per soddisfare requisiti di dispositivi alimentati a batteria con risorse CPU e RAM limitate 
e di reti con connettività limitata o molta congestione, in cui protocolli basati su TCP,
come MQTT, hanno difficoltà ad operare.

CoAP funziona su ``UDP``, si basa su un modello *client/server* e su interazioni machine-to-machine
*stateless* di tipo *request/response* modello *Representational State Transfer*  (:blue:`M2M-REST`), 
con i server che rendono disponibili rappresentazioni di risorse identificate da un ``URI`` .

CoAP non può utilizzare ``SSL/TLS`` per garantire la sicurezza della comunicazione (che richiede TCP),
ma si può fare riferimento allo standard *Datagram Transport Layer Security (DTLS)*, che funziona su UDP.
Tuttavia CoAP è vulnerabile agli IP spoofing e quindi agli **attacchi DDoS** (*Distributed denial-of-service*).

Ciò nonostante, Shodan (un motore di ricerca per i dispositivi connessi a Internet) 
mostra 433,973 voci come risultato per la ricerca 'CoAP' (contro 249,335,005 per 'HTTP').

------------------------------------------------
La libreria Californium: un esempio di uso
------------------------------------------------

Come supporto di base per CoAP, usiamo la libreria Californium_ di Eclipse e

- per la costruzione di risorse CoAP, usiamo la classe  ``CoapResource`` di Californium;
- per la creazione di un sever CoAP, usiamo ``CoapServer`` di Californium;
- per la creazione di un client per CoAP, usiamo ``CoapClient`` di Californium;
- per la reagire in modo asincrono alle risposte di un CoAP-client che funge da osservatore, 
  implementiamo l'interfaccia ``CoapHandler`` 

Per fare un esempio di uso della libreria, impostiamo un programma (di testing) che procede nelle seguenti fasi, 
ciscuna realizzata da una specifica operazione:

#. configura il sistema attivando il ``CoapServer`` in cui ha inserito una risorsaCoap 'observable' e crea una istanza di ``CoapClient``
#. utilizza il client per leggere e modificare lo stato corrente della risorsa;
#. effettua la terminazione del sistema .

.. code:: Java

	public class CaliforniumUsageExample {
	private CoapServer coapServer;
	private ResourceObserverExample observer;
	private CoapClient client;

		public void configure() {...}
		public void execute() { ... }
		public void terminate() {...}

		public static void main(String[] args)  {
			CaliforniumUsageExample sys = new CaliforniumUsageExample();
			sys.configure();
			sys.execute();
			//sys.executeQuery();
			sys.executeQuerySameclient();
			Utils.delay(10000);
			sys.terminate();
		}
	}

++++++++++++++++++++++++++++++++++++++
Configurazione
++++++++++++++++++++++++++++++++++++++

   .. image:: ./_static/img/Architectures/CaliforniumExample.png 
     :align: center
     :width: 60%
 

.. code:: Java

	public void configure() {
		coapServer        = new CoapServer();
		CoapResource root = new CoapResource("root");
		res               = new CoapResourceExample("example");
		observer          = new ObserverExample();
		
		root.add( res );
		coapServer.add( root );
		coapServer.start();

		String url               = "coap://localhost:5683/root/example" ;
		client                   = new CoapClient( url );
	}

La risorsa di esempio definisce uno stato interno in forma di String e accumula le modifiche 
fatte mediante PUT:

.. code:: Java

	class CoapResourceExample extends CoapResource{
	String state = "s0";
		public CoapResourceExample(String name) {
			super(name);
			setObservable(true); 
		}
		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond( state );
		}
		@Override
		public void handlePUT(CoapExchange exchange) {
			state = state+"_"+exchange.getRequestText();
			changed();
			exchange.respond(CHANGED);
		}
		@Override
		public void handlePOST(CoapExchange exchange) {
			exchange.respond(CHANGED);
		}
		@Override
		public void handleDELETE(CoapExchange exchange) {
			delete();
			exchange.respond(DELETED);
		}	
	}

++++++++++++++++++++++++++++++++++++++
Esecuzione 'naive'
++++++++++++++++++++++++++++++++++++++

L'esecuzione più semplice legge lo stato della risorsa tramite una invocazione GET e modifica lo stato
tramite una PUT:

.. code:: Java

	public void execute() {
 		showTheResource(client);
		modifyTheResource(client,"s1");		
		showTheResource(client);		
	}
	protected void showTheResource(CoapClient client) {
		CoapResponse answer  = client.get(  );
		System.out.println("showTheResource | get answer="+answer.getResponseText() 
		          + " code=" + answer.getCode());		
	}
	protected void modifyTheResource(CoapClient client, String newState) {
		CoapResponse answer  = client.put(newState, 0);
		System.out.println("modifyTheResource | put answer="+answer.getResponseText()
		     + " code=" + answer.getCode());		
	}


Il risultato mostra anche i codici di risposta tipici del protocollo CoAP:

.. code:: Java

		 examplehandleGET request=
	showTheResource | get answer=s0 code=2.05
			examplehandlePUT request=s1
	modifyTheResource | put answer= code=2.04
			examplehandleGET request=
	showTheResource | get answer=s0_s1 code=2.05

++++++++++++++++++++++++++++++++++++++
Esecuzione 'con observer'
++++++++++++++++++++++++++++++++++++++

Introduciamo un osservatore che implementa l'interfaccia ``CoapHandler``:

.. code:: Java

	class ObserverExample implements CoapHandler{
		@Override
		public void onLoad(CoapResponse response) {
			Colors.outappl("ResourceObserverExample:" + response.getResponseText(),
				 Colors.GREEN);
		}

		@Override
		public void onError() {
			Colors.outerr("ResourceObserverExample error"  );	
		}
	}

In questa esecuzione, introduciamo l'osservatore e vediamo che esso viene attivato ad ogni PUT

.. code:: Java

	public void executeWithObserver() {
		//inviamo una richiesta di osservazione sulla risorsa
		CoapObserveRelation obsrelation = client.observe( observer );	
		Utils.delay(1000); //per vedere che l'observer mostra subito lo stato 
		// showTheResource(client);
		// Utils.delay(1000);
		modifyTheResource(client,"sobs");	//modifichiamo la risorsa	
		Utils.delay(1000);	//l'observer ha tempo di mostrare la modifica		
 		cancelObserverRelation(obsrelation);	//OPZIONALE: elimina l'observer
	}

	protected void cancelObserverRelation(CoapObserveRelation obsrelation) {
		obsrelation.proactiveCancel();
		Utils.delay(1000);	//diamo tempo ...
		Colors.outappl( "nObsOn_res="+res.getObserverCount() + 
		" obsrelation_isCanceled=" + obsrelation.isCanceled(), Colors.ANSI_PURPLE);		
	}

Il risultato mostra che per ogni PUT (che modifica) viene eseguita una GET (per l'osservabilità).
Se la parte opzionale non è commentata, si vede anche l'effetto della rimozione dell'observer.

.. code:: Java

		 examplehandleGET request=
	ResourceObserverExample:s0_s1
		 examplehandlePUT request=sobs
		 examplehandleGET request=
	modifyTheResource | put answer= code=2.04
		 ResourceObserverExample:s0_s1_sobs
		 examplehandleGET request=
	ResourceObserverExample:s0_s1_sobs
		 n obs su res=0 obsrelation isCanceled=true


++++++++++++++++++++++++++++++++++++++
Accesso GET 'con query'
++++++++++++++++++++++++++++++++++++++

Estendiamo la risposta a una GET, gestendo la presenza di un parametro nella richiesta:

.. code:: Java

	@Override
	public void handleGET(CoapExchange exchange) {
		String query = exchange.getQueryParameter("q");
		if( query == null ) {
			Colors.out( getName() + "handleGET request=" + exchange.getRequestText() );
			exchange.respond( state );
		}else{
			Colors.out( getName() + "handleGET query  =" + query);
			if( query.equals("time")) 
				exchange.respond( state + " at " + System.currentTimeMillis() );
		}		
	}


Utilizziamo il client per inviare una query con un parametro:

.. code:: Java

	public void executeQuerySameclient() {
 		String url  = "coap://localhost:5683/root/example/?q=time" ;
		Colors.outappl(   "executeQuerySameclient url=" + url  );
		client.setURI(url);
		CoapResponse answer    = client.get(  );
		Colors.outappl("executeQuery | get answer="+answer.getResponseText()  
			+ " code=" + answer.getCode());			
		modifyTheResource(client, "squery");
	}


Il risultato:

.. code:: Java

	executeQuerySameclient: url=coap://localhost:5683/root/example/?q=time
			examplehandleGET query  =time
	executeQuerySameclient: get answer=s0_s1_sobs at 1640001483853 code=2.05
			examplehandlePUT request=squery
	modifyTheResource: put answer= code=2.04

------------------------------------------------
Il CoapSupport
------------------------------------------------


Su queste basi, vediamo ora come è definito il nostro supporto per l'uso di CoAP, già menzionato in
predenza, che implementa l'interfaccia ``Interaction2021`` :
 
.. code:: Java

    public class CoapSupport implements Interaction2021  {
    private CoapClient client;
    private CoapObserveRelation relation = null;
    private String url;

	public CoapSupport( String address, String path) {  
		url = "coap://"+address + ":5683/"+ path;
		client = new CoapClient( url );
		client.setTimeout( 1000L );		 
	}
 	
	public String readResource(   ) throws  Exception {
		CoapResponse respGet = client.get( );
		return respGet.getResponseText();
	}
	public String readResource( String query  ) throws  Exception {
		CoapClient myclient  = new CoapClient( url+"?q="+query );
		CoapResponse respGet = myclient.get(  );
		return respGet.getResponseText();
 	}
	public void removeObserve() {
	    relation.proactiveCancel();	
	}
	public void  observeResource( CoapHandler handler  ) {
	    relation = client.observe( handler );
	}

La parte che implementa ``Interaction2021`` mappa i metodi dell'interfaccia nelle operazioni interne precedenti.

.. code:: Java

	protected void updateResource( String msg ) throws  Exception {
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
	}
	@Override
	public void forward(String msg) throws Exception {
        updateResource(msg);
    }
 	@Override
	public String request(String query) throws Exception{
        return readResource(query);
    }
	@Override
	public String receiveMsg() throws Exception {
 		throw new Exception("CoapSupport | receiveMsg alone not allowed");
	}
 	@Override
	public void close() throws Exception {
		client.delete();		
	}
 
------------------------------------------------
Il RadarSystem basato su Tcp e CoAP
------------------------------------------------

Il nostro interesse su CoAP si concentra , per ora, sui seguenti aspetti:

#. CoAP fornisce un modello di interazione ancora punto-a-punto ma, essendo di tipo ``REST``, il suo utilizzo
   implica schemi di progettazione molto simili a quelli di applicazioni Web basate su HTTP;
#. l'uso di CoAP modifica il modello concettuale di riferimento per le interazioni, in quanto propone
   l'idea di accesso in lettura (GET) o modifica (PUT) a :blue:`risorse` identificate da ``URI`` attraverso un 
   unico :blue:`CoapServer`.

    
   .. image:: ./_static/img/Architectures/CoapResources.png 
     :align: center
     :width: 60%

   Da questo punto di vista, il modello è simile a quanto poroposto in  :doc:`ContextServer`, ma con
   una forte forma di :blue:`standardizzazione` sia alivello di 'verbi' di interazione (GET/PUT) sia a livello di 
   organizzazione del codice applicativo (gerarchia di risorse);
#. l'adozione del protocollo CoAP come supporto alle interazioni potrebbe indurci a modificare radicalmente 
   il software di livello applicativo già sviluppato usando TCP. Oviamente sarebbe opportuno poter 
   adottare il nuovo protocollo modificando il meno possibile quanto già prodotto.

In questa sezione vediamo come affrontare il terzo punto con riferimento al RadarSystem.

Vediamo subito il risultato.



++++++++++++++++++++++++++++++++++++++++++
Un Led accessibile via Tcp o CoAP
++++++++++++++++++++++++++++++++++++++++++

 
Impostiamo un programma (di testing) che procede nelle seguenti fasi, ciscuna realizzata da una 
specifica operazione:

#. definisce i parameteri di configurazione tramite lettura di un file o 
   mediante assegnamenti diretti alle variabili della classe ``RadarSystemConfig``;
#. configura un sistema costuito da un solo Led remoto cui 
   accede utilizzando il protocollo (Tcp o CoAP) specificato nel file di configurazione;
#. esegue almeno una volta tutte operazioni rese disponibili dalla interfaccia ``ILed``;
#. effettua la terminazione del sistema disattivando i server creati.

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

	public void setup( String fName) { 
		if( fName != null )  RadarSystemConfig
		else{
			RadarSystemConfig.protcolType = ProtocolType.coap;
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



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Configurazione 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La fase di configurazione viene divisa in due parti:

- la costruzione di un enabler tipo-server;
- la costruzione di (alemno) un proxy tipo-client.

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

La costruzione della ``LedResourceCoap`` provoca la attivazione diuna versione specializzate
del ``CoAPServer`` (un singleton di classe ``CoAPApplServer``),  se non già avvenuta in precedenza. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Esecuzione 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Terminazione 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

	public void terminate() {
		if( led instanceof LedMockWithGui ) { 
			((LedMockWithGui) led).destroyLedGui(  ); 
		}
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) ledServer.deactivate();
		else {
			CoapApplServer.getServer().stop();
			CoapApplServer.getServer().destroy();
		}
	}






------------------------------------------------
Organizzazione delle risorse
------------------------------------------------
Le risorse del nostro dominio applicativo  saranno organizzate come nella figura che segue:

.. image:: ./_static/img/Radar/CoapRadarResources.png 
    :align: center
    :width: 60%

- Il codice applicativo di gestione del Sonar viene incapsulato in una risorsa il cui URI è
  ``devices/input/sonar``
- Il codice applicativo di gestione del Led viene viene incapsulato in una risorsa di URI è
  ``devices/output/led``

Le risorse del dominio sono introdotte come specializzazioni di una classe-base.

++++++++++++++++++++++++++++++++++++++++
La risorsa-base CoapDeviceResource
++++++++++++++++++++++++++++++++++++++++

La classe astratta ``CoapDeviceResource`` è una  ``CoapResource`` che realizza la gestione delle richieste GET e PUT 
demandandole rispettivamente ai metodi ``elaborateGet`` ed  ``elaboratePut``delle classi specializzate.

.. code:: Java

   public abstract class CoapDeviceResource extends CoapResource {

    protected abstract String elaborateGet(String req);
 	protected abstract void elaboratePut(String req);	

	@Override
	public void handleGET(CoapExchange exchange) {
		Colors.out(getName() + " | handleGET arg=" + exchange.getRequestText() + " param=" + exchange.getQueryParameter("q"));
  		String answer = elaborateGet( exchange.getQueryParameter("q") );
  		exchange.respond(answer);
	}
 	@Override
	public void handlePUT(CoapExchange exchange) {
 		String arg = exchange.getRequestText() ;
 		elaboratePut( arg );
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

La classe definisce un costruttore che provvede ad  
aggiungere al server CoAP (un singleton) la risorsa creata, attivando il server se non fosse già attivo.

La risorsa viene creata come :blue:`risorsa osservabile`.

.. code:: Java

	public CoapDeviceResource(String name, DeviceType dtype)  {
		super(name);
		setObservable(true); 
		CoapApplServer coapServer = CoapApplServer.getServer(); //SINGLETION
 		if( dtype==DeviceType.input )        coapServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
 		else if( dtype==DeviceType.output )  coapServer.addCoapResource( this, CoapApplServer.outputDeviceUri);
	}

 

++++++++++++++++++++++++++++++++++++++++
Una risorsa per il Led
++++++++++++++++++++++++++++++++++++++++

.. code:: Java

	public class LedResourceCoap extends CoapDeviceResource {
	private ILed led; 
	
	public LedResourceCoap(String name, ILed led ) {
		super(name, DeviceType.output);
		this.led = led;
		//led = LedModel.create();
 	}

	@Override
	protected String elaborateGet(String req) {
 		return ""+led.getState();
	}

	@Override
	protected void elaboratePut(String req) {
		//System.out.println( getName() + " |  before elaboratePut req:" + req + " led:" + led.getState()  );
		if( req.equals( "on") ) led.turnOn();
		else if( req.equals("off") ) led.turnOff();		
		//System.out.println( getName() + " |  after elaboratePut :" + led.getState()  );
	}  

	}


++++++++++++++++++++++++++++++++++++++++
Una risorsa per il Sonar
++++++++++++++++++++++++++++++++++++++++

.. code:: Java



------------------------------------------------
TODO
------------------------------------------------

- LedUsageMain
- SonarUsageMain
- RadarSystemMainOnPcCoap
- RadarSystemMainOnPcLikeRaspCoap