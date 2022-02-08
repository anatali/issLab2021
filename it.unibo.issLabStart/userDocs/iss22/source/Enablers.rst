.. role:: red 
.. role:: blue 
.. role:: remark

.. _pattern-proxy: https://it.wikipedia.org/wiki/Proxy_pattern

.. _port-adapter: https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)

.. _CoAP: https://coap.technology/

=====================================================
Abilitatori di comunicazione
=====================================================

L'analisi del problema ha posto in evidenza (si veda :ref:`concettodienabler`) 
la opportunità/necessità,
di introdurre nel sistema degli :blue:`enabler`, che hanno lo scopo di fornire funzionalità
di ricezione/trasmissione di informazione su rete a un nucleo di 
*core-code* incapsulato al proprio interno.

Nell'ambito di un processo di sviluppo bottom-up in cui il procollo TCP è
la tecnologia di riferimento per le comunicazioni, risulta naturale pensare subito a 
un enabler *tipo-server* capace di ricevere richieste  da parte di client remoti (normalmente
dei Proxy).

.. due tipi di enabler: uno per ricevere (diciamo un enabler *tipo-server*) e uno per trasmettere (diciamo un enabler *tipo-client*).
 
Come suggerito nell'analisi, ponendo il ``Controller`` su PC, 
possiamo  (senza modificare il codice introdotto in :ref:`Controller<controller>`)
impostare una architettura come quella rappresentata in figura:

.. image:: ./_static/img/Radar/ArchLogicaOOPEnablersBetter.PNG 
   :align: center
   :width: 50%

Ricordando la proposta delle architetture  `port-adapter`_,  decidiamo, come progettisti,
di proseguire lo sviluppo del software del sistema con riferimento ad una architettura a livelli
rappresentata come segue:


.. image:: ./_static/img/Architectures/cleanArchCone.jpg 
   :align: center
   :width: 50%

.. _Enabler:

------------------------------------------------
Enabler tipo-server
------------------------------------------------

Iniziamo con il definire un enabler *tipo-server* che demanda la gestione dei messaggi ricevuti 
ad oggetti di una classe che implementa :ref:`IApplMsgHandler`.

.. image:: ./_static/img/Radar/EnablerAsServer.PNG
   :align: center 
   :width: 60%
 
.. code:: java

  public class EnablerAsServer{
    private static int count=1;
    protected String name;
    protected ProtocolType protocol; 
    protected TcpServer serverTcp;

    public EnablerAsServer(String name, int port,  
                       ProtocolType protocol, IApplMsgHandler handler ) {
    try {
      this.name     			= name;
      this.protocol 			= protocol;
      if( protocol != null ) setServerSupport( port, protocol, handler );
      }catch (Exception e) { ... }
    }	
    protected void setServerSupport(int port,ProtocolType protocol,
                      IApplMsgHandler handler) throws Exception{
      if( protocol == ProtocolType.tcp ) {
          serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port, handler );        
      }else if( protocol == ... ) { ...  }
      ...
    }	 
    public void activate() {
      if( protocol == ProtocolType.tcp ) {
        serverTcp.activate();
      }else  ...	
    }   
  public void deactivate() {
      if( protocol == ProtocolType.tcp ) {
        serverTcp.deactivate();
      }else ...
    }   
  }

Notiamo che:

- nel caso ``protocol==null``, non viene creato alcun supporto. 
  Questo caso sarà applicato più avanti, nella sezione  :doc:`ContestiContenitori`.
- si prevede anche la possibilità di utilizzare altri protocolli
  .. un supporto per il protocollo CoAP (:doc:`RadarSystemCoap`), di cui parleremo nella sezione :doc:`RadarGuiCoap`.

.. _IApplLogicNoCtx:

------------------------------------------
Trasduttori applicativi
------------------------------------------

Ogni enabler deve ricevere in ingresso un gestore  applicativo (handler) che implementa :ref:`IApplMsgHandler` estendendo 
la classe :ref:`ApplMsgHandler<ApplMsgHandler>`. L'handler deve definire il metodo ``elaborate`` che gestisce
i comandi o le richieste ricevute dal sever in forma di messaggi.

L'handler deve quindi fare fronte a due compiti:

#. interpretare un messagio e tradurlo in un comando o richiesta al dispositivo Sonar
#. inviare al mittente la risposta, in caso il messaggio sia una richiesta

Facendo riferimento al single responsibility principle, conviene delegare il primo compito ad
un componente che non 'sappia nulla' della *dimensione interazione* e che si occupi solo della
interpretazione del messaggio. Introduciamo una interfaccia per componenti di questo tipo: 



.. code:: java

  public interface IApplLogic {
    public String elaborate( String message );
  }


.. _LedApplLogicNoCtx:

+++++++++++++++++++++++++++++++++++++
Un trasduttore per il Led
+++++++++++++++++++++++++++++++++++++

