package com.redhat.gpe.training.messaging.security;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class SimpleProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);

    private static Boolean 	NON_TRANSACTED = false;
    private static long 	MESSAGE_TIME_TO_LIVE_MILLISECONDS = 0;
    private static int 		MESSAGE_DELAY_MILLISECONDS = 100;
    private static int 		NUM_MESSAGES_TO_BE_SENT = 100;
    private static String 	CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static String 	DESTINATION_NAME = "queue/simple";

    public static void main(String args[]) {
        Connection connection = null;

        try {
		 	// Initialize control variables from the command line
            MESSAGE_TIME_TO_LIVE_MILLISECONDS =
				Long.parseLong(System.getProperty("TimeToLive", "0"));
            MESSAGE_DELAY_MILLISECONDS =
				Integer.parseInt(System.getProperty("Delay", "100"));
            NUM_MESSAGES_TO_BE_SENT =
				Integer.parseInt(System.getProperty("NumMessages", "100"));
            DESTINATION_NAME =
				System.getProperty("Destination", "queue/simple");

            String uri = "tcp://localhost:61616";
            
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, uri);
            
            Context context = new InitialContext(props);
            
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination) context.lookup(DESTINATION_NAME);

            //connection = factory.createConnection("system", "manager");
            connection = factory.createConnection("guest", "password");
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
 
            LOG.info("Successfully connected to: " + uri);
            
            producer.setTimeToLive(MESSAGE_TIME_TO_LIVE_MILLISECONDS);

            for (int i = 1; i <= NUM_MESSAGES_TO_BE_SENT; i++) {
                TextMessage message = session.createTextMessage(i + ". message sent");
                LOG.info("Sending to destination: " + destination.toString() + " this text: '" + message.getText());
                producer.send(message);
                Thread.sleep(MESSAGE_DELAY_MILLISECONDS);
            }

            // Cleanup
            producer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error("JMS Issue",t);
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
                    LOG.error("JMS Issue",e);
                }
            }
        }
    }
}
