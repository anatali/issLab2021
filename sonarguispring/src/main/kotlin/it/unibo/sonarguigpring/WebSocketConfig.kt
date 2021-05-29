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
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    companion object {
        const val topicForClient = "/topic/infodisplay"
        const val brokerDestinationPrefix = "/topic"
        const val applDestinationPrefix = "/app"
        const val stompEndpointPath = "/it-unibo-iss"
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        colorPrint("configureMessageBroker |  config=$config", Color.MAGENTA)
        config.enableSimpleBroker(brokerDestinationPrefix)
        config.setApplicationDestinationPrefixes(applDestinationPrefix)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        colorPrint("registerStompEndpoints | registry=$registry", Color.MAGENTA)
        registry.addEndpoint(stompEndpointPath).withSockJS()
    }


}