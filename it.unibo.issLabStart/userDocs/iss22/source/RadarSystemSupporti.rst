.. role:: red 
.. role:: blue 
.. role:: remark

.. _tcpsupport:

===============================================
Supporti per TCP (SPRINT1)
===============================================
  
Il secondo punto del piano di lavoro (si veda :ref:`PianoLavoro`) prevede:

#. definizione di alcuni supporti TCP per componenti lato client e lato server, con l'obiettivo di
   formare un insieme riusabile anche in applicazioni future; 

Abbiamo detto che la creazione di questi supporti non è indispensabile, ma può costituire un 
elemento strategico a livello aziendale, per evitare di rifare ogni volta il codice
che permette di scambiare informazioni via rete.

Inizieremo focalizzando l'attenzione sul protocollo TCP, per verificare poi, al termine
del lavoro, la possibilità di estendere anche ad altri protocolli i supporti creati.

:remark:`il software relativo ai supporti sarà scritto in un package dedicato xxx.supports`
 

.. _tcpsupportClient:

-------------------------------------
TCPClient
-------------------------------------
Introduciamo la classe ``TcpClient`` con cui istanziare oggetti che stabilisccono una connessione 
su un data coppia ``IP,Port``. Il metodo  static ``connect`` restiruisce un oggetto 
che implementa l'interfaccia  :ref:`Interaction2021<conn2021>`  
e che potrà essere usato per inviare-ricevere messaggi sulla connessione.

.. code:: Java

  public class TcpClient {

    public static Interaction2021 connect(
              String host,int port,int nattempts) throws Exception{
      for( int i=1; i<=nattempts; i++ ) {
        try {
          Socket socket        =  new Socket( host, port );
          Interaction2021 conn =  new TcpConnection( socket );
          return conn;
        }catch(Exception e) {
          Colors.out("Attempt to connect:" + host + " port=" + port);
          Thread.sleep(500);
        }
      }//for
      throw new Exception("Unable to connect to host:" + host);
    }
  }

Si noti che il client fa un certo numero di tentativi prima di segnalare la impossibilità di connessione.

.. _tcpsupportServer:

-------------------------------------
TCP Server
-------------------------------------

Alla semplicità del supporto lato client si contrappone una maggior complessità lato server, in quanto
occorre:

- permettere di stabilire connenessioni con più client;
- fare in modo che si stabilisca una diversa connessione con ciascun client;
- fare in modo che i messaggi ricevuti su una specifica connessione siano elaborati da opportuno 
  codice applicativo.


.. _IApplMsgHandler:

+++++++++++++++++++++++++++++++++++++++++++
L'interfaccia ``IApplMsgHandler``
+++++++++++++++++++++++++++++++++++++++++++

Nel seguito, incapsuleremo il codice applicativo  entro oggetti che implementano l'interfaccia
``IApplMessageHandler``.

.. code:: Java

  public interface IApplMsgHandler {
    public String getName(); 
    public  void elaborate( String message, Interaction2021 conn ) ;	 
    public void sendMsgToClient( String message, Interaction2021 conn  );
  }

Il costruttore del TCP server avrà quindi la seguente signature:

.. code:: Java

  public TcpServer(String name,int port, IApplMessageHandler userDefHandler) 

cioè riceverà un oggetto di livello applicativo (``userDefHandler``) capace di:

- gestire i messaggi ricevutisulla connessione :ref:`Interaction2021<conn2021>` che il server avrà stabilito con i clienti 
- inviare risposte ai clienti sulla stessa connessione.

----------------------------------------------------------------------
``TcpConnection`` implementa ``Interaction2021``
----------------------------------------------------------------------

La classe ``TcpConnection`` costituisce una implementazione della interfaccia 
:ref:`Interaction2021<conn2021>`
e quindi realizza i metodi di supporto per la ricezione e la trasmissione di
messaggi applicativi sulla connessione fornita da una ``Socket``.

