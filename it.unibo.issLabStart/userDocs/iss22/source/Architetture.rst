.. contents:: Overview
   :depth: 4
.. role:: red 
.. role:: blue 
.. role:: remark

.. `` 



======================================
Architetture
======================================


.. csv-table::  
    :align: left
    :widths: 60, 40
    :width: 100%

    "Nel testo  (``FSA - ISBN-10 : 1492043451``)  vengono presentati diversi tipi di Architetture Sosftware.",.. image:: ./_static/img/Architectures/softwareArch.webp
    "Nel testo  (``CA  - ISBN-10 : 0134494164``)  approfondisce il ruolo delle architetture nel processo di produzione del software.",.. image:: ./_static/img/Architectures/cleanArchBook.jpg
  
Da questi testi leggiamo:

- Architecture is about the important stuff ... whatever that is (Ralph Johson) (FSA pg. 1)

- Everything in software architecture is a trade-off. (**First law** of Software Architecture) (FSA pg. 18)

- :blue:`Why` is more important than :blue:`how`. (**Second law** of Software Architecture) (FSA pg. 18)

- The architecture for a software system arises from the combinations of requriments and additional concerns 
  (Auditability, Performance, Security, Legality, Scalability ...) each protected by fitness functions 

- All architecture become iterative becuase of :blue:`unknown of unknowns` (FSA pg. 14). 
  What we need today is an :blue:`Evolutionary Architecture`. (FSA pg. 15).

- An *Evolutionary Architecture* supports guided, incremental changes across multiple dimensions including -ilities
  (si veda `List of system quality attributes <https://en.wikipedia.org/wiki/List_of_system_quality_attributes>`_)


Altrove (ad esempio in *Building Evolutionary Architectures* - ``ISBN : 9781492043454`` ) leggiamo:

- The software development ecosystem (tools, frameworks, prectices, ..) forms a dynamic equilibrium - much like a biological system.

- The *Continuos Delivery* and *DevOps* movements added a new factor in the dynamic equilibrium.  

- To build evolvable software systems, architets must think :blue:`beyond just the technical` architecture.

--------------------------------------
Architetture preliminari
-------------------------------------- 


.. csv-table::  
    :align: left
    :widths: 50, 50
    :width: 100%

    :blue:`bigballofmud`,:blue:`clientServer`
    .. image:: ./_static/img/Architectures/bigballofmud.png,.. image:: ./_static/img/Architectures/clientServer3Tier.png
    "obbrobrio (122/52)","3 tier (124)"

--------------------------------------
Architetture monolitiche
-------------------------------------- 

.. csv-table::  
    :align: left
    :widths: 33, 33, 33
    :width: 100%

    :blue:`Layers`,:blue:`Pipeline`, :blue:`microkernel`
    .. image:: ./_static/img/Architectures/layered.png , .. image:: ./_static/img/Architectures/pipline.png, .. image:: ./_static/img/Architectures/microkernel.png
    "(145)","(151/58)"


--------------------------------------
Architetture distribuite
-------------------------------------- 

.. csv-table::  
    :align: left
    :widths: 33, 33, 33
    :width: 100%

    :blue:`service-based`, :blue:`Event-driven`, :blue:`Space-based`
    .. image:: ./_static/img/Architectures/service-based.jpg, .. image:: ./_static/img/Architectures/event-driven-broker.png, .. image:: ./_static/img/Architectures/Space_based_architecture.gif
    "(163)","(179/70)","211/63"



.. csv-table::  
    :align: left
    :widths: 50, 50
    :width: 100%

    :blue:`Service-oriented`, :blue:`Microservices` 
    .. image:: ./_static/img/Architectures/soa-esb.png, .. image:: ./_static/img/Architectures//microservices.png
    "(235/65)","(245/68)" 



A queste aggiungiamo le seguenti:
    
--------------------------------------
Architetture per applicazioni
-------------------------------------- 

.. csv-table::  
    :align: left
    :widths: 33, 33, 33
    :width: 100%

    :blue:`Hexagonal`, :blue:`Subsumption`,:blue:`IOT`
    .. image:: ./_static/img/Architectures/hexArchitectureInKotlin.svg, .. image:: ./_static/img/Architectures/Subsumption_ArchitectureRobotic.gif, .. image:: ./_static/img/Architectures/iot0.png
    " "," ",""



--------------------------------------
L'Architettura Esagonale (Port-Adapter)
-------------------------------------- 
Il concetto di `Hexagonal Architecture <https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)>`_ 
è stato introdotto nel 2005 da Alistair Cockburn come un contributo
ad evitare ben note 'trappole strutturali' nella progettazione ad oggetti, come ad esempio
dipendenze non desiderate tra layers o indebite inserzioni di codice applicativo nel codice
delle interfacce-utente.

Si tratta di un approccio alternativo alle tradizionali architetture 'a livelli' e, a detta di molti,
segna anche l'origine delle *architetture a microservizi*. In sintesi, questo approccio:

- Rende un'applicazione inconsapevole (e quindi indipendente) della natura dei dispositivi di ingresso.
  Un evento giunge dal mondo esterno tramite una porta e un opportuno adapter converte l'evento in una
  chiamata di procedura o in un messaggio verso l'applicazione.

- Rende un'applicazione inconsapevole (e quindi indipendente) della natura dei dispositivi di uscita: 
  quando essa deve emettere informazione
  verso il mondo esterno, utilizza una porta associata ad un adapter che crea gli opportuni segnali
  necessari per la specifica tencologia ricevente (umana o automatizzata).

.. image:: ./_static/img/Architectures/portAdapterArch.png
   :width: 70% 

- Un'applicazione ha una interazione con gli adapter legata alla sua propria semantica interna e 
  non deve conoscere la natura di ciò che compare al di là degli adapter. 

- :remark:`Permette di sviluppare e testare un'applicazione in modo indipendente da eventuali dispositivi run-time
  e/o database e di essere egualmente attivabile da utenti umani, programmi, test automatizzati o scripts.`

.. image:: ./_static/img/Architectures/hexlinear.PNG
   :width: 80%  

 

--------------------------------------
La Clean Architecture
-------------------------------------- 



Proposta nel 2012 da `Robert C. Martin <https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html>`_
specializza l'archiotettura esagonale fornendo ulteriori dettagli sui componenti, che sono presentati
in anelli concentrici.

.. image:: ./_static/img/Architectures/cleanArch.jpg
   :width: 70% 

Gli adapter e le interfacce sono relegate negli anelli più esterni, mentre le parti centrali sono riservate
alle entità e ai casi d'uso.

Viene imposto il vincolo che sono sempre gli anelli esterni a dover dipendere da quelli interni e mai viceversa,
evocando quindi il principio della `inversione delle dipendenze <https://en.wikipedia.org/wiki/Dependency_inversion_principle>`_
che stabilisce quanto segue:

- I componenti software di alto livello :red:`non devono mai dipendere` da componenti di livello più basso.
- :remark:`Le astrazioni non devono dipendere dai dettagli`.
  Sono i dettagli (ad esempio le implementazioni concrete) che devono dipendere dalle astrazioni.


+++++++++++++++++++++++++++++++++++++ 
I principi SOLID
+++++++++++++++++++++++++++++++++++++

Questo tipo di architettura vuole anche promuovere i principi 
`SOLID <https://it.wikipedia.org/wiki/SOLID>`_ per la progettazione/costruzione pulita del software.

Si veda `Clean Architecture by Uncle Bob: Summary and review <https://clevercoder.net/2018/09/08/clean-architecture-summary-review>`_.  