+++++++++++++++++++++++++++++++++++++++++++++
Prodotti della analisi
+++++++++++++++++++++++++++++++++++++++++++++

Importanti prodotti, al termine della fase di analisi dei requisiti e del problema sono:

-  la definizione di una :blue:`architettura logica` di riferimento che tiene conto dei vincoli posti 
   dai requisiti e dal problema che ne consegue;
-  la proposta di un :blue:`piano di lavoro` per lo sviluppo del sistema.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Architettura logica come modello di riferimento
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'architettura logica di un sistema costituisce un :blue:`modello del sistema` ispirato dai requisiti funzionali 
e dalle forze in gioco nel dominio applicativo o nella specifica applicazione e mira ad identificare 
i macro-sottosistemi in cui il **problema stesso** suggerisce di articolare il sistema risolvente. 

L'architettura logica è il più possibile **indipendente da ogni ipotesi sull'ambiente di implementazione**.

Un modo per *valutare la qualità* di una architettura logica e la *coerenza con i requisiti* 
è dare risposta a opportune domande, come le seguenti:

- E' possibile addentrarsi nei dettagli dell'architettura procedendo :blue:`incrementalmente` 
  a livelli di astrazione via via descrescenti (con tecniche di raffinamento e :blue:`zooming`) 
  o siamo di fornte a un ammasso non organizzato di parti?
- Le dipendenze tra le parti sono state impostate a livello logico o riflettono (erroneamente) 
  una *visione implementativa*?
- Se nel modello compaiono entità denotate da **termini non definiti** nel glossario costruito 
  dall'analista dei requisiti, quale è la motivazione della loro presenza? 
  Sono elementi realmente necessari o siamo di fronte ad una prematura anticipazione di elementi di progettazione?
- Se nel modello **non compaiono** entità corrispondenti a termini definiti nel glossario, 
  quale è la motivazione della loro mancanza? Siamo di fronte a una dimenticanza 
  o vi sono ragioni reali per non includere questi elementi?

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Architettura logica ad oggetti
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Se astraiamo dalla distribuzione (supponendo ad esempio che tutto il sistema possa
essere supportato sul RaspberryPi), l'architettura logica del sistema risulta
riconducibile a un classico schema :blue:`read-eval-print` in cui:  

.. epigraph:: 

  Il componente ``Controller`` deve leggere dati dal Sonar 
  come dispositivo di input e inviare comandi al Led e al RadarDisplay 
  come dispositvi di output.

Per rendere comprensibile questa architettura anche alla 'macchina' senza entrare in dettagli
implementativi, possiamo introdurre opportuni :blue:`modelli` dei componenti utlizzando qualche linguaggio
di programmazione.

Nel caso di Java, il costrutto interface può essere usato per denotare un componente catturandone
come aspetto essenziale le funzionalità che esso deve offrire e una sorta di :blue:`contratto` 
sull’uso del componente.

Introduciamo dunque i nostri primi modelli di componenti definendo interfacce Java per il *Led,
il Sonar e il RadarDisplay*.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Le interfacce ILed, ISonar e IRadarDisplay
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


.. list-table::
  :widths: 32, 32, 36
  :width: 100%

  * -  Sonar
    -  Led
    -  RadarDisplay
  * -        
      .. code:: java

       interface ISonar {
         void activate();		 
         void deactivate();
         int getVal();	
         boolean isActive();
       }
    -        
      .. code:: java

        interface ILed {
          void turnOn();
          void turnOff();
          boolean getState();
        }
    -        
      .. code:: java     

        interface IRadarDisplay{
          void update(
           String d, String a);
        }  

La :blue:`architettura logica` suggerita dal problema è rappresentabile con la figura che segue:


 
.. image:: ./_static/img/Radar/ArchLogicaOOP.PNG
   :align: center
   :width: 50%

 
:remark:`Non vi sono situazioni di uso concorrente di risorse.`

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
La logica del Controller
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. Poichè l'analisi ha evidenziato l'opportunità di incapsulare la logica applicativa entro un componente
  ad-hoc (il ``Controller``), 

A questo punto possiamo anche esprimere il funzionamento del ``Controller`` come segue:

.. code:: java

  ISonar        sonar;
  ILed          led;
  IRadarDisplay radar;
  ...
  while( sonar.isactive() ){
    int v = sonar.getVal(); //Acquisizione di un dato dal sonar
    if( v < DLIMIT )        //Elaborazione del dato
      Led.turnOn() else Led.turnOff  //Gestione del Led
    radar.update( v, "90")    //Visualizzazione su RadarDisplay
  }

