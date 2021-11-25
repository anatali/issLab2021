.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

.. `` 

======================================
RadarSystem
======================================
Tetendo conto dal nostro motto 
:remark:`Non c’è codice senza progetto. Non c’è progetto senza analisi del problema. Non c’è problema senza requisiti`
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
   :widths: 40,60
   :width: 100%

   * - Il LED può/deve essere connesso allo stesso RaspberryPi del sonar? 
     - Al momento si. In futuro però il LED potrebbe essere connesso a un diverso nodo di elaborazione.
   * - Il committente fornisce qualche libreria per la costruzione del display ?
     - Si, viene reso disponibile il supporto  ``radarPojo.jar`` scritto in JAVA che fornisce un oggetto
       di classe ``radarSupport`` capace di creare una GUI in 'stile radar' e di visualizzare dati su di essa:

       .. code::

         public class radarSupport {
         private  static RadarControl radarControl;
           public static void setUpRadarGui( ) {
             radarControl = new RadarControl( null ); }
 	        public static void update(String d,String theta){
		       radarControl.update( d, theta );
	        }
         }    

       Il supporto è realizzato dal progetto *it.unibo.java.radar*.
   * - Il valore ``DLIMIT`` deve essere cablato nel sistema o è bene sia 
       definibile in modo configurabile dall'utente finale?
     - L'utente finale deve essere in grado di specificare in un 'file di configurazione' il valore di questa distanza.
 
Dai requisiti possiamo asserire che:

- si tratta di realizzare il software per un :blue:`sistema distribuito` costituito da due nodi di elaborazione:
  un RaspbeddyPi e un PC convenzionale;
- i due nodi di elaborazione devono potersi :blue:`scambiare informazione via rete`, usando supporti WIFI;
- i due nodi di elaborazione devono essere 'programmati' usando :blue:`tecnologie software diverse`.

In sintesi:

:remark:`Si tratta di realizzare un sistema software distribuito ed eterogeno`

+++++++++++++++++++++++++++++++++++++
Piano di testing (funzionale)
+++++++++++++++++++++++++++++++++++++  

.. Requisito :blue:`ledAlarm`:

Un test funzionale consiste nel porre un ostacolo davanti al Sonar
prima a una distanza ``D > DLIMIT`` e poi a una distanza ``D < DLIMIT`` e osservare il valore
visualizzato sulla GUI.

Tuttavia questo modo di procedere non è automatizzabile, in quanto richiede 
la presenza di un operatore umano. Nel seguito cercheremo di organizzare le cose in modo
da permettere :blue:`Test automatizzati`.

--------------------------------------
Analisi del problema
--------------------------------------

Per analizzare le problematiche implicite nei requisiti, possiamo seguire due diversi approcci:

- approccio :blue:`bottom-up`: partiamo da quello che abbiamo a disposizione e 'assembliamo le parti dispoibili'
  in modo da costruire un sistema che soddisfa i requisiti funzionali;
- approccio :blue:`top-down`: partiamo analizzando le proprietà che il sistema deve 'logicamente' avere per soddisfare i  
  requisiti funzionali senza legarci a priori ad alcun specifico componente e/o tecmologia.

E' molto probabile che la maggior marte delle persone sia propensa a seguire (almeno inizialmente) un
approccio bottom-up, essendo l'approccio top-down meno riconducibile a enti che sia possibile usare 
concretamente come punto di partenza per 'sintetizzare una soluzione'. 

Osserviamo però che :blue:`compito della analisi` non è quello di trovare una soluzione, ma quello di porre in luce 
le problematiche poste dai requisiti (il :red:`cosa` si deve fare) e capire con quali risorse 
(tempo, persone, denaro, etc. )  queste problematiche debbano/possano essere affrontate e risolte.
Sarà compito deo progettisti quello di trovare il modo (il :red:`come`) pervenore ad una soliuzione 'ottimale'
date le risorse a disposizione.

