.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

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
Actor22 annotated
-----------------------------------------

In questa parte introdurremo alcuni miglioramenti alla implementazione Actor22 degli attori con lo 
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
introducendo :ref:`Modelli` esguibili del sistema da sviluppare, coorredati da opportuni
:ref:`piani di testing<Passi operativi 'a regime'>`, da cui i porgettisti potranno
partire per le evoluzioni incrementali che, con diversi :ref:`SPRINT<SCRUM>`, 
porteranno alla versione finale del sistema. 

 
