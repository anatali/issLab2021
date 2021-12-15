.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark
 

.. ``  https://bashtage.github.io/sphinx-material/rst-cheatsheet/rst-cheatsheet.html

.. _pattern-proxy: https://it.wikipedia.org/wiki/Proxy_pattern

.. _port-adapter: https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)

.. _clean-architecture:  https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html

.. _microservizio: https://en.wikipedia.org/wiki/Microservices

.. _pattern-decorator: https://it.wikipedia.org/wiki/Decorator

======================================
RadarSystem
======================================
Tetendo conto dal nostro motto: 

:remark:`non c’è codice senza progetto, progetto senza analisi del problema, problema senza requisiti`

impostiamo un processo di produzione del software partendo da un insieme di requisiti.

--------------------------------------
Requisiti
--------------------------------------

Si desidera costruire un'applicazione software capace di: 

- (requisito :blue:`radarGui`) mostrare le distanze rilevate da un sensore ``HC-SR04`` connesso a un RaspberryPi 
  su un display (``RadarDisplay``) a forma di radar connesso a un PC
  
.. image:: ./_static/img/Radar/radarDisplay.png 
   :align: center
   :width: 20%
   
- (requisito :blue:`ledAlarm`) accendere un LED se la distanza rilevata è inferiore a un valore limite prefissato
  denominato ``DLIMIT``.

--------------------------------------
Analisi dei Requisiti
--------------------------------------
Iniziamo anallizzando il testo, cercando di chiarire con il committente il signifcato dei termini in esso presenti.
Questa comunicazione a livello umano è fondamentale per formulare requisiti che siano:

- Chiari, Corretti, Completi, Concisi
- Non ambigui, consistenti
- Tracciabili, Realizzabili, Collaudabili

+++++++++++++++++++++++++++++++++++++
Tracciabilità
+++++++++++++++++++++++++++++++++++++
Poichè il testo dei requisiti fornisce già un nome per ciascun requisito, si ha già un solido punto
di partenza per la :blue:`forward traceability`.

+++++++++++++++++++++++++++++++++++++
User stories
+++++++++++++++++++++++++++++++++++++

Una user-story che esprime il funzionamento atteso del sistema, catturando tutti i requisiti può essere
così espressa:

.. epigraph:: 
  
   :blue:`User-story US1`: come utente mi aspetto che il Led si accenda se pongo un ostacolo a distanza ``d<DILIMT`` 
   dal Sonar e che il Led si spenga non appena porto l'ostacolo ad una  distanza ``d>DILIMT``.
   In ogni caso posso vedere illuminarsi un punto sul ``RadarDisplay`` a distanza ``d`` 
   dal centro lungo   una  retta che forma un angolo :math:`\theta` 
   rispetto all'asse orizzontale del display.

   

+++++++++++++++++++++++++++++++++++++
Piano di testing (funzionale)
+++++++++++++++++++++++++++++++++++++  

La user-story precedente suggerisce anche un possibile test funzionale per la verifica del 
comportamento del software da sviluppare.

.. Un possibile test funzionale consiste nel porre un ostacolo davanti al Sonar
   prima a una distanza ``D>DLIMIT`` e poi a una distanza ``D<DLIMIT`` e osservare il valore
   visualizzato sulla GUI e lo stato del Led.

Tuttavia questo modo di procedere non è automatizzabile, in quanto richiede 
la presenza di un operatore umano. Nel seguito cercheremo di organizzare le cose in modo
da permettere :blue:`Test automatizzati`.


+++++++++++++++++++++++++++++++++++++
Glossario
+++++++++++++++++++++++++++++++++++++
La redazione di un glossario è utile per pervenire alla definizione di *Costumer requirements* 
(:blue:`C-requirements`) chiari e possibilmente non ambigui. 
Il nostro glossario, la cui redazione lasciamo al lettore, dovrà includere i termini 
*Sensore, Led, RadarDisplay* che corrispondono ad altrettanti :blue:`componenti` del sistema.

In questa sede però, la nostra attenzione si rivolge alla possibilità/necessità di esprimere
i requisiti ponendoci dal punto di vista dell'elaboratore, che (fortunatamente?!) non comprende
il linguaggio naturale.

Dal punto di vista della 'macchina', l'unico modo per relazionarsi con un ente menzionato nel glossario 
è avere del software che lo rappresenta.

Poniamo dunque al committente anche domande da questo punto di vista, e altre domande volte 
a chiarire bene la natura del sistema da realizzare.

