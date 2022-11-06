.. role:: red 
.. role:: blue 
.. role:: brown 
.. role:: remark
.. role:: worktodo  

=============================
QakRest
=============================

Obiettivo: costruire una facade REST per un sistema Qak.

#. Usiamo SprinBoot per costruire una Facade di acesso ad un sistems Qak utilizzabile da utenti umani e 
   da  programmi.
#. Le funzionalità della Facade sono:

   - attivare la applicazione Qak 
   - fornire la lista dei nomi degli attori che compaiono nell'applicazione Qak
   - fornire la lista dei messaggi che gli attori gestiscono nelle diverse transizioni di stato
   - creare/elimnare observer sugli attori (in relazione alle informazioni emesse via Coap)
   - inviare messaggi ad attori

   Un esempio di uso è fornito nel video ...

++++++++++++++++++++++++++++++
QakFacadeApi
++++++++++++++++++++++++++++++

Le funzionalità che costituiscono la 'core application' sono definite dalla interfaccia *QakFacadeApi*:

   .. code:: java

    public interface QakFacadeApi {
        public void startTheQakSystem(String sysDescr);
        public List<String> getActorNames();
        public String getActor(String name);
        public String getActorsApi();
        public void sendMessage(String msg);
        public String manageObserver( String observed, boolean create );
    }
 


%%%%%%%%%%%%%%%%%%%%%%%%%%
QakSystemFacade        
%%%%%%%%%%%%%%%%%%%%%%%%%%

Le funzionalità che costituiscono la 'core application' della Facade e sono realizzate dal POJO 
*QakSystemFacade* che implementa la interfaccia *QakFacadeApi*; ad esempio:

   .. code:: java

    public class QakSystemFacade implements QakFacadeApi {
    ...
        @Override
        public  List<String> getActorNames() {
            if( ! qakSys_started ) return createEmptyAnswer();
            List<String> actors = sysUtil.getAllActorNames();
            actors.forEach( a -> ColorsOut.outappl( a, ColorsOut.CYAN) );
            return actors;
        }

        
    }

++++++++++++++++++++++++++++++
QakRest GUI
++++++++++++++++++++++++++++++

