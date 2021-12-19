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

Il nostro interesse su CoAP si concentra , per ora, sui seguenti aspetti:

#. CoAP fonsice un modello di interazione ancora punto-a-punto ma, essendo di tipo ``REST``, il suo utilizzo
   pone problemi di progettazione molto simili a quelli di applicazioni Web basate su HTTP;
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

------------------------------------------------
Il RadarSystem basato su Tcp e CoAP
------------------------------------------------


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
Il CoapSupport
------------------------------------------------

Come supporto di base per CoAP, usiamo la libreria Californium_ di Eclipse e

- per la costruzione di risorse CoAP, usiamo la classe  ``CoapResource`` di Californium;
- come sever CoAP usiamo ``CoapServer`` di Californium.

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


++++++++++++++++++++++++++++++++++++++++
Una risorsa per il Sonar
++++++++++++++++++++++++++++++++++++++++


------------------------------------------------
TODO
------------------------------------------------

- LedUsageMain
- SonarUsageMain
- RadarSystemMainOnPcCoap
- RadarSystemMainOnPcLikeRaspCoap