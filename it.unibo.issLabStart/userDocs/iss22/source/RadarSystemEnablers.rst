+++++++++++++++++++++++++++++++++++++++++++++
Gli enablers
+++++++++++++++++++++++++++++++++++++++++++++

L'analisi del problema ha posto in evidenza la opportunità/necessità,
di introdurre nel sistema degli :blue:`enabler`, che hanno lo scopo di incapsulare 
:blue:`core-code` all'interno di un component capace di ricevere e trasmettere informazione.

Nell'ambito di un processo di sviulppo bottom-up, in cui abbiamo selezionato il procollo TCP come
tecnologia di riferimento per le comunicazioni, risulta naturale pensare a 
due tipi di enabler: uno per ricevere (diciamo un *server*) e uno per trasmettere (diciamo un *client*).
 
Nel quadro di una architettura port-adapter, ponendo il ``Controller`` su PC, 
questi, senza modificare il codice introdotto in :ref:`Controller<controller>`:

- accederà al Sonar attraverso un adapter-enabler *tipo server* che implementa l'interfaccia ``ISonar``; 
- accederà al Led utilizzando un adapter-enabler *tipo client*  che implementa l'interfaccia ``ILed``  
  
Dualmente, sul Raspberry dovremo porre:

- un enabler *tipo server* per il Led, per ricevere i comandi di accensione/spegnimento;
- un enabler *tipo client* per il Sonar, per inviare dati e per ricevere comandi dal server.

Avendo anche la consapevolezza che questa parte di lavoro potrebbe farci pervenire alla
costruzione di :blue:`supporti riusabili`,
cercheremo di impostare il progetto degli enabler in modo da dipendere 'il meno possibile'
dalla tecnologia di base per la comunicazione (protocollo) tra componenti software
distribuiti.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler astratto per ricezione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Iniziamo con il definire un server astratto che crea il supporto di comunicazione 
relativo al protocollo specificato e demanda la gestione dei messaggi inviati da un client
alle classi specializzate.

.. image:: ./_static/img/Radar/EnablerAsServer.PNG
   :align: center 
   :width: 40%
 
