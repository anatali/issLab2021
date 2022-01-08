.. contents:: Contenuti
   :depth: 5
.. role:: red
.. role:: blue 
.. role:: remark 

.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket
.. _Springio: https://start.spring.io/
.. _SockJs: https://openbase.com/js/sockjs/documentation#what-is-sockjs
 
.. _`WebSockets`:

======================================
Web sockets
======================================
WebSocket_ è un protocollo che consente a due o più computer di comunicare tra loro  
in modo full-duplex su una singola connessione TCP.
È uno strato molto sottile su TCP che trasforma un flusso di byte in un flusso di messaggi 
(testo o binario).

Nella sua forma più semplice, 

:remark:`un WebSocket è solo un canale di comunicazione tra due applicazioni` 

e non deve essere necessariamente coinvolto un browser.

A differenza di HTTP, che è un protocollo a livello di applicazione, nel protocollo WebSocket 
semplicemente non ci sono abbastanza informazioni in un messaggio in arrivo affinché 
un framework o un container sappia come instradarlo o elaborarlo.

Per questo motivo il WebSocket RFC definisce l'uso di sottoprotocolli. 
Durante l'handshake, il client e il server possono utilizzare l'intestazione 
*Sec-WebSocket-Protocol* per :blue:`concordare un sottoprotocollo`, ovvero un protocollo 
a livello di applicazione superiore da utilizzare. 
L'uso di un sottoprotocollo non è richiesto, ma anche se non utilizzato, le applicazioni 
dovranno comunque scegliere un formato di messaggio che sia il client che il server 
possano comprendere. 


Tuttavia l'uso più comune di WebSocket è facilitare la comunicazione tra un un'applicazione
server e un'applicazione basata su browser.
Infatti, rispetto a HTTP RESTful ha il vantaggio di realizzare comunicazioni  a 
bidirezionali e in tempo reale. Ciò consente al server di inviare informazione al client 
in qualsiasi momento, anziché costringere il client al polling.

I WebSocket utilizzano le Socket nella loro implementazione basata su un protocollo standard
che definisce un *handshake* di connessione e un *frame* di messaggio.

------------------------------------------------------
WebSocket in SpringBoot: versione base
------------------------------------------------------

.. https://www.dariawan.com/tutorials/spring/spring-boot-websocket-basic-example/

Come primo semplice esempio di uso di WebSocket in Spring, creiamo una applicazione che consente
a un client di utilizzare un browser per inviare un messaggio o una immagine a un server 
che provvede a visualizzare il messaggio o l'immagine presso tutti i client collegati.

.. _SetupNoStomp:

+++++++++++++++++++++++++++++++++++++++++++++++
Setup
+++++++++++++++++++++++++++++++++++++++++++++++

#. Iniziamo creando una applicazione *SpringBoot* collegandoci a Springio_ e selezionando 
   come da figura:

.. image:: ./_static/img/springioBase.PNG
     :align: center
     :width: 80%

#. Specifichiamo una nuova porta (il deafult è ``8080``) ponendo in *resources/application.properties*

    .. code:: Java

       server.port = 8070

#. Inseriamo un file ``index.html`` in **resources/static** per poter lanciare un'applicazione che 
   presenta un'area  di ouput per la visualizzazione di messaggi e un'area di input per la loro 
   immissione:


.. _indexNoImagesNoStomp:

    .. code:: html

        <html>
        <head>
            <style>
                .messageAreaStyle {
                    text-align: left;
                    width: 50+;
                    padding: 1em;
                    border: 1px solid black;
                }
            </style>
            <title>wsdemoNoStomp client</title>
        </head>

        <body>
        <h1>Welcome</h1>
        <div id="messageArea"  class="messageAreaStyle"></div>

        <div class="input-fields">
            <p>Type a message and hit send:</p>
            <input id="inputmessage"/><button id="send">Send</button>
        </div>

        <script src="wsminimal.js"></script>
        </body>
        </html>

    La pagina iniziale si presenta come segue:

.. image:: ./_static/img/pageMinimal.PNG
    :align: center
    :width: 60%

    

