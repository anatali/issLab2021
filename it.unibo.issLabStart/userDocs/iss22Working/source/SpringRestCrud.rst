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
- https://start.spring.io/
- https://www.youtube.com/playlist?list=PL9l1zUfnZkZmcVtnrtCJLnoeKwWE6oylK   (SpringBoot complete tutorial)
- https://www.baeldung.com/java-in-memory-databases
- https://www.baeldung.com/spring-boot-h2-database
- http://www.h2database.com/html/cheatSheet.html
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
              oppure jdbc:h2:file:./data/sample
        spring.jpa.hibernate.ddl-auto=update    

+++++++++++++++++++++++++++++++
Eseguiamo l'applicazione
+++++++++++++++++++++++++++++++

.. code::

    gradlew bootrun

Una volta attivata l'applicazione Spring che gestisce il database H2 in memoria o su file,
possiamo 

- utilizzare la :ref:`H2 console` per agire direttamente sul database attraverso comandi SQL.
- utilizzare lo :ref:`HAL Browser` per attivare gli endpoint REST utilizzando i verbi HTTP nel modo che segue:

  - :blue:`GET` per richidere informazioni
  - :blue:`POST`: per inserire nuovi elementi nel database
  - :blue:`PUT`: per modificare in modo completo un elemento 
  - :blue:`PATCH`: per modificare in modo parziale un elemento 
  - :blue:`DELETE`: per eliminare un elemento 

- effettuare richieste HTTP attraverso l' :ref:`Uso di curl`
- effettuare richieste HTTP attraveso l' :ref:`Uso di Java`, Python, etc.

++++++++++++++++++++++++
H2 console
++++++++++++++++++++++++

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

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Popoliamo il database usando la H2 console
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  

.. code::

    INSERT INTO CATEGORY VALUES(1, 'glass', 'glass', 'glass')
    INSERT INTO CATEGORY VALUES(2, 'plastic', 'plastic', 'plastic')

    INSERT INTO PRODUCT VALUES(1,'001', 'cup', '', 'cup',85.0,'cup',1)
    INSERT INTO PRODUCT VALUES(2,'002', 'box', '', 'box',21.0,'box',2)


+++++++++++++++++++++++++
HAL Browser
+++++++++++++++++++++++++

.. list-table:: 
  :widths: 50,50
  :width: 100%

  * - http://localhost:8080/

      .. image:: ./_static/img/Spring/SpringRestH2HAlExplorer.png 
         :align: center
         :width: 100%
    - click su :blue:`<` di **products**
      
      .. image:: ./_static/img/Spring/SpringRestH2Products.png 
        :align: center
        :width: 100%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
HAL Browser POST 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Click su :blue:`+` 

 .. list-table:: 
  :widths: 60,40
  :width: 100%

  * - H2 Console Login

      .. image:: ./_static/img/Spring/SpringRestH2CategoryPOST.png 
         :align: center
         :width: 100%
    
    - Crea una nuova categoria

      .. code::

        {
        "name": "food",
        "description": "food",
        "title": "food"
        }
    
      Incrementa in modo automatico l'id

Crea un nuovo prodotto:

.. code::

    {
    "category": "category/1"
    "code": "003",
    "price": "75",
    "name": "new cup",
    "description": "cup of glass",
    "title": "new cup",
    }




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
HAL Browser PUT
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Click su :blue:`>` (a sinistra). I dati devono essere forniti in modo completo


.. code::

    {
    "category": "category/1"
    "code": "003",
    "price": "65",
    "name": "new cup ",
    "description": "cup of glass",
    "title": "new cup updated",
    }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
HAL Browser PATCH
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Click su :blue:`>` (a destra). I dati possono essere forniti in modo parziale. Ad esempio, con riferimento 
a product/2

.. code::

    {
     "price": "60",
     "title": "new cup discounted",
    }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
HAL Browser DELETE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Click su :blue:`x` .

+++++++++++++++++++++++++++++++++++
Uso di curl
+++++++++++++++++++++++++++++++++++

%%%%%%%%%%%%
curl GET
%%%%%%%%%%%%

.. code::

    curl localhost:8080/products 
    curl localhost:8080/categories

Stessa risposta  mostrata dalla :ref:`HAL Browser` nel campo :blue:`Response Body`.

%%%%%%%%%%%%
curl POST
%%%%%%%%%%%%

%%%%%%%%%%%%
curl PUT
%%%%%%%%%%%%

%%%%%%%%%%%%
curl PATCH
%%%%%%%%%%%%
.. code::

  curl -X PATCH -H "Content-Type: application/json" -d "{\"title\" : \"Glass\"}" localhost:8080/categories/1
  curl -X PATCH -H "Content-Type: application/json" -d "{\"price\": 11}"} localhost:8080/products/1


%%%%%%%%%%%%
curl DELETE
%%%%%%%%%%%%

+++++++++++++++++++++++++++++++++++
Uso di Java
+++++++++++++++++++++++++++++++++++

In Java ci possiamo avvalere della libreria OKHTTP (https://www.baeldung.com/guide-to-okhttp).

Aggiungiamo la dipendenza in build.gradle:

.. code::

    implementation 'com.squareup.okhttp:okhttp:2.7.5'




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Java POST
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
 




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Java PUT
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Java PATCH
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


.. code::

    http://localhost:8080/swagger-ui.html