.. code:: java

  public abstract class EnablerAsServer extends ApplMessageHandler{
  protected ApplMessageHandler handler;
    public EnablerAsServer(String name, int port, ProtocolType protocol) {
      super(name);
      setServerSupport( port, this, protocol );
    }	
    protected void setServerSupport( int port, ApplMessageHandler handler, 
          ProtocolType protocol ) throws Exception{
      this.handler = handler;
      if( protocol == ProtocolType.tcp ) {
        TcpServer server = new TcpServer( "ServerTcp", port,  handler );
        server.activate();
      }else if( protocol == ProtocolType.coap ) { ... }
    }	 
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Tipi di protocollo supportati
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La classe ``ProtocolType`` enumera i protocolli utlizzabili dagli enablers.  

.. code:: java

  public enum ProtocolType {  tcp, coap }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler astratto per trasmissione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

All'enabler-ricevitore, affianchiamo suibito un enabler astratto per trasmettere informazione,
che delega a classi specializzate la definizione del metodo ``handleMessagesFromServer`` per
gestire i messaggi ricevuti dal server.

.. code:: java

  public abstract class EnablerAsClient {
  private Interaction2021 conn; 
  protected String name ;	
    public EnablerAsClient( String name,String host,int port ProtocolType protocol) {
      try {
        this.name = name;
        setConnection(host,  port, protocol);
        startHandlerMessagesFromServer(conn);
      } catch (Exception e) {...}
    }

    protected void setConnection(String host,int port,ProtocolType protocol) throws Exception{
      if( protocol == ProtocolType.tcp) {
        conn = TcpClient.connect(host,  port, 10);
      }else if( protocol == ProtocolType.coap ) { ...	}
    }

    protected void startHandlerMessagesFromServer( Interaction2021 conn) {
      new Thread() {
        public void run() {
          try {
            handleMessagesFromServer(conn);
          } catch (Exception e) { ...	}				
          }
      }.start();
    }

    protected abstract void handleMessagesFromServer(Interaction2021 conn) throws Exception;
    
    protected void sendValueOnConnection( String val ) {
      try {
        conn.forward(val);
      } catch (Exception e) {...}
    }  
    public Interaction2021 getConn() { return conn; }
  }  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Gli Enabler per il Sonar
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Abbiamo già anticipato che, nel caso il Controller sia su PC, il Sonar richiede:

- su PC: un adapter-enabler *tipo server* che implementa l'interfaccia ``ISonar`` per ricevere dati;
- su RaspberryPi: un enabler *tipo client* per inviare dati e per ricevere comandi.

Al momento, come supporti di comunicazione useremo quanto sviluppato come :ref:`Supporti TCP<tcpsupport>`.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Adapter-Enabler come server di ricezione per il Sonar 
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

.. image:: ./_static/img/Radar/EnablersAndAdapters.PNG
   :align: center
   :width: 40% 

L'adapter di ricezione *tipo server* per il Sonar specializza EnablerAsServer 
definendo il metodo ``elaborate`` sui messaggi inivati da un client:

.. code:: java

  public class SonarAdapterEnablerAsServer 
                  extends EnablerAsServer implements ISonar{
  private int lastSonarVal = 0;		 
  private boolean stopped  = true;	//mirror value
  private boolean produced = false;

  public SonarAdapterServer( String name, int port, ProtocolType protocol ) {
    super(name, port, protocol);
  }
  @Override  //from ApplMessageHandler
  public void elaborate(String message) {
    lastSonarVal = Integer.parseInt( message );
    valueUpdated( );  //riattiva processi in attesa su getVal
  } 
  protected synchronized void valueUpdated( ){
    produced = true;
    this.notify();
	}

Inoltre l'enabler funge anche come adapter, (re)implementando i metodi di  ``ISonar`` in modo
da interagire con l'enabler-client remoto:


.. code:: java

  @Override
  public void activate() {
    sendCommandToClient("activate");
    stopped = false;
  }
  @Override
  public void deactivate() {
    sendCommandToClient("deactivate");
    stopped = true;
  }
  @Override   
  public int getVal() {  
    sendCommandToClient("getVal");
    waitForUpdatedVal();
    return lastSonarVal;
  }
  private synchronized void waitForUpdatedVal() {
    try {
      while( ! produced ) wait();
      produced = false;
    }catch (InterruptedException e) { ...	}		
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Enabler come client di trasmissione per il Sonar
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

.. code:: java

  public class SonarEnablerAsClient extends EnablerAsClient{
  private ISonar sonar ;
	
    public SonarEnablerAsClient( 
        String name, String host, int port, ProtocolType protocol, ISonar sonar ) {
      super( name,  host,  port, protocol );
      this.sonar = sonar;
    }

    public void handleMessagesFromServer( Interaction2021 conn ) throws Exception {
      while( true ) {
        String cmd = conn.receiveMsg();
        if( cmd.equals("activate")) {
          sonar.activate();
         }else if( cmd.equals("getVal")) {
            String data = ""+sonar.getVal();
            sendValueOnConnection(data);
        }
        else if( cmd.equals("deactivate")) {
          sonar.deactivate();
          break;
        }
      }//while
    }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Gli Enabler per il Led
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Abbiamo già anticipato che, nel caso il Controller sia su PC, il Led richiede:

- su PC: un adapter-enabler *tipo client* che implementa l'interfaccia ``ILed`` per trasmetter comandi;
- su RaspberryPi: un enabler *tipo server* per ricevere comandi.

Al momento, come supporti di comunicazione useremo quanto sviluppato come :ref:`Supporti TCP<tcpsupport>`.


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Adapter-Enabler come client di trasmissione per il Led
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

.. code:: java

  public class LedAdapterEnablerAsClient extends EnablerAsClient implements ILed {
  public LedAdapterEnablerAsClient(String name,String host,int port,ProtocolType protocol){
    super(name,host,port, protocol);
  }
  @Override
  public void turnOn() { 
    try {
      sendValueOnConnection( "on" );
      ledStateMirror = true;
    } catch (Exception e) {...}
  }
  @Override
  public void turnOff() {   
    try {
      sendValueOnConnection( "off" );
      ledStateMirror = false;
    } catch (Exception e) { ... }
  }
  @Override
  public boolean getState() { return ledStateMirror;	}	
  @Override
  protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
    while( true ) {
      String msg = conn.receiveMsg();  //bòlocking
      System.out.println(name+" |  I should be never here .... " + msg   );		
    }
  }
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Enabler di ricezione per il Led 
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

 

.. code:: java

  public class LedEnablerAsServer extends EnablerAsServer  {
  ILed led ;

    public LedServer(  String name,  int port, ProtocolType protocol, ILed led  )   {
      super(name, port, protocol );
      this.led = led;	
    }
 
    @Override		//from ApplMessageHandler
    public void elaborate(String message) {
      if( message.equals("on")) led.turnOn();
      else if( message.equals("off") ) led.turnOff();
    }
  
  }


 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Testing degli enabler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Una TestUnit relativa agli enablers può essere definita in modo da:

- Simulare un Controller su PC che usa
     -  un SonarAdapterEnablerAsServer sulla porta 8013
     - un LedAdapterEnablerAsClient 
- Simulare un RaspberryPi che usa
      - un SonarEnablerAsClient 
      - un LedEnablerAsServer sulla porta 8015


.. code::  java

  

 
 

  