+++++++++++++++++++++++++++++++++++++++++++++++
wsminimal.js
+++++++++++++++++++++++++++++++++++++++++++++++

.. _wsminimal:

Lo script  ``wsminimal.js`` definisce funzioni che realizzano la connessione con il server
e funzioni di I/O che permettono di inviare un messaggio al server e di visualizzare la risposta.
 
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Funzioni di connessione e ricezione messaggi
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

- *connect*: effettua una connessione alla WebSocket e riceve i messaggi inviati dal server.

.. _connect:

.. code:: js

    var socket = connect();

    function connect(){
        var host     = document.location.host;
        var pathname =  document.location.pathname;
        var addr     = "ws://" +host + pathname + "socket"  ;

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        var socket = new WebSocket(addr); //CONNESSIONE

        socket.onopen = function (event) {
            addMessageToWindow("Connected");
        };
        socket.onmessage = function (event) {
            addMessageToWindow(`Got Message: ${event.data}`);
        };
        return socket;
    }//connect


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Funzioni di input/output
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

- *sendMessage*: invia un messaggio al server attraverso la socket 
- *addMessageToWindow* : visualizza un messaggio nella output area 


.. code:: js

    const messageWindow   = document.getElementById("messageArea");
    const messageInput    = document.getElementById("inputmessage");
    const sendButton      = document.getElementById("send");

    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    }
    function sendMessage(message) {
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }
    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

+++++++++++++++++++++++++++++++++++++++++++++++
Configurazione
+++++++++++++++++++++++++++++++++++++++++++++++

Affinché l'applicazione Spring inoltri le richieste di un client al server, 
è necessario registrare un gestore utilizzando una classe di configurazione 
che implementa l'interfaccia ``WebSocketConfigurer``.

.. code:: java

    @Configuration
    @EnableWebSocket
    public class WebSocketConfiguration implements WebSocketConfigurer {
      @Override
      public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(
        new WebSocketHandler(), "/socket").setAllowedOrigins("*");
      }
    }

L'annotazione ``@EnableWebSocket`` (da aggiungere a una classe qualificata ``@Configuration``)  
abilita l'uso delle plain WebSocket. 

In base alla configurazione, il server risponderà, con una istanza di ``WebSocketHandler``, 
a richieste inviate al seguente indirizzo:

.. code:: java

    ws://<serverIP>:8070/socket

+++++++++++++++++++++++++++++++++++++++++++++++
Il gestore WebSocketHandler
+++++++++++++++++++++++++++++++++++++++++++++++

La classe  ``WebSocketHandler`` definisce un gestore custom di messaggi come specializzazione della classe astratta
``AbstractWebSocketHandler`` (o delle sue sottoclassi ``TextWebSocketHandler`` o ``BinaryWebSocketHandler``).    

Nel nostro caso, la gestione consisterà nel reinviare sulla WebSocket il messaggio ricevuto.
Questa azione del server porrà in esecuzione sul client  l'operazione ``socket.onmessage`` 
(si veda `connect`_) che visualizzerà il messaggio nell'area di output.

.. code:: java

    public class WebSocketHandler extends AbstractWebSocketHandler {
        ...
        @Override
        protected void handleTextMessage(WebSocketSession session, 
                            TextMessage message) throws IOException {
            session.sendMessage(message);
        }
        @Override
        protected void handleBinaryMessage(WebSocketSession session, 
                            BinaryMessage message) throws IOException {
            session.sendMessage(message);
        }
    }

+++++++++++++++++++++++++++++++++++++++++++++++
Propagazione a tutti i client
+++++++++++++++++++++++++++++++++++++++++++++++

Per propagare un messaggio a tutti i client connessi attraverso la WebSocket, basta tenere traccia
delle sessioni.

.. code:: java

    public class WebSocketHandler extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions=
                            new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(
                WebSocketSession session) throws Exception{
        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed( WebSocketSession session, 
                            CloseStatus status) throws Exception{
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, 
                        TextMessage message) throws IOException{
        sendToAll(message);
    }
    protected void sendToAll(TextMessage message) throws IOException{
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }
    }

