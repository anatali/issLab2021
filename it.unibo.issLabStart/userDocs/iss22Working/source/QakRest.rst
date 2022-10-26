.. role:: red 
.. role:: blue 
.. role:: brown 
.. role:: remark
.. role:: worktodo  

=============================
QakRest
=============================

Obiettivo: costruire una facade REST per un sistema Qak.

Usiamo https://start.spring.io/ 

 

.. image:: ./_static/img/QakRest/QakRestInit.png 
    :align: center
    :width: 80%  


Costruisco una Spring REST app che crea l'applicazione e fornisce anche un HIControl



++++++++++++++++++++++++++++++++++++++++++
QakRest - build.gradle
++++++++++++++++++++++++++++++++++++++++++

.. code::
    
    repositories {
        mavenCentral()
        flatDir {  
        dirs 'C:/Didattica2021/privato/userxyz-/QakRest/unibolibs'
        }	  
    }
    dependencies {
        ...
        //CUSTOM
        implementation name: 'uniboInterfaces'
        implementation name: '2p301'
        implementation name: 'unibo.qakactor22-3.2'
    }

++++++++++++++++++++++++++++++++++++++++++
QakRest - application.properties
++++++++++++++++++++++++++++++++++++++++++

.. code::

   server.port = 8085
   spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
   management.endpoints.web.exposure.include=*


++++++++++++++++++++++++++++++++++++++++++
QakRest - project
++++++++++++++++++++++++++++++++++++++++++

.. code::
    
    1) interface  QakApi
    2) QakSystem implements QakApi  (busimess logic of the Facade)
    3) interface QakService e  QakHIService
    4) M2MController implements QakService
    5) HIController implements QakHIService

++++++++++++++++++++++++++++++++++++++++++
QakRest - usage
++++++++++++++++++++++++++++++++++++++++++

.. code::

    http://localhost:8085/swagger-ui/index.html
