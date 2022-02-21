.. role:: red
.. role:: blue  
.. role:: remark   


.. _SEDisasters : Software_engineering_disasters
.. _OpenGroupArch : Open Group Architectural Framework
.. _Design Pattern : Design Pattern
.. _Patten Software Architectures : ppp 
.. _SitoWebIssUnibo : https://www.unibo.it/it/didattica/insegnamenti/insegnamento/2021/468003
.. _GitHubIss2022 : https://github.com/anatali/issLab2022
.. _VideoStudenti : https://unibo.cloud.panopto.eu/Panopto/Pages/Sessions/List.aspx#folderID=%222f957969-7f72-4609-a690-aca900aeba02%22

 
.. _DockerRepo : https://hub.docker.com/repositories

.. _Dispense Ingegneria del software : ./NatMolBook/bookEntry.html  

.. _robot reali: _static/devsDdr.html

.. _template2022 : _static/templateToFill.html

.. _SCRUM : 

======================================
Introduzione
======================================


.. image:: ./_static/img/Intro/fig0-2022.png 
   :align: center
   :width: 90%


--------------------------------------
Contenuti del corso
--------------------------------------

Riportiamo qui quanto si legge nel Sito Web del corso :  
https://www.unibo.it/it/didattica/insegnamenti/insegnamento/2021/468003.


Al termine del corso lo studente:

- è in grado di impostare :blue:`processi di sviluppo cooperativo` del software basati su approcci agili 
  (in particolare SCRUM) avvalendosi anche di modelli eseguibili, espressi mediante meta-modelli custom;
- è' in grado di progettare e sviluppare sistemi software e relativi piani di testing in modo :blue:`incrementale 
  ed evolutivo`, partendo dal problema e dal dominio applicativo piuttosto che dalla tecnologia realizzativa, 
  attraverso la definizione di modelli eseguibili dell':blue:`analisi dei requisiti e dell'analisi del problema`;
- è in grado di blue:`valutare in modo critico` le continua evoluzione delle tecnologie informatiche, 
  sia a livello computazionale, sia livello di sviluppo-software, acquisendo :blue:`conoscenze teorico-operative` 
  su linguaggi, metododologie e strumenti quali *Kotlin, Gradle, SCRUM, SpringBoot, Devops, Docker*;
- è in grado di comprendere il ruolo dei diversi blue:`stili di architetture software` 
  (layers, client-server, pipeline, microkernel, service-based, event-driven, space-based, microservices) 
  e di come :blue:`scegliere lo stile architetturale più opportuno` per i diversi sotto-sistemi individuati;
- è in grado di affrontare l'analisi, il progetto e la costruzione di applicazioni distribuite ed eterogenee 
  di tipo proattivo e/o reattivo (unitamente a loro possibili piattaforme di sviluppo e di supporto run-time) 
  con particolare riferimento a :blue:`modelli computazionali a scambio di messaggi e ad eventi`;
- è in grado di realizzare le :blue:`interazioni a scambio di messaggi` tra componenti distribuiti utilizzando 
  modelli logici di alto livello e implementazioni basate su protocolli diversi (TCP, UDP, HTTP, CoAP, MQTT);
- è in grado di comprendere come sia possibile progettare e costruire ambienti di sviluppo custom capaci 
  di :blue:`generazione automatica di codice` (Software Factories in 'ecosistemi' come Eclipse/IntelliJ), 
  basandosi su Model Driven Software Development (MDSD) e sull'uso di Domain Specific Languages (DSL);
- è in grado di sviluppare applicazioni :blue:`capaci di combinare` aspetti di alto livello (in particolare di AI) 
  con aspetti di basso livello relativi a dispositivi di Internet of Things (IOT), utilizzando sia ambienti 
  virtuali sia dispositivi reali costruibili utilizzando elaboratori a basso costo quali RaspberryPi e Arduino;
- è in grado di :blue:`applicare` i concetti, i dispositivi, e gli strumenti sviluppati in modo concreto ed operativo 
  durante il corso per lo sviluppo di una :blue:`applicazione finale` che utilizza uno o più dispositivi IOT 'situati', 
  con particolare riferimento a Differental Drive Robots (DDR) con sensori 
  che possono agire in modo relativamente autonomo in :blue:`diversi` ambienti virtuali o reali, 
  senza modificare il software che esprime la 'business logic' del problema.