Notiamo che l'applicazione funziona anche in assenza di un controller, in quanto Spring utilizza di deafult il file
**resources/static/index.html**.


+++++++++++++++++++++++++++++++++++++++++++++++
Un client in Java
+++++++++++++++++++++++++++++++++++++++++++++++

Come esempio di machine-to-machine (M2M) interaction, definiamo
una classe ``WebsocketClientEndpoint.java`` che riproduce in Java la stessa struttura del client già
vista in JavaScript; in più permettiamo di salvare su file l'informazione ricevuta 
(in particolare immagini di tipo ``jpg``).


.. code:: java

    @ClientEndpoint
    public class WebsocketClientEndpoint {

    Session userSession = null;
    private IMessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI) {
     try {
        WebSocketContainer container=
            ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, endpointURI);
     } catch (Exception e) { throw new RuntimeException(e); }
    }

L'annotazione ``@ClientEndpoint`` (che corrisponde alla interfaccia ``javax.websocket.ClientEndpoint``)
denota che un POJO è un web socket client. Come tale, questo POJO può definire i metodi delle web socket lifecycle
usando le *web socket method level annotations*.

.. code:: java

    //Callback hook for Connection open events.
    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    //Callback hook for Connection close events.
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    //Callback hook for Message Events, invoked when a client send a message.
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }
    //Callback hook for images
    @OnMessage
    public void onMessage(ByteBuffer bytes) {
     try{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes.array());
        //Dai bytes alla immagine e salvataggio in un file
        BufferedImage bImage2    = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("outputimage.jpg") );
     }catch( Exception e){ throw new RuntimeException(e); }
    }

    //register message handler
    public void addMessageHandler(IMessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    //Send a message.
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
    }



+++++++++++++++++++++++++++++++++++++++++++++++
Introduzione di un Controller
+++++++++++++++++++++++++++++++++++++++++++++++
Abbiamo già osservato che l'applicazione funziona anche in assenza di un controller, 
in quanto Spring utilizza di default il file **resources/static/index.html**.
Tuttavia l'introduzione di un controller può essere utile per gestire più casi, come ad esempio
un servizio senza/con la possibilità di trasferire immagini.

.. code:: java

    package it.unibo.wsdemoNoSTOMP;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class WebSocketController {
        @RequestMapping("/")
        public String textOnly() {
            return "indexNoImages"; 
        }

        @RequestMapping("/alsoimages")
        public String alsoImages() {
            return "indexAlsoImages";
        }
    }

Il file ``indexNoImages.html`` è simile a al precedente indexNoImagesNoStomp_, mentre il file 
``indexAlsoImages.html`` include anche una sezione per il trasferimento immagini:

.. code:: html

    <!-- indexAlsoImages.html -->
    <html>
    <head>
        <style>
            #messages {
                text-align: left;
                width: 50%;
                padding: 1em;
                border: 1px solid black;
            }
            #images {
                text-align: left;
                width: 50%;
                padding: 1em;
                border: 1px solid red;
            }
        </style>
        <title>WebSocket Client</title>
    </head>
    <body>
    <div class="container">
        <div id="messages"   class="messages"></div>
        <div id="images"     class="images"></div>

        <div class="input-fields">
            <p>Type a message and hit send:</p>
            <input id="message"/><button id="send">Send</button>

            <p>Select an image and hit send:</p>
            <input type="file" id="file" accept="image/*"/>
            <button id="sendImage">Send Image</button>
        </div>
    </div>
    </body>
    <script src="wsalsoimages.js"></script>
    </html>

+++++++++++++++++++++++++++++++++++++++++++++++
Gestione di immagini
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsalsoimages.js`` usato da ``indexAlsoImages.html`` definisce funzioni per la gestione 
delle immagini.

.. code:: java

    sendImageButton.onclick = function (event) { //event is a PointerEvent
        let file = fileInput.files[0];  //file: object File
        sendMessage(file);
        fileInput.value = null;
    };

    socket.onmessage = function (event) {
        if (event.data instanceof ArrayBuffer) {
            addMessageToWindow('Got Image:');
            addImageToWindow(event.data);
        } else {
            addMessageToWindow(`Got Message: ${event.data}`);
        }
    };

    function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        imageWindow.innerHTML += `<img src="${url}"/>`
    }


