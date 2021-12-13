.. _proxy-pattern: https://it.wikipedia.org/wiki/Proxy_pattern

.. _port-adapter: https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)

.. _clean-architecture:  https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html

.. _microservizio: https://en.wikipedia.org/wiki/Microservices

+++++++++++++++++++++++++++++++++++++++++++++
Prodotti della analisi
+++++++++++++++++++++++++++++++++++++++++++++

Importanti prodotti, al termine della fase di analisi dei requisiti e del problema sono:

- la definizione di una :blue:`architettura logica` di riferimento che tiene conto dei vincoli posti 
  dai requisiti e dal problema che ne consegue;
- la proposta di un :blue:`piano di lavoro` per lo sviluppo del sistema.


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
Architettura ad oggetti
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Se astraiamo dalla distribuzione (supponendo ad esempio che tutto il sistema possa
essere supportato sul RaspberryPi), l'architettura logica del sistema risulta
riconducibile a un classico schema :blue:`read-eval-print` in cui:  

.. epigraph:: 

  Il componente ``Controller`` deve leggere dati dal ``Sonar`` 
  come dispositivo di input e inviare comandi al ``Led`` e al ``RadarDisplay`` 
  come dispositvi di output.

:remark:`Il sistema presenta quattro componenti: tre dispositivi e un Controller che li gestisce`

Per rendere comprensibile questa architettura anche alla 'macchina' senza entrare in dettagli
implementativi, possiamo introdurre opportuni :blue:`modelli dei componenti` utlizzando qualche linguaggio
di programmazione.

Nel caso di Java, il costrutto interface può essere usato per denotare un componente catturandone
come aspetto essenziale le funzionalità che esso deve offrire e una sorta di :blue:`contratto` 
sull’uso del componente.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Modello ad oggetti del dominio
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
I modelli iniziali dei componenti descritti da interfacce Java per il *Led,
il Sonar e il RadarDisplay* costuiscono il nostro :blue:`modello del dominio`. 
Ispirandoci agli schemi port-adapter_ e clean-architecture_:

:remark:`il modello del dominio sarà al centro della architettura del sistema`

:remark:`il software relativo dominio sarà scritto in un package dedicato`

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Le interfacce ILed e IRadarDisplay
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


.. list-table::
  :widths: 50, 50
  :width: 100%

  * -  Led
    -  RadarDisplay
  * -        
      .. code:: java

        public interface ILed {
          public void turnOn();
          public void turnOff();
          public boolean getState();
        }
    -        
      .. code:: java     

        public interface IRadarDisplay{
          public void update(String d, String a);
        }  

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Le interfacce IDistance e ISonar
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
.. list-table::
  :widths: 50, 50 
  :width: 100%

  * -  Sonar State
    -  Sonar

  * -        
      .. code:: java

       public interface IDistance {
        public void setVal( int d );
        public int getVal(   );
       }
    -        
      .. code:: java

       public interface ISonar {
         public void activate();		 
         public void deactivate();
         public ISonarDistance getDistance();	
         public boolean isActive();
       }

In quanto generatore di dati, ``ISonar`` offre metodi per attivare/disattaivare il dispositivo e il
metodo ``getVal`` per fornire il valore corrente di distanza misurata. 

La interfaccia ``ISonarDistance`` è introdotta per reppresentare il concetto di distanza, in modo
da non ridurre quato concetto a un tipo predefinito, come ``int``.

Notiamo che, invece, per il Led abbiamo 'ridotto' il concetto di stato del Led al 
tipo predefinito  ``boolean``. Questa diverso modo di procedere avrà conseguenze, che verranno
poste meglio in luce in seguito.


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Architettura logica del sistema
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

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
    IDistance d = sonar.getDistance(); //Acquisizione di un dato dal sonar
    if( d.getVal()) < DLIMIT )        //Elaborazione del dato
      Led.turnOn() else Led.turnOff  //Gestione del Led
    radar.update( ""+d.getVal(), "90")    //Visualizzazione su RadarDisplay
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
all'interno di un involucro capace di ricevere e trasmettere informazione.

Ad esempio, il ``Controller`` su PC potrebbe utilizzare un TCP-server con interfaccia ``ISonar`` che riceverà i dati 
dal Sonar posto sul Raspberry, rendendoli disponibili con il metodo ``getDistance``.
Inoltre  potrebbe utilizzare un TCP-client con interfaccia ``ILed`` che trasmetterà i comandi al Led 
sul Raspberry.


.. image:: ./_static/img/Radar/ArchLogicaOOPEnablers.PNG 
   :align: center
   :width: 50%


Tuttavia, per limitare il traffico di rete, è inutile inviare i dati del sonar anche quando non
sono richiesti dal sever, per cui, come analisti, riteniamo opportuno che sul PC vengano definiti, ad uso
del  ``Controller``, due enabler *tipo-client*, uno per il Led e uno per il Sonar che interagiranno cone due
enabler *tipo-server* complementari posti sul RaspberryPi, inviando:

- messaggi interpretabili come :blue:`comandi` (ad esempio ``activate``, ``turnOff``)
- messaggi interpretabili cone :blue:`richieste` (ad esempio ``getVal``, ``getState``)

.. image:: ./_static/img/Radar/ArchLogicaOOPEnablersBetter.PNG 
   :align: center
   :width: 50%
 
Notiamo che gli *enabler tipo-client* sono  una forma di proxy-pattern_.



L'idea di :blue:`enabler` sembra dunque promettente come strumento per un passaggio graduale
e sistematico dalla programmazione tradizionale ad oggetti alla programmazione distribuita.
Siamo di fornte ai primi passi relativi a un 
 
:remark:`nuovo paradigma di programmazione per sistemi distribuiti`

che troverà più avanti un suo pieno sviluppo con i concetti di :blue:`attore` e di microservizio_.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Piano di lavoro
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Trattandosi di uno sviluppo di tipo bottm-up, il piano di lavoro parte dallo sviluppo dei componenti,
seguito da un opportuno 'assemblaggio' degli stessi in modo da formare il sistema che soddisfa i requisiti.

Poichè il nostro obiettivo è anche quello di riusare :blue:`core-code` fornito dal committente, possiamo pensare di procedere come segue:

#. definizione dei componenti software di base legati ai dispositivi di I/O (Sonar, RadarDisplay e Led);
#. definizione di alcuni supporti TCP per componenti lato client a lato server, con l'obiettivo di
   formare un insieme riusabile anche in applicazioni future; 
#. definizione di componenti  :blue:`enabler`  capaci di abilitare  
   alle comunicazioni TCP (o mediante altri tipi di protocollo) i componenti-base;
#. assemblaggio dei componenti  per formare il sistema distribuito.

Il punto 2 relativo ai supporti non è indispensabile, ma, come detto, può costituire un elemento strategico 
a livello aziendale.

.. Il punto 3 sugli :blue:`enabler` nasce dall'idea di incapsulare software 'convenzionale' utile e 
   testato (che possiamo denominare :blue:`core-code`) all'interno di un involucro capace di ricevere e inviare 
    informazione, che funga da una sorta di 'membrana cellulare'.

..  Ad esempio, il software capace di accendere un Led fornito dal committente è un file bash che
    un opportuno :blue:`enabler` può porre in esecuzione ricevendo un comando dal ``Controller``.


.. ----> RadarSystemComponenti