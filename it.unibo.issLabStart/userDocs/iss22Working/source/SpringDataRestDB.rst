.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=======================================
SpringDataRestDB
=======================================


 


-------------------------------------
Progetto SpringDataRestDB - database
-------------------------------------

Progetto: :remark:`issLab2021\SpringDataRestDB`

Introduce un database H2 che memorizza dati relativi alla entià di Dominio Person definita da una classe
Java, che funge da modello.

++++++++++++++++++++++++++++++++++++++++++
SpringDataRestDB - dipendenze per il db
++++++++++++++++++++++++++++++++++++++++++

Il progetto inizia con le seguenti dipendenze:

.. code:: 

   dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    runtimeOnly 'com.h2database:h2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
   }

+++++++++++++++++++++++++++
Entity Person
+++++++++++++++++++++++++++

.. code:: Java

    @Entity  
    //@Table(name="PERSONA")
        public class Person {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private long id;
            private String firstName;
            private String lastName;
            public String getFirstName() { return firstName; }
            public void setFirstName(String firstName) { this.firstName = firstName; }
            public String getLastName() {return lastName; }
            public void setLastName(String lastName) { this.lastName = lastName; }
        }

- La annotazione @Entity denota una entità in JPA (*Java Persistence API*).
- Le entità in JPA sono POJO che rappresentano dati che possono essere mantenuti nel database. 
- Un'entità rappresenta una tabella nel database. Ogni istanza di un'entità rappresenta una riga nella tabella.
- Se non utilizziamo l annotazione :blue:`@Table`, il nome della tabella sarà il nome dell'entità.
- Una 'entità deve avere un costruttore no-arg e una chiave primaria. L'annotazione :blue:`@Id` definisce la chiave primaria.
- Poiché varie implementazioni JPA proveranno a creare sottoclassi dellla nostra entità per fornire la loro funzionalità, 
  le classi di entità **non** devono essere dichiarate **final**.

- I metodi getter e setter possono essere omessi utilizzando lombok.

Per altre informazioni, si veda: https://www.baeldung.com/jpa-entities.

+++++++++++++++++++++++++++
PersonRepository
+++++++++++++++++++++++++++

Spring Data REST si basa sul progetto Spring Data e semplifica la creazione di servizi Web REST basati 
su ipermedia che si connettono ai repository di Spring Data, 
il tutto utilizzando :blue:`HAL` (*JSON Hypertext Application Language*) come tipo di ipermedia
(si veda https://www.baeldung.com/spring-rest-hal).

La interfaccia  *PagingAndSortingRepository* permette di  specificare che vogliamo ottenere i dati dalla nostra 
:ref:`Entity Person`.

.. code:: Java

    @RepositoryRestResource(collectionResourceRel = "people", path = "people")
    public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

        //Nuova operazione che fornisce l'elenco di Person  che hanno il lastName specificato
        List<Person> findByLastName(@Param("name") String name);
    }

L'annotazione *@RepositoryRestResource* è facoltativa e viene utilizzata per personalizzare l'endpoint REST.
Nel caso specifico, si intende usare **/people** invece del valore di default */persons*.

In fase di esecuzione, Spring Data REST crea automaticamente un'implementazione di questa interfaccia. 
Quindi usa l'annotazione @RepositoryRestResource per dirigere Spring MVC per creare endpoint RESTful.

Spring Boot avvia automaticamente Spring Data JPA per creare un'implementazione concreta di *PersonRepository*
e configurarlo per comunicare con un back end in-memory database utilizzando JPA.

Spring Data REST si basa su Spring MVC. Crea una raccolta di controller Spring MVC, 
convertitori JSON e altri bean per fornire un front-end RESTful. 
Questi componenti si collegano al backend Spring Data JPA. 



++++++++++++++++++++++++++++++++++++++
SpringDataRest wirh db- esecuzione
++++++++++++++++++++++++++++++++++++++

Eseguiamo l'applicazione con il comando:

.. code::

    gradlew bootrun

Una volta attivata l'applicazione Spring che gestisce il database H2 in memoria o su file,
possiamo attivare gli endpoint REST in molti modi diversi, avendo cura di 
di utilizzando i verbi HTTP nel modo che segue:

  - :blue:`GET` per richidere informazioni
  - :blue:`POST`: per inserire nuovi elementi nel database
  - :blue:`PUT`: per modificare in modo completo un elemento 
  - :blue:`PATCH`: per modificare in modo parziale un elemento 
  - :blue:`DELETE`: per eliminare un elemento 
  
Tra i diversi modi di accesso con richieste HTTP, ricordiamo:  

