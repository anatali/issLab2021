.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

.. ``  https://bashtage.github.io/sphinx-material/rst-cheatsheet/rst-cheatsheet.html

======================================
RadarSystem
======================================
Tetendo conto dal nostro motto: 

:remark:`non c’è codice senza progetto, progetto senza analisi del problema, problema senza requisiti`

impostiamo un processo di produzione del software partendo da un insieme di requisiti.

--------------------------------------
Requisiti
--------------------------------------

Si desidera costruire un'applicazione software capace di: 

- (requisito :blue:`radarGui`) mostrare le distanze rilevate da un sensore ``HC-SR04`` connesso a un RaspberryPi 
  su un display (``RadarDisplay``) a forma di radar connesso a un PC
  
.. image:: ./_static/img/Radar/radarDisplay.png
   :align: center
   :width: 20%
   
- (requisito :blue:`ledAlarm`) accendere un LED se la distanza rilevata è inferiore a un valore limite prefissato
  denominato ``DLIMIT``.

--------------------------------------
Analisi dei Requisiti
--------------------------------------

Iniziamo ponendo al customer una serie di domande e riportiamone le risposte:

.. list-table:: 
   :widths: 50,50
   :width: 100%

   * - Il LED può/deve essere connesso allo stesso RaspberryPi del sonar? 
     - Al momento si. In futuro però il LED potrebbe essere connesso a un diverso nodo di elaborazione.
   * - Il committente fornisce qualche libreria per la costruzione del display ?
     - Si, viene reso disponibile il supporto  ``radarPojo.jar`` scritto in JAVA che fornisce un oggetto
       di classe ``radarSupport`` capace di creare una GUI in 'stile radar' e di visualizzare dati su di essa:

       .. code:: java

        public class radarSupport {
        private static RadarControl rc;
        public static void setUpRadarGui( ){
          rc=...
        }
        public static void update(String d,
                                  String dir){
		      rc.update( d, dir );
        }
        }    

       Il supporto è realizzato dal progetto *it.unibo.java.radar*.
   * - Il valore ``DLIMIT`` deve essere cablato nel sistema o è bene sia 
       definibile in modo configurabile dall'utente finale?
     - L'utente finale deve essere in grado di specificare in un 'file di configurazione' il valore di questa distanza.
 
Dai requisiti possiamo asserire che:

- si tratta di realizzare il software per un **sistema distribuito** costituito da due nodi di elaborazione:
  un RaspberryPi e un PC convenzionale;
- i due nodi di elaborazione devono potersi  `scambiare informazione via rete`, usando supporti WIFI;
- i due nodi di elaborazione devono essere 'programmati' usando **tecnologie software diverse**.

In sintesi:


:remark:`Si tratta di realizzare un sistema software distribuito ed eterogeno`

+++++++++++++++++++++++++++++++++++++
Piano di testing (funzionale)
+++++++++++++++++++++++++++++++++++++  

.. Requisito :blue:`ledAlarm`:

Un test funzionale consiste nel porre un ostacolo davanti al Sonar
prima a una distanza ``D>DLIMIT`` e poi a una distanza ``D<DLIMIT`` e osservare il valore
visualizzato sulla GUI.

Tuttavia questo modo di procedere non è automatizzabile, in quanto richiede 
la presenza di un operatore umano. Nel seguito cercheremo di organizzare le cose in modo
da permettere :blue:`Test automatizzati`.

--------------------------------------
Analisi del problema
--------------------------------------

Per analizzare le problematiche implicite nei requisiti, dobbiamo porre molta attenzione a non confondere 
l'analisi **del problema** con l'analisi **di come pensiamo di risolvere** il problema.

Due sono gli approcci principali possibili:

- approccio :blue:`bottom-up`: partiamo da quello che abbiamo a disposizione e analizziamo i problemi che
  sorgono per 'assemblare le parti disponibili' in modo da costruire un sistema che soddisfi i requisiti funzionali;
