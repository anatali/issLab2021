.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=======================================
SpringRestCrud
=======================================
#. Creazione di un progetto SpringBoot :ref:`Progetto SpringDataRest - iniziale` con 
   interazioni *HumanMachine* (:blue:`hm`) e *MachineToMachine* (:blue:`m2m`)
#. Testing con RestTemplate ()
#. Swagger
#. Creazione di un database usando H2 : :ref:`Progetto SpringDataRest - database`
#. Testing con :ref:`MockMvc`  
#. Progetto SpringDataRest - servizi (e controller)
#. SpringDataRest - HAL browser
 


- :blue:`HATEOAS` sta per *Hypermedia as the Engine of Application State*.
- :blue:`HAL` (*Hypertext Application Language*)  fornisce un formato coerente  per il collegamento 
  ipertestuale tra le risorse.

.. Buone spiegazioni in https://spring.io/guides/gs/accessing-data-rest/ Accessing JPA Data with REST

-------------------------------------
Progetto SpringDataRest - iniziale
-------------------------------------

+++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - dipendenze iniziali
+++++++++++++++++++++++++++++++++++++++++++

Il progetto inizia con le seguenti dipendenze:

.. code:: 

  dependencies {
   implementation 'org.springframework.boot:spring-boot-starter-data-rest'
   implementation 'org.springframework.boot:spring-boot-starter-web'
   testImplementation 'org.springframework.boot:spring-boot-starter-test'
  //Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
  //For java HTTP caller
	  implementation 'com.squareup.okhttp:okhttp:2.7.5'
  //Human-machine interface
    implementation "org.springframework.boot:spring-boot-starter-thymeleaf"
  }


+++++++++++++++++++++++++++
Person
+++++++++++++++++++++++++++

.. code:: Java

    @Data  //Lombok
    public class Person {
      private long id;
      private String firstName;
      public String toString(){
        return "person id="+id+" firstName="+firstName + " lastName="+lastName;
    }        
    }

+++++++++++++++++++++++++++
DataHandler
+++++++++++++++++++++++++++

.. code:: Java

  public class DataHandler {
      private static Vector<Person> userDataList = new Vector<Person>();
      private static int id = 0;

      private static void addNewPerson(){
          Person userData = new Person();
          userData.setId(id);
          userData.setFirstName("dummy");
          userData.setLastName("dummy");
           userDataList.add(userData);
          id++;
      }
      public static void addPerson(Person userData){
          userDataList.add(userData);
      }
      public static Person getLast(){
          if( userDataList.isEmpty()) addNewPerson();
          return userDataList.lastElement();
      }
      public static Person getFirst(){
          if( userDataList.isEmpty()) addNewPerson();
          return userDataList.firstElement();
      }
      public static String getPersonWithLastName(String lastName){
          String pFound = "person not found";
          //Scandisce userDataList cercando la prima persona con userDataList
          ...
          return pFound;
      }
  }

+++++++++++++++++++++++++++
PersonGuiNaive
+++++++++++++++++++++++++++

.. code:: html

  <!DOCTYPE html>
  <html xmlns="http://www.w3.org/1999/xhtml"
        xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>PersonGuiNaive</title>
      <style>
  h1{
    color:forestgreen;
  }
  </style>
  </head>
  <body>
  <h1>UserData Gui</h1>

  <h2>Last Person inserted</h2>

  id=<span th:text="${lastperson.id}"> Replaceable text </span >
  firstName=<span th:text="${lastperson.firstName}"> Replaceable text </span >
  lastName=<span th:text="${lastperson.lastName}"> Replaceable text </span >
 
  <h2>Insert a new Person</h2>
  <form method="POST" action="/Api/createPerson"  th:object="${personmodel}">
      <label for="id">ID : </label>
      <input type="text" th:field="*{id}"><br/>

      <label for="firstName">FIRSTNAME : </label>
      <input type="text" th:field="*{firstName}"><br/>

      <label for="lastName">LASTNAME : </label>
      <input type="text" th:field="*{lastName}">
      <input type="submit" value="submit">
  </form>

  <h3>Answer to Api/getAPerson?lastName=... </h3>
  <div id="FOUND" th:text="${personfound}"> Replaceable text </div>

  <h3>Answer to Api/getAllPersons</h3>
  <table id="ALLPERSONS">
      <tr th:each="person: ${persons}">
          <td th:text="${person.id}" />
          <td th:text="${person.firstName}" />
          <td th:text="${person.lastName}" />
      </tr>
  </table>
  </body>
  </html> 


