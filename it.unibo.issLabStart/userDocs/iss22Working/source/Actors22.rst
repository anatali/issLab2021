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

La conseguenza più importante  sarà la possibilità di impostare il processo 
di produzione del software in modo :ref:`topDown<Abstraction GAP e topDown>`, ponendo in primo 
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

    @Context22(name = "pcCtx",   host = "localhost",    port = "8080")
    @Context22(name = "raspCtx", host = "192.168.1.12", port = "8082")
    @Actor22(name = "a1",  contextName = "pcCtx", implement=A1Actor22OnPc.class)
    @Actor22(name = "a2",  contextName = "raspCtx" )

    public class MainAnnotationDemo22Pc {
    ...
    }

Questo programma dichiara il sistema composto da due attori: 

- l'attore  ``a1``, che opera nel contesto di nome ``pcCtx`` locale al PC in quanto specifica che il suo host è  
  :blue:`localhost`.  Ne viene quindi fornita anche la classe che lo implementa
- l'attore  ``a2``, che opera nel contesto di nome ``raspCtx`` con **host diverso da  localhost**.
  L'attore viene dunque visto (in questa prospettiva del sistema) come  **remoto**  e se ne specifica dunque la classe 
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
- l'attore di nome  ``a2`` su ``raspCtx`` viene visto come locale al RaspberryPi e se ne fornisce dunque 
  la classe di implementazione.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Configurazione del sistema  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Le annotazioni  sono gestite da :ref:`Qak22Context`. Il programma di ciascun nodo avrà unaa stessa, semplice
fase di configurazione; ad esemppio:

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





Poichè un contesto:

    - conosce tutti gli altri contesti del sistema e la dislocazione di ogni attore nei diversi contesti
    - conosce la dislocazione di ogni attore nei diversi contesti

La annotazione precedente viene elaborata (da :ref:`Qak22Context` col metodo ``setContexts22``) tenendo traccia
di tutti i contesti dichiarati

.. list-table:: 
  :widths: 100
  :width: 100%


  * - :remark:`un attore nasce, vive e muore in un contesto`
  * - 
     ``@Context22(name=<STRING>, host=<STRING>, port=<STRING>)``

     Esempio:

      ``@Context22(name = "demoCtx", host = "localhost", port = "8080")``
  
  * - :remark:`un attore è un ente attivo, dotato di un nome (univoco)`
  * - 
     ``@Actor22(name=<STRING>,  contextName=<STRING>, implement=<CLASS>)``

     ``@Actor22(name = "a1",  contextName = "demoCtx")``

     Esempio:

      ``@Context22(name = "demoCtx", host = "localhost",    port = "8080")``
  
  
  * - :remark:`un attore è un ente attivo, dotato di un nome (univoco)`
  * - 
     ``@Actor22(name=<STRING>,  contextName=<STRING>, implement=<CLASS>)``

     ``@Actor22(name = "a1",  contextName = "demoCtx")``

     Esempio:

      ``@Context22(name = "demoCtx", host = "localhost",    port = "8080")``

 
