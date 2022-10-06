package io.eventuate.tram.examples.basic.messages;

import io.eventuate.tram.consumer.common.MessageConsumerImplementation;
import io.eventuate.tram.inmemory.InMemoryMessageConsumer;
import io.eventuate.tram.inmemory.InMemoryMessageProducer;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.MessageBuilder;
import io.eventuate.tram.messaging.producer.MessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import io.eventuate.tram.messaging.common.Message;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public  class NatMessageTest {

  private long uniqueId = System.currentTimeMillis();

  private String subscriberId = "subscriberId" + uniqueId;
  private String destination = "destination" + uniqueId;
  private String payload = "{" + "\"Hello\":" + uniqueId + "}";


   private InMemoryMessageConsumer messageConsumer = new MyConsumer();
   private NatInMemoryMessageProducer messageProducer = new MyProducer(messageConsumer);
  private BlockingQueue<Message> queue = new LinkedBlockingDeque<>();


  public void shouldReceiveMessage() throws InterruptedException {
    System.out.println("------- shouldReceiveMessage " + destination);

    messageConsumer.subscribe(subscriberId,
            Collections.singleton(destination), this::handleMessage);
    //messageProducer.send(destination, MessageBuilder.withPayload(payload).build());
    messageProducer.send( MessageBuilder.withPayload(payload).build());

    Message m = queue.poll(10, TimeUnit.SECONDS);

    //assertNotNull(m);
    //assertEquals(payload, m.getPayload());
  }

  private void handleMessage(Message message) {
    System.out.println("------------------------- handleMessage " + message);
    queue.add(message);
  }

  public static void main(String[] args){
    System.out.println("hello");
    NatMessageTest appl = new NatMessageTest();
    try {
       appl.shouldReceiveMessage();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
