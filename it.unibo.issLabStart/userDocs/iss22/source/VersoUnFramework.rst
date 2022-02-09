.. role:: red 
.. role:: blue 
.. role:: remark
  
.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/

==================================================
Verso un framework per la interazione distribuita
==================================================

I primi SPRINT dello sviluppo hanno seguito un processo bottom-up, che ha fatto riferimento
a TCP come protocollo per le comunicazioni di rete.

Abbiamo anche costruito un  :ref:`prototipo<primoPrototipo>` di una versione distribuita del sistema, 
la cui architettura è schematizzata nella figura che segue:

.. image:: ./_static/img/Radar/sysDistr1.PNG
   :align: center 
   :width: 70%

Con maggior dettaglio, questa architettura si basa sugli elementi costitutivi di figura:

.. image:: ./_static/img/Architectures/framework0.PNG
   :align: center  
   :width: 70%


- Un oggetto (POJO) di interfaccia ``Ixxx`` che definisce il comportamento di un dispositivo reale o simulato.   
- Un oggetto di interfaccia :ref:`IApplIntepreter<IApplIntepreterEsteso>` che trasforma messaggi (di comando e richieste
  di informazione)   in chiamate a metodi di ``Ixxx``.
- Un oggetto di interfaccia :ref:`IApplMsgHandler<IApplMsgHandlerEsteso>` che definisce il codice di gestione
  dei messaggi di livello applicativo indirizzati a un particolare dispositivo.
- Un oggetto di tipo :ref:`ContextMsgHandler<ContextMsgHandler>` che realizza un gestore dei sistema dei messaggi 
  che ne attua il reindirizzamento (dispatching) agli opportuni handler applicativi.
- Un (unico) :ref:`TcpContextServer<TcpContextServer>` attivato su un nodo di elaborazione ``A`` (ad esempio un Raspberry) che 
  permette a componenti :ref:`proxy<ProxyAsClient>` allocati su nodi esterni  (ad esempio un PC)
  di interagire con i dispositivi allocati su ``A``.

La domanda che ci poniamo ora è se questa organizzazione possa essere riusata nel caso in cui si voglia sostituire
al protocolllo TCP un altro protocollo, tra quelli indicati in:ref:`ProtocolType`.

---------------------------------------
Il caso UDP
---------------------------------------

La possibilità di sostituire TCP con UDP è  resa possibile dalla libreria  ``unibonoawtsupports.jar`` sviluppata
in anni passati. Il compito non si è rivelato troppo difficle, visto la relativa vicinanza concettuale tra i due 
protocolli.

Più arduo sembra invece il caso di un protocollo di tipo publish-subscribe come MQTT o di un protocollo come CoAP
che cambia l'impostazione logica in modo simile ad HTTP-REST, che mira a modellizzare
tutte le *interazioni client/server* come uno :blue:`scambio di rappresentazioni di risorse`.

---------------------------------
Il caso HTTP
---------------------------------

Affronteremo l'uso di questo protocollo più avanti, in relazione alla costruzione di un componente  Web GUI.

.. code:: Java

  HttpURLConnection con =
  IssHttpSupport

---------------------------------
I ContextServer
---------------------------------
Come primo passo per la definizione di un nostro framework, introduciamo un contratto per 
il concetto di ContextServer che imponga metodi per attivare/disattivare il server e per
aggiungere/rimuovere compoenti di tipo :ref:`IApplMsgHandler`:


++++++++++++++++++++++++++++++++++++++++++++++
IContext
++++++++++++++++++++++++++++++++++++++++++++++

.. code:: java

  public interface IContext {
    public void addComponent( String name, IApplMsgHandler h);
    public void removeComponent( String name );
    public void activate();
    public void deactivate();
  }

Questo contratto è già rispettato da :ref:`TcpContextServer`, così che possiamo estendere la sua definizione come segue: 

  public class TcpContextServer extends TcpServer :blue:`implements IContext`

