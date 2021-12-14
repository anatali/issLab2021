.. _tcpsupport:

+++++++++++++++++++++++++++++++++++++++++++++
Supporti per TCP
+++++++++++++++++++++++++++++++++++++++++++++

Introduciamo classi di supporto per TCP lato client e lato server.

.. _tcpsupportClient:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCPClient
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Mediante la classe ``TcpClient`` possiamo istanziare oggetti che stabilisccono una connessione 
su un data coppia ``IP, Port``. Il metodo  static ``connect`` restiruisce un oggetto 
che implementa l'interfaccia  :ref:`Interaction2021<conn2021>`  
e che potrà essere usato per inviare-ricevere messaggi.

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
          System.out.println("Attempt to connect:" + host + " port=" + port);
          Thread.sleep(500);
        }
      }//for
      throw new Exception("Unable to connect to host:" + host);
    }
  }

Si noti che il client fa un certo numero di tentativi prima di segnalare la impossibilità di connessione.

.. _tcpsupportServer:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TCP Server
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Alla semplicità del supporto lato client si contrappone una maggior complessità lato server, in quanto
occorre:

- permettere di stabilire (in generale) connenessioni con più client;
- fare in modo che si stabilisca una diversa connessione con ciascun client;
- fare in modo che i messaggi ricevuti su una specifica connessione siano elaborati da opportuno 
  codice applicativo.

Per raggiungere questi obiettivi, introduciamo un insieme di supporti che permettano al server di
porre in esecuzione codice applicativo  rappresentato da oggetti costruiti come specializzazioni
di una classe astratta ``ApplMessageHandler``:

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
ApplMessageHandler
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

.. _msgh: 

.. code:: Java

  public abstract class ApplMessageHandler {  
  protected Interaction2021 conn;		//Injected by setConn
  protected String name;
    public ApplMessageHandler( String name ) { this.name = name; }
    ...
    public abstract void elaborate( String message ) ;
    
    public void setConn( Interaction2021 conn) { ... }
    public Interaction2021 getConn(  ) {  return conn;  }
  }

La classe astratta  ``ApplMessageHandler``  definisce il metodo abstract ``elaborate( String message )``
che le classi applicative devono implementare per realizzare la voluta  gestione dei messaggi.

Questa classe può ricevere per *injection* (metodo ``setConn``) una connessione 
di tipo :ref:`Interaction2021<conn2021>` che il metodo *elaborate* 
può utilizzare per l'invio di messaggi (di risposta) sulla connessione.

Questa connessione sarà fornita ad ``ApplMessageHandler`` dai supporti di più basso livello che ora
introdurremo.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
TcpConnection
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe ``TcpConnection`` costituisce una implementazione della interfaccia 
:ref:`Interaction2021<conn2021>`
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

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
TcpMessageHandler
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Mediante la classe ``TcpMessageHandler`` possiamo creare un
oggetto (dotato di un Thread interno) che si occupa di ricevere messaggi su una data connessione 
:ref:`Interaction2021<conn2021>`, delegandone la gestione a un oggetto dato, 
di tipo  :ref:`ApplMessageHandler<msgh>`.

.. _tcpmsgh: 