++++++++++++++++++++++++++++++++++++++
Un approccio bottom-up
++++++++++++++++++++++++++++++++++++++

Il sistema pone le seguenti :blue:`problematiche`:

.. list-table::
   :widths: 30,70
   :width: 100%

   * - Gestione del sensore ``HC-SR04``.
     - A questo fine la software house dispone già di codice riutilizzabile, ad esempio 
       ``SonarAlone.c`` (progetto *it.unibo.rasp2021*)
   * - Gestione del display  .
     - A questo fine è disponibile il POJO realizzato da  ``radarPojo.jar`` 
   * - Gestione del LED.
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

Focalizzando l'attenzione sulla interazione *sonar-radar* possiamo rappresentare la situazione come segue:

.. list-table::
   :widths: 30,70
   :width: 100%

   *  - Comunicazione diretta:
      -   .. image:: ./_static/img/Radar/srrIntegrate1.png
            :width: 100%
   *  - Comunicazione mediata:
      -   .. image:: ./_static/img/Radar/srrIntegrate2.png
            :width: 100%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quale 'collante'?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il meditore potrebbe anche fungere da componente capace di realizzare la logica applicativa. 
Ma è giusto/opportuno procedere i questo modo?

Seguendo un punto di vista logico e il principio :red:`xxx` possiamo sostenre, come analisti del problema,
l'opportunità di introdurre un componente (``Controller``), diverso dai dispositivi, che abbia la
:blue:`responabilità di realizzare la logica applicativa`.

Ma ecco sorgere un'altra problematica:

.. list-table::
   :widths: 40,60
   :width: 100%
 
   * - Distribuzione.
     - Il ``Controller`` deve ricevere in ingresso i dati del sensore ``HC-SR04``, elaborarli e  
       inviare comendi al LED e dati alla RADAR-GUI.
       
       Il ``Controller`` puo risiedere su RaspberryPi, sul PC o su un terzo nodo. 
       
       Un colloquio con il committente esclude (per motivi di costo) la possibilità di introdurre un terzo
       nodo di elaborazione. 

Dunque si tratta di analizzare se sia meglio allocare il ``Controller`` sul RaspberryPi o sul PC.

.. list-table::
   :widths: 40,60
   :width: 100%

   * - ``Controller`` sul RaspberryPi.
     - Si avrebbe una maggior reattività nella accensione del Led in caso di allarme. Inoltre ...
       
   * - ``Controller`` sul PC.
     - Si avrebbe più facilità nel modificare la logica applicativa,
       lasciando al Raspberry solo la responsabilità di gestire dispositivi. Inoltre ...
       

++++++++++++++++++++++++++++++++++++++
Un approccio top-down
++++++++++++++++++++++++++++++++++++++

Nell'impostare l'analisi del problema posto dai requisiti, partiamo ora considerando il sistema nel suo
complesso e non dai singoli dispositivi (di input/output).

Questo 'ribaltamento' di impostazione ci induce a focalizzare l'attenzione su tre dimensioni fondamentali:

- la :blue:`struttura` del sistema, cioè di quali parti è composto;
- la :blue:`connessione/interazione` tra le parti del sistema in modo da formare un 'tutto' con precise proprietà
  non (completamente) riducibili a quelle delle singole parti;
- il :blue:`comportamento` (autonomo o indotto) di ogni singola parte in modo che siano assicurate le interazioni
  volute.

Un modo per considerare in modo unitario queste tre dimensioni è quello di impostare l':blue:`architettura`
del sistema, cerando di dare risposta a un insieme di domande fondamentali:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quali componenti?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Quali componenti fanno sicuramente parte del sistema, considerando i requisiti? 