.. code:: Java

  public class TcpConnection implements Interaction2021{
    ...
  public TcpConnection( Socket socket  ) throws Exception { ... }
 
Le implementazione delle operazioni si riduce alla scrittura/lettura di informazione sulla Socket 
e si rimanda quindi direttamente al codice.

.. _ApplMessageHandler:

----------------------------------------------------------------------
``ApplMessageHandler`` implementa ``IApplMsgHandler``
----------------------------------------------------------------------

Per agevolare il lavoro dell'application designer, viene definita una classe astratta che 
implementa la interfaccia :ref:`IApplMsgHandler<IApplMsgHandler>`.
Questa classe realizza l'invio di messaggi ai clienti, ma
delega alle classi specializzate il compito di definire il metodo  ``elaborate`` per la gestione
dei messaggi in ingresso.

.. _msgh: 

.. code:: Java

  public abstract class ApplMessageHandler implements IApplMsgHandler{  
  protected String name;
    public ApplMessageHandler( String name ) { this.name = name; }
    
    public Interaction2021 getName(  ) {  return name;  }
    
    public void sendMsgToClient( String message, Interaction2021 conn  ) {
      try {  conn.forward( message );
      }catch(Exception e){Colors.outerr(name + " | ERROR " + e.getMessage());}
    } 
    
    public abstract void elaborate( String message, Interaction2021 conn ) ;
   }

----------------------------------------------------------------------
Il TCPserver come oggetto attivo
----------------------------------------------------------------------
 
.. Mediante la classe ``TcpServer`` possiamo istanziare oggetti che realizzano un server TCP che apre una ``ServerSocket`` e gestisce la richiesta di connessione da parte dei clienti.

Il ``TcpServer`` viene definito come un Thread che definisce  metodi per essere attivato e disattivato
e il metodo ``run`` che ne specifica il funzionamento.

.. riceve un :ref:`ApplMessageHandler<msgh>` come oggetto di  'callback' che contiene la logica di gestione dei messaggi applicativi ricevuti dai client che si connetteranno.
 

.. server: 
 

.. code:: Java

  public class TcpServer  extends Thread{
  private boolean stopped = true;
  private IApplMsgHandler userDefHandler;
  private int port;
  private ServerSocket serversock;

  public TcpServer(String name,int port,IApplMessageHandler userDefHandler){
    super(name);
    this.port        = port;
    this.applHandler = applHandler;
    try {
      serversock = new ServerSocket( port );
      serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
    }catch (Exception e) { 
      Colors.outerr(getName() + " | ERROR: " + e.getMessage());
    }
  }
  public void activate() {
    if( stopped ) {
      stopped = false;
      this.start();
    }
  }
  public void deactivate() {
    try {
      stopped = true;
      serversock.close();
    }catch (IOException e) {
      Colors.outerr(getName() + " | ERROR: " + e.getMessage());	 
    }
  }

  @Override
  public void run() { ... }
  
++++++++++++++++++++++++++++++++++++++++++++
Il funzionamento del TCPserver
++++++++++++++++++++++++++++++++++++++++++++
Il metodo ``run`` che specifica il funzionamento del server, opera come segue:

#.  attende una richiesta di connessione;  
#.  all'arrivo della richiesta, creae un oggetto (attivo)
    di classe :ref:`TcpMessageHandler<tcpmsgh>` passandondogli l':ref:`ApplMessageHandler<msgh>` 
    ricevuto nel costruttore e la connessione (di tipo ):ref:`Interaction2021<conn2021>`) appena stabilita.
    Questo oggetto attende messaggi sulla nuova connessione 
    e ne delega la gestione all':ref:`ApplMessageHandler<msgh>` ricevuto
#.  torna in fase di attesa di conessione con un altro client.

.. code:: Java

  @Override
  public void run() {
  try {
    while( ! stopped ) {
      //Accept a connection				 
      Socket sock  = serversock.accept();	//1
      Interaction2021 conn = new TcpConnection(sock);
      applHandler.setConn(conn);
      //Create a message handler on the connection
      new TcpApplMessageHandler( userDefHandler, conn ); //2			 		
    }//while
  }catch (Exception e) {...}

La figura che segue mostra l'architettura che si realizza in seguito a chiamate 
da parte di due client diversi

.. image:: ./_static/img/Architectures/ServerAndConnections.png 
    :align: center
    :width: 80%
 
:remark:`Notiamo che vi può essere concorrenza nell'uso di oggetti condivisi.` 

----------------------------------------------------------------------
TcpMessageHandler
----------------------------------------------------------------------
La classe ``TcpMessageHandler`` definisce oggetti (dotati di un Thread interno) che si occupano
di ricevere messaggi su una data connessione 
:ref:`Interaction2021<conn2021>`, delegandone la gestione all':ref:`ApplMessageHandler<msgh>` ricevuto
nel costruttore.

.. _tcpmsgh: 

.. code:: Java

  public class TcpApplMessageHandler extends Thread{
  public TcpApplMessageHandler(IApplMsgHandler handler,Interaction2021 conn){ 
    @Override
    public void run() {
      ...
      while( true ) {
        String msg = conn.receiveMsg();
        if( msg == null ) {
          conn.close();
          break;
        } else{ handler.elaborate( msg, conn ); }
      }
    }
  }

----------------------------------------------------------------------  
Architettura del supporto
----------------------------------------------------------------------

L'architettura del sistema in seguito a due chiamate da parte di due client diversi, può essere 
rappresentata come nella figura che segue:

.. image:: ./_static/img/Architectures/ServerAndConnections.png 
   :align: center
   :width: 80%
 
:remark:`Notiamo che vi può essere concorrenza nell'uso di oggetti condivisi.` 


----------------------------------------------------------------------
Una TestUnit
----------------------------------------------------------------------
Una TestUnit può essere utile sia come esempio d'uso dei suppporti, sia per chiarire le
interazioni client-server.

Per impostare la TestUnit, seguiamo le seguente user-story:

.. epigraph:: 

  :blue:`User-story TCP`: come TCP-client mi aspetto di poter inviare una richiesta di connessione al TCP-server
  e di usare la connessione per inviare un messaggio e per ricevere una risposta.
  Mi aspetto anche che altri TCP-client possano agire allo stesso modo senza che le
  loro informazioni interferiscano con le mie.

++++++++++++++++++++++++++++++++++++++++
Metodi before/after
++++++++++++++++++++++++++++++++++++++++

Il metodo che la JUnit esegue dopo ogni test, disattiva il server (se esiste): 

.. code:: Java

  public class TestTcpSupports {
  private TcpServer server;
  public static final int testPort = 8111; 
 
  @After
  public void down() {
    if( server != null ) server.deactivate();
  }	
  protected void startTheServer(String name) {
    erver = new TcpServer(name,testPort, NaiveHandler.create());
    server.activate();		
  }

Il metodo ``startTheServer`` verrà usato dalle operazioni di test per creare ed attivare il TCPServer.

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
L'handler dei messaggi applicativi ``NaiveHandler``
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

La classe ``NaiveHandler`` definisce l'handler che useremo nel test per elaborare i messaggi inivati dai clienti. 
Il metodo di elaborazione si avvale della connessione ereditata da ':ref:`ApplMessageHandler<msgh>`
per inviare al cliente una risposta che contiene anche il messaggio ricevuto.

.. code:: Java

  class NaiveHandler extends ApplMessageHandler {
    public NaiveHandler(String name) { super(name); }
    @Override
    public void elaborate(String message, Interaction2021 conn) {
      System.out.println(name+" | elaborates: "+message);
      sendMsgToClient("answerTo_"+message, conn);	//send a reply
    }
  }

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Un semplice client per i test
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Un semplice client di testing viene definito in modo che (metodo ``doWork``) il client :

#. si connette al server
#. invia un messaggio
#. attende la risposta del server
#. controlla che la risposta sia quella attesa 

.. code:: Java

  class ClientForTest{
    public void doWork(String name, int ntimes, boolean withserver) {
      try {
        Interaction2021 conn  = 
          TcpClient.connect("localhost",TestTcpSupports.testPort,ntimes);//1
        String request = "hello from" + name;
        conn.forward(request);              //2
        String answer = conn.receiveMsg();  //3
        System.out.println(name + " | receives the answer: " +answer );	
        assertTrue( answer.equals("answerTo_"+ request)); //4
      } catch (Exception e) {
        if( withserver ) fail();
      }
    }
  }

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Test per l'interazione senza server
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Il test controlla che un client esegue un certo numero di tetnativi ogni volta
che tenta di connettersi a un server:

.. code:: Java

  @Test 
  public void testClientNoServer() {
    int numAttempts = 3;
    boolean withserver = false;
    ClientForTest.withserver = false; //per evitare fallimento 
    new ClientForTest().doWork("clientNoServer",numAttempts,withserver);
  }

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Test per l'interazione client-server
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Un test che riguarda il funzionamento atteso in una interazione tra un singolo client e il server
può essere così definito:

.. code:: Java

  @Test 
  public void testSingleClient() {
    startTheServer("oneClientServer");
    boolean withserver = true;
    new ClientForTest().doWork("client1",10,withserver);
  }
	
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Test con molti clienti
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

.. code:: Java

  @Test 
  public void testManyClients() {
    startTheServer("manyClientsServer");
    boolean withserver = true;
    new ClientForTest().doWork("client1",10,withserver);
    new ClientForTest().doWork("client2",1,withserver);
    new ClientForTest().doWork("client3",1,withserver);
  }	


.. L'errore da indagare:
.. .. code:: Java
.. oneClientServer | ERROR: Socket operation on nonsocket: configureBlocking


---------------------------------
Supporti per altri protocolli
---------------------------------

Udp, Bluetooth  ``unibonoawtsupports.jar``
 
+++++++++++++++++++++++++++++++++++++++++++++++
La libreria ``unibonoawtsupports.jar``
+++++++++++++++++++++++++++++++++++++++++++++++

  
---------------------------------
Supporti per HTTP
---------------------------------

.. code:: Java

  HttpURLConnection con =
  IssHttpSupport

