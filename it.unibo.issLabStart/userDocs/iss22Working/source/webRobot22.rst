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
- In gradle.build modifico:
  
  .. code::

      version = '1.0'
      sourceCompatibility = '17'

-  Inserisco ``robotGuiPost.html`` in *src\main\resources\templates*
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

    mainClassName = 'unibo.webRobot22.WebRobot22Application'

    jar {
        println("executing jar")
        from sourceSets.main.allSource
        manifest {
            attributes 'Main-Class': "$mainClassName"
        }
    }
- Eseguo ``gradlew run`` e apro un browser su ``localhost:8085``