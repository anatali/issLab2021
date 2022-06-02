.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=========================================
TemaFinale22
=========================================

A company intends to build a service for :blue:`Separate collection of waste`  composed of a set of elements:

#. a service area (rectangular, flat) that includes:
  
   - an INDOOR port, to enter waste material
   - a PlasticBox container, devoted to store objects made of plastic, upto MAXPB kg of material.
   - a GlassBox container, devoted to store objects made of glass, upto MAXGB kg of material.
   - fixed obstacles, as shown in the following picture:
   
    .. image:: ./_static/img/Intro/ScienzaEIng.PNG
      :align: center
      :width: 10%



#. a DDR robot working as a transport trolley, that is intially situated in its HOME location. The transport trolley has
   the form of a square of side length RD .

      .. image:: ./_static/img/Intro/ScienzaEIng.PNG
        :align: center
        :width: 10%

   A map of the service area, represented as a grid of squares of side length RD , is available in the file
   serviceAreaMap (.txt, .bin):

#. a Service-manager (an human being) which supervises the state of the service-area and handles critical situations. 


The proper scene for the WEnv is reported in: serviceAreaConfig.js

-----------------------------
Requirements
-----------------------------

The WasteService should create a WasteServiceGUI for the manager and then perform the following tasks:

-----------------------------
User stories
-----------------------------

As a GarbageTruck 

 - I intend to 
  
As a Service-manager

-----------------------------
Non functional requirements
-----------------------------

#. The ideal work team is composed of 3 persons. Teams of 1 or 2 persons (NOT 4 or more) are also allowed.
#. The team must present a workplan as the result of the requirement/problem analysis,
   including some significan TestPlan.
#. The team must present the sequence of SPRINT performed, with appropriate motivations.
#. Each SPRINT must be associated with its own 'chronicle' (see templateToFill.html) that presents, in concise way,
   the key-points related to each phases of development.
   Hopefully, the team could also deploy the system using docker.
#. Each team must publish and maintain a GIT-repository (referred in the templateToFill.html)
   with the code and the related documents.
#. The team must present (in synthetic, schematic way) the specific activity of each team-component.

-----------------------------
Guidance
-----------------------------

Oltre al codice sviluppato durante il corso, il progetto it.unibo.qakDemo
include codice che potrebbe risultare utile
per l'applicazione finale.
Il numero e le finalità degli SPRINT sono definiti dal Team di sviluppo dopo opportune interazioni con il
committente.
Il committente (e/o il product-owner) è disponibile ONLINE in linea di massima ogni Giovedi dalle 15 alle 18
fino a fine Luglio, ma è sempre contattabile on-demand via email.

Lo svolgimento del lavoro è auspicabile avvenga in diverse fasi:

1. Fase di analisi, che termina con la definizione di una architettura logica del sistema, di modelli eseguibili e
alcuni, significativi piani di testing.
E' raccomandato che i risultati di questa fase vengano presentati al committente (con opportuno
appuntamento) prima della consegna finale del prodotto.
2. Fase di progetto e realizzazione, che termina con il deployment del prodotto.
3.
4. Fase di discussione del lavoro svolto, che potrebbe (auspicabilmente) svolgersi IN PRESENZA in LAb2.
E'
opportuno che ogni partecipante sia pronto a discutere anche sugli elaborati che ha prodotto durante il corso.

AL TERMINE DEL LAVORO:
1) Porre il sistema in uno stato con
0 < nfree < 6 slot liberi e 0 < nbusy < 6 slot occupati.
2) Ipotizzare che la temperatura sia e permanga ok, il fan spento,
INDOOR e OUTDOOR libere
3) Simulare che il sistema riceva, a distanza di 2 sec l'una dall'altra:
a) una enterrequest
b) una carenter
c) una pickup request
4) Definire le variazioni di stato che il SISTEMA REALIZZATO effettua.
Ad esempio:
Il sistema risponde al client al punto a) e poi inizia una acceptin al punto b).
In questa fase, il cliente vede ... mentre il manager vede ...
Quando arriva c) ...
5) Indicare la strategia seguita per poter automatizzare un test di questo tipo
Modalità relativa al colloquio orale
Si svolgerà in tre fasi, ma 48 h prima del colloqio , il codice del sistema deve essere stato pubblicato sul sito del
gruppo,
dandone relativa informazione via mail al docente.
Inoltre il giorno del colloquio , ogni gruppo deve avere effettuato gli opportuni preparativi per la/le demo, in modo
da essere subito operativo.

FASI del colloquio
1. A) Presentazione (collettiva di gruppo) di una demo 'live' del sistema
(preferibilmente, ma non obbligatoriamente
distribuito) di durata 10-15(max) minuti.
L'ordine di presentazione dei gruppi verrà opportunamente stabilito dal docente.
La demo deve mostrare la esecuzione di almeno un Test(Plan) automatizzato ritenuto significativo.
Per applicazioni che NON usano robot reali NON sono ammessi video
(che potrebbero essere invece utili per
mostrare il funzionamento di robot reali
o di sistemi che includono il RaspberryPi o altro dispositivo)
2. B) Presentazione (collettiva di gruppo) del progetto del sistema e della sua relazione
con la fase di analisi.
In
questa fase è RICHIESTA la preparazione di 2-3 SLIDES di illustrazione delle architetture con figure e (se
ritenuto utile) riferimenti al codice.
Al termine di queste fasi il gruppo può raggiungere un punteggio
massimo di 27/30.
3. C) Domande (per esempi, si veda il file domande.html) rivolte dal docente a singole persone,
riguardo al
prodotto, al progetto e alla analisi del problema /requisiti.
Al termine di questa fase una singola persona può
raggiungere un punteggio massimo di 29/30.
4. D) Altre domande rivolte dal docente a singole persone.
Al termine di questa fase, una singola persona può
raggiungere un punteggio di 30elode.