------------------------------------------------------
WebSocket in SpringBoot: versione STOMP
------------------------------------------------------
:blue:`Simple Text Oriented Message Protocol`
(STOMP) è un protocollo di messaggistica text-based progettato per operare con MOM 
(Message Orinented Middleware) ed originariamente creato per l'uso 
in linguaggi di scripting con frame ispirati a HTTP. 
E' una alternativa a AMQP (Advanced Message Queuing Protocol) e JMS (Java Messaging Service).

STOMP può essere utilizzato anche senza WebSocket, ad esempio tramite una connessione 
Telnet, HTTP o un  message broker. Tuttavia,
STOMP è ampiamente supportato e adatto per l'uso su WebSocket e sul web.

STOMP è progettato per interagire con un :blue:`broker di messaggi` realizzato in memoria (lato server);
dunque, rispetto all'uso delle WebSocket, rende più semplice inviare messaggi solo 
a un particolare utente o ad utenti che sono iscritti a un particolare argomento. 

++++++++++++++++++++++++++++++++++++++++++++++++
Setup e Dipendenze
++++++++++++++++++++++++++++++++++++++++++++++++

Partendo dal SetUp precedente `SetupNoStomp`_, aggiungiamo alcune dipendenze nel file ``build.gradle``:

.. code::

    dependencies {
    //Dipendenze generate dal Setup
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
    
    //Nuove dipendenze
    implementation 'org.webjars:webjars-locator-core'
    implementation 'org.webjars:sockjs-client:1.5.1'
    implementation 'org.webjars:stomp-websocket:2.3.4' 
    implementation 'org.webjars:bootstrap:5.1.3'
    implementation 'org.webjars:jquery:3.6.0'

I :blue:`WebJar` sono dipendenze lato client impacchettate in file JAR e non sono legate a Spring.
Per approfondire, si veda: https://www.baeldung.com/maven-webjars e https://mvnrepository.com/artifact/org.webjars. 

++++++++++++++++++++++++++++++++++++++++++++++++ 
WebSocket vs. SockJs
++++++++++++++++++++++++++++++++++++++++++++++++
A partire dal 2018, il supporto WebSocket nei browser è quasi onnipresente. 
Tuttavia, per supportare vecchi browwer, potrebbe essere necessario fare uso di 
SockJS_, con le seguenti avvertenze:

- Le convenzioni del protocollo URL sono diverse per WebSocket ( ``ws:/`` o ``wss:``) e SockJS ( ``http:`` o ``https:``).
- Le sequenze di handshake interne sono diverse, quindi alcuni broker utilizzeranno punti finali diversi per entrambi i protocolli.
- Nessuno di questi consente di impostare intestazioni personalizzate durante l'handshake *HTTP*.
- *SockJS* supporta internamente diversi meccanismi di trasporto. Si potrebbe dover affrontare limitazioni 
  specifiche a seconda del trasporto effettivo in uso.
- La riconnessione automatica non è abbastanza affidabile con *SockJS*.
- Gli heartbeat potrebbero non essere supportati su *SockJS* da alcuni broker.
- *SockJS* non consente più di una connessione simultanea allo stesso broker. 
  Questo di solito non è un problema per la maggior parte delle applicazioni.

 


++++++++++++++++++++++++++++++++++++++++++++++++
Configurazione 
++++++++++++++++++++++++++++++++++++++++++++++++
Il servizio viene configurato in SpringBoot da una classe che implementa l'interfaccia 
``WebSocketMessageBrokerConfigurer`` :

.. code:: Java

   @Configuration
   @EnableWebSocketMessageBroker
   public class WebSocketConfig 
            implements WebSocketMessageBrokerConfigurer{

   @Override
   public void configureMessageBroker(MessageBrokerRegistry config){
    config.enableSimpleBroker("/demoTopic");   
    config.setApplicationDestinationPrefixes(
                   "/demoInput","/anotherInput");
   }

   @Override
   public void registerStompEndpoints(StompEndpointRegistry registry) {
     registry.addEndpoint("/unibo");  //.withSockJS();  
   }
   }  

Nella configurazione specificata, il servizio:

#. abilita il supporto STOMP su *WebSocket* (escludiamo *SockJS*) registrando l'endpoint ``unibo``.  
   Dunque l'indirizzo per connetersi sarà: ``ws://<serverIP>:8080/unibo``;
#. abilita un broker su memoria comune, con prefisso di destinazione ``demoTopic``. I client 
   si possono sottoscrivere a endpoint che iniziano con questo prefisso, ad es. ``/demoTopic/output``;
#. imposta  ``demoInput`` e ``anotherInput`` come prefissi di destinazione dell'applicazione. 
   I clienti quindi invieranno messaggi agli endpoint che iniziano con ``/demoInput/unibo`` oppure
   ``/anotherInput/unibo``;


++++++++++++++++++++++++++++++++++++++++++++++++ 
La funzione del servizio
++++++++++++++++++++++++++++++++++++++++++++++++

Il servizio:

#. riceve un messaggio (in formato JSON) inviato su endpoint= ``/demoInput/unibo``;
   il messaggio viene mappato in Java usando come DAO la classe ``InputMessage``
#. elabora il messaggio
#. costruisce un messaggio di risposta di tipo ``OutputMessage`` e lo pubblica
   (ancora in formato JSON) su endpoint ``/demoTopic/output``.

La conversione dei messaggi da JSon a Java e viceversa è effettuata in modo automatico 
in SpringBoot, una volta definito un opportuno Controller.


++++++++++++++++++++++++++++++++++++++++++++++++ 
Il controller
++++++++++++++++++++++++++++++++++++++++++++++++

Il controller specifica la gestione delle richieste ``WebSocket`` avviene in modo simile 
alle normali richieste ``HTTP``, ma utilizzando ``@SubscribeMappinge`` o ``@MessageMapping`` 
(e non ``@RequestMapping`` o ``@GetMapping``).

Nel caso specifico, utilizziamo ``@MessageMapping`` per mappare i messaggi diretti a ``input``.

L'annotazione ``@SendTo`` indica che il valore di ritorno   
deve essere inviato come messaggio alla destinazione specificata ``/unibo/output``.

.. code:: Java

    @Controller
    public class HIController {

	@MessageMapping("/unibo")     
	@SendTo("/demoTopic/output")	    
	public OutputMessage elabInput(InputMessage msg) throws Exception{
		return new OutputMessage("Elaborated: " 
               + HtmlUtils.htmlEscape(msg.getName()) + " ");
	}

	@RequestMapping("/")
	public String entryMinimal() { return "indexNoImages"; }
    }

L'operazione ``HtmlUtils.htmlEscape`` elabora il nome nel messaggio di input in modo da poter
essere reso nel DOM lato client.

Il file  ``indexNoImages.html`` restituito da ``HIController`` è simile a quanto già introdotto nella versione 
non-STOMP indexNoImagesNoStomp_, con un set più ampio di dipendenze:

.. code:: html

    <html>
    <head>
        <style>
            .messageAreaStyle {
                text-align: left;
                width: 80%;
                padding: 1em;
                border: 1px solid black;
            }
        </style>
        <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="/main.css" rel="stylesheet">
        <script src="/webjars/stomp-websocket/stomp.min.js"></script>
        <title>wsdemoNoStomp</title>
    </head>

    <body>
    <h1>Welcome</h1>
    <div id="messageArea"  class="messageAreaStyle"></div>

    <div class="input-fields">
        <p>Type a message and hit send:</p>
        <input id="inputmessage"/><button id="send">Send</button>
    </div>
 
    <script src="wsStompMinimal.js"></script>

    </body>
    </html>

La pagina HTML utilizza il file wsStompMinimal.js identico a wsminimal_ della versione non-STOMP per
quanto riguarda la parte relativa alla gestione della pagina e con nuove funzioni per quanto riguarda
la parte di interazione:

.. code:: js
    //Parte di gestione pagina
    ...

    //Parte di interazione
    function connect() {
        var host       = document.location.host;
        var addr       = "ws://" + host  + "/unibo"  ;
        var socket     = new WebSocket(addr);

        socket.onopen = function (event) {
            addMessageToWindow("Connected");
        };

        socket.onmessage = function (event) {
            addMessageToWindow(`Got Message: ${event.data}`);

        };

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            addMessageToWindow("Connected " + frame);
            stompClient.subscribe('/demoTopic/output', function (greeting) {
                showAnswer(JSON.parse(greeting.body).content);
            });
        });
    }

    function sendMessage(message) {
        stompClient.send("/demoInput/unibo", {}, JSON.stringify({'name': message}));
        addMessageToWindow("Sent Message: " + message ); //+ " stompClient=" + stompClient
    }