.. code:: java
  
  public class LedApplLogic implements IApplLogic  {
  ILed led;
    public LedApplLogic(  ILed led) { this.led = led; }

    public String elaborate( String message ) {
      //Analizza message e invoca il led, restituendo un risultato o una risposta
      if( message.equals("getState") ) return ""+led.getState() ;
      else if( message.equals("on")) led.turnOn();
      else if( message.equals("off") ) led.turnOff();	
      return message+"_done";
    }

.. _SonarApplLogicNoCtx:

+++++++++++++++++++++++++++++++++++++
Un trasduttore per il Sonar
+++++++++++++++++++++++++++++++++++++

Ad esempio, nel caso del Sonar:

.. code:: java

  public class SonarApplLogic implements IApplLogic{
  private	ISonar sonar;

    public SonarApplLogic(ISonar sonar) { this.sonar = sonar; }    
    @Override
      public String elaborate(String message) {
      //Analizza message e invoca il Sonar restituendo un risultato o una risposta
      }
  }

 

.. I messaggi possono essere semplici sringhe oppure oggetti di tipo :ref:`ApplMessage<ApplMessage>` che introdurremto in :doc::`ApplMessage<ApplMessage>`.


------------------------------------------
Il caso del Sonar 
------------------------------------------

.. image::  ./_static/img/Radar/EnablerProxySonar.PNG
         :align: center 
         :width: 60%


++++++++++++++++++++++++++++++++++++++++
Enabler per il Sonar
++++++++++++++++++++++++++++++++++++++++
.. list-table::
  :widths: 30,70
  :width: 100%

  * - .. image::  ./_static/img/Radar/EnablerAsServerSonar.PNG
         :align: center 
         :width: 80%
    - L'*enabler tipo server* per il Sonar è un ``EnablerAsServer`` connesso un gestore 
      applicativo ``SonarApplHandler`` che si avvale di :ref:`SonarApplLogicNoCtx` per 
      trasformare messaggi in chiamate di metodi:
      
.. di tipo ``IApplMsgHandler`` che estende  la classe :ref:`ApplMsgHandler<ApplMsgHandler>` fornendo un metodo che elabora:

      - i *comandi*: ridirigendoli al sonar locale 
      - le *richieste*:  ridirigendole al sonar locale e inviando la risposta al client 

.. _SonarApplHandlerNoContext:

+++++++++++++++++++++++++++++++++++
SonarApplHandler
+++++++++++++++++++++++++++++++++++

.. code:: java

  public class SonarApplHandler extends ApplMsgHandler  {
  private IApplLogic sonarLogic;

    public SonarApplHandler(String name, ISonar sonar) {
      super(name);
      sonarLogic = new SonarApplLogic(sonar);
    }

    @Override
    public void elaborate(String message, Interaction2021 conn) {
      if( message.equals("getDistance") || message.equals("isActive")  ) {
      }else sonarLogic.elaborate(message);
    }
  }

.. _SonarProxyAsClientNoContext:

++++++++++++++++++++++++++++++++++++++++
Proxy per il Sonar
++++++++++++++++++++++++++++++++++++++++

.. list-table::
  :widths: 30,70
  :width: 100%

  * - .. image::  ./_static/img/Radar/SonarProxyAsClient.PNG
         :align: center 
         :width: 70%
    - Il '*proxy tipo client* per il Sonar è una specializzazione di  :ref:`ProxyAsClient` che implementa i 
      metodi di ``ISonar`` inviando comandi o richieste all'*enabler tipo server* sulla connessione 
      :ref:`Interaction2021<Interaction2021>`:


.. code:: java

  public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
    public SonarProxyAsClient( 
         String name, String host, String entry, ProtocolType protocol ) {
    super( name,  host,  entry, protocol );
    }
    @Override
    public void activate() { sendCommandOnConnection("activate"); }
    @Override
    public void deactivate() { sendCommandOnConnection("deactivate"); }
    @Override
    public IDistance getDistance() {
      String answer = sendRequestOnConnection("getDistance");
      return new Distance( Integer.parseInt(answer) );
    }
    @Override
    public boolean isActive() {
      String answer = sendRequestOnConnection("isActive");
      return answer.equals( "true" );
    }
  }

 

-----------------------------------------
Il caso del Led
-----------------------------------------
Il caso del Led è simile al caso del Sonar, sia per quanto riguarda l'enabler, sia per quanto riguarda il proxy.

.. image::  ./_static/img/Radar/EnablerProxyLed.PNG
         :align: center 
         :width: 60%

 
Riportiamo qui solo la struttura dell'handler che realizza la logica applicativa.

.. _LedApplHandlerNoContext:

+++++++++++++++++++++++++++++++++++
LedApplHandler
+++++++++++++++++++++++++++++++++++

.. code:: Java

  public class LedApplHandler extends ApplMsgHandler   {
  private IApplLogic ledLogic;

    public LedApplHandler(String name, ILed led) {
      super(name);
      ledLogic = new LedApplLogic(led) ;
    }
    
    @Override
    public void elaborate(String message, Interaction2021 conn) {
      if( message.equals("getState") ) sendMsgToClient( ledLogic.elaborate(message), conn );
      else ledLogic.elaborate(message);
    }
  }

.. _testingEnablers:

-----------------------------------------
Testing degli enabler
-----------------------------------------

La procedura si setup (configurazione) del testing crea gli elementi della architettura di figura:

