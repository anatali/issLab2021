
.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket
 

.. _`WebSockets`:

======================================
Web sockets
======================================

.. code:: java

    anaconda
    sphinx-quickstart

WebSocket_ è un protocollo che consente a due o più computer di comunicare tra loro 
contemporaneamente (full-duplex) su una singola connessione TCP.

.. code:: java

    https://start.spring.io/
    Gradle, Java 2.6.2 Jar Java11


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