- :ref:`Accesso mediante browser`
- :ref:`Accesso mediante H2 console` per agire direttamente sul database attraverso comandi SQL.
- :ref:`Accesso mediante HAL browser`  
- :ref:`Accesso mediante curl`
- :ref:`Accesso mediante Java`, Python, etc.
- utilizzare :blue:`springdoc-openapi`, 
  (https://springdoc.org/#Introduction e https://www.youtube.com/watch?v=utRxyPfFlDw) 
  la libreria Java che aiuta ad automatizzare la generazione della documentazione 
  API utilizzando progetti SpringBoot.

+++++++++++++++++++++++++++++
Accesso mediante browser
+++++++++++++++++++++++++++++
.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/` 
  * - Restituisce dati JSON relativi al top level service.
  
      La risposta utilizza il formato HAL per l'output JSON e 
      indica che il server offre un  collegamento situato a http://localhost:8080/people e 
      le opzioni *?page, ?size, e ?sort*.

      .. code::

        {
            "_links": {
              "people": {
              "href": "http://localhost:8080/people{?page,size,sort}",
              "templated": true
            },
            "profile": {
               "href": "http://localhost:8080/profile"
               }
            }
        }


.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/people?page=0&size=2&sort=lastName` 
  * - Restituisce l'elenco delle persone ordinato per cognome, con due valori per pagina

++++++++++++++++++++++++
Accesso mediante curl
++++++++++++++++++++++++

Per visualizzare e modificare il database, possiamo usare il comando :blue:`curl`. 

Riportiamo alcuni esempi:
 

.. list-table:: 
  :width: 90%

  * - Popolare il database 
  * -   
      .. code::

        curl -i -H "Content-Type:application/json" 
          -d "{\"firstName\":\"Alessando\", \"lastName\":\"Manzoni\"}"
          http://localhost:8080/people
        curl -i -H "Content-Type:application/json" 
          -d "{\"firstName\":\"Ugo\", \"lastName\":\"Foscolo\"}"
          http://localhost:8080/people
        curl -i -H "Content-Type:application/json" 
          -d "{\"firstName\":\"Dante\", \"lastName\":\"Alighieri\"}"
          http://localhost:8080/people
        curl -i -H "Content-Type:application/json" 
          -d "{\"firstName\":\"Giacomo\", \"lastName\":\"Leopardi\"}"
          http://localhost:8080/people

  * - Modificare un elemento (:blue:`PUT` sostituisce un intero record. I campi non forniti vengono sostituiti con **null**)
  * -  
      .. code::

         curl -X PUT -H "Content-Type:application/json" 
           -d "{\"firstName\": \"Alessandro\",\"lastName\":\"MANZONI\"}"
           http://localhost:8080/people/1

  * - Modificare parte di un elemento (:blue:`PATCH`)
  * -  
      .. code::

        curl -X PATCH -H "Content-Type:application/json"
              -d "{\"firstName\": \"ALESSANDRO\"}"
              http://localhost:8080/people/1

  * - Cancellare un elemento  
  * -  
      .. code::

         curl -X DELETE http://localhost:8080/people/1

  * - Cercare un elemento (query personalizzata) 
  * -  
      .. code::

        curl http://localhost:8080/
            people/search/findByLastName?name=Leopardi
  * - Ottenere l'elenco delle persone ordinato per cognome, con due valori per pagina
  * -  
      .. code::

         curl "http://localhost:8080/people?sort=lastName&page=0&size=2"   
         //double quotes necessarie in Windows

+++++++++++++++++++++++++++++++
Accesso mediante H2 console
+++++++++++++++++++++++++++++++
Spring Boot configura l'applicazione per la connessione a un **archivio in memoria**, con il nome utente *sa* 
e una password vuota.

Aggiungiamo una proprietà nel file :blue:`application.properties`:

.. code::
  
    spring.h2.console.enabled=true

Una volta riattivata l'applicazione, apriamo un browser e inseriamo
il comando *http://localhost:8080/h2-console*: si apre una console che permette la gestione del database attraverso 
statement SQL.

.. list-table:: 
  :widths: 35,65
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

    INSERT INTO PERSON VALUES(1, 'Ugo', 'Foscolo' )
    INSERT INTO PERSON VALUES(2, 'Giacomo', 'Leopardi' )
    INSERT INTO PERSON VALUES(3, 'Dante', 'Alighieri' )
    INSERT INTO PERSON VALUES(4, 'Alessandro', 'Manzoni' )

++++++++++++++++++++++++
Archivio su file
++++++++++++++++++++++++

Spring Boot configura l'applicazione per la connessione a un **archivio in memoria**, con il nome utente *sa* 
e una password vuota.
Questi parametri possono essere modificati aggiungendo proprietà nel file :blue:`application.properties`:

Per modificare il database usato da Spring Boot è sufficiente modificare una proprietà in :blue:`application.properties`.
Ad esempio, per memorizzare i dati in modo permanente su file, possinao specificare:

.. code::

    spring.datasource.url= jdbc:h2:file:./data/people
 
++++++++++++++++++++++++++++++
Accesso mediante Java
++++++++++++++++++++++++++++++

++++++++++++++++++++++++++++++
Accesso mediante Python
++++++++++++++++++++++++++++++
Usiamo Jupyter







++++++++++++++++++++++++++++++
MockMvc
++++++++++++++++++++++++++++++

- C:\Didattica\SpringExamples\spring-boot-hateoas
- https://howtodoinjava.com/spring-boot2/rest/rest-with-spring-hateoas-example/
- https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
- https://howtodoinjava.com/series/spring-mvc-tutorials/

Spring WebMVC (o Spring MVC ) contiene il model-view-controller (MVC) di Spring 
e l'implementazione dei servizi Web REST per le applicazioni Web. 
È progettato attorno a un  *DispatcherServlet* che trasferisce le richieste in arrivo 
per richiedere i metodi del gestore.

https://howtodoinjava.com/spring-mvc/contextloaderlistener-vs-dispatcherservlet/

Spring MVC fornisce una netta separazione tra il modello di dominio e il livello web. 
Si integra inoltre perfettamente con altri moduli Spring come Spring Security e Spring Data 
per funzionalità aggiuntive.

.. code::

   <iframe width="560" height="315" src="https://www.youtube.com/embed/eGUEAvNpz48" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework

MockMvc è definito come un punto di ingresso principale per i test Spring MVC lato server. 
I test MockMvc si trovano a metà strada tra i test di unità e di integrazione.





-------------------------------------
Progetto SpringDataRest - servizi
-------------------------------------

++++++++++++++++++++++++++++++
Accesso mediante HAL browser
++++++++++++++++++++++++++++++

Aggiungianmo le dipendenze che permettono l'usop di HAL explorer:

.. code::

    dependencies {
      ...
      implementation 'org.springframework.data:spring-data-rest-hal-explorer'
    }

.. list-table:: 
  :widths: 40,60
  :width: 100%

  
  * - *http://localhost:8080/*
      restituisce HAL page
     
       .. image:: ./_static/img/Spring/SpringRestH2HAlExplorer.png 
         :align: center
         :width: 100%
    - click su :blue:`<` di **products**
      
      .. image:: ./_static/img/Spring/SpringRestH2Products.png 
        :align: center
        :width: 100%     
 

 
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
SpringRestH2 Workspace
+++++++++++++++++++++++++++++++++++

.. list-table:: 
  :widths: 50,50
  :width: 100%

  * - 
     .. image:: ./_static/img/Spring/SpringRestH2Workspace.png 
         :align: center
         :width: 70%
    - application.properties  (per usare la ui-console)
        



 





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

Aggiungiamo la dipendenza :

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




 




-------------------------------------
Servizi Web REST
-------------------------------------

I servizi Web REST sono diventati il ​​mezzo numero uno per l'integrazione delle applicazioni sul Web. 
Al suo interno, REST definisce un sistema costituito da risorse con cui interagiscono i client. 
Queste risorse sono implementate in modo ipermediale. 
Spring MVC e Spring WebFlux offrono ciascuna una solida base per costruire questi tipi di servizi. 

Tuttavia, l'implementazione anche del principio più semplice dei servizi Web REST per un sistema 
di oggetti multidominio può essere piuttosto noioso e comportare molto codice standard.

Spring Data REST si basa sui repository :ref:`Spring Data` e li esporta automaticamente come risorse REST. 
Sfrutta l'ipermedia per consentire ai client di trovare automaticamente le funzionalità esposte dai 
repository e di integrare queste risorse nelle relative funzionalità basate sull'ipermedia.

.. code::

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        implementation 'org.springframework.boot:spring-boot-starter-data-rest'
        runtimeOnly 'com.h2database:h2'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
    }

   curl http://localhost:8080/people
   curl -i -H "Content-Type:application/json" -d "{\"firstName\": \"Frodo\", \"lastName\": \"Baggins\"}" http://localhost:8080/people
   curl http://localhost:8080/people/search
   curl http://localhost:8080/people/search/findByLastName?name=Baggins
   curl -X PUT -H "Content-Type:application/json" -d "{\"firstName\": \"Bilbo\", \"lastName\": \"Baggins\"}" http://localhost:8080/people/1
   curl -X PATCH -H "Content-Type:application/json" -d "{\"firstName\": \"Bilbo Jr.\"}" http://localhost:8080/people/1
   curl -X DELETE http://localhost:8080/people/1

PUT replaces an entire record. Fields not supplied are replaced with null. You can use PATCH to update a subset of items.


-------------------------------------
Spring data
-------------------------------------

La missione di Spring Data è fornire un modello di programmazione basato su Spring familiare e coerente 
per l'accesso ai dati, pur mantenendo le caratteristiche speciali dell'archivio dati sottostante.

Semplifica l'utilizzo di tecnologie di accesso ai dati, database relazionali e non relazionali, 
framework di riduzione delle mappe e servizi dati basati su cloud. 
Questo è un progetto ombrello che contiene molti sottoprogetti specifici di un determinato database. 


-------------------------------------
Spring Statemachine
-------------------------------------
Spring Statemachine è un framework per gli sviluppatori di applicazioni per utilizzare concetti di macchina 
a stati con le applicazioni Spring. 