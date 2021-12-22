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
	private ResourceObserverNaive observer;
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
		observer          = new ObserverNaive();
		
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

   class ObserverNaive implements CoapHandler{
     @Override
     public void onLoad(CoapResponse response) {
        Colors.outappl("ObserverNaive:" + response.getResponseText(),Colors.GREEN);
     }
     @Override
     public void onError() { Colors.outerr("ObserverNaive error"  ); }
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
	ResourceObserverNaive:s0_s1
		 examplehandlePUT request=sobs
		 examplehandleGET request=
	modifyTheResource | put answer= code=2.04
		 ResourceObserverNaive:s0_s1_sobs
		 examplehandleGET request=
	ResourceObserverNaive:s0_s1_sobs
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
       Colors.out( getName() + "handleGET request="+exchange.getRequestText());
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
predenza, che implementa l'interfaccia ``Interaction2021``.

Nel costruttore, creiamo un CoapClient verso una CoAP resource denotata dal suo URI 
(dato l'host e il path):
 
.. code:: Java

    public class CoapSupport implements Interaction2021  {
    private CoapClient client;
    private String url;

	public CoapSupport( String address, String path) {  
		url = "coap://"+address + ":5683/"+ path;
		client = new CoapClient( url );
		client.setTimeout( 1000L );		 
	}

Aggiungiamo metodi per aggiungere un observer di tipo ``CoapHandler`` che restituisce un oggetto
di tipo ``CoapObserveRelation`` che dovrà essere utilizzato come parametro di input del metodo 
per la rimozione dell'observer: 


.. code:: Java

	public CoapObserveRelation observeResource( CoapHandler handler  ) {
	    CoapObserveRelation relation = client.observe( handler );
		return relation;
	}
	public void removeObserve(CoapObserveRelation relation) {
	    relation.proactiveCancel();	
	}

La parte che implementa ``Interaction2021`` viene realizata invocando operazioni GET e PUT tramite il CoapClient:

.. code:: Java

	@Override
	public void forward(String msg)   {
		client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
    }
	@Override
	public String request(String query)  {
		String param = query.isEmpty() ? "" : "?q="+query;
		client.setURI(url+param);
		CoapResponse respGet = client.get(  );
		return respGet.getResponseText();
	}
	@Override
	public String receiveMsg()   {
		throw new Exception("CoapSupport | receiveMsg not allowed");
	}
 	@Override
	public void close()   {
		client.shutdown();		
	}


++++++++++++++++++++++++++++++++++++++++++
Esempio di uso del CoapSupport
++++++++++++++++++++++++++++++++++++++++++

Dalla classe ``CoapSupportExampleMain`` riportiamo la configurazione:

.. code:: Java

	public class CoapSupportExampleMain {
	private CoapSupport cps;
	private CoapServer coapServer;
	private CoapResourceExample res;

	public void configure() {
		coapServer               = new CoapServer();	
		//Create the resource
		Resource resource = new CoapResourceExample("example");
		CoapResource root        = new CoapResource("root");
		res                      = new CoapResourceExample("example");
		//Add the reosurce to the server
		root.add( res );
		coapServer.add( root );
		//Start the server
		coapServer.start(); 		
		//Create the CoapSupport for the resource
		String resourceuri = "root/"+resource.getName();
		cps = new CoapSupport("localhost", resourceuri );	
	}

Per l'esecuzione, introduciamo due osservatori e qualche azione di modifica e lettura:

.. code:: Java

   public void executeWithObserver() {
    try {
      CoapObserveRelation rel1=cps.observeResource(new ObserverNaive("obs1"));
      CoapObserveRelation rel2=cps.observeResource(new ObserverNaive("obs2"));
      for( int i=1; i<=3; i++ ) {
        String vs = cps.request("");
        Colors.outappl("executeWithObserver: state i=" 
                          + i + " vs="+vs, Colors.BLUE);
        cps.forward("s"+i);
        Utils.delay(200);
      }
      Utils.delay(300); 
      //Remove the first observer
      cps.removeObserve(rel);
      Utils.delay(200);
      //Remove the second observer
      cps.removeObserve(rel2);
      Utils.delay(200);
    } catch (Exception e) {
      Colors.outerr("executeWithObserver error"+ e.getMessage());	 
    }	
   }




.. DOPO L'esecuzione mostra che la ``SonarResourceCoap`` include un Sonar (attivato con il dispatch 
.. **activate**) la cui attività modifica il valore corrente
.. della risorsa che viene osservato dagli observer (quando presenti) o con la esecuzione di una
.. request **getDistance**



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
aggiungerla al server CoAP (il singleton ``CoapApplServer``), attivandolo - se già non lo fosse.

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

------------------------------------------------
Il Server delle risorse applicative
------------------------------------------------
 
Il server ``CoapApplServer`` è una estensione di ``CoapServer`` che realizza un singleton capace 
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
La risorsa CoAP  per il Led è una specializzazione di ``CoapDeviceResource`` che 
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
- la costruzione di (alemno) un proxy tipo-client per il Led.

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
Esecuzione 
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
La risorsa CoAP  per il Sonar è una specializzazione di ``CoapDeviceResource`` che 
incorpora un Sonar, a cui ridirige le richieste GET di lettura  e i comandi 
PUT di attivazione/disativazione.

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


Le possibili configurazioni sono:

- Sonar come oggetto semplice (POJO), che implementa l'interfaccia  `ISonar`_ ; 
- Sonar come POJO osservabile che implementa l'interfaccia `ISonarObservable`_.

Il Sonar (semplice od osservabile) può essere reso utilizzabile da remoto: 

- via TCP, con un enabler tipo-server (``EnablerSonarAsServer``) che invia i messaggi 'semplici' al gestore 
  applicativo ``SonarApplHandler``;
- via TCP, attraverso un ``TcpContextServer``, che ridirige il payload di messaggi di tipo ``ApplMessage``
  al gestore applicativo ``SonarApplHandler``;
- via CoAP, all'interno di una risorsa ``CoapResource`` ( come ``SonarResourceCoap`` che estende ``CoapDeviceResource``)
  con URI= ``devices/ouput/sonar``.

I paranetri di configurazione sono espressi dalle seguenti variabili:

.. code:: Java

  RadarSystemConfig.protcolType
  boolean withContext   //usata se RadarSystemConfig.protcolType=ProtocolType.tcp
  

AL Sonar può essere associato un observer (o più):

- realizzato come POJO che implementa la interfaccia `IObserver`_ (ad esempio ``SonarObserverFortesting``);
- realizzato come componente CoAP che implementa l'interfaccia ``CoapHandler`` (ad esempio ``CoapApplObserver``).


Per accedere al Sonar si possono usare:

- clienti di tipo ``ProxyAsClient`` (come ``SonarProxyAsClient``) che implementano l'interfaccia ``ISonar``. 
  Questi client inviano messaggi (semplici o di tipo ``ApplMessage``, secondo la configurazione selezionata) al server 
  cui sono connessi;
- supporti di tipo ``CoapSupport`` che implementano l'interfaccia ``Interaction2021`` inviando richieste GET/PUT


.. code:: Java

  public class SonarUsageMain  {
  private EnablerAsServer sonarServer;
  private ISonar client1, client2;



------------------------------------------------
TODO
------------------------------------------------

- LedUsageMain
- SonarUsageMain
- RadarSystemMainOnPcCoap
- RadarSystemMainOnPcLikeRaspCoap