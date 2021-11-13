.. contents:: Overview
   :depth: 4
.. role:: red 
.. role:: blue 
.. role:: remark

.. `` 

======================================
RadarSystem
======================================
Tetendo conto dal nostro motto 
:remark:`Non c’è codice senza progetto. Non c’è progetto senza analisi del problema. Non c’è problema senza requisiti`
impostiamo un processo di produzione del software partendo da un insieme di requisiti.

--------------------------------------
Requisiti
--------------------------------------

Si desidera costruire un'applicazione software capace di: 

- (requisito :blue:`radarGui`:) mostrare le distanze rilevate da un sensore ``HC-SR04`` connesso a un RaspberryPi 
  su un display a forma di radar connesso a un PC
  
.. image:: ./_static/img/Radar/radarDisplay.png
   :align: center
   :width: 20%
   
- (requisito :blue:`ledAlarm`:) accendere un LED se la distanza rilevata è inferiore a un valore limite prefissato
  denominato ``DLIMIT``.

--------------------------------------
Analisi dei Requisiti
--------------------------------------

Iniziamo ponendo al customer una serie di domande e riportiamone le risposte:

.. list-table:: 
   :widths: 40,60
   :width: 100%

   * - Il LED può/deve essere connesso allo stesso RaspberryPi del sonar? 
     - Al momento si. In futuro però il LED potrebbe essere connesso a un diverso nodo di elaborazione.
   * - Il committente fornisce qualche libreria per la costruzione del display ?
     - Si, viene reso disponibile il supporto  ``radarPojo.jar`` scritto in JAVA che fornisce un oggetto
       di classe ``radarSupport`` capace di creare una GUI in 'stile radar' e di visualizzare dati su di essa:

       .. code::

         public class radarSupport {
         private  static RadarControl radarControl;
           public static void setUpRadarGui( ) {
             radarControl = new RadarControl( null ); }
 	        public static void update(String d,String theta){
		       radarControl.update( d, theta );
	        }
         }    

       Il supporto è realizzato dal progetto *it.unibo.java.radar*.
   * - Il valore ``DLIMIT`` deve essere cablato nel sistema o è bene sia 
       definibile in modo configurabile dall'utente finale?
     - L'utente finale deve essere in grado di specificare in un 'file di configurazione' il valore di questa distanza.
   * - Dove deve risiedere il file di configurazione?
     - Per agevolare l'utente finale, è bene che il file di configurazione risieda sul PC.

Dai requisiti possiamo asserire che:

- si tratta di realizzare il software per un *sistema distribuito* costituito da due nodi di elaborazione:
  un RaspbeddyPi e un PC convenzionale;
- i due nodi di elaborazione devono potersi *scambiare informazione via rete*, usando supporti WIFI;
- i due nodi di elaborazione devono essere 'programmati' usando *tecnologie software diverse*.

In sintesi:

:remark:`Si tratta di realizzare un sistema software distribuito ed eterogeno`

+++++++++++++++++++++++++++++++++++++
Piano di testing
+++++++++++++++++++++++++++++++++++++  

.. Requisito :blue:`ledAlarm`:

Un test funzionale consiste nel porre un ostacolo davanti al Sonar
prima a una distanza ``D > DLIMIT`` e poi a una distanza ``D < DLIMIT`` e osservare il valore
visualizzato sulla GUI.

Tuttavia questo modo di procedere non è automatizzabile, in quanto richiede 
la presenza di un operatore umano. Nel seguito cercheremo di organizzare le cose in modo
da permettere Test automatizzati.

--------------------------------------
Analisi del problema
--------------------------------------

Per analizzare le problematiche implicite nei requisiti, possiamo seguire due diversi approcci:

- approccio :blue:`bottom-up`: partiamo da quello che abbiamo a disposizione e 'assembliamo le parti dispoibili'
  in modo da costruire un sistema che soddisfa i requisiti funzionali;
