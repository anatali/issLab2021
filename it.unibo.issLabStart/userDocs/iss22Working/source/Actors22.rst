.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo


.. _visione olistica: https://it.wikipedia.org/wiki/Olismo

======================================
Actors22
======================================

Il lavoro svolto fino ad ora ha coperto buona parte di quanto prospettato nella :ref:`FASE1` e nella
:ref:`FASE2`.

----------------------------------------
Uno sguardo alla Fase1
----------------------------------------

La :ref:`FASE1` ci ha progressivamente portato a costruire supporti per comunicazioni 
via rete basati su diversi protocolli: dapprima TCP, UDP e successivamente anche HTTP e WS.

Per agevolare lo sviluppo di applicazioni abbiamo 'nascosto' i dettagli tecnologici di ciascun protocollo,
introducendo classi che implementano l'astrazione :blue:`connessione`, 
espressa dall'interfaccia :ref:`Interaction2021<Interaction2021>`.

La implementazione del concetto di connessione per le WebSocket (:ref:`WsConnection`) ha introdotto l'idea 
di **connessione osservabile**, per la gestione dei :ref:`messaggi di stato`.

Abbiamo rimandato alcune voci:

- Refactoring del sistema a fronte dell’uso di altri protocolli: MQTT e CoAP.
- Come dotare l’applicazione di una WebGui usando SpringBoot.

Le riprenderemo dopo avere completato il lavoro della :ref:`FASE2`.

----------------------------------------
Uno sguardo alla Fase2
----------------------------------------

La :ref:`FASE2` ci ha condotto a sviluppare supporti quali i
:ref:`Contesti-contenitori` e l':ref:`EnablerContext` che si avvale di un gestore di sistema di messaggi
:ref:`ContextMsgHandler<Il gestore di sistema dei messaggi>` per reindirizzare un messaggio a un 
handler applicativo (un POJO) di tipo :ref:`IApplMsgHandler<IApplMsgHandler>`.

Abbiamo poi pensato di sostituire i POJO :ref:`IApplMsgHandler<IApplMsgHandler>` con 
enti attivi, capaci di gestire in modo 'nativo' (FIFO) messaggi inseriti nella coda di ingresso loro associata.
Abbiamo denotato questi nuovi componenti con il nome di :ref:`Attori`  e ne abbiamo fornito una prima implementazione
:ref:`QakActor22<ActorQak e QakActor22>` che ha creato una corrispondeza tra 
il ‘macro-mondo’ rappresentato dai servizi distribuiti sulla rete 
e il ‘micro-mondo’ rappresentato dai componenti interni a questi servizi. 

Il problema della possibile prolificazione di Thread dovuta al concetto di Attore è stato superato
sfruttando una implementazione in Kotlin (libreria ``it.unibo.qakactor-2.7.jar``) 
realizzata in anni passati.
Abbiamo cioè costruito ed iniziato ad usare:

- una infrastruttura per attori message-driven come supporto alla costruzione di software distribuiti ed eterogeni

Al momento abbiamo lassciato in sospeso due temi: 

- Introduzione al linguaggio Kotlin.
- Dalle coroutine Kotlin agli attori Kotlin.


Li riprederemo dopo avere migliorato la nostra attuale infrastruttura, al fine di anticipare gli aspetti
più importanti che avevamo riportato come :ref:`FASE3`:

- da bottom-up a top-down: il ruolo dei modelli
- uso di modelli eseguibili nelle fasi di analisi dei requisiti e del problema,
  come premessa per l’abbattimento dei costi (e degli imprevisti) di produzione

Il lavoro che ci accingiamo a svolgere comprende anche un altro punto menzionato nella :ref:`FASE2`

- da attori message-driven ad :blue:`attori message-based` che operano come **automi a stati finiti**.

-----------------------------------------
Preludio alla Fase3
-----------------------------------------

In questa parte che precede la :ref:`FASE3` del nostro piano di lavoro,
introdurremo alcuni miglioramenti alla implementazione degli attori con lo 
scopo di agevolare quanto più possibile il lavoro dell'Application designer.

A questo fine, faremo ampio ricorso allo strumento delle :ref:`Annotazioni` che 
permettono  di dare semantica aggiuntiva a classi e metodi Java attraverso frasi 'dichiarative' che 
aiutano a meglio comprenderne il codice e a colmare in modo automatico 
l':ref:`abstraction gap<Abstraction GAP e topDown>` tra la nuova semantica e il livello tecnologico
sottostante.