.. code:: Java

  public class TcpApplMessageHandler extends Thread{
  public TcpApplMessageHandler( ApplMessageHandler handler ) { 
    @Override
    public void run() {
      Interaction2021 conn = handler.getConn() ;
      ...
      //Attendi messaggio su conn
      String msg = conn.receiveMsg();
      ...
      handler.elaborate( msg );
    }
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il TCPserver come oggetto attivo
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
 
Mediante la classe ``TcpServer`` possiamo istanziare oggetti che realizzano un server TCP che
apre una ``ServerSocket`` e gestisce la richiesta di connessione da parte dei clienti.

Il ``TcpServer`` viene definito come un Thread che riceve un :ref:`ApplMessageHandler<msgh>` come oggetto di 
'callback' che contiene la logica di gestione dei messaggi applicativi ricevuti dai client che si connetteranno.
Il server defisce anche metodi per essere attivato e deattivato:.

.. code:: Java

  public class TcpServer  extends Thread{
  private boolean stopped = true;
  private ApplMessageHandler applHandler;
  private int port;
  private ServerSocket serversock;

  public TcpServer(String name, int port, ApplMessageHandler applHandler) {
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

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe `Colors`
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe :blue:`Colors` è una utility per scrivere su standard ouput messaggi colorati. 
Il metodo ``Colors.outerr`` visualizza un messaggio in colore rosso, 
mentre ``Colors.out`` lo fa con il colore blu o con un colore specificato come parametro.

Per ottenere messaggi colorati in Eclipse, occorre installare il plugin  *ANSI-Escape in Console*.

  
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il funzionamento del server
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il metodo che definisce il funzionamento del server è il metodo ``run``
che attende una richiesta di connessione e quando questa arriva creae un oggetto (attivo)
di classe :ref:`TcpMessageHandler<tcpmsgh>` adibito alla ricezione dei messaggi inviati dai client
con l':ref:`ApplMessageHandler<msgh>` ricevuto al momento della costruzione del server.

.. code:: Java

  @Override
  public void run() {
  try {
    while( ! stopped ) {
      //Accept a connection				 
      Colors.out(getName() + " | waits on server port=" + port);	 
      Socket sock  = serversock.accept();	
      Interaction2021 conn = new TcpConnection(sock);
      applHandler.setConn(conn);
      //Create a message handler on the connection
      new TcpApplMessageHandler( applHandler );			 		
    }//while
  }catch (Exception e) {...}

L'architettura del sistema in seguito a due chiamate da parte di due client diversi, può essere 
rappresentata come nella figura che segue:

.. image:: ./_static/img/Architectures/ServerAndConnections.png 
   :align: center
   :width: 80%
 
Notiamo che vi può essere concorrenza nell'uso di oggetti condivisi. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Una TestUnit
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Una TestUnit può essere utile sia come esempio d'uso dei suppporti, sia per chiarire le
interazioni client-server.

Per impostare la TestUnit, seguiamo le seguente user-story:

.. epigraph:: 

  :blue:`User-story TCP`: come TCP-client mi aspetto di poter inviare una richiesta di connessione al TCP-server
  e di usare la connessione per inviare un messaggio e per ricevere una risposta.
  Mi aspetto anche che altri TCP-client possano agire allo stesso modo senza che le
  loro informazioni interferiscano con le mie.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Metodi before/after
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

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

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
L'handler dei messaggi applicativi ``NaiveHandler``
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe ``NaiveHandler`` definisce l'handler che useremo nel test per elaborare i messaggi inivati dai clienti. 
Il metodo di elaborazione si avvale della connessione ereditata da ':ref:`ApplMessageHandler<msgh>`
per inviare al cliente una risposta che contiene anche il messaggio ricevuto.

.. code:: Java

  class NaiveHandler extends ApplMessageHandler {
  private static int count = 1;
  static NaiveHandler create() {
    return new NaiveHandler( "nh"+count++);
  }
  private NaiveHandler(String name) {
    super(name);
  }
  public void elaborate( String message ) {
    try {
      conn.forward("answerTo_"+message);
    } catch (Exception e) {...}
  }
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Un semplice client per i test
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Un semplice client di testing viene definito in modo che (metodo ``doWork``) il client :

#. si connette al server
#. invia un messaggio
#. attende la risposta del server
#. controlla che la risposta sia quella attesa 

.. code:: Java

  class ClientForTest{
  public static boolean withserver=true; //per test di client senza server
    public void doWork(String name, int ntimes) {
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

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Test per l'interazione senza server
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Il test controlla che un client esegue un certo numero di tenativi ogni volta
che tenta di connettersi a un server:

.. code:: Java

  @Test 
  public void testClientNoServer() {
		ClientForTest.withserver = false; //per non fare faillire il test
    new ClientForTest().doWork("clientNoServer",3 );
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Test per l'interazione client-server
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Un test che riguarda il funzionamento atteso in una interazione tra un singolo client e il server
può essere così definito:

.. code:: Java

  @Test 
  public void testSingleClient() {
    server.activate();
    new ClientForTest().doWork("client1");
  }
	
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Test con molti clienti
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

.. code:: Java

  @Test 
  public void testManyClients() {
    server.activate();
    System.out.println("testManyClients");
    new ClientForTest().doWork("client1");
    new ClientForTest().doWork("client2");
    new ClientForTest().doWork("client3");
  }	


.. L'errore da indagare:
.. .. code:: Java
.. oneClientServer | ERROR: Socket operation on nonsocket: configureBlocking
 
+++++++++++++++++++++++++++++++++++++++++++++
Supporti per altri protocolli
+++++++++++++++++++++++++++++++++++++++++++++

Udp, Bluetooth  ``unibonoawtsupports.jar``
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
La libreria ``unibonoawtsupports.jar``
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

  

