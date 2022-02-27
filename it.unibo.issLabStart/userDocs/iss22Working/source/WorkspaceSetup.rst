.. role:: red
.. role:: blue  
.. role:: remark   

.. _rst editor: http://rst.ninjs.org/

.. _gradle: https://gradle.org/ 
.. _IntelliJ: https://www.jetbrains.com/idea/
.. _template2022: _static/templateToFill.html
.. _Eclipse IDE for Java and DSL Developers: https://www.eclipse.org/downloads/packages/release/juno/sr2/eclipse-ide-java-and-dsl-developers
.. _Basic Git commands: https://confluence.atlassian.com/bitbucketserver/basic-git-commands-776639767.html
.. _Video on GIT: https://www.youtube.com/watch?v=HVsySz-h9r4
.. _github: https://github.com/

======================================
WorkspaceSetup
======================================    

- :ref:`Creazione progetto con Gradle`
- :ref:`Uso di GIT`
 

.. _it.unibo.radarSystem22:

----------------------------------
Creazione progetto con Gradle
----------------------------------

Iniziamo creando un progetto con l'aiuto di `Gradle`_

#. Apro sul PC una directory di lavoro (ad esempio **issLab2022**) e mi posiziono in essa.
#. Creo una directory per il progetto di nome :blue:`it.unibo.radarSystem22` e mi posiziono in essa.
#. Eseguo il comando
  
   .. code::

      gradle init (select 2, 3, 1, 2, 1, -, -)

#. Eseguo il codice generato:

    .. code::

      gradlew build -x test  //esclude i test
      gradlew run

    L'output dovrebbe essere:

   .. code::

        > Task :app:run
        Hello World!
        BUILD SUCCESSFUL in 2s
        2 actionable tasks: 1 executed, 1 up-to-date

#. Apro il progetto in `IntelliJ`_, noto le directories ``main`` e ``test`` 
   
   .. image:: ./_static/img/Intro/projectntelliJ.PNG 
      :align: center
      :width: 60%

#. Osservo il contenuto di 
   ``app/build.gradle.kts``:

    .. code::

        plugins {
            // Apply the application plugin 
            //to add support for building a CLI application in Java.
            application
        }
        repositories {
            // Use JCenter for resolving dependencies.
            jcenter()
        }
        dependencies {
            // Use JUnit test framework.
            testImplementation("junit:junit:4.13")

            // This dependency is used by the application.
            implementation("com.google.guava:guava:29.0-jre")
        }
        application {
            // Define the main class for the application.
            mainClass.set("it.unibo.radarSystem22.App")
        }





#. Eseguo (posso usare il Terminal di `IntelliJ`) i test:

   .. code::

        gradlew build  	//does also the testing
        gradlew test	//does only the tests

#. Osservo ``app/build/reports/tests/test/index.html``


----------------------------------
Uso di GIT
----------------------------------

Per un aiuto ad usare GIT può essere utile consultare `Basic Git commands`_
e/o guardare il video `Video on GIT`_ di cui  riportiamo i tempi di alcuni punti salienti:

.. code::

    0:00  - Introduction
    1:31  - Distributed vs Central Version Control
    3:17  - Installing Git
    3:39  - First Time Setup
    6:36  - Getting Started (Local repository)
    10:41 - Git File Control
    14:55 - Getting Started (Remote repository)
    20:37 - Branching
    20:50 - Common Workflow
    23:03 - Push Branch on remote
    27:38 - Faster Example
    29:41 - Conclusion


Per quanto riguarda il nostro progetto:

#. Mi posiziono sulla directory di lavoro  ``issLab2022``.
#. Eseguo:
   
   .. code::

       git init  //creates the directory .git	
       git status

#. Osservo il contenuto del file generato ``.gitignore`` con il comando:

   .. code::
 
      git status --ignored	//see ignored files 

   I files elencati non saranno salvati sul repository.
#. Eseguo i comandi     
   .. code::
 
       git add -A
       git commit -m "progetto iniziale"
       git log
       git status

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Creazione di un repository remoto   
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

