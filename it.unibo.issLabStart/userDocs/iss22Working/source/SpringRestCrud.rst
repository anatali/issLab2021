.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=======================================
SpringRestCrud
=======================================

HATEOAS sta per Hypermedia as the Engine of Application State.

--------------------------------
HAL 
--------------------------------

- HAL fornisce un formato coerente  per il collegamento ipertestuale tra le risorse.
- I browser HAL sono applicazioni basate sulla specifica HAL per la gestione dei dati HAL + JSON
- Rest Repositories crea dinamicamente gli endpoint URL per le risorse REST correlate agli oggetti nell'applicazione.
- https://www.youtube.com/playlist?list=PL9l1zUfnZkZmcVtnrtCJLnoeKwWE6oylK   (SpringBoot complete tutorial)
- https://www.baeldung.com/java-in-memory-databases
- https://www.baeldung.com/spring-boot-h2-database

- https://www.youtube.com/watch?v=m7YBEj-9MHc

- Con HAL Explorer si possono esplorare le API RESTful Hypermedia basate su HAL e HAL-FORMS.  


.. image:: ./_static/img/Spring/SpringRestH2.png 
   :align: center
   :width: 90%

+++++++++++++++++++++++++++++++++++
build.gradle - dipendenze
+++++++++++++++++++++++++++++++++++
       
.. code::

    ...
    dependencies {
         implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
         implementation 'org.springframework.boot:spring-boot-starter-data-rest'
         implementation 'org.springframework.boot:spring-boot-starter-hateoas'
         implementation 'org.springframework.boot:spring-boot-starter-web'
         implementation 'org.springframework.data:spring-data-rest-hal-explorer'
         compileOnly 'org.projectlombok:lombok'
         runtimeOnly 'com.h2database:h2'
         annotationProcessor 'org.projectlombok:lombok'
         testImplementation 'org.springframework.boot:spring-boot-starter-test'
    }

+++++++++++++++++++++++++++++++++++
SpringRestH2 Workspace
+++++++++++++++++++++++++++++++++++

.. list-table:: 
  :widths: 50,50
  :width: 100%

  * - 
     .. image:: ./_static/img/Spring/SpringRestH2Workspace.png 
         :align: center
         :width: 70%
    - application.properties
       .. code::

        spring.h2.console.enabled=true
        spring.datasource.platform=h2
        spring.datasource.url=jdbc:h2:mem:haldb
        spring.jpa.hibernate.ddl-auto=update    

+++++++++++++++++++++++++++++++++++
browser windows
+++++++++++++++++++++++++++++++++++

%%%%%%%%%%%%%%%%%%%%%%%%%%%
HAL Browser
%%%%%%%%%%%%%%%%%%%%%%%%%%%

     .. image:: ./_static/img/Spring/SpringRestH2HAlExplorer.png 
         :align: center
         :width: 100%




%%%%%%%%%%%%%%%%%%%%%%%%%%%
H2 console
%%%%%%%%%%%%%%%%%%%%%%%%%%%
.. list-table:: 
  :widths: 40,60
  :width: 100%

  * - H2 Console Login

      .. image:: ./_static/img/Spring/SpringRestH2h2consoleInit.png 
         :align: center
         :width: 100%
    - H2 Console
      
      .. image:: ./_static/img/Spring/SpringRestH2h2console.png 
         :align: center
         :width: 100%

++++++++++++++++++++++++++++++++++++++++++++++++
Popoliamo il database usando la H2 console
++++++++++++++++++++++++++++++++++++++++++++++++      

.. code::

    INSERT INTO CATEGORY VALUES(1, 'glass', 'glass', 'glass')
    INSERT INTO CATEGORY VALUES(2, 'plastic', 'plastic', 'plastic')

    INSERT INTO PRODUCT VALUES(1,'001', 'cup', '', 'cup',85.0,'cup',1)
    INSERT INTO PRODUCT VALUES(2,'002', 'box', '', 'box',21.0,'box',2)


.. image:: ./_static/img/Spring/SpringRestH2Products.png 
    :align: center
    :width: 100%

.. code::

    curl localhost:8080/products 
    curl localhost:8080/categories

Stessa risposta  mostrata dalla H2 Console nel campo :blue:`Response Body`.


+++++++++++++++++++++++++++++++++++
Popoliamo il database usando POST
+++++++++++++++++++++++++++++++++++
 
 .. list-table:: 
  :widths: 80,20
  :width: 100%

  * - H2 Console Login

      .. image:: ./_static/img/Spring/SpringRestH2CategoryPOST.png 
         :align: center
         :width: 100%
    - Incrementa in modo automatico l'id

 

.. code::

    {
    "name": "hardware",
    "description": "hardware",
    "title": "hardware"
    }


+++++++++++++++++++++++++++++++++++
Modifichiamo usando PUT
+++++++++++++++++++++++++++++++++++

.. image:: ./_static/img/Spring/SpringRestH2ProductPUT.png 
    :align: center
    :width: 100%

.. code::

  "name": "cup",
  "code": "001",
  "title": "cup",
  "description": "cup",
  "imgUrl": "",
  "price": 63

+++++++++++++++++++++++++++++++++++
Modifichiamo usando PATCH
+++++++++++++++++++++++++++++++++++

.. code::

  curl -X PATCH -H "Content-Type: application/json" -d "{\"title\" : \"Glass\"}" localhost:8080/categories/1
  curl -X PATCH -H "Content-Type: application/json" -d "{\"price\": 11}"} localhost:8080/products/1