+++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - Human-machine controller
+++++++++++++++++++++++++++++++++++++++++++
.. code:: Java

  @Controller
  @RequestMapping("/Api")
  public class HIController {

      private void updateTheModel(Model model, Person lastPerson, String foundPerson){
          model.addAttribute("personmodel", new Person());
          model.addAttribute("lastperson",  lastPerson);
          model.addAttribute("personfound", foundPerson );
      }

      @GetMapping
      public String get(Model model){
          updateTheModel(model, DataHandler.getLast(), "todo");
          return "PesronGuiNaive";
      }
      @GetMapping("/getAPerson") //getAPerson?lastName=Foscolo
      public String getAPerson(Model model, @RequestParam( "lastName" ) String lastName){
          String ps = DataHandler.getPersonWithLastName(lastName);
          updateTheModel(model, DataHandler.getLast(), ps);
          return "PesronGuiNaive";
      }
      @PostMapping("/createPerson")
      public String post(
        @ModelAttribute("personmodel") Person userData, Model model) {
          DataHandler.addPerson(userData);
          updateTheModel(model, DataHandler.getLast(), "todo");
          return "PesronGuiNaive";
      }
      public void runGet(String lastName){
        String response =  doGet("http://localhost:8080/Api/getAPerson?lastName="+lastName);
        readTheHtmlPage(response,"FOUND");
      }
      public void runGetAll( ){
         String response =  doGet("http://localhost:8080/Api/getAllPersons");
         readTheHtmlPage(response,"ALLPERSONS");
      }
      public void runPost() {
        String json = "{\"id\": \"1\",\"firstName\": \"Ugo\",\"lastName\": \"Foscolo\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String response = doPost(BASE_URL + "/createPerson", body);
        System.out.println("runPost response="+response);
     }
  }


  
+++++++++++++++++++++++++++++
SpringDataRest - esecuzione
+++++++++++++++++++++++++++++

Eseguiamo l'applicazione con il comando:

.. code::

    gradlew bootrun


+++++++++++++++++++++++++++++++++++++++++++++
DataOnly: accesso con browser
+++++++++++++++++++++++++++++++++++++++++++++

.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/` 
  * - Restituisce dati JSON relativi al top level service.
  
      La risposta utilizza il formato HAL per l'output JSON e 
      indica che il server offre un  collegamento situato a http://localhost:8080/

      .. code::

        {
          "_links": {
            "profile": {
              "href": "http://localhost:8080/profile"
            }
          }
        }


.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/Api` 
  * - Restituisce la pagina generata da  :ref:`PersonGuiNaive` mediante Thymeleaf.
  
      .. image:: ./_static/img/SpringDataRest/SpringDataRestGuiInit.png 
         :align: center
         :width: 40%

+++++++++++++++++++++++++++++++++++++++++++++
DataOnly: accesso con curl
+++++++++++++++++++++++++++++++++++++++++++++
.. list-table:: 
  :width: 90%

  * - Creare dati (POST)
  * -   
      .. code::

        curl -d "id=2&firstName=Alessando&lastName=Manzoni" -H 
             "Content-Type: application/x-www-form-urlencoded" 
             -X POST http://localhost:8080/Api/createPerson
  * - Cercare un dato (GET)
  * -   
      .. code::

        curl http://localhost:8080/Api/getAPerson?lastName=Manzoni
  * - Cercare tutti dati (GET)
  * -   
      .. code::

        curl http://localhost:8080/Api/getAllPersons 

+++++++++++++++++++++++++++++++++++++++++++++
DataOnly: accesso con Java
+++++++++++++++++++++++++++++++++++++++++++++

