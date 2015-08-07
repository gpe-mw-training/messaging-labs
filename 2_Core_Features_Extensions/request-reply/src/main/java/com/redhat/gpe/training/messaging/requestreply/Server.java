package com.redhat.gpe.training.messaging.requestreply;

import java.math.BigDecimal;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/request";
    private static final int MESSAGE_TIMEOUT_MILLISECONDS = 120000;

    public static void main(String args[]) {
        Connection connection = null;
        
        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Context context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination) context.lookup(DESTINATION_NAME);

            connection = factory.createConnection("admin", "admin");
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);
            
            // Setup the producer that handles the reply to the request.
            MessageProducer producer = session.createProducer(null);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            LOG.info("Start consuming messages from " + destination.toString() + " with " + MESSAGE_TIMEOUT_MILLISECONDS + "ms timeout");

            // Synchronous message consumer
            while (true) {
                Message message = consumer.receive(MESSAGE_TIMEOUT_MILLISECONDS);
                if (message != null) {
                    if (message instanceof ObjectMessage) {
                        Object o = ((ObjectMessage) message).getObject();
                        if (o instanceof Integer) {
	                    	Integer factorialInput = (Integer)o;
	                        LOG.info("Received request to compute factorial(" + factorialInput + ")");
	                        BigDecimal result = Factorial.factorial(factorialInput);
	                        	
	                        // Create a reply message
	                        Message replyMessage = session.createObjectMessage(result);
	                        
	                        // Carry over the correlation id so that the client can determine for which
	                        // request this reply is for.
	                        replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());
	                        
	                        // Send the processed message to the JMSReplyTo value
	                        producer.send(message.getJMSReplyTo(), replyMessage);
                        } else {
                        	LOG.warn("Unexepected object message: " + o.getClass().getName());
                        }
                    }
                } else {
                    break;
                }
            }

            consumer.close();
            producer.close();
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
