.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

.. `` 

======================================
Approccio top-down
======================================
Partiamo dalla architettura logica definita dall'analisi del problema.

- Il ``Controller`` usa i disposiitivi mediante le loro interfacce (``ISonar``, ``ILed``, ``IRadarGui``) indipendentemente dal fatto
  che essi siano locali o remoti.
- Nel caso il Sonar sia remoto, l'oggetto che implementa ``ISonar`` deve essere 'di tipo server', cioè un oggetto attivo 
  che riceve i dati via rete e li rende disponibili al ``Controller`` con il metodo ``getVal()``.
- Nel caso il Led sia remoto, l'oggetto che implementa ``ILed`` deve essere 'di tipo client', cioè un oggetto   
  che trasmette via rete i comandi (``turnOn``, ``turnOff``) del ``Controller`` .



--------------------------------------
Tipi di protocollo
--------------------------------------

La interazione tra un cient e un server può avvenire utilizzando diversi tipi di protocollo, che possiamo
diviedere in due categorie:

- protocolli punto-a-punto che stabiliscono un *canale bidirezionale* tra il client e il server. Esempi
  di questo tipo di protocolli sono ``UDP, TCP, HTTP e CoAP``.
- protocolli publish-subscribe che si avvalgono di un mediatore (broker) tra client e server. Esempio
  di questo tipo di protocollo è sono ``MQTT`` che viene supportato da broker come ``Mosquitto e RabbitMQ``. 

.. https://www.eclipse.org/community/eclipse_newsletter/2014/february/article2.php

In questa fase ci concentremo su protocolli punto-a-punto, con l'obiettivo di costruire una infrastruttura
sofwtare che permetta di astrarre dai dettagli dei specifici protocolli e
stabilire una connessione client-server di alto livello per l'invio e la ricezione dei messsaggi.

In questo modo:

- la logica applicativa risulterà indipendente dai dettagli del codice necessari per implementare interazioni
  basaate su uno specifico protocollo;
- si facilita la possibilità di modificare il protocollo di interazione lasciando inalterato il codice che
  esprime la logica applicativa;
- si contribuisce alla definizione di un ``modello`` implementabile con linguaggi di programmazione diversi e quindi utilizzabile
  in diversi contesti applicativi.

--------------------------------------
Tipi di messaggio
--------------------------------------

I messaggi scambiati verranno logicamente suddivisi in tre categorie:

- :blue:`dispatch`: un messaggio inviato a un preciso destinatario senza attesa  di una risposta 
  (in modo detto anche :blue:`fire-and-forget`);
- :blue:`invitation`: un messaggio inviato a un preciso destinatario aspettandosi un 'ack' da parte di questi;
- :blue:`request`: un messaggio inviato a un preciso destinatario aspettandosi da parte di questi una 
  :blue:`response/reply` logicamente correlata alla richiesta;
- :blue:`event`: un messaggio inviato a chiunque sia in grado di elaborarlo.

--------------------------------------
Interaction2021
--------------------------------------

Una connessione punto-a-punto sarà rappresentata da un oggetto che implements la seguente interfaccia, che permette di
inviare/ricevere messaggi astraendo dallo specifico protocollo:

.. code:: Java

  interface Interaction2021  {	 
    public void forward(  String msg ) throws Exception;
    public String receiveMsg(  )  throws Exception;
    public void close( )  throws Exception;
  }

Il metodo di invio è denominato ``forward`` per rendere più evidente il fatto che si tatta di una trasmissione 
di tipo :blue:`fire-and-forget`. La stringa restituita dal metodo ``receiveMsg`` può rappresentare un 
*dispatch/invitation/event* oppure un *ack/reply*.

Si noti che l'informazione scambiata è rappresenta da una ``String`` che è un tipo di dato presente in tutti
i linguaggi di programmazione.
Non viene definito un tipo (Java)  ``Message`` perchè si vuole permettere la interazione tra client e server
scritti in linguaggi diversi.

--------------------------------------
Messaggi di livello applicativo
--------------------------------------

Ovviamente componenti scritti in linguaggi diversi potrenno comprendersi solo condividendo il modo di
interpretazione delle stringhe.

