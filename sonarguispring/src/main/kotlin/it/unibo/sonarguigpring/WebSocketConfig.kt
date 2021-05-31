package it.unibo.sonarguigpring

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil.colorPrint
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

//See https://spring.io/guides/gs/messaging-stomp-websocket/
@Configuration
@EnableWebSocketMessageBroker   //enables WebSocket message handling, backed by a message broker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    companion object {
        const val topicForClient = "/topic/infodisplay"
        const val brokerDestinationPrefix = "/topic"
        const val applDestinationPrefix = "/app"
        const val stompEndpointPath = "/it-unibo-iss"
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        colorPrint("configureMessageBroker |  config=$config", Color.MAGENTA)
        /*
        Enable a simple memory-based message broker to carry messages
        back to the client on destinations prefixed with /topic
         */
        config.enableSimpleBroker(brokerDestinationPrefix)
        /*
        Designates the /app prefix for messages that are bound for methods
        annotated with @MessageMapping.
        This prefix will be used to define all the message mappings. For example, /app/update
         */
        config.setApplicationDestinationPrefixes(applDestinationPrefix) //
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        colorPrint("registerStompEndpoints | registry=$registry", Color.MAGENTA)
        /*
         Registers the /it-unibo-iss endpoint, enabling SockJS fallback options
         so that alternate transports can be used if WebSocket is not available.
          */
         registry.addEndpoint(stompEndpointPath).withSockJS()
    }


}