.. Questa impostazione astrae completamente dal fatto che il sistema sia distribuito, in quanto vuole 
   solo porre in luce la relazione logica tra i componenti individuati dall'analisi del problema.

Il :blue:`come` avviene l'interazione tra le parti relativa alla acqusizione dei dati e all'invio dei comandi
non è specificato al momento. 
Come analisti del problema possiamo però evidenziare quanto segue:

#. l'uso della memoria comune come strumento di comunicazione va evitato, per  
   ottenere la flessibità di poter eseguire ciascun componente su un diverso nodo di elaborazione; 
#. il ``Controller`` può acquisire i dati in due modi diversi:

  #. inviando una richieste al Sonar, che gli fornisce un dato come risposta
  #. il Sonar non lavora come 'produttore a richiesta' ma pubblica dati su un broker 
     accessibile al ``Controller``.

Poichè abbiamo in precedenza escluso forme di interazione *publish-subscribe*, abbiamo al momento
ipotizzato il caso 2.1. 

Questo modello sembra portare intrinsecamente in sè l'idea di una classica applicazione   
ad oggetti che deve essere eseguita su un singolo elaboratore (o una singola Java virtual machine).
Ma forse non è proprio così.

.. Dunque sappiamo :blue:`cosa` fare e non fare: 
    in particolare, l'interazione Controller-Sonar sarà basata su una interazione punto-a-punto utilizzando
    il protocollo TCP.  Il :blue:`come` realizzare questa interazione sarà compito del progettista.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dagli oggetti alla distribuzione: gli enablers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il fatto di avere espresso il ``Controller`` con riferimento a interfacce e non ad oggetti concreti, 
significa che il progettista si può avvalere di appropriati :blue:`design pattern` per 
implememtare i componenti in modo che possano scambiare informazione via rete.

A questo fine possiamo introdurre, come analisti, l'idea di un nuovo tipo di ente,
denominato :blue:`enabler`, che ha come scopo quello di incapsulare software 'convenzionale' utile e 
testato ma non adatto alla distribuzione (che possiamo denominare :blue:`core-code`) 
all'interno di un involucro che funga da una sorta di  'membrana' capace di ricevere e 
trasmettere informazione.

.. image:: ./_static/img/Radar/ArchLogicaOOPEnablers.PNG 
   :align: center
   :width: 50%


Ad esempio, il ``Controller`` su PC utilizzerà un TCP-server con interfaccia ``ISonar`` che riceverà i dati 
dal Sonar posto sul Raspberry, rendendoli disponibili con il metodo ``getVal``.
Inoltre utilizzerà un TCP-client con interfaccia ``ILed`` che trasmetterà i comandi al Led 
sul Raspberry.

Questa idea di :blue:`enabler` sembra dunque promettente come strumento per un passaggio graduale
e sistematico dalla programmazione tradizionale ad oggetti alla programmazione distribuita.

Di fatto stiamo delienando la nascita di un :blue:`nuovo paradigma di programmazione` che troverà
più avanti un suo pieno sviluppo con i concetti di :blue:`attore` di :blue:`microservizio`. 

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Piano di lavoro
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Trattandosi di uno sviluppo di tipo bottm-up, il piano di lavoro parte dallo sviluppo dei componenti,
seguito da un opportuno 'assemblaggio' degli stessi in modo da formare il sistema che soddisfa i requisiti.

Poichè il nostro obiettivo è anche quello di riusare :blue:`core-code` fornito dal committente, possiamo pensare di procedere come segue:

#. definizione dei componenti software di base legati ai dispositivi di I/O (Sonar, RadarDisplay e Led);
#. definizione di alcuni supporti TCP per componenti lato client a lato server, con l'obiettivo di
   formare un insieme riusabile anche in applicazioni future; 
#. definizione componenti (denominati genericamente :blue:`enabler`)  capaci di abilitare  
   alle comunicazioni TCP i componenti-base;
#. assemblaggio dei componenti `enabler` per formare il sistema distribuito.

Il punto 2 relativo ai supporti non è indispensabile, ma, come detto, può costituire un elemento strategico 
a livello aziendale.

.. Il punto 3 sugli :blue:`enabler` nasce dall'idea di incapsulare software 'convenzionale' utile e 
   testato (che possiamo denominare :blue:`core-code`) all'interno di un involucro capace di ricevere e inviare 
    informazione, che funga da una sorta di 'membrana cellulare'.

..  Ad esempio, il software capace di accendere un Led fornito dal committente è un file bash che
    un opportuno :blue:`enabler` può porre in esecuzione ricevendo un comando dal ``Controller``.
 