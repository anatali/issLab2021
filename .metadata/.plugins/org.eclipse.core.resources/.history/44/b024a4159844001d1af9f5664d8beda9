package io.eventuate.tram.examples.basic.inmemory.messages;

import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.consumer.MessageHandler;
import io.eventuate.tram.messaging.consumer.MessageSubscription;

import java.util.Set;

public class MyConsumer implements MessageConsumer {


    @Override
    public MessageSubscription subscribe(
            String subscriberId, Set<String> channels, MessageHandler handler) {
        System.out.println("MyConsumer subscribe " + subscriberId + " " + channels);
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void close() {

    }
}