.. list-table::
   :width: 100%

   * - Il sistema deve possedere parti software capaci di gestire il :blue:`Sonar`, il :blue:`RadarDisplay` e il :blue:`Led`.
       Questi componenti rappresentano dispositivi di input/ouput ovvero sensori ed attuatori. 
       Ma un dispostivo di I/O non dovrebbe mai includere codice relativo alla logica applicativa.
       
       Dunque la nostra analisi ci induce a introdurre un altro componente, che denominiamo  :blue:`Controller`, 
       con l'idea i dispositivi di I/O possano  essere riusati, senza varuazioni, per fomare molti sistemi diversi 
       modificando in modo opportuno solo il ``Controller``.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Oggetti o enti attivi?
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Considerando (il software relativo a) ciascun componente, questo può/deve essere visto come un :blue:`oggetto` 
che definisce operazioni attivabili con chiamate di procedura o come un 
:blue:`ente attivo` capace di comportamento autonomo?

.. list-table::
   :width: 100%

   * - Analizzando il software disponibile, possiamo dire che:
     
       -  il ``Sonar`` è un ente attivo che scrive dati su un dispositivo standard di output
       -  il ``Led`` è un oggetto  che implementa l'interfaccia
          
          .. code::  

             interface ILed {
                  void turnOn()
                  void turnOff()
                  boolean isOn()
             }
       -  il ``radarSupport`` è un oggetto singleton che può essere usato invocando il metodo ``update``
 
Se anche il ``RadarDisplay`` fosse sul RaspberryPi, il ``Controller`` potrebbe essere definito come segue:

.. code::

  while True :
    d = Sonar.getVal()
    radarSupport.update( d,90 )       
    if( d <  DLIMIT )  then Led.turnOn() else Led.TurnOff()

Da un punto di vista logico, il ``Controller`` è un ente attivo 
che può operare sul PC o sul RaspberryPi (un terzo nodo è escluso).

- Nel caso il ``Controller`` operi sul PC, lo schema precedente non va più bene, 
  perchè il ``Controller`` deve poter interagire via rete con il ``Sonar``e con il ``Led``.
  Inoltre, il ``Sonar``e il ``Led`` devono essere :blue:`embedded` in qualche altro componente
  capace di ricevere/trasmettere messaggi.

- Nel caso il ``Controller`` operi sul RaspberryPi, lo schema precedente non va più bene, 
  perchè il ``Controller``  deve poter interagire via rete con il ``RadarDisplay``. 
  In questo caso il  ``RadarDisplay`` si presenta logicamente come un ente attivo capace di ricevere/trasmetter messaggi 
  utilizzando poi ``radarSupport`` per visualizzare l'informazione ricevuta dal ``Controller``.
  


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quali interazioni?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Come punto saliente della analisi condotta fino a questo punto possiamo affermare che:

:remark:`Il problema ci induce a parlare di interazioni basate su messaggi.`

.. list-table::
   :width: 100%

   * - Di fronte alla necessità di progettare e realizzare *sistemi software distribuiti*, 
       la programmazione ad oggetti comincia a mostrare i suoi limiti 
       e si richiede un :blue:`ampliamento dello spazio concettuale di riferimento`.

       A questo riguardo, può essere opportuno affrontare il passaggio :blue:`dagli oggetti agli attori` come
       passaggio preliminare per il passaggio *da sistemi concentrati a sistemi distribuiti*. 

       Affronteremo più avanti questo passaggio, dopo avere cercato di realizzare il sistema impostando
       ancora un sistema ad oggetti che utilizzano opportuni protocolli di comunicazione.



&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Sincrone o asincrone?
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Dirette o mediate?
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quali comportamenti?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il comportamento di ciascun componente ha ora l'obiettivo principale di :blue:`realizzare le interazioni` che
permettono alle 'parti'  di agire in modo da formare un 'tutto' (il sistema) capace di soddifare i requisiti
funzionali attraverso opportune elaborazioni delle informazioni ricevute e tramesse tra i componenti stessi.

Il ``Controller`` potrebbe essere ora definito come segue:

.. code::

  while True :
    invia al Sonar la richiesta di un valore d 
    invia d al RadarDisplay in modo che lo visualizzi
    if( d <  DLIMIT ) invia al Led un comando di accensione 
    else invia al Led un comando di spegnimento

