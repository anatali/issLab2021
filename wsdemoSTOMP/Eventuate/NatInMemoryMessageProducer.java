package io.eventuate.tram.examples.basic.messages;


import io.eventuate.common.id.ApplicationIdGenerator;
import io.eventuate.tram.inmemory.EventuateTransactionSynchronizationManager;
import io.eventuate.tram.inmemory.InMemoryMessageConsumer;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.producer.common.MessageProducerImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatInMemoryMessageProducer implements MessageProducerImplementation {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final InMemoryMessageConsumer messageConsumer;
  private final EventuateTransactionSynchronizationManager eventuateTransactionSynchronizationManager;

  //private final ApplicationIdGenerator applicationIdGenerator = new ApplicationIdGenerator();

  public NatInMemoryMessageProducer(InMemoryMessageConsumer messageConsumer,
                                    EventuateTransactionSynchronizationManager eventuateTransactionSynchronizationManager) {
    this.messageConsumer = messageConsumer;
    this.eventuateTransactionSynchronizationManager = eventuateTransactionSynchronizationManager;
    System.out.println(" === NatInMemoryMessageProducer logger " + logger);
  }

  @Override
  public void withContext(Runnable runnable) {
    if (eventuateTransactionSynchronizationManager.isTransactionActive()) {
      //logger.info("Transaction active");
      System.out.println(" === NatInMemoryMessageProducer Transaction active"  );
      eventuateTransactionSynchronizationManager.executeAfterTransaction(runnable);
    } else {
      //logger.info("No transaction active");
      System.out.println(" === NatInMemoryMessageProducer No transaction active"  );
      runnable.run();
    }
  }

  @Override
  public void setMessageIdIfNecessary(Message message) {
    //message.setHeader(Message.ID, applicationIdGenerator.genId(null, null).asString());
  }

  @Override
  public void send(Message message) {
    System.out.println(" === NatInMemoryMessageProducer send " + message );
    messageConsumer.dispatchMessage(message);
  }
}
