.. contents:: Contenuti
   :depth: 3
.. role:: red
.. role:: blue 
.. role:: remark 

.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket
.. _Springio: https://start.spring.io/
 
.. _`WebSockets`:

======================================
Web sockets
======================================

.. code:: java 
 
    anaconda
    sphinx-quickstart

WebSocket_ è un protocollo che consente a due o più computer di comunicare tra loro 
contemporaneamente (full-duplex) su una singola connessione TCP.
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

Come primo semplice esempio di uso di WebSocket in Spring, creiamo una applicazione che consente
a un client di utilizzare un browser per inviare un messaggio o una immagine a un server 
che provvede a visualizzare il messaggio o l'immagine presso tutti i client collegati.

+++++++++++++++++++++++++++++++++++++++++++++++
Setup
+++++++++++++++++++++++++++++++++++++++++++++++

#. Iniziamo creando una applicazione *SpringBoot* collegandoci a Springio_ e selezionando 
   come da figura:

   .. image:: ./_static/img/springioBase.PNG
     :align: center
     :width: 90%
#. Specifichiamo una nuova porta (il deafult è ``8080``) ponendo in *resources/application.properties*

    .. code:: Java

       server.port = 8070

#. Inseriamo un file ``index.html`` in **resources/static** per poter lanciare un'applicazione che 
   presenta un'area  di ouput per  la visualizzazione di messaggi e un'area di input per la loro 
   immissione

    .. code:: html

        <html>
        <head>
            <style>
                .messageAreaStyle {
                    text-align: left;
                    width: 50%;
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

        <script src="wsdemominimal.js"></script>
        </body>
        </html>

    La pagina iniziale si presenta come segue:

    .. image:: ./_static/img/pageMinimal.PNG
     :align: center
     :width: 50% 
    

+++++++++++++++++++++++++++++++++++++++++++++++
wsdemominimal.js
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsdemominimal.js`` contine funzioni che inviano al server il messaggio di input e che aggiungono
messaggi nella output area:

.. code:: js

    const messageWindow   = document.getElementById("messageArea");
    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("inputmessage");

    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    };

    function sendMessage(message) {
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

-  ``WebSocketConfiguration`` implementa ``WebSocketConfigurer`` e definisce metodi di callback
   per configurare WebSocket request handling via ``@EnableWebSocket`` annotation. Nel nostro caso
   aggiunge WebSocketHandler per il path **/socket**:      

   .. code:: java 

    @Configuration
    @EnableWebSocket
    public class WebSocketConfiguration implements WebSocketConfigurer {
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024000);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/socket").setAllowedOrigins("*");
    }
    }

-  ``WebSocketHandler`` definisce l'handler custom come specializzazione della classe astratta
   ``AbstractWebSocketHandler`` o delle sue sottoclassi ``TextWebSocketHandler`` o ``BinaryWebSocketHandler``    

    .. code:: java

       public class WebSocketHandler extends AbstractWebSocketHandler {
            ...
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            System.out.println("New Text Message Received");
            session.sendMessage(message);
        }
        @Override
        protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
            System.out.println("New Binary Message Received");
            session.sendMessage(message);
        }
       }

------------------------------------------------------
WebSocket in SpringBoot: versione STOMP
------------------------------------------------------

STOMP è un semplice protocollo di messaggistica originariamente creato per l'uso 
in linguaggi di scripting con frame ispirati a HTTP. 
STOMP è ampiamente supportato e adatto per l'uso su WebSocket e sul web.

STOMP può essere utilizzato anche senza websocket, ad esempio tramite una connessione 
Telnet o un servizio di message broker.




https://www.baeldung.com/websockets-spring


https://www.dariawan.com/tutorials/spring/build-chat-application-using-spring-boot-and-websocket/