package io.eventuate.tram.examples.basic.inmemory.messages;

import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.producer.MessageProducer;

public class MyProducer implements MessageProducer {

    @Override
    public void send(String destination, Message message) {
        System.out.println("MyProducer send " + message.getPayload());

    }
}