++++++++++++++++++++++++++++++++++++++++++++++++ 
Componenti
++++++++++++++++++++++++++++++++++++++++++++++++

I componenti-base della applicazione in versione STOMP sono quindi oggetti DTO (:blue:`Data Transfer Object`)
rappresentati dalle classi ``InputMessage`` e ``OutputMessage`` .
  

.. list-table::
   :width: 100%
   :widths: 50,50
   
   * - .. code:: Java
          
          public class InputMessage {
            private String name;
            public InputMessage(String name) {
                this.name = name;}
            public String getName() {return name;}
            public void setName(String name) {
                this.name = name;}
          }
     - .. code:: Java
          
        public class OutputMessage {
        private String content;
        public OutputMessage(String content) {
            this.content = content; }
        public String getContent() { 
            return content; }
        }
 
 

 

++++++++++++++++++++++++++++++++++++++++++++++++
Client (in Java per programmi)
++++++++++++++++++++++++++++++++++++++++++++++++

.. code:: Java
 
    public class StompClient {

    private static final String URL = "ws://localhost:8080/unibo";  

    private static WebSocketStompClient stompClient;

    protected static void connectForSockJs(){
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockjsClient = new SockJsClient(transports);
        stompClient               = new WebSocketStompClient(sockjsClient);

    }
    protected static void connectForWebSocket(){
        WebSocketClient client  = new StandardWebSocketClient();
         stompClient            = new WebSocketStompClient(client);
    }
    public static void main(String[] args) {
        //connectForSockJs();  //To be used when the server is based
        connectForWebSocket();
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(URL, sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
    }


.. code:: Java

    public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
         session.subscribe("/demoTopic/output", this);
         session.send("/anotherInput/unibo", getSampleMessage());
     }

    @Override
    public void handleException(StompSession session, 
      StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        ....
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return OutputMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
         if( payload instanceof OutputMessage) {
            OutputMessage msg = (OutputMessage) payload;
         }
    }
    
    private InputMessage getSampleMessage() {
        InputMessage msg = new InputMessage();
        msg.setName("Nicky");
        return msg;
    }
    }
 

