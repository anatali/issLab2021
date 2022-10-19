package io.eventuate.tram.examples.basic.inmemory.messages;

import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.MessageBuilder;
import io.eventuate.tram.messaging.producer.MessageProducer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import io.eventuate.tram.messaging.common.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public  class NatMessageTest {

  private long uniqueId = System.currentTimeMillis();

  private String subscriberId = "subscriberId" + uniqueId;
  private String destination = "destination" + uniqueId;
  private String payload = "{" + "\"Hello\":" + uniqueId + "}";


  private MessageProducer messageProducer = new MyProducer();
  private MessageConsumer messageConsumer = new MyConsumer();
  private BlockingQueue<Message> queue = new LinkedBlockingDeque<>();


  public void shouldReceiveMessage() throws InterruptedException {
    System.out.println("shouldReceiveMessage " + destination);
    messageConsumer.subscribe(subscriberId,
            Collections.singleton(destination), this::handleMessage);
    messageProducer.send(destination, MessageBuilder.withPayload(payload).build());

    Message m = queue.poll(10, TimeUnit.SECONDS);

    //assertNotNull(m);
    //assertEquals(payload, m.getPayload());
  }

  private void handleMessage(Message message) {
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
