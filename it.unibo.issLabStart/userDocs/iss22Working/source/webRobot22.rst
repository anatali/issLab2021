.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo


.. _bannerOnline: https://manytools.org/hacker-tools/ascii-banner/

========================================
webrobot22
========================================

-----------------------------------------------------------
webrobot22: startup
-----------------------------------------------------------

- Estraggo ``webRobot22.zip`` in webRobot22
- Cambio da ``7.4.1`` a ``7.4.2`` in ``webRobot22\gradle\wrapper\gradle-wrapper.properties``
- In ``build.gradle`` modifico:
  
  .. code::

      version = '1.0'
      sourceCompatibility = '11'

-  Aggiungo il file ``gradle.properties`` con il contenuto:

   .. code::

       kotlinVersion = 1.6.0

-  Inserisco ``banner.txt`` in *src\main\resources\* usando `bannerOnline`_ (*small* font)
-  In ``application.properties`` in *src\main\resources\* inserisco*

   .. code::

       spring.application.name = webRobot22
       spring.banner.location  = classpath:banner.txt
       server.port             = 8085      

- In ``build.gradle`` inserisco:
 
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
 
- Eseguo ``gradlew run`` e apro un browser su ``localhost:8085``


-----------------------------------------------------------
webrobot22: pagina
-----------------------------------------------------------

- :ref:`WebApplication con SpringBoot`
- :ref:`Configurazione con WebSocketConfigurer`
- :ref:`Trasferimento di immagini: indexAlsoImages.html`
- :ref:`Bootstrap e webJars`
- :ref:`WebSocket in SpringBoot: versione STOMP`
- :ref:`Client (in Java per programmi)`

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



-----------------------------------------------------------
Comandare il robot
-----------------------------------------------------------



Handler dispatch failed; nested exception is java.lang.NoClassDefFoundError: kotlin/jvm/internal/Intrinsics


++++++++++++++++++++++++++++++++++++
Bootstrap
++++++++++++++++++++++++++++++++++++
The Bootstrap grid system has four classes: xs (phones), sm (tablets), md (desktops), and lg (larger desktops).


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