Oltre il ContextServer per TCP, dovremo introdurre anche ContextServer per MQTT (si veda :ref:`MqttContextServer`) 
e per CoAP (si veda :ref:`CoapContextServer`).

.. Individuare i punti in cui occorre tenere conto dello specifico protocollo per definire i parametri delle *operazioni astratte*

Al solito, è opportuno definire  una Factory per la creazione del ContextServer appropriato, in funzione del protocolllo:


---------------------------------------
Context2021
---------------------------------------

.. code:: java

  public class Context2021 {

    public static IContext create(String id, String entry ) {
    IContext ctx = null;
    ProtocolType protocol = RadarSystemConfig.protcolType;
      switch( protocol ) {
      case tcp : {
        ctx=new TcpContextServer(id, entry);
        ctx.activate();
        break;
      }
      case mqtt : {
        ctx= new MqttContextServer( id, entry);
        ctx.activate();
        break;
      }
      case coap : {
        ctx = new CoapContextServer( );
        ctx.activate();
        break;
      }
      default:
        break;
      }
      return ctx;
    }//create  

 


I parametri ``id`` ed ``entry`` da specificare nel costruttore sono:

===========================   ===========================    =========================== 
        Server                            id                        entry
---------------------------   ---------------------------    ---------------------------
:ref:`TcpContextServer`               nome host                 port
:ref:`MqttContextServer`              id del client              nome topic     
:ref:`CoapContextServer`                    -                      -
===========================   ===========================    ===========================   


Il :ref:`CoapContextServer` non ha bisogno di parametri in quanto basta conoscere l'indirizzo el broker,
definito nel parametro di configurazione:

.. code::

    RadarSystemConfig.mqttBrokerAddr = "tcp://broker.hivemq.com"


Come passo successivo, creiamo la possibilità di definire proxy diversi per i diversi protocolli

--------------------------------------------------------
Estensione della classe :ref:`ProxyAsClient`
--------------------------------------------------------

.. code:: java

  public class ProxyAsClient {
   ....
  protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
    witch( protocol ) {
    case tcp : {
      int port = Integer.parseInt(entry);
      conn = TcpClientSupport.connect(host,  port, 10); //10 = num of attempts
      break;
    }
    case coap : {
      conn = new CoapSupport("CoapSupport_"+name, host,  entry);  //entry is uri path
      break;
    }
    case mqtt : {
      conn = MqttSupport.getSupport();					
      break;
    }	
    default :{
      ColorsOut.outerr(name + " | Protocol unknown");
    }
  }


---------------------------------------
I nuovi ContextServer
---------------------------------------


+++++++++++++++++++++++++++++++++++++++
MqttContextServer
+++++++++++++++++++++++++++++++++++++++

 

+++++++++++++++++++++++++++++++++++++++
CoapContextServer
+++++++++++++++++++++++++++++++++++++++



CoAP mira a modellizzare
tutte le interazioni client/server come uno scambio di rappresentazioni di risorse. L'obiettivo
è quello di realizzare una infrastruttura di gestione delle risorse remote tramite alcune semplici
funzioni di accesso e interazione come quelle di HTTP: PUT, POST, GET, DELETE.

La libreria ``org.eclipse.californium`` offre ``CoapServer`` che viene decorato da ``CoapApplServer``.

- ``CoapApplServer`` extends CoapServer implements :ref:`IContext`
- class ``CoapSupport`` implements :ref:`Interaction2021`
- abstract class ``ApplResourceCoap`` extends CoapResource implements :ref:`IApplMsgHandler`
- 

La classe ``CoapResource`` viene decorata da ``ApplResourceCoap`` per implementare ``IApplMsgHandler``.
In questo modo una specializzazione come ``LedResourceCoap`` può operare come componente da aggiungere 
al sistema tramite ``CoapApplServer`` che la ``Context2021.create()`` riduce a ``CoapServer`` in cui 
sono registrate le risorse.