----------------
OLD
----------------


https://spring.io/guides/gs/messaging-stomp-websocket/

- Possibile premessa https://www.baeldung.com/intro-to-project-lombok
- 
- 
  


- Create a Resource Representation Class. HelloMessage. Spring will use the Jackson JSON library to automatically marshal instances of type Greeting into JSON.
-  model the greeting representation, Greeting
- Create a Message-handling Controller. GreetingController
  
  .. code:: Java

  	@MessageMapping("/hello")    
      un msg inviato a /hello induce l'esecuzione del metodo con input un oggetto di tipo HelloMessage
      ricavato dal payload del emssaggio
    @SendTo("/topic/greetings")
      induce a inviare la risposta del metodo a tutti i sottoscrittori di /topic/greetings

- Configure Spring for STOMP messaging. WebSocketConfig
- 
  .. code:: Java

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


- Create a Browser Client . index.html
- ./gradlew bootRun
- java -jar build/libs/gs-messaging-stomp-websocket-0.1.0.jar


https://www.baeldung.com/websockets-spring

https://www.dariawan.com/series/build-spring-websocket-application/

https://www.dariawan.com/tutorials/spring/spring-boot-websocket-basic-example/

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Esempio più articolato
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

https://www.dariawan.com/tutorials/spring/build-chat-application-using-spring-boot-and-websocket/

 