- approccio :blue:`top-down`: partiamo analizzando le proprietà che il sistema deve 'logicamente' avere 
  senza legarci a priori ad alcun specifico componente e/o tecnologia e poi evidenziamo le
  problematiche che sorgono per soddisfare i requisiti funzionali e per utilizzare (se si pone il caso) 
  componenti forniti dal committente o dalla nostra azienda e/o framework e infrastrutture disponibili sul mercato 
  (con una evidente propensione  all'open-source e al free software).

E' molto probabile che la maggior marte delle persone sia propensa a seguire (almeno inizialmente) un
approccio bottom-up, essendo l'approccio top-down meno legato a enti concretamente usabili come 
'building blocks'. 

Osserviamo però che il compito della analisi del problema non è quello di trovare subito una soluzione, 
ma quello di porre in luce le problematiche in gioco (il :blue:`cosa` si deve fare) e capire con quali risorse 
(tempo, persone, denaro, etc. )  queste problematiche debbano/possano essere affrontate e risolte.
Sarà compito deo progettisti quello di trovare il modo (il :blue:`come`) pervenore ad una soliuzione 'ottimale'
date le risorse a disposizione.

Anticipiamo subito che il nsotro approccio di riferimento sarà di tipo top-down, per motivi che si dovrebbero
chirarire durante il percorso che cominciamo adesso seguendo, al momento, un tipico modo di procedere bottom-up.

Sarà proprio rendendoci conto dei limiti di questo modo di procedere che acquisiremo (se non l'abbiamo già)
il convicimento che conviene chiarire bene il :blue:`cosa` prima di affrontare il :blue:`come`.

++++++++++++++++++++++++++++++++++++++
Un approccio bottom-up
++++++++++++++++++++++++++++++++++++++

La costruzione del sistema pone le seguenti :blue:`problematiche`:

.. list-table::
   :widths: 40,60
   :width: 100%

   * - Gestione del sensore ``HC-SR04``.
     - A questo fine la software house dispone già di codice riutilizzabile, ad esempio 
       ``SonarAlone.c`` (progetto *it.unibo.rasp2021*)
   * - Realizzazione del ``RadarDisplay``.
     - A questo fine è disponibile il POJO realizzato da  ``radarPojo.jar`` 
   * - Gestione del Led.
     - A questo fine la software house dispone già di codice riutilizzabile, ad esempio 
       ``led25GpioTurnOn.sh`` e ``led25GpioTurnOff.sh``.
   * - Quale assemblaggio?
     - .. image:: ./_static/img/Radar/RobotSonarStarting.png
            :width: 100%
       Occorre capire come i dati del sonar generati sul Raspberry possano raggiungere il PC ed essere usati per
       aggiornare la ``RadarGui`` e per accendere/spegnere il ``Led``.

La necessità di integrare i componenti disponibili *fa sorgere altre problematiche*:

   #. è opportuno incapsulare i componenti disponibli entro altri componenti capaci di interagire via rete?
   #. dove è più opportuno inserire la 'businenss logic'? In un oggetto che estende il sonar o il ``radarSupport``?
      Oppure è meglio introdurre un terzo componente?
   #. quale forma di interazione è più opportuna? diretta/mediata, sincrona/asincrona?.

Focalizzando l'attenzione sul requisito :blue:`RadarGui` e quindi sulla interazione *sonar-radar* 
(per il Led valgono considerazioni analoghe)
possiamo rappresentare la situazione come segue:

.. list-table::
   :widths: 50,50
   :width: 100%

   *  - :blue:`Comunicazione diretta`
        
        Le 'nuovolette' in figura rappresentano gli strati di software che permettono ai dati generati dal Sonar 
        di eseere ricevuti dal ``RadarDisplay``.

      -   .. image:: ./_static/img/Radar/srrIntegrate1.png
            :width: 100%
   *  - :blue:`Comunicazione mediata`

        Richiede la presenza di un :blue:`componente mediatore (broker)`, di solito realizzato da terze parti 
        come servizio disponibile in rete. Un generatore di dati (come il Sonar) pubblica informazione  
        su una :blue:`topic` del broker; tale informazione
        che potrebbe essere ricevuta ('osservata') da uno o più ricevitori (come il RadarDisplay) che si iscrivono 
        a quella *topic*.  

      -   .. image:: ./_static/img/Radar/srrIntegrate2.png
            :width: 100%
          
          TODO: Modificare la figura
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quale 'collante'?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Seguendo il principio che la responsabilità di realizzare gli use-cases applicativi non deve essere attribuita
al software di gestione dei dispositivi di I/O, la nostra analisi ci induce a sostenere
l'opportunità di introdurre un nuovo componente, che possiamo denominare ``Controller``), che abbia la
:blue:`responabilità di realizzare la logica applicativa`.

Ma ecco sorgere un'altra problematica legata alla distribuzione:

-  Il ``Controller`` deve ricevere in ingresso i dati del sensore ``HC-SR04``, elaborarli e  
   inviare comandi al Led e dati al  ``RadarDisplay``.
       
- Il ``Controller`` può risiedere su RaspberryPi, sul PC o su un terzo nodo. 
  Tuttavia, un colloquio con il committente ha escluso (per motivi di costo) la possibilità di introdurre un altro
  nodo di elaborazione. 

- La presenza di un broker in forme di comunicazioni mediata  potrebbe indurci ad attribuire responsabiliotà
  applicative al mediatore. Ma è giusto/opportuno procedere i questo modo?

Dunque si tratta di analizzare dove sia meglio allocare il ``Controller`` :

.. list-table::
   :widths: 30,70
   :width: 100%

   * - ``Controller`` sul RaspberryPi.
     - Si avrebbe una maggior reattività nella accensione del Led in caso di allarme. Inoltre ...
       
   * - ``Controller`` sul PC.
     - Si avrebbe più facilità nel modificare la logica applicativa,
       lasciando al Raspberry solo la responsabilità di gestire dispositivi. Inoltre ...
   * - ``Controller`` sul broker.
     - Al momento escludiamo questa possibilità, riservandoci di riprendere il problema quando esamineremo
       architetture distribuite 'space-based'.

Queste considerazioni ci inducono a riflettere sul :blue:`tipo di protocollo` da scegliere per le comunicazioni via rete
e sul :blue:`tipo di architettura` che   scaturisce da questa scelta.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Tipi di protocollo
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

In questa fase, possiamo diviedere i protocolli di comunicazioni più diffusi in due macro-categorie:

- protocolli :blue:`punto-a-punto` che stabiliscono un *canale bidirezionale* tra compoenenti di solito
  denominati client e  server. Esempi di questo tipo sono ``UDP, TCP, HTTP, CoAP, Bluetooth``.
- protocolli :blue:`publish-subscribe` che si avvalgono di un mediatore (broker) tra client e server. Esempio
  di questo tipo di protocollo è ``MQTT`` che viene supportato da broker come ``Mosquitto e RabbitMQ``. 

Al momento dovremmo avere conoscenze su come usare protocolli quali TCP/UDP e HTTP
ma siamo forse meno esperti nell'uso di supporti per la comunicazione mediata tramite broker.

Seguiamo dunque l'idea delle **comunicazioni dirette** facendo riferimento al protocollo TCP
(più affidabile di UDP e supporto di base per HTTP)  che assume quindi il ruolo di 'collante' principale tra le parti.

+++++++++++++++++++++++++++++++++++++++++++++++++
Analisi delle interazioni (basate su TCP)
+++++++++++++++++++++++++++++++++++++++++++++++++
A questo punto è necessario approfondire l'analisi delle problematiche che si pongono quando si voglia 
far comunicare due componenti software con un protocollo di comunicazione punto-a-punto come TCP.
Ovviamente in questa fase non ci interessano tanto i dettagli tecnici di come opera il protocollo,
quanto le ripercussioni dell'uso del protocollo sulla architettura del sistema.

A questo riguardo possiamo dire che nel sistema dovremo avere componenti capaci
di operare come un `client-TCP` e componenti capacai di operare come un `server-TCP`.

.. list-table::
  :widths: 15,85
  :width: 100%

  * - Server
    - Il server opera su un nodo con indirizzo IP noto (diciamo ``IPS``) , apre una ``ServerSocket`` su una  porta 
      (diciamo ``P``) ed attende messaggi  di connessione su ``P``.

  * - Client
    - Il client deve dapprima aprire una ``Socket`` sulla coppia ``IPS,P`` e poi inviare o ricevere messaggi su tale socket.
      Si stabilisce così una *connessione punto-a-punto bidirezionale* tra il nodo del client e quello del server.

Inizialmente il server opera come ricevitore di messaggi e il client come emettitore. Ma su una connessione TCP,
il server può anche dover inviare messaggi ai client, ad esempio quando  si richiede una interazione di tipo
:blue:`request-response`. In tal caso, il client deve essere anche capace di agire come ricevitore di messaggi.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'idea di connessione: l'interfaccia ``Interaction2021``
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'analisi bottom-up sull'uso del protocollo TCP  ha evidenziato che, volendo riusare i componenti software resi disponibile dal commitente,
risulta necessario dotarli della capacità di inviare e ricevere messaggi via rete.

Questa necessità segnala un :blue:`gap`  tra il livello tecnologico di partenza e le necessità del problema.

Coma analisti, osserviamo anche che un *gap* relativo alle comunicazioni di rete **si presenta in modo sistematico
in tutti i sistemi distribuiti**. Sarebbe dunque opportuno cercare di colmare questo *gap* in modo non episodico,
introducendo :blue:`componenti riusabili` che possano 'sopravvivere' alla applicazione che stiamo costruendo
per poter essere impiegati in futuro in altre applicazioni distribuite.

Astraendo dallo specifico protocollo, osserviamo che tutti i principali protocolli punto-a-punto 
sono in grado di stabilire una :blue:`connessione` stabile sulla quale inviare e ricevere messaggi.

Questo concetto può essere realizzato da un oggetto che rende disponibile opprtuni metodi, come quelli definiti
nella seguente interfaccia:

.. code:: Java

  interface Interaction2021  {	 
    public void forward(  String msg ) throws Exception;
    public String receiveMsg(  )  throws Exception;
    public void close( )  throws Exception;
  }

Il metodo di trasmissione è denominato ``forward`` per rendere più evidente il fatto che pensiamo ad un modo di operare 
:blue:`fire-and-forget`. 

L'informazione scambiata è rappresenta da una ``String`` che è un tipo di dato presente in tutti
i linguaggi di programmazione.
Non viene introdotto un tipo (non-primitivo) diverso (ad esempio ``Message``) perchè non si vuole staibilire 
il vincolo che gli end-points della connessione siano componenti codificati nello medesimo linguaggio di programmazione

La ``String`` restituita dal metodo ``receiveMsg`` può rappresentare una risposta a un messaggio
inviato in precedenza con ``forward``.

Ovviamente la definizione di questa interfaccia potrà essere estesa e modificata in futuro, ad esempio nella fase di
progettazione, ma rappresenta una forte indicazione dell'analista di pensare alla costruzione di componenti
software che possano ridurre il costo delle applicazioni future.

--------------------------------------
Progettazione
--------------------------------------

Iniziamo il nostro progetto con questo piano di lavoro:

#. definizione dei componenti software legati ai dispositivi di I/O (Sonar, RadarDisplay e Led);
#. definizione di alcuni supporti di base per componenti lato client a lato server;
#. definizione componenti (denominati genericamente :blue:`enabler`)  capaci di abilitare i componenti-base 
  alle comunicazioni via rete (con TCP).

+++++++++++++++++++++++++++++++++++++++++++++
Componenti per i dispositivi di I/O
+++++++++++++++++++++++++++++++++++++++++++++

E' buona pratica impostare la definzione di un componente partendo dalla specifica delle funzionalità
che esso offre.

Per il Sonar e il Led, introduciamo le seguenti interfacce:

.. list-table::
  :widths: 50, 50
  :width: 100%

  * -  Sonar
    -  Led
   
  * -  
      
       .. code:: java

        public interface ISonar {
          public void activate();		 
          public void deactivate();
          public int getVal();	
          public boolean isActive();
        }
    -  
       .. code:: java

         public interface ILed {
          public void turnOn();
          public void turnOff();
          public boolean getState();
        }
   

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dispositivi reali e Mock, DeviceFactory e file di configurazione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Per agevolare la messa a punto di una applicazione, conviene spesso introdurre Mock-objects, cioè
dispositivi simulati che riproducono il comportamento dei dispositivi reali in modo controllato.

Inoltre, per facilitare la costruzione di dispositivi senza dover denotare in modo esplicito le classi
di implementazione, conviene introdurre una Factory:

.. code:: java

  public class DeviceFactory {
    public static ILed createLed() { ... }
    public static ISonar createSonar() { ... }
    public static IRadarGui createRadarGui() {
  }

Ciasun metodo di ``DeviceFactory`` restitusce una istanza di dispositivo reale o Mock in accordo alle specifiche
contenute in un file di Configurazione (``RadarSystemConfig.json``) scritto in JSon:

.. code:: java

  {
  "simulation"       : "true",
   ...
  "DLIMIT"           : "15",
  }

Si noti che questo file contiene anche la specifica di ``DLIMIT`` come richiesto in fase di analisi dei requisiti.

Questo file di configurazione viene letto dal metodo *setTheConfiguration* di un singleton Java ``RadarSystemConfig``
che inizializza variabili ``static`` accessibili all'applicazione:

.. code::  java

  public class RadarSystemConfig {
    public static boolean simulation = true;  //overridden by setTheConfiguration
    ...
    public static void setTheConfiguration( String resourceName ) { 
      ... 
      fis = new FileInputStream(new File(resourceName));
	    JSONTokener tokener = new JSONTokener(fis);
	    JSONObject object   = new JSONObject(tokener);

      simulation = object.getBoolean("simulation");
      ...
    }
  }

Per essere certi che un dispositivo Mock possa essere un sostituto efficace di un dispositivo reale,
introduciamo per ogni dispositivo una classe astratta comune alle due tipologie, che funga anche da factory.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Led
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

Un Led è un dispositivo di output che può essere modellato e gestito in modo semplice.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe astratta LedModel
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe astratta relativa al Led introduce un metodo :blue:`abstract` denominato ``ledActivate``
cui è demandata la responsabilità di accendere/spegnare il Led.

.. code:: java

  public abstract class LedModel implements ILed{
    private boolean state = false;	

    //Factory methods    
    public static ILed create() {
      ILed led;
      if( RadarSystemConfig.simulation ) led = createLedMock();
      else led = createLedConcrete();
      led.turnOff();      //Il led iniziale è spento
    }
    public static ILed createLedMock() { return new LedMock();  }
    public static ILed createLedConcrete() { return new LedConcrete();     }	
    
    //Abstract methods
    protected abstract void ledActivate( boolean val);
    
    protected void setState( boolean val ) { 
      state = val; ledActivate( val ); 
    }
    @Override
    public void turnOn(){ setState( true ); }
    @Override
    public void turnOff() { setState( false ); }
    @Override
    public boolean getState(){  return state;  }
  }

La variabile locale booleana ``state`` viene posta a ``true`` quando il led è acceso.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il LedMock
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

In pratica il LedModel è già un LedMock, in quanto tiene traccia dello stato corrente nella variabile
``state``. 

Tuttavia può essere opportuno ridefinrire ``ledActivate`` in modo da rendere visibile 
sullo standard output lo stato del Led . 

Una implementazione più user-friendly potrebbe 
introdurre una GUI che cambia di colore e/o dimensione a seconda che il Led sia acceso o spento.

.. code:: java

  public class LedMock extends LedModel implements ILed{
    @Override
    protected void ledActivate(boolean val) {	 showState(); }

    protected void showState(){ System.out.println("LedMock state=" + getState() ); }
  }


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il LedConcrete
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Il componente che realizza la gestione di un Led concreto, conesso a un RaspberryPi si può avvalere
del software reso disponibile dal committente:

.. code:: java

  public class LedConcrete extends LedModel implements ILed{
  private Runtime rt  = Runtime.getRuntime();    
    @Override
    protected void ledActivate(boolean val) {
      try {
        if( val ) rt.exec( "sudo bash led25GpioTurnOn.sh" );
        else rt.exec( "sudo bash led25GpioTurnOff.sh" );
      } catch (IOException e) { ... }
    }
  }


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Testing del dispositivo Led
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Un test automatizzato di tipo unit-testing sul Led può essere espresso usando JUnit come segue:

.. code-block:: java

  public class TestLed {
    @Before
    public void up(){ System.out.println("up");	}
    @After
    public void down(){ System.out.println("down"); }	
    @Test 
    public void testLedMock() {
      RadarSystemConfig.simulation = true; 
      
      ILed led = DeviceFactory.createLed();
      assertTrue( ! led.getState() );
      
      led.turnOn();
      assertTrue(  led.getState() );
      
      led.turnOff();
      assertTrue(  ! led.getState() );		
    }	
  }

Un test sul LedConcrete ha la stessa struttura del test sul LedMock, ma bisogna avere l'avvertenza
di eseguirlo sul RaspberryPi. Eseguendo il test sul PC non vengono segnalati errori (in quanto
il Led 'funziona' da un punto di vista logico) ma compaiono messaggi del tipo:

.. code-block::

  LedConcrete | ERROR Cannot run program "sudo": ...  






%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Sonar 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

Un Sonar è un dispositivo di input che deve fornire dati quando richiesto dalla applicazione.

Il software fornito dal committente per l'uso di un Sonar reale ``HC-SR04`` ci fornisce
un componente attivo, che produce in modo autonomo,
con una certa frequenza, una sequenza di valori interi di distanza sul dispositivo standard di output.

La modellazione di un componente produttore di dati è più complicata di quella di un dispositivo passivo
(come un dispositivo di output) in quanto occorre affrontare un tipico problema produttore-consumatore.
AL momento seguiremo un approccio tipico della programmazione concorrente basato su memoria comune


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe astratta SonarModel
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe astratta relativa al Sonar introduce due metodi :blue:`abstract`,  uno per specificare il modo di inizializzare il sonar 
(metodo ``sonarSetUp``) e uno per specificare il modo di produzione dei dati (metodo ``sonarProduce``).
Inoltre, estesa definisce due metodi ``create`` che costitusicono factory-methods per un sonar Mock e un sonar reale.

      
.. code:: java

  abstract class SonarModel implements ISonar{
  protected  static int curVal = 0;     //valore corrente prodotto dal sonar
  protected boolean stopped = false;    //quando true, il sonar si ferma

    //Factory methods
    public static ISonar create() {
		  if( RadarSystemConfig.simulation )  return createSonarMock(); 
      else  return createSonarConcrete();		
	  }
    public static ISonar createSonarMock() { return new SonarMock(); }
    public static ISonar createSonarConcrete() { return new SonarConcrete(); }


Il Sonar viene modellato come un processo produttore di dati sulla variabile locale ``curVal``
che risulta attivo quando la variabile locale ``stopped`` risulta ``true``. Di qui le seguenti
definizioni:

.. code:: java

    @Override
    public void deactivate() { stopped = true; }
    @Override
	  public boolean isActive() { return ! stopped; }


Il codice realativo alla produzione dei dati viene incapsulato in un metodo abstract ``sonarProduce``
che dovrà essere definito in modo diverso da un SonarMork e un SonarConcrete, così come il
metodo di inizializzazione ``sonarSetUp``:

.. code:: java

    //Abstract methods
    protected abstract void sonarSetUp() ;		 
    protected abstract void sonarProduce() ;


Con queste premesse, il metodo ``activate`` può essere impostato in modo da inizializzare il Sonar
e attivare un Thread interno di produzione di dati:

.. code:: java

    @Override
    public void activate() {
      sonarSetUp();
      stopped = false;
      new Thread() {
        public void run() {
          while( ! stopped  ) { sonarProduce(); }
        }
      }.start();
    }

La parte applicativa che funge da consumatore dei dati prodotti dal Sonar dovrà invocare il metodo
``getVal`` che viene definito in modo da bloccare il chiamante se il Sonar è in 'fase di produzione'
riattivandolo non appena il dato è stato prodotto:  

.. code:: java

    protected boolean produced   = false;   //synch var

    @Override
	  public int getVal() {   //non può essere qualificato synchronized perchè violerebbe l'interfaccia
    	waitForUpdatedVal();
		  return curVal;
    }   
    
    private synchronized void waitForUpdatedVal() {
     	while( ! produced ) wait();
 			produced = false;
    }

    protected synchronized void setVal( ){
    		produced = true;
		    notify();   //riattiva il Thread in attesa su getVal
   }
  }



&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il SonarMock
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Un Mock-sonar che produce valori da ``90`` a ``0`` può quindi ora essere definito come segue.

.. code:: java
  public class SonarMock extends SonarModel implements ISonar{
    @Override
    protected void sonarSetUp(){  curVal = 90;  }
    @Override
    protected void sonarProduce() {
      curVal--;
      if( curVal == 0 ) stopped = true;
      setVal(   );    //produce
      delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
    }
  }  

Si noti che RadarSystemConfig.sonarDelay ...

.. alla produzione di un nuovo valore di distanza deve aggiornare il valore corrente letto (``curVal``)  e riattivare l'eventuale Thread in attesa di esso su ``getVal``.

 


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il SonarConcrete
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Testing del dispositivo Sonar
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il RadarDisplay
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

Per il ``RadarDisplay`` abbiamo già visto che è disponibile un oggetto singleton che fornisce due metodi:

       .. code:: java

        public class radarSupport {
        private static RadarControl rc;
        public static void setUpRadarGui( ){
          rc=...
        }
        public static void update(String d, String dir){
          rc.update( d, dir );
        }
        }   






+++++++++++++++++++++++++++++++++++++++++++++
Il sistema simulato su PC
+++++++++++++++++++++++++++++++++++++++++++++

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Controller
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Testing del sistema simulato 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


+++++++++++++++++++++++++++++++++++++++++++++
Supporti per TCP
+++++++++++++++++++++++++++++++++++++++++++++

Introduciamo classi che permettano di istanziare oggetti di supporto lato client e lato server.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCPClient
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Mediante la classe ``TcpClient``: possiamo istanziare oggetti che stabilisccono una connessione 
su un data coppia ``IP, Port``. L'oggetto ``Interaction2021`` restiruito dal metodo static 
``connect`` potrà essere usato per inviare-ricevere messaggi.

.. code:: Java

  public class TcpClient {
	 public static Interaction2021 connect(String host, int port ) throws Exception {
   ...
   }

Alla semplicità del supporto lato client si contrappone una maggior complessità lato server, in quanto
occorre:

- permettere di stabilire (in generale) connenessioni con più client;
- fare in modo che si stabilisca una diversa connessione con ciascun client;
- fare in modo che i messaggi ricevuti su una specifica connessione siano elaborati da opportuno 
  codice applicativo 

Per raggoungere questi obiettivi, introduciamo un insieme di supporti che permettano al server di
porre in esecuzione codice applicativo  rapprsentato da oggetti costruiti come specializzazioni
di una classe astratta ``ApplMessageHandler``:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
ApplMessageHandler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.. code:: Java

  public abstract class ApplMessageHandler {  
    ...
    public abstract void elaborate( String message ) ;
    public void setConn( Interaction2021 conn) { ... }
  }

La classe astratta  ``ApplMessageHandler``:  definisce il metodo abstract ``elaborate( String message )``
che le classi applicative devono implementare per realizzare la voluta  gestione dei messaggi.

Questa classe riceve per *injection* una connessione di tipo ``Interaction2021`` che il metodo *elaborate* 
può utilizzare per l'invio di messaggi sulla connessione.
Questa connessione sarà fornita ad ``ApplMessageHandler`` dai supporti di più basso livello che ora
introdurremo.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TcpConnection
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
La classe ``TcpConnection`` costituisce una implementazione della interfaccia ``Interaction2021``
e quindi realizza i metodi di supporto per la ricezione e la trasmissione di
messaggi applicativi sulla connessione fornita da una ``Socket``.

.. code:: Java

  public class TcpConnection implements Interaction2021{
    ...
  public TcpConnection( Socket socket  ) throws Exception { ... }
    @Override
	  public void forward(String msg)  throws Exception { ... }
    @Override
	  public String receiveMsg()  { ... }
    @Override
	   public void close() { ... }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TcpMessageHandler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Mediante la classe ``TcpMessageHandler`` possiamo creare un
oggetto (dotato di un Thread interno) che si occupa di ricevere messaggi su una data connessione 
``Interaction2021``, delegandone la gestione a un oggetto dato, di tipo  ``ApplMessageHandler``.

.. code:: Java

  public class TcpApplMessageHandler {
  public TcpApplMessageHandler( ApplMessageHandler handler ) { ... }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCP Server
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Mediante la classe ``TcpServer`` possiamo istanziare oggetti che realizzano un server TCP che
apre una ``ServerSocket`` e gesticse la richiesta di connessione da parte di un client
creando un oggetto di classe ``TcpMessageHandler`` adibito alla ricezione dei messaggi inviati dai client.

.. code:: Java

	public TcpServer( String name, int port, ApplMessageHandler applHandler  ) {
		new Thread() {
			public void run() {
		      try {
			    ServerSocket serversock = new ServerSocket( port );
			    serversock.setSoTimeout( ... );
				while( true ) {
					//Accept a connection				 
			 		Socket sock          = serversock.accept();	
			 		Interaction2021 conn = new TcpConnection(sock);
			 		applHandler.setConn(conn);
			 		//Create a message handler on the connection
			 		new TcpApplMessageHandler( applHandler );			 		
				}//while
			  }catch (Exception e) {	...   }	
			}
		}.start();
	}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Unit testing
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Esempio di uso
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

TODO

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Gli enablers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

un nuovo tipo di oggetto (che denominiamo al momento genericamente :blue:`enabler`) 
capace di ricevere-trasmettere messaggi vie rete e di ricondurre i messaggi ricevuti alla esecuzione di 
metodi di un altro oggetto 'embedded' locale, incapace di interagire via rete.

Ad esempio, con riferimento al ``Led``, il componente di base dovrebbe implementare una interfaccia ome quella che segue:

.. code:: java

  public interface ILed {
    public void turnOn();
    public void turnOff();
    public boolean getState();
  }



L'*enabler* relativo al Led (che denominiamo ``LedServer``) dovrebbe comportarsi come segue:

.. code:: java

  led : ILed 
  while True :
    attendi un messaggio di comando per un Led
    analizza il contenuto del comando ed esegui  
       led.turnOn()  oppure led.turnOff()

.. L'invio e la ricezione di messaggi via rete richiede l'uso di componenti *infrastrutturali* capaci di realizzare  un qualche prototcollo di comunicazione. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCPServer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


Questa connessione è rappresentata nella infrastruttura software che ci aggingiamo a definire da un oggetto di 
classe ``TcpConnection`` che  implementa l'interfaccia  ``Interaction2021`` così definita:

.. code::

  interface Interaction2021  {	 
    public void forward(  String msg ) throws Exception;
    public String receiveMsg(  )  throws Exception;
    public void close( )  throws Exception;
  }

Il metodo di invio è denominato ``forward`` per rendere più evidente il fatto che si tatta di una trasmissione 
di tipo :blue:`fire-and-forget`.

La classe ``TcpConnection`` implementa questa interfaccia  utilizzando la ``java.net.Socket``
specificata nel costruttore, utilizzando opportuni Stream Java (forniti da ``java.io``) costruiti su take socket.
 
Inizialmente il server opera come ricevitore di messaggi e il client come emettitore. Ma su una connessione TCP,
il server può anche dover inviare messaggi ai client, quando  si richiede una interazione di tipo
:blue:`request-response`. In tal caso, il client deve essere anche capace di agire come ricevitore di messaggi.

Per agevolare la costruzione di componenti software capaci di agire sia come come emettitori sia come ricevitori di messaggi 
su una connessione di tipo ``Interaction2021``, introduciamo alcune classi di supporto:

- ``class TcpMessageHandler``:  oggetto dotato di un Thread interno che si occupa di
  ricevere messaggi su una data connessione ``Interaction2021``, delegandone la gestione a un oggetto dato, di tipo 
  ``ApplMessageHandler``.

- ``class ApplMessageHandler``:  classe astratta che definisce il metodo abstract ``elaborate( String message )``
  che opportune classi applicative devono implementare per realizzare la voluta  gestione dei messaggi. 
  Questa classe riceve per *injection* una connessione di tipo ``Interaction2021`` che il metodo *elaborate* può
  utilizzare per l'invio di messaggi


Queste classi servono per poter definire supporti capaci di realizzare un server e un client, delegando la logica
applicativa ad opportuni oggetti definiti dall'application designer. 

- ``class TcpEnabler``: realizza il server che apre una ``ServerSocket`` 
  e crea ad un oggetto di classe ``TcpMessageHandler`` adibito alla ricezione dei messaggi inviati dai client
  sulla  connessione stabilita attraverso la ``ServerSocket``.
  Al momento della creazione, l'application designer specifica nel costruttore l'handler 
  di tipo ``ApplMessageHandler`` per la gestione di messaggi a livello applicativo
  che il server passa a una nuova istanza di ``TcpMessageHandler`` dopo avervi 'iniettato' la connessione.
 



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler per ricezione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Si tratta di definire un server che l'application designer può specializzare 
con riferimento a un preciso protocollo e a un metodo di elaborazione dei messaggi ricevuti.

.. code:: java

  public abstract class EnablerAsServer extends ApplMessageHandler{
    public EnablerAsServer(String name, int port) {
      super(name);
      setProtocolServer( port, this );
    }	
    public abstract void setProtocolServer(int port, ApplMessageHandler handler);    	
    @Override
    public abstract void elaborate(String message);
  }

La classe ``ApplMessageHandler`` è una classe astratta che definisce il metodo  ``abstract void elaborate( String message )``
che opportune classi applicative devono implementare per realizzare la voluta  gestione dei messaggi. 
Questa classe riceve per *injection* una connessione di tipo ``Interaction2021`` che il metodo *elaborate* può
utilizzare per l'invio di messaggi

Un esempio di specializzazione relativo a Led :

.. code:: java

  public class LedServer extends ApplMessageHandler  {
  ILed led = LedAbstract.createLedConcrete();

    public LedServer(  int port  )   {
      super("LedServer");
      setProtocolServer(port,this);	
    }
    
    public void setProtocolServer(int port, ApplMessageHandler enabler) {
      try {
        new TcpServer( name+"Server", port,  this );
      } catch (Exception e) {
        e.printStackTrace();
      } 			
    }
    
    @Override		//from ApplMessageHandler
    public void elaborate(String message) {
      System.out.println(name+" | elaborate:" + message);
      if( message.equals("on")) led.turnOn();
      else if( message.equals("off") ) led.turnOff();
    }
  
  }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler per trasmissione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: java

  public abstract class EnablerAsClient {
  private Interaction2021 conn; 
  protected String name ;	

    public EnablerAsClient( String name, String host, int port ) {
      try {
        this.name = name;
        conn = setProtocolClient(host,  port);
      } catch (Exception e) {
        System.out.println( name+"  |  ERROR " + e.getMessage());		}
    }
    
    protected abstract Interaction2021 setProtocolClient( String host, int port  ) throws Exception;
    
    protected void sendValueOnConnection( String val ) {
      try {
        conn.forward(val);
      } catch (Exception e) {
        System.out.println( name+" |  ERROR " + e.getMessage());
      }
    }
    
    public Interaction2021 getConn() {
      return conn;
    }
  }  

Un 'piano di testing' può spiegare meglio di molte parole il funzionamento della infrastruttura che abbiamo in mente,
astraendo dallo specifico protocollo.


Definiamo dunque in Java due classi:

.. La classe ``TcpEnabler`` abilita alla ricezione di connessioni TCP delegando all'``ApplMessageHandler`` ricevuto nel costruttore
   il compito di gestire i messaggi inviati da una client su quella conessione.

- per il server, la classe  ``TcpEnabler``: apre una ``ServerSocket`` 
  e crea ad un oggetto di classe ``TcpMessageHandler`` adibito alla ricezione dei messaggi inviati dai client
  sulla  connessione stabilita attraverso la ``ServerSocket``.
  Questo handler si occupa di ricevere i messaggi e di invocare il metodo ``void elaborate( String message )``
  di un oggetto di classe ``ApplMessageHandler`` ricevuto al momento della creazione.
  
- per il client, la classe  ``TcpClient``   che stabilisce una connessione su un data coppia ``IP, Port`` e fornisce
  il metodo ``void forward( String msg ) `` per inviare messaggi sulla connessione.
  Un oggetto di questo tipo permette anche la ricezione di messaggi 'di replica' inviati dal server.

 
+++++++++++++++++++++++++++++++++++++++++++++
Il sistema distribuito
+++++++++++++++++++++++++++++++++++++++++++++

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Deployment
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: 

  gradle build jar -x test

Crea il file `build\distributions\it.unibo.enablerCleanArch-1.0.zip` che contiene la directory bin  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Test funzionale
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



-------------------------------------
Un approccio top down
-------------------------------------


Si veda :doc:`ApproccioTopdown`.



  

