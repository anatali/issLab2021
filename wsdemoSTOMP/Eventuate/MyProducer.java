package io.eventuate.tram.examples.basic.messages;

import io.eventuate.tram.common.spring.inmemory.EventuateSpringTransactionSynchronizationManager;
import io.eventuate.tram.consumer.common.MessageConsumerImplementation;
import io.eventuate.tram.inmemory.EventuateTransactionSynchronizationManager;
import io.eventuate.tram.inmemory.InMemoryMessageConsumer;
import io.eventuate.tram.inmemory.InMemoryMessageProducer;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.MessageProducer;
import io.eventuate.tram.messaging.producer.common.MessageProducerImpl;
import io.eventuate.tram.messaging.producer.common.MessageProducerImplementation;

public class MyProducer extends NatInMemoryMessageProducer{ //implements MessageProducer {
private InMemoryMessageProducer myProducer;

    public MyProducer(InMemoryMessageConsumer myConsumer){
        //EventuateTransactionSynchronizationManager m =
        //        new EventuateSpringTransactionSynchronizationManager();
        //myProducer = new InMemoryMessageProducer( myConsumer,m);
        super( myConsumer, new EventuateSpringTransactionSynchronizationManager());
    }

    @Override
    //public void send(String destination, Message message) {
    public void send( Message message) {
        System.out.println("------------- MyProducer send " + message.getPayload());
        super.send( message );
    }
}