La conseguenza più importante  sarà la possibilità di agevolare processi 
di produzione  :ref:`topDown<Abstraction GAP e topDown>` del software, ponendo in primo 
piano i requisiti e il problema, in modo da introdurre le tecnologie come risposta ad esigenze
esplicitamente espresse e motivate.

Faremmo anche passi sostanziali nel concretizzare il lavoro delle fasi di analisi (dei requisiti e del problema)
introducendo :ref:`Modelli` **eseguibili** del sistema da sviluppare, coorredati da opportuni
:ref:`piani di testing<Passi operativi 'a regime'>`, da cui i porgettisti potranno
partire per le evoluzioni incrementali che, con diversi :ref:`SPRINT<SCRUM>`, 
porteranno alla versione finale del sistema. 


-----------------------------------------
Actor22 annotated
-----------------------------------------

In una `visione olistica`_ di un sistema software, cercheremo di superare la visione 'tecnicistica' introdotta in
:ref:`Configurare con Annotation`, cercando di creare una corrispondenza sistematica tra
i concetti-base del nostro :ref:`Modello ad Attori<Il paradigma ad Attori>` e le nostre nuove frasi dichiarative
in forma di :ref:`Annotazioni` Java.

++++++++++++++++++++++++++++++++++++++++
Un esempio di sistema a due nodi
++++++++++++++++++++++++++++++++++++++++

Riportiamo subito un esempio di come si presentereranno le dichiarazioni per un sistema distribuito formato da due nodi:

- un PC, su cui attiviamo il programma ``MainAnnotationDemo22Pc``
- un RaspberryPi, su su cui attiviamo il programma ``MainAnnotationDemo22Rasp``

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Parte del sistema su PC
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

    @Context22(name="pcCtx",host="localhost",port="8080")
    @Context22(name="raspCtx",host ="192.168.1.12",port="8082")
    @Actor22(name="a1",contextName="pcCtx",implement=A1Actor22OnPc.class)
    @Actor22(name="a2",contextName="raspCtx" )

    public class MainAnnotationDemo22Pc {
    ...
    }

Questo programma dichiara il sistema composto da due attori: 

- l'attore  ``a1``, che opera nel contesto di nome ``pcCtx`` **locale** al PC in quanto specifica che il suo host è  
  :blue:`localhost`.  Ne viene quindi fornita anche la classe che lo implementa
- l'attore  ``a2``, che opera nel contesto di nome ``raspCtx`` con **host diverso da  localhost**.
  L'attore viene dunque visto (in questa prospettiva del sistema) come  **remoto**  e NON se ne specifica la classe 
  di implementazione.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Parte del sistema su RaspberryPi
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

    @Context22(name = "pcCtx",  host = "192.168.1.12", port = "8080")
    @Context22(name = "raspCtx",host = "localhost",    port = "8082")
    @Actor22(name = "a1",  contextName = "pcCtx" )
    @Actor22(name = "a2",  contextName = "raspCtx", implement=A2Actor22OnRasp.class )

    public class MainAnnotationDemo22Rasp {
    ...
    }

Questo programma dichiara il sistema nello stesso modo, ma con una **prospettiva diversa**: 

- l'attore di nome ``a1`` su ``pcCtx`` viene visto come **remoto**
- l'attore di nome  ``a2`` su ``raspCtx`` viene visto come **locale** al RaspberryPi e se ne fornisce dunque 
  la classe di implementazione.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Configurazione del sistema  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Le annotazioni  sono gestite da :ref:`Qak22Context`. Il programma di ciascun nodo avrà unaa stessa, semplice
fase di configurazione; ad esempio:

.. code::

    public class MainAnnotationDemo22Pc {
        Qak22Context.configureTheSystem(this);
    }

Vediamo dunque come si è pervenuti a questo modo di specifica, dando anche qualche dettaglio su come opera 
il metodo  ``Qak22Context.configureTheSystem``.

++++++++++++++++++++++++++++++++++++++++
Annotazioni per dichiarare Contesti
++++++++++++++++++++++++++++++++++++++++

Nella sezione  :ref:`Dal locale al distribuito` abbiamo detto che:

:remark:`Un sistema distribuito è di norma formato da due o più contesti` 
      
