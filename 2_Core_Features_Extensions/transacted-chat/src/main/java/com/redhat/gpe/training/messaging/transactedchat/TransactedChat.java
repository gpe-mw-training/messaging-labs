package com.redhat.gpe.training.messaging.transactedchat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TransactedChat {
    private static final Logger LOG = LoggerFactory.getLogger(TransactedChat.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final Boolean TRANSACTED = true;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "topic/chat";
    private static final long MESSAGE_LIFESPAN = 30 * 60 * 1000; // 30 minutes

    public static void main(String args[]) {
        Connection connection = null;

        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Context context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Topic destination = (Topic) context.lookup(DESTINATION_NAME);

            connection = factory.createConnection("admin", "admin");

            String chatter = System.getProperty("ChatName");
            connection.setClientID(chatter);

            Session pubSession = connection.createSession(TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = pubSession.createProducer(destination);

            Session subSession = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer subscriber = subSession.createConsumer(destination);

            subscriber.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            String text = ((TextMessage) message).getText();
                            System.out.println("RECEIVED >> '" + text + "'");
                        }
                    } catch (JMSException e) {
                        LOG.error("Got an JMSException handling message: " + message, e);
                    }
                }
            });

            // Get the User Input and handle it appropriately
            System.out.println(
                    "\nTransacted Chat application:\n"
                            + "===========================\n\n"
                            + "The application user " + chatter + " connects to the broker.\n"
                            + "The application will stage messages to the " + destination.toString() + " topic until you either commit them or roll them back.\n"
                            + "The application also subscribes to that topic to consume any committed messages published there.\n\n"
                            + "1. Enter text to publish and then press Enter to stage the message.\n"
                            + "2. Add a few messages to the transaction batch.\n"
                            + "3. Then, either:\n"
                            + "     o Enter the text 'COMMIT', and press Enter to publish all the staged messages.\n"
                            + "     o Enter the text 'CANCEL', and press Enter to drop the staged messages waiting to be sent.\n\n"
            );

            // Start connection AFTER System.out because if there are pending messages
            // they would get printed BEFORE the direction text
            connection.start();

            Scanner inputReader = new Scanner(System.in);

            while (true) {
                String line = inputReader.nextLine();
                if (line == null) {
                    LOG.info("No Line -> Exit this chat");
                    break;
                } else if (line.length() > 0) {
                    if (line.trim().equalsIgnoreCase("CANCEL")) {
                        // Rollback the messages. A new transaction is implicitly
                        // started for following messages.
                        System.out.println("Cancelling messages...");
                        pubSession.rollback();
                        System.out.println("Staged messages have been cleared.");
                    } else if (line.trim().equalsIgnoreCase("COMMIT")) {
                        // Commit (send) the messages. A new transaction is
                        // implicitly  started for following messages.
                        System.out.println("Committing messages... ");
                        pubSession.commit();
                        System.out.println("Staged messages have all been sent.");
                    } else {
                        try {
                            TextMessage message = pubSession.createTextMessage();
                            message.setText(chatter + ": " + line);
                            System.out.println("SEND >> '" + message.getText() + "'");
                            // Publish the message persistently:
                            publisher.send(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, MESSAGE_LIFESPAN);
                        } catch (JMSException e) {
                            LOG.error("Exception during publishing a message: ", e);
                        }
                    }
                }
            }

            // Cleanup
            subscriber.close();
            subSession.close();
            publisher.close();
            pubSession.close();
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