Pe raggiungere questi obiettivi, il corso 2021-2022 si articolerà in tre fasi:

++++++++++++++++++++++++++++++++
FASE1
++++++++++++++++++++++++++++++++

:remark:`Dalla OOP alla costruzione di sistemi software distribuiti eterogenei a scambio di messggi.`

- Sviluppo di un sistema (:doc:`RadarSystem`) basato su un PC e su un RaspberryPi uando TCP e seguendo un 
  processo di sviluppo agile ed evolutivo (ispirato a SCRUM) di tipo :blue:`bottom-up`.
- Primi approfondimenti sulla fase di analisi dei requisiti e sulla analisi del problema. 
  Il ruolo della architettura logica (come artefatto della analisi) per l'analisi dei rischi e per la pianificazione dei lavori.
- Il ruolo del Testing e della pianificazione di test automatizzabili (con JUnit).
- Refactoring del sistema a fronte dell'uso di altri protocolli: MQTT e CoAP. 
- Come rendere il software applicativo indipendente dal protocollo.
- Come dotare l'applicazione di una WebGui usando SpringBoot.
- Costruzione di un primo prototipo e suo deployment.

++++++++++++++++++++++++++++++++
FASE2
++++++++++++++++++++++++++++++++
:remark:`Degli oggetti ad attori che interagiscono a messaggi.`

- Il modello di programmazione a scambio di messaggi portato a livello di componenti.
- Introduzione al linguaggio Kotlin.
- Dalle coroutine agli atttori
- Da attori message-driven ad attori message-based che operano come automi a stati finiti
- Definizione di una infrastruttura per attori capaci di formare sistemi software distribuiti ed eterogeni.
   

++++++++++++++++++++++++++++++++
FASE3
++++++++++++++++++++++++++++++++

:remark:`Da bottom-up a top-down: il ruolo dei modelli.`

- Definizione di una linguaggio/metamodello custom (Qakctor) per la costruzione di sistemi basati su attori
- Applicazione di quanto sviluppato per lo sviluppo incrementale di una applicazione finale IOT che utilizza 
  dispositivi robotici virtuali e/o  `robot reali`_, costruiti estendendo il sistema della FASE1. 
- Il vantaggio dell'uso di modelli eseguibili nelle fasi di analisi dei requisiti e del problema e come premessa
  per l'abbattimento dei costi (e degli imprevisti) di produzione.


.. image:: ./_static/img/Intro/mbotIot.png 
   :align: center
   :width: 70%
 

--------------------------------------
Propedeuticità
--------------------------------------

`Dispense Ingegneria del software`_


--------------------------------------
Link utili
--------------------------------------

- `SitoWebIssUnibo`_: https://www.unibo.it/it/didattica/insegnamenti/insegnamento/2021/468003
- GITHUB del corso `GitHubIss2022`_:  https://github.com/anatali/issLab2022
- Template  `template2022`_
- Video tema finale studenti (Panopto) `VideoStudenti`_
- books.html
   
--------------------------------------
Tools su PC
--------------------------------------

#. Installare GIT
 
#. Clonare https://github.com/anatali/issLab2022 in una directory vuota e.g. C:/.../iss2022

#. Installare Gradle

#. Installare IntelliJ

#. Installare Eclipse IDE for Java and DSL Developers (2021 06)  

#. Installare Docker

#. Installare Node.js

--------------------------------------
Materiale di laboratorio
--------------------------------------

#. RaspberryPi 3 Model B+
#. devsDdr.html


-------------------------------------
Il motto 
-------------------------------------


:remark:`Non c'è codice senza progetto.`

:remark:`Non c'è progetto senza analisi del problema.`

:remark:`Non c'è problema senza requisiti.`


---------------------------
Il template
---------------------------
Il documento ``template2021.html`` costituisce un punto di riferimento ma è
'process agnostic', cioè non indica il processo di sviluppo che adottiamo
per costruirlo.



------------------------------------------
Passi operativi
------------------------------------------
#. Costruire un repository GIT del progetto
#. Definire un primo modello del sistema come risultato della analisi del problema (e non del progetto della soluzione)
#. Includere nel documento di analisi gli appropriati riferimenti al modello
#. Definire qualche testplan significativo (cioè legato ai casi di uso) basato sul modello