Inoltre, un contesto:

        - opera su un nodo di elaborazione associato a un indirizzo IP
        - utilizza almeno un protocollo di comunicazione (tra cui sempre TCP) per ricevere messaggi 
          su una data porta di ingresso (che potrebbe assumere la forma di un URI

Ne consegue una annotazione dichiarativa della forma:

    ``@Context22(name=<STRING>, host=<STRING>, port=<STRING>)``


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Qak22Context.setContexts22
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La annotazione precedente viene elaborata (da :ref:`Qak22Context` col metodo ``setContexts22``) che tiene traccia
di tutti i contesti dichiarati.

Nel caso di contesto con **host="localhost"**, si crea un oggetto che implementa l'interfaccia :ref:`IContext`
come istanza della classe ``EnablerContextForActors`` definita nel 
:ref:`Package unibo.actor22Comm` che utilizza il :ref:`ContextMsgHandler per attori`.


++++++++++++++++++++++++++++++++++++++++
Annotazioni per dichiarare Attori
++++++++++++++++++++++++++++++++++++++++

:remark:`Un attore nasce, vive e muore in un contesto`

- Nel caso di attore **locale**, ne consegue una annotazione dichiarativa della forma:

    ``@Actor22(name=<STRING>,  contextName=<STRING>, implement=<CLASS>)``
  
  La annotazione precedente viene elaborata (da :ref:`Qak22Context` col metodo ``setActorAsLocal``) che:

  - crea una istanza dell'attore come implementazione della classe specificata

.. - invia all'attore un messaggio di attivazione


- Nel caso di attore **remoto**, ne consegue una annotazione dichiarativa della forma:

    ``@Actor22(name=<STRING>,  contextName=<STRING> )``

  La annotazione precedente viene elaborata (da :ref:`Qak22Context` col metodo ``setActorAsRemote``) che: 

  - crea un proxy (singleton) per il contesto in cui risiede l'attore
  - memorizza il proxy in una mappa utilizzata dalla operazione :ref:`sendMsgToRemoteActor` invocata da 
    :ref:`sendMsg<Invio di messaggi da attore>`)


----------------------------------------
Actor22 esempi con WEnv
----------------------------------------

- TODO ActorUsingWEnvBetter 
- BoundaryWalkerActor  come FSM 'a mano' : 
  it.unibo.virtualrobotclient/app/src/main/java/it/unibo/robotWithActorJava/BoundaryWalkerActor.java
- ActorRobotCleaner usa fsm fatto a mano

Problematiche:

- defnire una strategia di copertura: goingDown e goingUp 
- definizione di uno robot move-step 'sincrono' che può avere successo o fallire. Problema del recupero
  dell'esito della operazione dalla gestione di SystemData.wsEventId emmessa da WsConnection
- fsm come soluzione e come segnale per pasare da message-driven a Actor22Fsm
- ridefinizione dell'attore con Actor22Fsm
- il problema di capire quando il lavoro è terminato
- il problema della verifica della copertura 
- la memorizzazione del lavoro svolto (del percorso effettuato)


----------------------------------------
RobotCleaner
----------------------------------------

++++++++++++++++++++++++++++
RobotCleaner: requisiti
++++++++++++++++++++++++++++
Muovere il VirtualRobot in modo da coprire tutta la superficie di una stanza vuota.


+++++++++++++++++++++++++++++++++++++++++++
RobotCleaner: analisi dei requisiti
+++++++++++++++++++++++++++++++++++++++++++ 

- Il VirtualRobot (detto brevemente robot) è quello introdotto in :ref:`VirtualRobot`.
- La stanza ha pavimento piano, forma rettangolare ed è delimitata da muri.
- Il robot parte dalla zona detta HOME 

+++++++++++++++++++++++++++++++++++++++++++
RobotCleaner: analisi del problema
+++++++++++++++++++++++++++++++++++++++++++

Come analisti, poniamo in evidenza le seguenti problematiche:

#. *Proattività*: il robot deve muoversi in modo autonomo fino a compimento del lavoro.
#. *Copertura*: il robot deve seguire una strategia di movimento che garantisca di 
   esplorare la superficie in modo completo.
#. *Verifica*: occorre un criterio per stabilire che la copertura sia stata realizzata.

Si possono pensare diverse possibili strategie di movimento sistematico che permettono la verifica.
Ad esempio:



.. list-table:: 
  :widths: 50,50
  :width: 100%

  * - .. image::  ./_static/img/VirtualRobot/columnMove.PNG
         :align: center 
         :width: 80%

    - .. image::  ./_static/img/VirtualRobot/spiralmove0.PNG
         :align: center 
         :width: 80%
     

Per semplificare suppoaniamo che il robot possa essere inscritto in un cerchio minimo di diamtero R.

La lunghezza dei lati della stanza può quindi essere misurata in multipli di R.

La stanza stessa può essere pensata come suddivisa in celle quadrate di lato R.

  

.. image::  ./_static/img/VirtualRobot/plant0.PNG
    :align: center 
    :width: 30% 


Una possibile strategia di movimento semplice che permette la verifica è la seguente:

- con