#. Supponendo di avere accesso su `github`_ come user di nome ``userxyz``, creiamo un repository personale di nome ``iss2022``, 
   selezionando il tipo **public**, con *README* file e   **Add .gitignore** (*template Java*). Quindi aggiungiamo
   il nostro progetto al repo:

    .. code::

        git remote add origin https://github.com/userxyz/iss2022 
        git remote -v   //osservo

#. Posizionato sulla directory ``issLab2022``, salvo il progetto corrente sul repository remoto.
   
    .. code::

       git push origin master


----------------------------------
Verso l'applicazione
----------------------------------
All'interno del progetto `it.unibo.radarSystem22`_:

#. Seleziono :blue:`versione 1.8` del **compilatore Java** e della  **jre Java**
#. Creo directory ``userDocs`` 
#. Inserisco in ``userDocs`` copia del file  `template2022`_ con mia foto, ridenominandolo `radarSytem22.html` 
   e copio i requisiti dati dal committente

-----------------------------------
TODO
-----------------------------------
#. Accedo a https://github.com/ e creo sito GIT con nome  ``myNameIss2``
#. Apro sul PC la mia directory di lavoro (ad esempio ``issLab2022``) e mi posiziono in essa
#. Clono il sito GIT  
#. Apro `Eclipse IDE for Java and DSL Developers`_ (2021 06)
#. Scelgo ``issLab2022`` come workspace
#. Creo progetto di nome `it.unibo.radarSystem22`
#. Creo un file di nome build.gradle con il seguente contenuto

   .. code::

     plugins {
      id 'java'
      id 'eclipse'
      id 'application'
    }

    version '1.0'
    sourceCompatibility = 1.8
    repositories {
        mavenCentral()
        jcenter()
        flatDir {   dirs '../unibolibs'	 }
    }

    dependencies {
        testCompile group: 'junit', name: 'junit', version: '4.12'
    //KOTLIN
        implementation group: 'org.jetbrains.kotlin', name: 'kotlin-stdlib-jdk8', version: '1.6.10'
    //MQTT
    // https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
      implementation group: 'org.eclipse.paho', name: 'org.eclipse.paho.client.mqttv3', version: '1.2.5'
    //JSON
        // https://mvnrepository.com/artifact/org.json/json
        compile group: 'org.json', name: 'json', version: '20160810'
    //COAP
      // https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
      compile group: 'org.eclipse.californium', name: 'californium-core', version: '2.0.0-M12'
      // https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
      compile group: 'org.eclipse.californium', name: 'californium-proxy', version: '2.0.0-M12'
    //LOG4j	//required by Californium
      //compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.25' 
      ext['log4j2.version'] = '2.17.0'
    //CUSTOM
        compile name: '2p301'
        compile name: 'uniboInterfaces'
        compile name: 'unibonoawtsupports'
      compile name: 'it.unibo.qakactor-2.5'
      
      //RADAR (support and GUI)
      compile name: 'radarPojo'
      // https://mvnrepository.com/artifact/org.pushingpixels/trident
      compile group: 'org.pushingpixels', name: 'trident', version: '1.3'
    //IMAGES	
      implementation group: 'commons-io', name: 'commons-io', version: '2.11.0'
      implementation group: 'org.apache.httpcomponents', name: 'httpmime', version: '4.5.13'


    //COROUTINE
        compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core-common', version: '1.1.0'
        compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core', version: '1.1.0'
      
    }


    sourceSets {
        main.java.srcDirs += 'src'
        main.java.srcDirs += 'resources'
        test.java.srcDirs += 'test'		//test is specific
    }

    //Avoid duplication of src 
    eclipse {
        classpath {
            sourceSets -= [sourceSets.main, sourceSets.test]	
        }		
    }
    

    mainClassName = 'it.unibo.enablerCleanArch.main.AllMainOnRasp'

    jar {
        println("building jar ...")
        from sourceSets.main.allSource	
        manifest {
            attributes 'Main-Class': "$mainClassName"
        }
    }

    /*
    distZip {
        into(project.name) {
            from '.'
            include 'config/RadarSystemConfig.json'
        }
    } 
    */
    distributions {
        main {
            //distributionBaseName = 'xxx'
            contents {
                from './RadarSystemConfig.json'
            }
        }
    }


