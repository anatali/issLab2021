.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo


.. _bannerOnline: https://manytools.org/hacker-tools/ascii-banner/

========================================
webrobot22
========================================

- Estraggo ``webRobot22.zip`` in webRobot22
- Cambio da ``7.4.1`` a ``7.4.2`` in ``webRobot22\gradle\wrapper\gradle-wrapper.properties``
- gradlew build
- In ``build.gradle`` modifico:
  
  .. code::

      version = '1.0'
      sourceCompatibility = '11'

-  Aggiungo il file gradle.properties con il contenuto:

   .. code::

       kotlinVersion = 1.6.0

-  Inserisco ``BasicRobotCmdGui.html`` in *src\main\resources\templates*
-  Aggiungo ``wsminimal.js`` in resources   
-  Inserisco ``banner.txt`` in *src\main\resources\* usando `bannerOnline`_ (*small* font)
-  In ``application.properties`` in *src\main\resources\* inserisco*

   .. code::

       spring.application.name=webRobot22
       spring.banner.location=classpath:banner.txt
       server.port   = 8085  
       
- Creo ``RobotController.java`` in *src\main\java\it\unibo\robotWeb2020*

- In ``build.gradle`` inserisco:
- 
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
        /* UNIBO ********************************** */
        implementation name: 'uniboInterfaces'
        implementation name: '2p301'
        implementation name: 'it.unibo.qakactor-2.7'
        implementation name: 'unibonoawtsupports'  //required by the old infrastructure
        implementation name: 'unibo.actor22-1.1'
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
Enable SpringBoot live DevTools
-----------------------------------------------------------
settings(ctrl +alt+s) -> Build,Execution,Deployment -> compiler, check "Build project automatically"
Enable option 'allow auto-make to start even if developed application is currently running' in Settings -> Advanced Settings under compiler