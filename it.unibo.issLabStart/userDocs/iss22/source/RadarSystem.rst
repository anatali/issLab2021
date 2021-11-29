affrontiama.. contents:: Overview
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
   :widths: 40,60
   :width: 100%

   *  - Comunicazione diretta
        
        Le nuovolette in figura rappresentano gli strati di software che dovrebbero permettere ai dati generati dal Sonar 
        di raggiungere il ``RadarDisplay``.

      -   .. image:: ./_static/img/Radar/srrIntegrate1.png
            :width: 100%
   *  - Comunicazione mediata

        Il meditore potrebbe anche fungere da componente capace di realizzare la logica applicativa. 
        Ma è giusto/opportuno procedere i questo modo?

      -   .. image:: ./_static/img/Radar/srrIntegrate2.png
            :width: 100%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quale 'collante'?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Seguendo il principio che la responsabilità di realizzare gli use-cases applicativi non deve essere attribuita
al software di gestione dei disposivi di I/O, la nostra analisi ci induce a sostenere
l'opportunità di introdurre un nuovo componente, che possiamo denominare ``Controller``), che abbia la
:blue:`responabilità di realizzare la logica applicativa`.

Ma ecco sorgere un'altra problematica:

.. list-table::
   :widths: 40,60
   :width: 100%
 
   * - Distribuzione.
     - Il ``Controller`` deve ricevere in ingresso i dati del sensore ``HC-SR04``, elaborarli e  
       inviare comendi al Led e dati al  ``RadarDisplay``.
       
      Il ``Controller`` puo risiedere su RaspberryPi, sul PC o su un terzo nodo. 
      Tuttavia, un colloquio con il committente ha escluso (per motivi di costo) la possibilità di introdurre un altro
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
       

Queste considerazioni ci inducono a riflettere sul tipo di architettura che scaturisce 
da queste prime analisi del problema.

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

Le nostre conoscenze ci inducono a dire che nel sistema dovremo avere componenti capaci
di operare come un client-TCP e componenti capacai di operare come un server-TCP.



Il server opera su un nodo con indirizzo IP noto (diciamo ``IPS``) , apre una ``ServerSocket`` su una  porta 
(diciamo ``P``) ed attende messaggi  di connessione su ``P``.

Il client deve dapprima aprire una ``Socket`` sulla coppia ``IPS,P`` e poi inviare o ricevere messaggi su tale socket.
Si stabilisce così una *connessione punto-a-punto bidirezionale* tra il nodo del client e quello del server.
 
Inizialmente il server opera come ricevitore di messaggi e il client come emettitore. Ma su una connessione TCP,
il server può anche dover inviare messaggi ai client, quando  si richiede una interazione di tipo
:blue:`request-response`. In tal caso, il client deve essere anche capace di agire come ricevitore di messaggi.





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

.. code:: java

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
un qualche prototcollo di comunicazione. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCPServer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Per interagire via TCP con un componente software abbiamo bisogno di un client e di un server.

Il server opera su un nodo con indirizzo IP noto (diciamo ``IPS``) , apre una ``ServerSocket`` su una  porta 
(diciamo ``P``) ed attende messaggi  di connessione su ``P``.

Il client deve dapprima aprire una ``Socket`` sulla coppia ``IPS,P`` e poi inviare o ricevere messaggi su tale socket.
Si stabilisce così una *connessione punto-a-punto bidirezionale* tra il nodo del client e quello del server.

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



  

