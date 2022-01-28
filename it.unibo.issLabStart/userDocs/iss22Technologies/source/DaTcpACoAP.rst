.. role:: red 
.. role:: blue 
.. role:: remark

.. _Californium: https://www.eclipse.org/californium/
.. _Shodan : https://www.shodan.io/ 


==================================================
Da TCP a CoAP
==================================================


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

Ciò nonostante, Shodan_ (un motore di ricerca per i dispositivi connessi a Internet) 
mostra 433,973 voci come risultato per la ricerca 'CoAP' (contro 249,335,005 per 'HTTP').
 
------------------------------------------------
La libreria Californium: un esempio di uso
------------------------------------------------

Come supporto di base per CoAP, usiamo la libreria Californium_ di Eclipse e

- per la costruzione di risorse CoAP, usiamo la classe  ``CoapResource``;
- per la creazione di un sever CoAP, usiamo ``CoapServer``;
- per la creazione di un client per CoAP, usiamo ``CoapClient``;
- per la reagire in modo asincrono alle risposte di un CoAP-client che funge da osservatore, 
  implementiamo l'interfaccia ``CoapHandler``.

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