Per agevolare la interoperabilità dei componenti si introduce una precisa struttura delle stringhe 
che rappresentano i messaggi di livello applicativo:

.. code::  Java

  msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )

  - MSGID:identificativo del messaggio
  - MSGTYPE: tipo del message (Dispatch, Invitation,Request,Reply,Event)  
  - SENDER: nome del componente che invia il messaggio
  - CONTENT: contenuto applicativo del messaggio (detto anche payload)
  - RECEIVER:  nome del componente chi riceve il messaggio 
  - SEQNUM: numero di sequenza del messaggio

--------------------------------------
Adapter di tipo server  
--------------------------------------
La classe astratta ``EnablerAsServer`` fattorizza le proprietà di tutti gli abilitatori 'di tipo server'. 

.. code:: Java

  public abstract class EnablerAsServer extends ApplMessageHandler{
        public EnablerAsServer(String name, int port) {
          super(name);
          //Invoca il metodo che inizializza il server e il supporto al protocollo da utilizzare
          try {
            setProtocolServer( port  );
          } catch (Exception e) { System.out.println(name+" ERROR " + e.getMessage() ); } 			
        }

        public abstract void setProtocolServer( int port ) throws Exception; 
        @Override //from ApplMessageHandler
        //Questo metodo deve essere definito dall'Application designer per gestire i messaggi ricevuti
        public abstract void elaborate(String message);
  }


La classe ``ApplMessageHandler`` è una  classe astratta che definisce il metodo abstract ``elaborate( String message )``.
Questo metodo dovrà essere definito nelle estensioni ella classe per realizzare la voluta  gestione dei messaggi.

.. code:: Java

  public abstract class ApplMessageHandler implements CoapHandler{  
  protected Interaction2021 conn;		//Injected
  protected String name;
    ... 
    public abstract void elaborate( String message ) ;

    public void setConn( Interaction2021 conn) { this.conn = conn; }
  }

Le istanze di questa classe ricevono per *injection* (col metodo ``setConn``)  
una connessione di tipo ``Interaction2021`` che l'application designer 
potrà utilizzare  nel metodo *elaborate* per l'invio di messaggi *ack/reply*.

--------------------------------------
Adapter  di tipo client
--------------------------------------

La classe astratta ``EnablerAsClient`` fattorizza le proprietà di tutti gli abilitatori 'di tipo client'. 
  
.. code:: Java

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
      
      protected void sendValueOnConnection( String val ) throws Exception{
        conn.forward(val);
      }
      
      public Interaction2021 getConn() {
        return conn;
      }
  }  

--------------------------------------
Il caso del sonar
--------------------------------------

Ad esempio, nel caso del sonar, definiamo un adapter che estende ``EnablerAsServer`` realizzando al contempo
l'interfaccia ``ISonar``.

Il metodo *setProtocolServer* deve attivare un server passandogli :blue:`this` in modo
che il server possa invocare il metodo *elaborate* per ogni dato ricevuto.
L'elaborazione del dato consiste nel renderlo disponibile al ``Controller`` che ha invocato una *getVal* bloccante.

.. code:: java

  public class SonarAdapterServer extends EnablerAsServer implements ISonar{
    public SonarAdapterServer( String name, int port ) { ... }
      @Override	//from EnablerAsServer
      public void setProtocolServer( int port ) throws Exception{
        //Attiva il server sulla port usando un certo protocollo (ad es. TCP)
        //Alla ricezione dei dati del sonar, il server chiama il metodo elaborate
      }	 

      @Override  //from ApplMessageHandler
      public void elaborate(String message) {
        //Elabora il valore corrente del sonar ricevuto dal server
        //rendendolo disponibile a chi invoca il metodo ISonar.getVal 
      }

      //METODI DI ISonar 
      @Override
      public void activate(){ ... }
      public void deactivate(){ ... }
      public int getVal(){ ... }
      public boolean isActive(){ ... }
  }

 

--------------------------------------
Il caso del led
--------------------------------------

Ad esempio, nel caso del Led, definiamo un adapter che estende ``EnablerAsClient`` realizzando al contempo
l'interfaccia ``ILed``.

