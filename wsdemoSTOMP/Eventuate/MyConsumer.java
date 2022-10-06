package io.eventuate.tram.examples.basic.messages;

import io.eventuate.tram.consumer.common.MessageConsumerImplementation;
import io.eventuate.tram.inmemory.InMemoryMessageConsumer;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.consumer.MessageHandler;
import io.eventuate.tram.messaging.consumer.MessageSubscription;
import io.eventuate.tram.consumer.common.MessageConsumerImpl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyConsumer extends InMemoryMessageConsumer{ //implements MessageConsumerImplementation {

    @Override
    public MessageSubscription subscribe(
            String subscriberId, Set<String> channels, MessageHandler handler) {
        System.out.println(" °°° MyConsumer subscribe " + subscriberId + " " + channels);
        return super.subscribe(subscriberId,channels,handler);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void dispatchMessage(Message message) {
        System.out.println(" °°° MyConsumer dispatchMessage " + message  );
        //String destination = message.getRequiredHeader(Message.DESTINATION);
        //List<MessageHandler> handlers = subscriptions.getOrDefault(destination, Collections.emptyList());
        //logger.info("sending to channel {} that has {} subscriptions this message {} ", destination, handlers.size(), message);
        //dispatchMessageToHandlers(destination, message, handlers);
        //logger.info("sending to wildcard channel {} that has {} subscriptions this message {} ", destination, wildcardSubscriptions.size(), message);
        //dispatchMessageToHandlers(destination, message, wildcardSubscriptions);
    }

}
