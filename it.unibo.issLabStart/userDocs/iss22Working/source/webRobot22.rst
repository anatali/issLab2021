.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _IPWebcam:  https://play.google.com/store/apps/details?id=nfo.webcam&hl=it&gl=US
.. _Thymeleaf: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
.. _ThymeleafSyntax: https://www.thymeleaf.org/doc/articles/standardurlsyntax.html

.. _bannerOnline: https://manytools.org/hacker-tools/ascii-banner/
.. _Bootstrap4: https://www.w3schools.com/bootstrap4/bootstrap_get_started.asp
.. _Bootstrap5: https://www.w3schools.com/bootstrap5/
.. _Grids: https://www.w3schools.com/bootstrap5/bootstrap_grid_basic.php
.. _Cards: https://www.w3schools.com/bootstrap5/bootstrap_cards.php
.. _Colors: https://getbootstrap.com/docs/4.0/utilities/colors/
.. _Spacing: https://getbootstrap.com/docs/5.0/utilities/spacing/
.. _Toasts: https://www.w3schools.com/bootstrap5/bootstrap_toast.php

.. _jsdelivr: https://www.jsdelivr.com/
.. _WebJars: https://mvnrepository.com/artifact/org.webjars
.. _WebJarsExplained: https://www.baeldung.com/maven-webjars 
.. _WebJarsDocs: https://getbootstrap.com/docs/5.1/getting-started/introduction/
.. _WebJarsExamples: https://getbootstrap.com/docs/5.1/examples/
.. _WebJarsContainer: https://getbootstrap.com/docs/5.1/layout/containers/
.. _Heart-beating: https://stomp.github.io/stomp-specification-1.2.html#Heart-beating


.. _basicrobot22Gui.html: ../../../../../webRobot22/src/main/resources/templates/basicrobot22Gui.html
.. _issSpec.css: ../../../../../webRobot22/src/main/resources/static/css/issSpec.css

 

========================================
webrobot22
========================================

#. Iniziamo il **progetto webrobot22** :ref:`webrobot22: startup`
#. Prepariamo la pagina usando `Bootstrap5`_
#. Sezione area comandi
#. Sezione WebCam
#. Connessione Ws con il server
#. Connessione TCP/CoAP con il robot (o applicazione)
#. Risposta js ai comandi
#. Azioni del controller in seguito a un comando

 
-----------------------------------------------------------
webrobot22: startup
-----------------------------------------------------------

#. Costruiamo il file ``webRobot22.zip`` in accordo a :ref:`Primi passi con SpringBoot`.
  
 
  .. image::  ./_static/img/Robot22/webRobot22Springio.PNG
    :align: center 
    :width: 50%

#. Scompattiamo il file ``webRobot22.zip``  nella nostra cartella di lavoro.
#. Modifichiamo   ``7.4.1``in ``7.4.2`` in ``webRobot22\gradle\wrapper\gradle-wrapper.properties``
#. Aggiungiamo il file ``gradle.properties`` con il contenuto:

   .. code::

       kotlinVersion = 1.6.0

#. Aggiungiamo il file ``banner.txt`` in ``src\main\resources\`` usando `bannerOnline`_ (*small* font)
#. Nel file  ``application.properties`` di ``src\main\resources\`` inseriamo:

   .. code::

       spring.application.name = webRobot22
       spring.banner.location  = classpath:banner.txt
       server.port             = 8080      


++++++++++++++++++++++++++++++++++++++
build.gradle di webRobot22 
++++++++++++++++++++++++++++++++++++++

- Aggiorniamo ``build.gradle``:
 
  .. code::

    plugins {
        ...
        id 'application'
    }     

    version = '1.0'
    sourceCompatibility = '11'

    repositories {
        mavenCentral()
        flatDir {   dirs '../unibolibs'	 }
    }
    dependencies {
        ...
        //Libreria Kotlin-runtime
        implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'

        //Per comunicazioni WebSocket NOSTOMP della pagina HTML
        implementation("org.springframework:spring-websocket:5.3.14")

        //webjars
        implementation 'org.webjars:webjars-locator-core'
        implementation 'org.webjars:bootstrap:5.1.3'
        implementation 'org.webjars:jquery:3.6.0'

        /* UNIBO ********************************** */
        implementation name: 'uniboInterfaces'
        implementation name: '2p301'
        implementation name: 'it.unibo.qakactor-2.7'
        implementation name: 'unibonoawtsupports'  //required by the old infrastructure
        implementation name: 'unibo.actor22-1.1'   //using actor22comm in ConnQakBase
    }
    mainClassName = 'unibo.webRobot22.WebRobot22Application'
    jar {
        println("executing jar")
        from sourceSets.main.allSource
        manifest {
            attributes 'Main-Class': "$mainClassName"
        }
    }
 