Il comportamento dei disposivi è una conseguenza logica di questo.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Message-driven o state-based?
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


++++++++++++++++++++++++++++++++++++++
Quale architettura?
++++++++++++++++++++++++++++++++++++++

--------------------------------------
Progettazione
--------------------------------------

L'analisi ha evidenziato che, volendo riusare i componenti software resi disponibile dal commitente,
e necessario dotare uno o più di essi della capacità di inviare e ricevere messaggi via rete.

Questa necessità segnala un :blue:`gap`  tra il livello tecnologico di partenza e le necessità del problema.
Iniziamo dunque il nostro progetto cercando di colmare questo gap con la introduzione di un nuovo componente riusabile.

Anche in questo caso possiamo seguire un approccio bottom-up oppure un approccio top-down.

+++++++++++++++++++++++++++++++++++++++
Approccio bottom-up
+++++++++++++++++++++++++++++++++++++++

Partiamo selezionando un protocollo di comunicazione (ad esempio TCP) e rendiamo i componenti del sistema
capaci di trasmettere-ricevere messaggi con questo protocollo, che assume il ruolo di 'collante' tra le parti.

A tal fine possaimo impostare un nuovo tipo di oggetto (che denominiamo al momento genericamente :blue:`enabler`) 
capace di ricevere-trasmettere messaggi vie rete e di ricondurre i messaggi ricevuti alla esecuzione di 
metodi di un altro oggetto 'embedded' locale, costituito dal componente iniziale incapace di interagire via rete.

Ad esempio, con riferimento al ``Led``, l'*enabler* (che denominiamo ``LedServer``) dovrebbe comportarsi come segue:

.. code::

  public interface ILed {
    public void turnOn();
    public void turnOff();
    public boolean getState();
  }

  led : ILed 
  while True :
    attendi un messaggio di comando
    analizza il contenuto del comando ed esegui  
       led.turnOn()  oppure led.turnOff()

L'invio e la ricezione di messaggi via rete richiede l'uso di componenti *infrastrutturali* capaci di realizzare 
un qualche prototcollo di comunicazione. Le scelte possibili sono oggi numerose:

- TCP
- UDP 
- HTTP
- CoaP 
- MQTT

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler per ricezione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Si tratta di definire un server che l'application designer può specializzare 
con riferimento a un preciso protocollo e a un metodo di elaborazione dei messaggi ricevuti.

.. code::

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

.. code::

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

.. code::

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




+++++++++++++++++++++++++++++++++++++++
Approccio top-down
+++++++++++++++++++++++++++++++++++++++
Partiamo dalla architettura logica definita dall'analisi del problema e 

 


 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCPEnabler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Per interagire via TCP con un componente software abbiamo bisogno di un client e di un server.

Il server opera su un nodo con indirizzo IP noto (diciamo ``IPS``) , apre una ``ServerSocket`` su una  porta 
(diciamo ``P``) ed attende messaggi  di connessione su ``P``.

Il client deve dapprima aprire una ``Socket`` sulla coppia ``IPS,P`` e poi inviare o ricevere messaggi su tale socket.
Si stabilisce così una *connessione punto-a-punto bidirezionale* tra il nodo del client e quello del server.

Questa connessione è rapprentata nella infrastruttura software che ci aggingiamo a definire da un oggetto di 
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
 
- ``class TcpClient``: realizza un client che stabilisce una connessione su un data coppia ``IP, Port`` e fornisce
  il metodo ``void forward( String msg ) `` per inviare messaggi sulla connessione.
  Un oggetto di questo tipo permette anche la ricezione di messaggi 'di replica' inviati dal server.


  
  




  

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

 

  

--------------------------------------
Deployment
--------------------------------------

.. code::

  gradle build jar -x test

Crea il file `build\distributions\it.unibo.enablerCleanArch-1.0.zip` che contiene la directory bin  