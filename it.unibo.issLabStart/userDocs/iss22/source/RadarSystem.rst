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
al software di gestione dei disposivi di I/O, la nostra analisi ci induce a sostenere
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
  di questo tipo di protocollo è sono ``MQTT`` che viene supportato da broker come ``Mosquitto e RabbitMQ``. 




Al momento abbiamo conoscenze che ci permettono di utilizzare protocolli come TCP/UDP e HTTP
e siamo forse meno esperti nell'uso di supporti per la comunicazione mediata tramite broker.

Seguiamo dunque l'idea delle **comunicazioni dirette** facendo riferimento al protocollo TCP
(più affidabile di UDP e base di HTTP) e passiamo a una fase di progettazione che
renda i componenti del sistema capaci di trasmettere-ricevere messaggi con il protocollo
TCP, che assume quindi il ruolo di 'collante' tra le parti.

+++++++++++++++++++++++++++++++++++++
Analisi delle interazioni con TCP
+++++++++++++++++++++++++++++++++++++
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

Coma analisti, osserviamo anche dire che un *gap* relativo alle comunicazioni di rete si presenta in modo sistematico
in tutti i sistemi distribuiti. Sarebbe dunque opportuno cercare di colmare questo *gap* in modo non episodico,
introducendo :blue:`componenti riusabili` che possano 'sopravvivere' alla applicazione che stiamo costruendo
per poter essere impiegati in futuro in altre applicazioni distribuite.

Astraendo dallo specifico protocollo, osserviamo che tutti i protocolli punto-a-punto come TCP, UDP, Bluetoooth, etc.
sono in grado di stabilire una :blue:`connessione` stabile sulla quale inviare e ricevere messaggi.

Questo concetto può essere realizzato da un oggetto che rende disponibile opprtuni metodi, come quelli definiti
nella seguente interfaccia:

.. code::

  interface Interaction2021  {	 
    public void forward(  String msg ) throws Exception;
    public String receiveMsg(  )  throws Exception;
    public void close( )  throws Exception;
  }

Il metodo di trasmissione è denominato ``forward`` per rendere più evidente il fatto che pensiamo ad un modo di operare 
:blue:`fire-and-forget`. La stringa restituita dal metodo ``receiveMsg`` può rappresentare una risposta a un messaggio
inviato in precedenza con ``forward``.

L'informazione scambiata è rappresenta da una ``String`` che è un tipo di dato presente in tutti
i linguaggi di programmazione.
Non viene introdotto un tipo (non-primitivo) diverso (ad esempio ``Message``) perchè non si vuole staibilire 
il vincolo che gli end-points della connessione siano componenti codificati nello medesimo linguaggio di programmazione

Ovviamente la definizione di questa interfaccia potrà essere estesa e modificata in futuro, ad esempio nella fase di
progettazione, ma rappresenta una forte indicazione dell'analista di pensare alla costruzione di componenti
software che possano ridurre il costo delle applicazioni future.

--------------------------------------
Progettazione
--------------------------------------



A tal fine possiamo impostare un nuovo tipo di oggetto (che denominiamo al momento genericamente :blue:`enabler`) 
capace di ricevere-trasmettere messaggi vie rete e di ricondurre i messaggi ricevuti alla esecuzione di 
metodi di un altro oggetto 'embedded' locale, costituito dal componente iniziale incapace di interagire via rete.

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
    attendi un messaggio di comando per il Led
    analizza il contenuto del comando ed esegui  
       led.turnOn()  oppure led.turnOff()

.. L'invio e la ricezione di messaggi via rete richiede l'uso di componenti *infrastrutturali* capaci di realizzare  un qualche prototcollo di comunicazione. 

++++++++++++++++++++++++++++++
TCPServer
++++++++++++++++++++++++++++++


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
TCP Client
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
- ``class TcpClient``: realizza un client che stabilisce una connessione su un data coppia ``IP, Port`` e fornisce
  il metodo ``void forward( String msg ) `` per inviare messaggi sulla connessione.
  Un oggetto di questo tipo permette anche la ricezione di messaggi 'di replica' inviati dal server.



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

 

  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Deployment
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: 

  gradle build jar -x test

Crea il file `build\distributions\it.unibo.enablerCleanArch-1.0.zip` che contiene la directory bin  



+++++++++++++++++++++++++++++++++++++++
Approccio top down
+++++++++++++++++++++++++++++++++++++++ 


Si veda :doc:`ApproccioTopdown`.



  