- Eseguo ``gradlew run`` e apro un browser su ``localhost:8080``


-----------------------------------------------------------
basicrobot22Gui.html
-----------------------------------------------------------
 
Avvalendoci di `Bootstrap5`_, impostiamo una pagina HTML (nel file `basicrobot22Gui.html`_ in ``src/main/resources/templates``) 
in modo che presenti le aree mostrate in figura:

.. image::  ./_static/img/Robot22/webRobot22GuiStructure.PNG
  :align: center 
  :width: 70%


- :ref:`ConfigurationArea`: area di input che include campi per la configurazione del sistema.
- :ref:`ConfigurationData`: area di output  che mostra i valori dei dati di configurazione fissati dall'utente.
- *infoDisplay*: area di output  che visualizza informazioni di sistema.
- *robotDisplay*: area di output  che visualizza informazioni relative al robot o al suo ambiente.
- :ref:`Ip Webcam Android<WebcamArea>`: area di output  che visualizza lo stream prodotto da un telecamera posta su Android (ad esempio `IpWebcam`_) o su PC.
  Viene introdotta per chi non abbia un robot fisico dotato di telecamera.
- :ref:`WebCam robot<WebcamArea>`: area di output che visualizza lo stream prodotto da un telecamera posta sul robot fisico.


+++++++++++++++++++++++++++++++
Uso di Bootstrap5
+++++++++++++++++++++++++++++++

Abilitiamo l'uso di `Bootstrap5`_, nella sezione ``head`` del file `basicrobot22Gui.html`_ e impostiamo la struttura 
della pagina:

.. code::

   <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
      <title>basicrobot22Gui</title>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
      <link rel="stylesheet" href="css/issSpec.css">                           <!-- stili custom -->
      <link rel="shortcut icon" href="images/mbotIot.png" type="image/x-icon"> <!-- ICONA su browser -->
    </head>

    <body>
        <div class="container-fluid pt-1 bg-primary text-white text-center">  
            <h1>basicrobot22 console</h1>
        </div>
    
        <div class="container-fluid">
            <!-- Contenuto della pagina -->
        </div>  

        <footer>
            <!-- FOOTER -->
        </footer>
    </body>

+++++++++++++++++++++++++++++++
Contenuto della pagina
+++++++++++++++++++++++++++++++

Il contenuto della pagina è strutturato in una riga (di ``12`` colonne, come indicato in `Grids`_ ) 
che contiene due colonne: la colonna di sinistra (di ampiezza ``7``) 
è riservata alla area di Input/Output, mentre la  la colonna di destra (di ampiezza ``5``)  è dedicata
alla visualizzazione degli stream di dati delle telecamere.

.. code::

    <!-- Contenuto della pagina -->
    <div class="row"> <!-- Page main row -->
        <div class="col-7">  <!-- I/O area col  -->
             <!-- CONFIGURATION Area and Data   -->
             <!-- ROBOT COMMANDS buttons        -->
             <!-- INFO display                  -->
             <!-- ROBOT display                 -->
        </div>
        <div class="col-5">  <!-- Webcam area col  -->
            <!-- IPWebcam Android  -->
            <!-- Webcam robot      -->
        </div>
    </div> <!-- Page main row -->


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Schema delle aree
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Le aree entro le colonne sono organizzate usando le  `Cards`_ secondo lo schema:

.. code::

      <div class="card BGSTYLE TEXTCOLOR">
          <div class="card-header px-1"> ... </div> <!-- px-N -->
          <div class="card-content px-1">
               <!-- CARDCONTENT -->
          </div>
      </div>

Per i colori del testo (``TEXTCOLOR``) faremo riferimento agli standard `Colors`_, mentre 
per lo stile di background (``BGSTYLE``) faremo riferimento a definizioni custom, definite nel file
`issSpec.css`_.

Per le specifiche del tipo ``px-N``, si veda `Spacing`_.


+++++++++++++++++++++++++++++++
WebcamArea
+++++++++++++++++++++++++++++++

Riportiamo la specifica della colonna relativa all'area di output che visualizza 
gli stream (``Ip Webcam Android`` e ``WebCam robot``) prodotti dalle telecamere.

Per la visualizzazione, sfrutteremo la specifica di URL *Protocol-relative* (``th:src``) di `ThymeleafSyntax`_.


.. code::

  <div class="col-5">  <!-- webcam col -->
    <div class="card iss-bg-webcamarea px-1 border">
     <div class="card-body">
      <div class="row">
         <img class="img-fluid" th:src="@{${ 'http://'+webcamip+':8080/video'} }"
            alt="androidcam" style="border-spacing: 0; border: 1px solid black;">
      </div>
       <div class="row">
         <img class="img-fluid" th:src="@{${ 'http://'+robotip+':8080/?action=stream'}}"
            alt="raspicam" style="border-spacing: 0; border: 1px solid black;">
      </div>
     </div> <!-- card body -->
     </div> <!-- card -->
   </div><!-- webcam col -->