- approccio :blue:`top-down`: partiamo analizzando le proprietà che il sistema deve 'logicamente' avere per soddisfare i  
  requisiti funzionali senza legarci a priori ad alcun specifico componente e/o tecmologia.

E' molto probabile che la maggior marte delle persone sia propensa a seguire (almeno inizialmente) un
approccio bottom-up, essendo l'approccio top-down meno riconducibile a enti che sia possibile usare 
concretamente come punto di partenza per 'sintetizzare una soluzione'. 

Osserviamo però che :blue:`compito della analisi` non è quello di trovare una soluzione, ma quello di porre in luce 
le problematiche poste dai requisiti (il :red:`cosa` si deve fare) e capire con quali risorse 
(tempo, persone, denaro, etc. )  queste problematiche debbano/possano essere affrontate e risolte.
Sarà compito deo progettisti quello di trovare il modo (il :red:`come`) pervenore ad una soliuzione 'ottimale'
date le risorse a disposizione.

++++++++++++++++++++++++++++++++++++++
Un approccio bottom-up
++++++++++++++++++++++++++++++++++++++

Il sistema pone le seguenti :blue:`problematiche`:

.. list-table::
   :widths: 40,60
   :width: 100%

   * - Gestione del sensore ``HC-SR04``.
     - A questo fine la software house dispone già di codice riutilizzabile, ad esempio 
       ``SonarAlone.c`` (project it.unibo.rasp2021)
   * - Gestione del display  .
     - A questo fine è disponibile il POJO realizato da  ``radarPojo.jar`` 
   * - Gestione del LED.
     - A questo fine la software house dispone già di codice riutilizzabile, ad esempio 
       ``LedControl.py`` (project ...)
   * - Quale assemblaggio?
     - .. image:: ./_static/img/Radar/RobotSonarStarting.png
            :width: 100%
   
La necessità di integrare i componenti disponibili *fa sorgere altre problematiche*:

   - incapsulare i componenti disponibli entro altri componenti capaci di interagire via rete
   - capire dove sia più opportuno inserire la 'businnss logic': estendendo il sonar o ``radarSupport``?
     Oppure introducendo un terzo componente?
   - capire quale forma di interazione sia più opportuna: diretta o mediata

Focalizzando l'attenzione sulla interazione sonar-radarSupport possiamo rappresentare la situazione come segue:

.. list-table::
   :widths: 30,70
   :width: 100%

   *  - Comunicazione diretta:
      -   .. image:: ./_static/img/Radar/srrIntegrate1.png
            :width: 100%
   *  - Comunicazione mediata:
      -   .. image:: ./_static/img/Radar/srrIntegrate2.png
            :width: 100%


Il meditore potrebbe anche fungere da componente capace di realizzare la logica applicativa. 
Ma è giusto/opportuno procedere i questo modo?

Seguendo un punto di vista logico e il principio :red:`xxx` possiamo sostenre, come analisti del problema,
l'opportunità di introdurre un componente (``Controller``), diverso dai dispositivi, che abbia la
:blue:`responabilità di realizzare la logica applicativa`.

Ma ecco sorgere un'altra problematica:

.. list-table::
   :widths: 40,60
   :width: 100%
 
   * - Distribuzione.
     - Il ``Controller`` deve ricevere in ingresso i dati del sensore ``HC-SR04``, elaborarli e  
       inviare comendi al LED e dati alla RADAR-GUI.
       
       Il ``Controller`` puo risiedere su RaspberryPi, sul PC o su un terzo nodo. 
       
       Un colloquio con il committente esclude (per motivi di costo) la possibilità di introdurre un terzo
       nodo di elaborazione. 

Dunque si tratta di analizzare se sia meglio allocare il ``Controller`` sul RaspberryPi o sul PC.

.. list-table::
   :widths: 40,60
   :width: 100%

   * - ``Controller`` sul RaspberryPi.
     - Pro  
   * - ``Controller`` sul PC.
     - Pro   

++++++++++++++++++++++++++++++++++++++
Un approccio top-down
++++++++++++++++++++++++++++++++++++++