L'accesso per gli utenti umani è realizzato da :ref:`QakRest HIController` che fornisce una pagina web 
che ha sezioni di input (per l'invio di comandi) e di output.

    .. image:: ./_static/img/QakRest/QakRestConsole.png 
       :align: center
       :width: 100%  

Le :blue:`sezioni di output` includono aree:

- per la visualizzazione delle risposte ai comandi (ad esempio aree *WELCOME, Transitions*). 
  Per il loro aggiornamento si veda :ref:`updateViewModel`;
- per la visualizzazione delle informazioni dinamicamente emesse dagli observer attivati sugli attori
  (area *Actor update area*). Per il loro aggiornamento si veda  :ref:`wsminimal.js`.


I pulsanti presenti nelle :blue:`sezioni di input` della pagina inviano richieste:

- HTTP-GET ( *getActorNames, getActorsApi, getActor* ) 
- HTTP-POST ( *START* )
- HTTP-PUT ( *sendMessage* )

++++++++++++++++++++++++++++++
QakRest HIController
++++++++++++++++++++++++++++++

HIController è un *Controller* Spring che implementa :ref:`L'interfaccia QakHIService`:

   .. code:: java

    @Controller
    public class HIController implements QakHIService {
        private QakSystemFacade qakSys  = new QakSystemFacade();
        ...

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'interfaccia QakHIService
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

   .. code:: java

        @GetMapping(value="/")
        String entry( Model viewmodel );

        @PostMapping(value="/startTheQakSystem")
        String startTheQakSystem(Model viewmodel, 
            @RequestParam(name="sysDescr", required=true) String sysDescr);

        @GetMapping(value="/getActorNames")
        String getActorNames( Model viewmodel ); //output in the page 

        @GetMapping(value="/getActorsApi")
        String getActorsApi( Model viewmodel );

        @GetMapping(value="/getActor")
        String getActor(Model viewmodel, @RequestParam String name);

        @PostMapping(value="/sendMessage")   //Put ???
        String sendMessage(Model viewmodel, 
            @RequestParam(name="name", required=true) String msg );

        @PostMapping(value="/manageObserver")
        String manageObserver(Model viewmodel,
            @RequestParam(name="observed", required=true) String observed,
            @RequestParam(name="create", required=true) String create );
    }



*HIController* realizza i comandi inviando opportuni metodi dell'istanza *qaSys* di 
:ref:`QakSystemFacade`, restituendo sempre una pagina HTML (:ref:`qakSystemGui.html`) con 
opportuni aggiornamenti del viemodel.

   .. code:: java

    @Controller
    public class HIController implements QakHIService {
        ...
        @Override
        public String getActorNames(Model viewmodel) {
            ...
            return "qakSystemGui";
        }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
qakSystemGui.html
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La pagina *qakSystemGui.html* organizza il suo layout utilizzando  bootstrap.
Essa include sezioni aggiornabili mediante l'uso dei meccanismi di Theamleaf. 
Ad esempio:

.. code:: html

   <div class="card iss-bg-infoarea text-primary">
     <div class="card-content px-1">
        <!-- id used by a Java page reader -->
        <div id="INFO" th:text="${info}" th:remove="tag">Tobereplaced</div>
    </div>
  </div>

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
updateViewmodel
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il metodo *updateViewmodel* rappresenta lo standard per la enmissioni di informazioni
sullo stato dei comandi:

.. code:: java

    private void updateViewmodel(Model model,String info ){
        model.addAttribute("info", info );
    }

Ad esempio:

.. code:: java

    @Override
    public String getActorNames(Model viewmodel) {
        List<String> actorNames = qakSys.getActorNames();
        updateViewmodel(viewmodel, "ActorNames:"+ actorNames.toString());
        return "qakSystemGui";
    }

Altre informazioni possono essere emesse ad hoc nelle sezioni di output della pagina
da parte di specifici comandi. Ad esempio, l'endpoint *getActorsApi* aggiorna 
il campo "transitions":

.. code:: java

    @Override
    public String getActorsApi(Model viewmodel) {
        Iterator<String> answer = qakSys.getActorsTransitions() ;
        viewmodel.addAttribute("transitions", answer );
        return "qakSystemGui";
    }
     

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
websockets
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La pagina include anche lo script *wsminimal.js* per gestire dinamicamente informazioni via websoket.

.. code:: javascript

    const infoDisplay  = document.getElementById("infodisplay");
    var socket;

    function connect(){
      //Binds socket 
      ...
    }

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
         setMessageToWindow(infoDisplay,"sending: " + jsonMsg);
    }




++++++++++++++++++++++++++++++
QakRest M2MController
++++++++++++++++++++++++++++++


L'accesso per i programmi è realizzato da un :blue:`RestController` Spring (*M2MController*) che fornisce accessi sincroni 
e accessi asincroni. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'interfaccia QakM2MServiceSynch
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'interfaccia *QakM2MServiceSynch* definisce gli endpoints sincroni che costitusicono un secondo modo di accesso
alle funzionalità di :ref:`QakSystemFacade`. 
 
   .. code:: java

    public interface QakM2MServiceSynch {
 
        @GetMapping(value="/qak/getActorNames", produces ="application/json")
        List<String> getActorNames( );
        @GetMapping(value="/qak/getActorsApi", produces ="application/json")
        String  getActorsApi( );
        @GetMapping(value="/qak/getActor", produces ="application/json")
        public String getActor(@RequestParam String name);
        @PostMapping(value="/qak/startTheQakSystem", produces ="application/json")
        String startTheQakSystem(@RequestBody String sysDescr );
        @PutMapping(value="/qak/sendMessage", produces ="application/json")
        void sendMessage(@RequestBody String msg );
    }

Esempi di uso con curl:

.. code:: java

    //Creazione della applicazione qak
    curl -d "{\"name\":\"qakrestdemo.pl\"}" 
         -H "Content-Type: application/json" 
         -X POST http://localhost:8090/qak/startTheQakSystem

    //Accesso a informazioni
    curl http://localhost:8090/qak/getActorNames
    curl http://localhost:8090/qak/getActor?name="led"

    //Invio di messaggio
    curl -d "{\"msg\":\"msg(cmd,dispatch,gui,led,on,1)\"}" 
         -H "Content-Type: application/json"
         -X PUT http://localhost:8090/qak/sendMessage

    //Creazione di un observer
    curl -d  "{\"name\":\"led\"}" 
         -H "Content-Type: application/json"
         -X PUT http://localhost:8090/qak/manageObserver

L'invio di un messaggio al *led* dopo avere creato un observer, provoca un aggiornamento della
*Actor update area* sulla :ref:`QakRest GUI` da parte di :ref:`QakSystemFacade`.
         
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
L'interfaccia QakM2MServiceAsynch
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'interfaccia *QakM2MServiceAsynch* definisce gli endpoints asincroni:
 
   .. code:: java

    public interface QakM2MServiceAsynch {
        @GetMapping(value="/qak/getmono", produces ="application/json")
        ResponseEntity<Mono<String>> getmono( );
        @GetMapping(value="/qak/getfluxcold", produces ="application/json")
        public Flux< Integer > getfluxcold( );
        @GetMapping(value="/qak/startfluxhot", produces ="application/json")
        public Flux<String> startfluxhot();
        @PostMapping( value="/qak/subscribehot", produces ="application/json" )
        public Flux<String>  subscribehot(  @RequestBody String cmd );
        @PostMapping(value="/qak/noblockcommand", produces ="application/json")
        public  Flux<String> noblockcommand( @RequestBody String cmd );
    }


++++++++++++++++++++++++++++++++++++++++++
QakRest - start
++++++++++++++++++++++++++++++++++++++++++

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


logging.level.io.netty.DEBUG=OFF

log4j.rootLogger=DEBUG, OFF    log4j.properties


+++++++++++++++++++++++++++++++
Logback.xml
+++++++++++++++++++++++++++++++

Si veda: https://www.baeldung.com/logback.

.. da https://stackify.com/compare-java-logging-frameworks/

SLF4J fornisce un'API standardizzata che in un modo o nell'altro è implementata dalla maggior parte di questi framework. 
Ciò  consente di modificare il framework di registrazione senza modificare il codice. 
Hai solo bisogno di cambiare la dipendenza in un framework diverso che implementa le interfacce SLF4J.

Apache Log4j è un framework di logging  molto vecchio ed è stato il più popolare per diversi anni. 
Ha introdotto concetti di base, come i livelli di log gerarchici e i logger, 
che sono ancora utilizzati dai moderni framework di registrazione.

Il team di sviluppo ha annunciato la fine del ciclo di vita di Log4j nel 2015. 
Sebbene molti progetti legacy lo utilizzino ancora, si deve preferire un framework più recente,
come Logback.

Logback è stato scritto dallo stesso sviluppatore che ha implementato Log4j con l'obiettivo di diventarne il successore. 
Segue gli stessi concetti di Log4j ma è stato riscritto per migliorare le prestazioni, 
supportare SLF4J in modo nativo e per implementare molti altri miglioramenti come opzioni 
di filtro avanzate e ricaricamento automatico delle configurazioni di registrazione.

Ogni starter, come il spring-boot-starter-web, dipende da spring-boot-starter-logging, 
che già richiama spring-jcl.

Quando un file nel percorso di classe ha uno dei seguenti nomi, Spring Boot lo caricherà automaticamente 
sulla configurazione predefinita (Spring consiglia di utilizzare la variante -spring):

.. code::

    logback-spring.xml
    logback.xml
    logback-spring.groovy
    logback.groovy

.. Si veda https://www.baeldung.com/spring-boot-logging


    <configuration>
 
    <appender name="STDOUT"
        class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n
            </pattern>
        </encoder>
    </appender>
 
     <appender name="Console"
              class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %black(%d{ISO8601}) %highlight(%-5level) [%blue(%t)] %yellow(%C{1.}): %msg%n%throwable
            </Pattern>
        </layout>
    </appender>

    <logger name="org.springframework" level="OFF"
        additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>

    <logger name="io.netty" level="OFF"
            additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>

    <root level="INFO">  ???
        <appender-ref ref="STDOUT" />
    </root>

    <root level="ERROR">
        <appender-ref ref="STDOUT" />
    </root>
 
</configuration>


+++++++++++++++++++++++++++++++++
Stream vs Flux
+++++++++++++++++++++++++++++++++

- Stream is single use, vs. you can subscribe multiple times to Flux
- Stream is pull based (consuming one element calls for the next one) vs. 
  Flux has an hybrid push/pull model where the publisher can push elements but still 
  has to respect backpressure signaled by the consumer
- Stream are synchronous sequences vs. Flux can represent asynchronous sequences