.. code:: Java

  public class DataHttpCaller {
    final private OkHttpClient client = new OkHttpClient();
    final private String BASE_URL     = "http://localhost:8080/Api";
 
    public void runGet(String lastName){
        String response =  doGet(BASE_URL +"/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        String response =  doGet(BASE_URL +"/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"ALLPERSONS");
    }
    public void runPost() {
      String personData  = "id=1&firstName=Ugo&lastName=Foscolo";
      RequestBody body   = RequestBody.create(
        MediaType.parse("application/x-www-form-urlencoded"), personData);
      int respCode = doPost(BASE_URL + "/createPerson", body);
      if( respCode == 200 ) System.out.println("runPost Foscolo ok" );
      else System.out.println("WARNING: runPost problem:" + respCode);
    }

    //get, post in Java ...
    //readTheHtmlPage   ... 

      public static void main(String[] args)  {
        //IPOTESI: applicazione attivata
        DataHttpCaller appl = new DataHttpCaller();
          appl.runGetAll();        
          appl.runGet("Foscolo");  //person not found
          appl.runPost();           
          appl.runGet("Foscolo");
      }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
get, post in Java
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

     private String doGet(String url)  {
      Request request = new Request.Builder()
          .url(url)
          .build();
      try{
        Response response = client.newCall(request).execute();
          return response.body().string();
      }catch(Exception e){...}
    }
    private int doPost(String urlStr, RequestBody body)  {
        try{
            Request request = new Request.Builder()
                .url(urlStr)
                .post(body)
                .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            return( response.code()   )  ;
        }catch(Exception e){ return 0; }
    }    

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
readTheHtmlPage
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

     private void readTheHtmlPage(String htmlString, String elementID){
        try {
             HTMLEditorKit htmlEditKit = new HTMLEditorKit();
             HTMLDocument htmlDocument = new HTMLDocument();
             try {
                htmlEditKit.read(new StringReader( htmlString ), htmlDocument, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Element foundField  = htmlDocument.getElement(elementID);
            int start  = foundField.getStartOffset();
            int length = foundField.getEndOffset() - start;
            String s   = foundField.getDocument().getText(start,length);
            System.out.println( s );
        } catch( Exception e){
             e.printStackTrace();
        }
    }

+++++++++++++++++++++++++++++++++++++++++++++
DataOnly: accesso con RestTemplate
+++++++++++++++++++++++++++++++++++++++++++++


-------------------------------------
Progetto SpringDataRest - database
-------------------------------------

Progetto: :remark:`issLab2021\SpringDataRest`

Introduce un database H2 che memorizza dati relativi alla entià di Dominio Person definita da una classe
Java, che funge da modello.

++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - dipendenze per il db
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
- :ref:`Accesso medinate HAL browser`  
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


++++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - Testing con RestTemplate
++++++++++++++++++++++++++++++++++++++++++++++
Per interagire con REST, il client deve creare un'istanza client e richiedere un oggetto, eseguire la richiesta, 
interpretare la risposta, mappare la risposta agli oggetti di dominio e anche gestire le eccezioni. 

Le informazioni consegnate al cliente possono essere in diversi formati, 
come ad esempio JSON, XML, HTML, PHP, text, etc.

Spring fornisce un modo conveniente per utilizzare le API REST, tramite la classe RestTemplate,
che costituisce un client sincrono, progettata per chiamare i servizi REST. 
I suoi metodi primari sono strettamente legati ai metodi del protocollo HTTP HEAD , GET , POST , PUT , DELETE e OPTIONS.

Usando RestTemplate (https://www.baeldung.com/rest-template)

RestTemplate sarà deprecato nelle versioni future di Spring a favore di 
WebClient (https://www.baeldung.com/spring-5-webclient) che fornisce un'API sincrona tradizionale, 
ma supporta anche un efficiente approccio reattivo, non bloccante e asincrono, 
che funziona tramite il protocollo HTTP/1.1.


- getForEntity(): executes a GET request and returns an object of ResponseEntity class that contains both the status code 
                and the resource as an object.

- getForObject() : similar to getForEntity(), but returns the resource directly.

- exchange(): executes a specified HTTP method, such as GET, POST, PUT, etc, and returns a ResponseEntity containing both the HTTP status code and the resource as an object.

- execute() : similar to the exchange() method, but takes additional parameters: RequestCallback and ResultSetExtractor.

- headForHeaders(): executes a HEAD request and returns all HTTP headers for the specified URL.

- optionsForAllow(): executes an OPTIONS request and uses the Allow header to return the HTTP methods that are allowed under the specified URL.

- delete(): deletes the resources at the given URL using the HTTP DELETE method.

- put(): updates a resource for a given URL using the HTTP PUT method.




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


++++++++++++++++++++++++++++++
SpringDataRest - Swagger
++++++++++++++++++++++++++++++


-------------------------------------
Progetto SpringDataRest - servizi
-------------------------------------

++++++++++++++++++++++++++++++
Accesso medinate HAL browser
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




------------------------------------
Swagger
------------------------------------

Spring Fox 3.0.0 not supporting new PathPattern Based Path Matching Strategy for Spring MVC which is now 
the new default from spring-boot 2.6.0.

- https://springdoc.org/#Introduction
- https://www.youtube.com/watch?v=utRxyPfFlDw

springdoc-openapi works by examining an application at runtime to infer API semantics based on spring configurations, 
class structure and various annotations.


.. code::

    http://localhost:8080/swagger-ui/

  spring:
   mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

  http://localhost:8080/swagger-ui/index.html
  http://localhost:8080/v3/api-docs

SpringFox hasn't been updated for a year or so, so I would prefer remove it completely from a project 
and replace it with maintained springdoc-openapi library.


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