.. image::  ./_static/img/Radar/TestEnablers.PNG
         :align: center 
         :width: 50%


.. code::  java

  public class TestEnablersTcp {
  @Before
  public void setup() {
    RadarSystemConfig.withContext= false; 
    RadarSystemConfig.simulation = true;
    RadarSystemConfig.ledGui     = true;
    RadarSystemConfig.ledPort    = 8015;
    RadarSystemConfig.sonarPort  = 8011;
    RadarSystemConfig.sonarDelay = 100;
    RadarSystemConfig.testing    = false;
    RadarSystemConfig.tracing    = false;
 
    //I devices
    sonar 	= DeviceFactory.createSonar();
    led     = DeviceFactory.createLed();
		
    //I server
    sonarServer = new EnablerAsServer("sonarSrv",RadarSystemConfig.sonarPort,
                protocol, new SonarApplHandler("sonarH", sonar) );
    ledServer   = new EnablerAsServer("ledSrv",  RadarSystemConfig.ledPort, 
                protocol, new LedApplHandler("ledH", led)  );
 
    //I proxy
    sonarPxy = new SonarProxyAsClient( "sonarPxy", "localhost", 
              ""+RadarSystemConfig.sonarPort, protocol );		
    ledPxy   = new LedProxyAsClient( "ledPxy",   "localhost", 
             ""+RadarSystemConfig.ledPort,   protocol );	
  }

  @After
  public void down() {
    ledServer.stop();
    sonarServer.stop();
  }	
	
 	

Il test simula il comportamento del Controller, senza RadarDisplay:

.. code::  java

	@Test 
	public void testEnablers() {
		sonarServer.start();
		ledServer.start();
		System.out.println(" ==================== testEnablers "  );
 		
		//Simulo il Controller
 		Utils.delay(500);		
		
		//Attivo il sonar
		sonarPxy.activate();
		System.out.println("testEnablers " + sonarPxy.isActive());
		
		while( sonarPxy.isActive() ) {
			int v = sonarPxy.getDistance().getVal();
			ColorsOut.out("testEnablers getVal="+v, ColorsOut.GREEN);
			//Utils.delay(500);
			if( v < RadarSystemConfig.DLIMIT ) {
				ledPxy.turnOn();
				boolean ledState = ledPxy.getState();
				assertTrue( ledState );	
			}
			else {
				ledPxy.turnOff();
				boolean ledState = ledPxy.getState();
				assertTrue( ! ledState );	
			}
		}		
	}

-----------------------------------------
Da POJO a gestori di messaggi
-----------------------------------------

Al termine di questa fase dello sviluppo, poniamo in evidenza alcuni punti, che potrebbero
emergere al termine della SPRINT-review:

- i nuovi componenti-base di livello applicativo non sono più POJO, ma sono
  gestori di messaggi, come ad esempio `SonarApplHandler`_  e `LedApplHandler`_;
- i POJO originali (come :ref:`Sonar<Sonar>` e :ref:`Led<Led>`) sono stati incapsulati 
  negli handler che specializzano la  classe :ref:`ApplMsgHandler<ApplMsgHandler>`;
- i gestori di messaggi lavorano all'interno di componenti (:ref:`Enabler<Enabler>`) 
  che forniscono una infrastruttura per le comunicazioni via rete. Il codice
  che realizza gli enabler e i proxy può essere riutilizzato in altre applicazioni;
- l'attenzione dell':blue:`Application Designer` si concentra sulla definizione del metodo 
  ``elaborate`` di componenti-gestori di tipo :ref:`ApplMsgHandler<ApplMsgHandler>` 
  che ricevono dalla
  infrastruttura-enabler un oggetto (di tipo  :ref:`Interaction2021<Interaction2021>`) 
  che abilita alle interazioni via rete;
- i messaggi gestiti dagli handler sono  ``String`` di struttura non meglio specificata;

.. notiamo però che gli handler sono già predisposti per gestire messaggi più strutturati,  rappresentati  dalla classe  ``ApplMessage`` (si veda :ref:`ApplMessage`).


Il :ref:`testing degli enablers<testingEnablers>`  ha già mostrato come sia possibile affrontare 
il punto 4 del nostro :ref:`piano di lavoro<PianoLavoro>` 

-  assemblaggio dei componenti  per formare il sistema distribuito.

Tuttavia emerge un punto critico:

:remark:`introdurre un serverTCP per ogni componente potrebbe essere, in generale, troppo costoso`

Un serverTCP richiede infatti la creazione di un nuovo Thread. Anche se il costo di questa
operazione potrebbe essere (notevolmente) ridotto sostituendo il Thread Java con la 
coroutine Kotlin, il team di sviluppo osserva che lo si può evitare con una modifica 
non troppo complessa.


La modifica parte da questa domanda: è possibile che i gestori applicativi di messaggi (gli handler)
possano essere dotati di capacità di comunicazione avvalendosi di un *singolo serverTCP* 
per nodo computazionale?


La prossima sezione sarà dedicata alla realizzazione di questa idea, che ci farà fare
un ulteriore passo in avanti nella transizione dal paradigma ad oggetti al paradigma
a messaggi.