+++++++++++++++++++++++++++++++++++++
Domande al committente
+++++++++++++++++++++++++++++++++++++


.. list-table:: 
  :widths: 50,50
  :width: 100%

  * - Il committente fornisce software relativo al Led ?
    - Si, ``led25GpioTurnOn.sh`` e ``led25GpioTurnOff.sh`` (progetto *it.unibo.rasp2021*)
  * - Il committente fornisce software per il Sonar ?
    - Si, ``SonarAlone.c`` (progetto *it.unibo.rasp2021*)
  * - Il committente fornisce qualche libreria per la costruzione del RadarDisplay ?
    - Si, viene reso disponibile (progetto *it.unibo.java.radar*)  il supporto  ``radarPojo.jar`` 
      che fornisce un singleton JAVA ``radarSupport`` capace di creare una GUI in 'stile radar' 
      e di visualizzare su di essa un valore di distanza intero fornito come ``String``:

      .. code:: java

        public class radarSupport {
        private static RadarControl rc;  
        public static void setUpRadarGui( ){
          rc=...
        }
        public static void update(String d,
              String dir){rc.update(d,dir);
        }
        }    
  * - Il LED può/deve essere connesso allo stesso RaspberryPi del sonar? 
    - Al momento si. In futuro però il LED potrebbe essere connesso a un diverso nodo di elaborazione.
  * - Il valore ``DLIMIT`` deve essere cablato nel sistema o è bene sia 
      definibile in modo configurabile dall'utente finale?
    - L'utente finale deve essere in grado di specificare in un 'file di configurazione' 
      il valore di questa distanza.
 
Dai requisiti possiamo asserire che:

- si tratta di realizzare il software per un **sistema distribuito** costituito da due nodi di elaborazione:
  un RaspberryPi e un PC convenzionale;
- i due nodi di elaborazione devono potersi  `scambiare informazione via rete`, usando supporti WIFI;
- i due nodi di elaborazione devono essere 'programmati' usando **tecnologie software diverse**.

+++++++++++++++++++++++++++++++++++++
In sintesi
+++++++++++++++++++++++++++++++++++++

:remark:`Si tratta di realizzare un sistema software distribuito ed eterogeneo`

Il sistema comprende un dispositivo di input (il Sonar) e due dispositivi di output (il Led e il RadarDisplay)


--------------------------------------
Analisi del problema
--------------------------------------

Per analizzare le problematiche implicite nei requisiti, dobbiamo porre molta attenzione a non confondere 
l'analisi **del problema** con l'analisi **di come pensiamo di risolvere** il problema.

Due sono gli approcci principali possibili:

- approccio :blue:`bottom-up`: partiamo da quello che abbiamo a disposizione e analizziamo i problemi che
  sorgono per 'assemblare le parti disponibili' in modo da costruire un sistema che soddisfi i requisiti funzionali;