I valori delle variabili ``webcamip`` e ``robotip`` sono definiti dai valori immessi dall'utente nella 
:ref:`ConfigurationArea` e visualizzati nella parte :ref:`ConfigurationData`.

Per queste e per le altre aree, tutte con la struttura indicata in
:ref:`Schema delle aree`, ci limiteremo ad indicare solo la parte ``CARDCONTENT``.





+++++++++++++++++++++++++++++++
ConfigurationArea
+++++++++++++++++++++++++++++++


+++++++++++++++++++++++++++++++
ConfigurationData
+++++++++++++++++++++++++++++++



+++++++++++++++++++++++++++++++
Costruzione della pagina
+++++++++++++++++++++++++++++++

- static/css/bootstrap.css
- Creo ``RobotController.java`` in *src\main\java\it\unibo\robotWeb2020*
-  Inserisco ``BasicRobotCmdGui.html`` in *src\main\resources\templates*
-  Aggiungo ``wsminimal.js`` in resources   
- static/vendors

- Robots/common/IWsHandler e WebSocketConfiguration
- preprazione della pagina
- definizione delle azioni
- wsminimal.js


- :ref:`WebApplication con SpringBoot`
- :ref:`Configurazione con WebSocketConfigurer`
- :ref:`Trasferimento di immagini: indexAlsoImages.html`
- :ref:`Bootstrap e webJars`
- :ref:`WebSocket in SpringBoot: versione STOMP`
- :ref:`Client (in Java per programmi)`



+++++++++++++++++++++++++++++++
Interazione pagina-server
+++++++++++++++++++++++++++++++

- cmdpageutils.js : callServerUsingAjax fa una POST con answer

-----------------------------------------------------------
Comandare il robot
-----------------------------------------------------------



Handler dispatch failed; nested exception is java.lang.NoClassDefFoundError: kotlin/jvm/internal/Intrinsics


++++++++++++++++++++++++++++++++++++
Bootstrap
++++++++++++++++++++++++++++++++++++

- `Bootstrap4`_ was released in 2018
- `Bootstrap5`_ has switched to JavaScript instead of jQuery.
- W3.CSS is an excellent alternative to Bootstrap 5.
- ``jsDelivr`` provides CDN support for Bootstrap's CSS and JavaScript:

 .. code::

    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

- There are two container classes to choose from: ``.container`` (fixed width)  ``.container-fluid``
- ``.container-sm|md|lg|xl`` classes to determine when the container should be responsive
- By default, containers have left and right padding, with no top or bottom padding.


- The Bootstrap `Grids`_ system has four classes: xs (phones), sm (tablets), md (desktops), and lg (larger desktops).
- Bootstrap's `Grids`_ system is built with flexbox and allows up to 12 columns across the page.
- The Bootstrap 5 `Grids`_ system has six classes:

    - ``.col-`` (extra small devices - screen width less than 576px)
    - ``.col-sm-`` (small devices - screen width equal to or greater than 576px)
    - ``.col-md-`` (medium devices - screen width equal to or greater than 768px)
    - ``.col-lg-`` (large devices - screen width equal to or greater than 992px)
    - ``.col-xl-`` (xlarge devices - screen width equal to or greater than 1200px)
    - ``.col-xxl-`` (xxlarge devices - screen width equal to or greater than 1400px)

- `Cards`_: bordered box with some padding around its content. 
  It includes options for headers, footers, content, colors, etc.

- Responsive images automatically adjust to fit the size of the screen.
  ``img-fluid`` class applies max-width: 100%; and height: auto; to the image.  
  The image will then scale nicely to the parent element.
  
++++++++++++++++++++++++++++++++++++
Card with webcam
++++++++++++++++++++++++++++++++++++

- Open Windows Settings and choose Devices
- Click the Windows Start Menu Button.
- Click Camera
- ipwecab e SimpleMjpegView

 .. code::
     
    <script>
    function myFunction() {
    window.open("https://www.w3schools.com");
    }
    </script>


-----------------------------------------------------------
Enable SpringBoot live DevTools
-----------------------------------------------------------
settings(ctrl +alt+s) -> Build,Execution,Deployment -> compiler, check "Build project automatically"
Enable option 'allow auto-make to start even if developed application is currently running' in 
Settings -> Advanced Settings under compiler



.. image::  ./_static/img/Robot22/webRobot22GuiAnnot.PNG
  :align: center 
  :width: 100%