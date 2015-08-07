package com.redhat.gpe.training.messaging.virtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.concurrent.CountDownLatch;

public class SimpleListener {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleListener.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String args[]) {
        final String destinationName = "destination/" + System.getProperty("destinationExt");

        Connection connection = null;

        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Context context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination) context.lookup(destinationName);

            connection = factory.createConnection("admin", "admin");

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener() {
                private int count = 0;

                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            String text = ((TextMessage) message).getText();
                            LOG.info("Got " + (count++) + ". message: " + text);
                        }
                    } catch (JMSException e) {
                        LOG.error("JMS Issue : ",e);
                    }
                }
            });

            connection.start();

            LOG.info("Start Listening on destination: " + destination.toString());

            // Wait till the user hit CTRL-C
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    latch.countDown();
                }
            });

            latch.await();

            // Cleanup
            consumer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error("JMS Issue : ", t);
        } finally {
            // Cleanup code
            // In general, you should always close producers, consumers,
            // sessions, and connections in reverse order of creation.
            // For this simple example, a JMS connection.close will
            // clean up all other resources.
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOG.error("JMS Issue : ",e);
                }
            }
        }
    }
}