- approccio :blue:`top-down`: partiamo analizzando le proprietà che il sistema deve 'logicamente' avere,
  senza legarci a priori ad alcun specifico componente e/o tecnologia. Successivamente, evidenziamo le
  problematiche che sorgono sia per soddisfare i requisiti funzionali sia per utilizzare (se si pone il caso) 
  componenti forniti dal committente o dalla nostra azienda, considerndo anche framework e infrastrutture 
  disponibili sul mercato (con una evidente propensione  all'open-source e al free software).

E' molto probabile che la maggior marte delle persone sia propensa a seguire (almeno inizialmente) un
approccio bottom-up, essendo l'approccio top-down meno legato a enti subito concretamente usabili come 
'building blocks'. 

Osserviamo però che il compito della analisi del problema non è quello di trovare una soluzione, 
ma quello di porre in luce le problematiche in gioco (il :blue:`cosa` si deve fare) e capire con quali risorse 
(tempo, persone, denaro, etc. )  queste problematiche debbano/possano essere affrontate e risolte.
Sarà compito dei progettisti quello di trovare il modo (il :blue:`come`) pervenire ad una soluzione 'ottimale'
date le premesse dell'analisi e le risorse a disposizione.

Anticipiamo subito che il nostro approccio di riferimento sarà di tipo top-down, per motivi che si dovrebbero
risultare chiari durante il percorso che ora iniziamo seguendo, al momento, un tipico modo di procedere bottom-up.

Sarà proprio rendendoci conto dei limiti di approcci bottom-up che acquisiremo (se non l'abbiamo già)
il convincimento che conviene chiarire bene il :blue:`cosa` prima di affrontare il :blue:`come` e che anche
il :blue:`come` può essere convenientemente affrontato ritardando o incapsulando il più possibile dettagli legati 
alle tecnologie utilizzate.

++++++++++++++++++++++++++++++++++++++
Un approccio bottom-up
++++++++++++++++++++++++++++++++++++++

La costruzione del sistema pone le seguenti :blue:`problematiche`:

.. list-table::
   :widths: 40,60
   :width: 100%

   * - Gestione del sensore ``HC-SR04``.
     - Il software fornito dal committente (``SonarAlone.c``) rende disponibile un generatore di dati
       sul dispositivo standard di output.  
   * - Realizzazione del ``RadarDisplay``.
     - A questo fine è disponibile il POJO realizzato da  ``radarPojo.jar`` 
   * - Gestione del Led.
     - Il software fornito dal committente (``led25GpioTurnOn.sh`` e ``led25GpioTurnOff.sh``) fornisce codice
       di basso livello per accendere e spegnere il Led.
   * - Quale assemblaggio?
     - .. image:: ./_static/img/Radar/RobotSonarStarting.png
            :width: 100%

       Occorre capire come i dati del sonar generati sul Raspberry possano raggiungere il PC ed essere usati per
       aggiornare il ``RadarDisplay`` e per accendere/spegnere il ``Led``.

 


La necessità di integrare i componenti disponibili *fa sorgere altre problematiche*:

   #. è opportuno incapsulare i componenti disponibli entro altri componenti capaci di interagire via rete?
   #. dove è più opportuno inserire la 'businenss logic'? In un oggetto che estende il sonar o il ``radarSupport``?
      Oppure è meglio introdurre un terzo componente?
   #. quale forma di interazione è più opportuna? diretta/mediata, sincrona/asincrona?.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dispositivi di input e di output 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Concettualmente, il Sonar è un dispositivo di input e il Led e il RadarDisplay sono dispositivi di output.

In generale, nella programmazione ad oggetti, 
per utilizzare un dispositivo di output è sufficiente invocare un metodo, mentre
l'uso di un dispositivo di input presenta due modalità principali:

- il componente interessato ai dati prodotti dal dispostivo di input, ne invoca un metodo
  *'bloccante'* (ad esempio ``read()``) che fornisce un dato non appena disponibile.
  Questo modo di procedere prende anche il nome di :blue:`interazione a polling`;
- il componente consumatore dei dati si relaziona con dispostivo di input seccondo 
  il  :blue:`pattern observer`.  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il pattern observer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Nella programmazione ad oggetti, un componente  :blue:`osservabile` invoca un metodo di
invio di dati (quando disponibili) a tutti i componenti che sono stati in precedenza registrati 
presso di lui  come *osservatori*. Un componente può essere registarto come osservatore solo
se implementa il metodo di invio dati (di solito denominato ``update``).

La registrazione di un *observer* presso un *observable*
può essere fatta dall'*observer* stesso o, preferibilmente, da un :blue:`configuratore` del sistema:
in questo modo nessuno dei due componenti avrebbe alcun riferimento staticamente definito all'altro.
  
Una 'variante' del pattern observer è costituita dalla possibilità che un dispositivo di input
possa 'pubblicare' i propri dati su una risorsa esterna osservabile. 
Torneremo su questa variante più avanti.

Notiamo che software disponibile per il Sonar opera come produttore di dati, ma non offre operazioni
per la registrazione di osservatori; un componente interessato ai dati del Sonar deve fare in modo 
che il proprio dispositivo di input
sia il dispositivo di output del Sonar e poi utilizzare una operazione come la ``read()``.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dai dispositivi al sistema
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Per costruire un sistema partendo dai dispositivi, occorre capire come l'informazione fornita
dal dispositivo di input Sonar possa essere elaborata in modo da fluire nel modo voluto
ai dispositivi di output.

Focalizzando l'attenzione sul requisito :blue:`RadarGui` e quindi sulla interazione *sonar-radar* 
(per il Led valgono considerazioni analoghe) possiamo rappresentare la situazione come segue:

.. list-table::
   :widths: 50,50
   :width: 100%

   *  - :blue:`Comunicazione diretta`
        
        Le 'nuvolette' in figura rappresentano gli strati di software che permettono ai dati generati dal Sonar 
        di eseere ricevuti dal ``RadarDisplay``.

      -   .. image:: ./_static/img/Radar/srrIntegrate1.png
            :width: 100%
   *  - :blue:`Comunicazione mediata`

        Richiede la presenza di un :blue:`componente mediatore (broker)`, di solito realizzato da terze parti 
        come servizio disponibile in rete. Un generatore di dati (come il Sonar) pubblica informazione  
        su una :blue:`topic` del broker; tale informazione
        che potrebbe essere ricevuta ('osservata') da uno o più ricevitori (come il RadarDisplay) che si iscrivono 
        a quella *topic*.  

      -   .. image:: ./_static/img/Radar/srrIntegrate2.png
            :width: 100%
          
 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Chi realizza la logica applicativa?
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Seguendo il :blue:`principio di singola responsabilità` (e un pò di buon senso) la realizzazione degli use-cases 
applicativi non deve essere attribuita al software di gestione dei dispositivi di I/O.

Dunque, la nostra analisi ci induce a sostenere
l'opportunità di introdurre un nuovo componente, che possiamo denominare ``Controller``), che abbia la
:blue:`responabilità di realizzare la logica applicativa`.

Il ``Controller`` deve ricevere in ingresso i dati del sensore ``HC-SR04``, elaborarli e  
inviare comandi al Led e dati al  ``RadarDisplay``.

Ma ecco sorgere un'altra problematica legata alla distribuzione:
       
- Il ``Controller`` può risiedere su RaspberryPi, sul PC o su un terzo nodo. 
  Tuttavia, un colloquio con il committente ha escluso (per motivi di costo) la possibilità di introdurre un altro
  nodo di elaborazione. 

- La presenza di un broker in forme di comunicazione mediata  potrebbe indurci ad attribuire responsabiliotà
  applicative al mediatore. Ma è giusto/opportuno procedere i questo modo?

Dunque si tratta di analizzare dove sia meglio allocare il ``Controller`` :

.. list-table::
   :widths: 30,70
   :width: 100%

   * - ``Controller`` sul RaspberryPi.
     - Si avrebbe una maggior reattività nella accensione del Led in caso di allarme. Inoltre ...
       
   * - ``Controller`` sul PC.
     - Si avrebbe più facilità nel modificare la logica applicativa,
       lasciando al Raspberry solo la responsabilità di gestire dispositivi. Inoltre ...
   * - ``Controller`` sul broker.
     - Al momento escludiamo questa possibilità, riservandoci di riprendere il problema quando esamineremo
       architetture distribuite 'space-based'.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Quale 'collante'? I protocolli di comunicazione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Dovendo realizzare un sistema distribuito (ed eterogeno), i componenti del sistema devono poter scambiare 
informazione (in modo che possano capirsi).

Per ottenere questo scopo, sono stati sviluppati numerosi protocolli che,
avvalendosi di una appropriata infrastruttura di rete,  permettono lo scambio di informazione
tra componenti che diventano la parti costituenti di un sistema proprio grazie al 'collante' 
offerto dal protocollo.

Poichè protcolli diversi inducono a concepire sistemi organizzati in modo diverso, è opportuno
riflettere sul :blue:`tipo di protocollo` che è possibile scegliere 
e sul :blue:`tipo di architettura` che  scaturisce da questa scelta.

In questa fase, possiamo diviedere i protocolli di comunicazioni più diffusi in due macro-categorie:

- protocolli :blue:`punto-a-punto` che stabiliscono un *canale bidirezionale* tra compoenenti di solito
  denominati client e  server. Esempi di questo tipo sono ``UDP, TCP, HTTP, CoAP, Bluetooth``.
- protocolli :blue:`publish-subscribe` che si avvalgono di un mediatore (broker) tra client e server. Esempio
  di questo tipo di protocollo è ``MQTT`` che viene supportato da broker come ``Mosquitto, RabbitMQ, HiveMq``, etc. 

Al momento dovremmo avere conoscenze su come usare protocolli quali TCP/UDP e HTTP
ma siamo forse meno esperti nell'uso di supporti per la comunicazione mediata tramite broker.

Seguiamo dunque l'idea delle **comunicazioni dirette** facendo riferimento al protocollo TCP
(più affidabile di UDP e supporto di base per HTTP)  che assume quindi al monento il ruolo di 'collante' 
principale tra le parti.

+++++++++++++++++++++++++++++++++++++++++++++++++
Considerazioni architetturali
+++++++++++++++++++++++++++++++++++++++++++++++++
Per approfondire l'analisi delle problematiche che si pongono quando si voglia 
far comunicare due componenti software con TCP, non ci interessano tanto i dettagli tecnici di come opera 
il protocollo, quanto le ripercussioni sulla architettura del sistema.

A questo riguardo possiamo dire che nel sistema dovremo avere componenti capaci
di operare come un `client-TCP` e componenti capacai di operare come un `server-TCP`.

.. list-table::
  :widths: 15,85
  :width: 100%

  * - Server
    - Il server opera su un nodo con indirizzo IP noto (diciamo ``IPS``) , apre una ``ServerSocket`` su una  porta 
      (diciamo ``P``) ed attende messaggi  di connessione su ``P``.

  * - Client
    - Il client deve dapprima aprire una ``Socket`` sulla coppia ``IPS,P`` e poi inviare o ricevere messaggi su tale socket.
      Si stabilisce così una *connessione punto-a-punto bidirezionale* tra il nodo del client e quello del server.

Inizialmente il server opera come ricevitore di messaggi e il client come emettitore. Ma su una connessione TCP,
il server può anche dover inviare messaggi ai client, ad esempio quando  si richiede una interazione di tipo
:blue:`request-response`. In tal caso, il client deve essere anche capace di agire come ricevitore di messaggi.



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'idea di connessione: l'interfaccia ``Interaction2021``
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
La necessità di  inviare e ricevere messaggi via rete segnala un :blue:`gap`  tra il livello tecnologico 
dei componenti software resi disponibili dal committente e le necessità del problema.

Coma analisti, osserviamo che un *gap* relativo alle comunicazioni di rete **si può presentare in modo sistematico
in tutte le applicazioni distribuite**. Sarebbe dunque opportuno cercare di colmare questo *gap* in modo non episodico,
introducendo :blue:`componenti riusabili` che possano 'sopravvivere' all'applicazione che stiamo costruendo
per poter essere impiegati in futuro in altre applicazioni distribuite.

Astraendo dallo specifico protocollo, osserviamo che tutti i principali protocolli punto-a-punto 
sono in grado di stabilire una :blue:`connessione` stabile sulla quale inviare e ricevere messaggi.

Questo concetto può essere realizzato da un oggetto che rende disponibile opportuni metodi, come quelli definiti
nella seguente interfaccia:

.. _conn2021: 

.. code:: Java

  interface Interaction2021  {	 
    public void forward(  String msg ) throws Exception;
    public String receiveMsg(  )  throws Exception;
    public void close( )  throws Exception;
  }

Il metodo di trasmissione è denominato ``forward`` per rendere più evidente il fatto che pensiamo ad un modo di operare 
:blue:`'fire-and-forget'`. 

L'informazione scambiata è rappresenta da una ``String`` che è un tipo di dato presente in tutti
i linguaggi di programmazione.
Non viene introdotto un tipo  diverso (ad esempio ``Message``) perchè non si vuole stabilire 
il vincolo che gli end-points della connessione siano componenti codificati nello medesimo linguaggio di programmazione

La ``String`` restituita dal metodo ``receiveMsg`` può rappresentare una risposta a un messaggio
inviato in precedenza con ``forward``.

Ovviamente la definizione di questa interfaccia potrà essere estesa e modificata in futuro, 
a partire dall fase di progettazione, ma rappresenta una forte indicazione dell'analista di 
pensare alla costruzione di componenti software che possano ridurre il costo delle applicazioni future.


.. include:: RadarSystemProdottiAnalisi.rst


--------------------------------------
Progettazione e sviluppo incrementale
--------------------------------------

Iniziamo il nostro progetto affrontando il primo punto del piano di lavoro proposto dall'analisi.

Usando la terminologia :blue:`SCRUM`, impostiamo il primo :blue:`SPRINT` dello sviluppo, al termine del  quale
la prevista :blue:`Srint Review` farà il punto della situazione con il committente e getterà le basi per
il passo successivo, che potrà coincidere o meno con quello pianificato nell'analisi.

.. include:: RadarSystemComponenti.rst

.. include:: RadarSystemSupporti.rst

.. include:: RadarSystemEnablers.rst

 
+++++++++++++++++++++++++++++++++++++++++++++
Il sistema reale distribuito
+++++++++++++++++++++++++++++++++++++++++++++

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Deployment
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: 

  gradle build jar -x test

Crea il file `build\distributions\it.unibo.enablerCleanArch-1.0.zip` che contiene la directory bin  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Test funzionale
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

==================================
Il concetto di contesto
==================================

Si veda :doc:`ContextServer`.

==================================
Un approccio top down
==================================


Si veda :doc:`ApproccioTopdown`.



  

