+++++++++++++++++++++++++++++++++++++++++++++
Gli enablers
+++++++++++++++++++++++++++++++++++++++++++++

L'analisi del problema ha posto in evidenza la opportunità/necessità, 
di introdurre nel sistema degli :blue:`enabler`, che hanno lo scopo di incapsulare 
:blue:`core-code` all'interno di un component capace di ricevere e trasmettere informazione.

Nell'ambito di un processo di sviulppo bottom-up, in cui abbiamo selezionato il procollo TCP come
tecnologia di riferimento per le comunicazioni, risulta naturale pensare a 
un enabler *tipo-server* capace di ricevere richieste di connessione da client remoti (normalmente
dei proxy).

.. due tipi di enabler: uno per ricevere (diciamo un enabler *tipo-server*) e uno per trasmettere (diciamo un enabler *tipo-client*).
 
Come suggerito nell'analisi, ponendo il ``Controller`` su PC, 
potremo procedere senza modificare il codice introdotto in :ref:`Controller<controller>`
impostando una architettura come quella rappresentata in figura:

.. image:: ./_static/img/Radar/ArchLogicaOOPEnablersBetter.PNG 
   :align: center
   :width: 50%

Ricordando la proposta delle architetture port-adapter_,  decidiamo, come progettisti,
di impostare lo sviluppo del software del sistema con riferimento ad una architettura a livelli
rappresentata come segue:


.. image:: ./_static/img/Architectures/cleanArchCone.jpg 
   :align: center
   :width: 50%

 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler tipo-server
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Iniziamo con il definire un server astratto che crea il supporto di comunicazione 
relativo al protocollo specificato e demanda la gestione dei messaggi  in input
alle classi specializzate.


.. image:: ./_static/img/Radar/EnablerAsServer.PNG
   :align: center 
   :width: 60%
 
.. code:: java

  public class EnablerAsServer{
    protected ProtocolType protocol;
  protected TcpServer serverTcp;
    public EnablerAsServer(String name, int port, 
                       ProtocolType protocol, IApplMsgHandler handler ) {
      super(name);
      try {
        this.protocol = protocol;
        if( protocol != null ) setServerSupport( port, protocol, handler );
      }catch (Exception e) { ... }
    }	
    protected void setServerSupport( 
                    int port, ProtocolType protocol,IApplMsgHandler handler ) throws Exception{
      if( protocol == ProtocolType.tcp ) {
        serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  handler );        
      }else if( protocol == ProtocolType.udp ) { ... 
      }else if( protocol == ProtocolType.coap ) { //DO nothing: we use a CoapServer 
      }
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

Notiamo che nel caso ``protocol==null``, non viene creato alcun supporto.
Questo caso sarà applicato più avanti: si veda  :doc:`ContextServer`.


 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Tipi di protocollo supportati
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La classe ``ProtocolType`` enumera i protocolli utlizzabili dagli enablers.  

.. code:: java

  public enum ProtocolType {  tcp, udp, coap }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Enabler per trasmissione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

All'enabler-ricevitore, affianchiamo suibito un enabler  per trasmettere informazione,

.. che delega a classi specializzate la definizione del metodo ``handleMessagesFromServer`` per gestire i messaggi ricevuti dal server.

.. code:: java

  public abstract class EnablerAsClient {
  private Interaction2021 conn; 
  protected String name ;	
  protected CoapSupport coapSupport;
    public EnablerAsClient( 
          String name, String host, int port, ProtocolType protocol ) {
      try {
        this.name = name;
        this.protocol = protocol;        
        setConnection(host,  port, protocol);
      } catch (Exception e) {...}
    }

    protected void setConnection(
          String host,int port,ProtocolType protocol) throws Exception{
      if( protocol == ProtocolType.tcp) {
        conn = TcpClient.connect(host,  port, 10);
      }else if( protocol == ProtocolType.coap ) {
        coapSupport = new CoapSupport(host, name );	
      }
    }
     
    protected void sendCommandOnConnection( String cmd ) {
      try {
        if( protocol == ProtocolType.tcp) {
        conn.forward(cmd);
      }else if( protocol == ProtocolType.coap) {
        coapSupport.updateResource(cmd);
      }
      } catch (Exception e) {...}
    }  

    public String sendRequestOnConnection( String request )  {
    	try {
        if( protocol == ProtocolType.tcp) {
        conn.forward(request);
        String answer = conn.receiveMsg();
        return answer;
      }else if( protocol == ProtocolType.coap) {
        String answer = coapSupport.readResource(request);
        return answer;
      }else return null;
      }catch (Exception e) { ... }
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
        String name,String host,int port,ProtocolType protocol,ISonar sonar ){
      super( name,  host,  port, protocol );
      this.sonar = sonar;
    }

    public void handleMessagesFromServer(
            Interaction2021 conn) throws Exception{
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

  public class LedAdapterEnablerAsClient 
            extends EnablerAsClient implements ILed{
  public LedAdapterEnablerAsClient(
        String name,String host,int port,ProtocolType protocol){
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
  protected void handleMessagesFromServer(
          Interaction2021 conn) throws Exception {
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

    public LedServer(String name,int port,ProtocolType protocol,ILed led){
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

  

 
 

  

