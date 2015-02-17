package com.redhat.gpe.training.messaging.requestreply;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private static final Boolean NON_TRANSACTED = false;
    
    private static final long MESSAGE_TIME_TO_LIVE_MILLISECONDS = 3000;
    private static final int MESSAGE_DELAY_MILLISECONDS = 2000;
    private static final int NUM_MESSAGES_TO_BE_SENT = 100;
    private static final int RAND_CEILING = 100;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String REQUEST_DESTINATION = "queue/request";

    public static void main(String args[]) {
    	Connection connection = null;
    	
    	// This simple Map is used to store requests by their correlation id.
    	// It serves as a simple illustration of how to use the JMSCorrelationID to
    	// match replies with requests.
    	final Map<String,ObjectMessage> correlationMap = Collections.synchronizedMap(new HashMap<String,ObjectMessage>());

        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Context context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination) context.lookup(REQUEST_DESTINATION);

            connection = factory.createConnection("admin", "admin");
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            producer.setTimeToLive(MESSAGE_TIME_TO_LIVE_MILLISECONDS);
            
            // Create a temporary queue that will serve as this client's reply channel.
            // This temporary queue will be properly cleaned up when the session is stopped.
            //Destination replyQueue = session.createTemporaryQueue();
            Destination replyQueue = session.createQueue("reply-queue");
            
            // Create a consumer for the reply channel by creating an anonymous inner class
            // that implements the onMessage() method.
            MessageConsumer consumer = session.createConsumer(replyQueue);
            consumer.setMessageListener(new MessageListener() {
            	@Override
            	public void onMessage(Message reply) {
            		// Handle replies
            		if (reply instanceof ObjectMessage) {
            			try {
            				Object o = ((ObjectMessage) reply).getObject();
            				if (o instanceof BigDecimal) {
            				
	            				// Simulate correlating the reply to the response by removing via the
	            				// JMSCorrelationID in the Map. If we removed it, we have "correlated"
	            				// the request/reply. If we did not find it, that implies that this
	            				// client has received a reply for which it did not initiate the
	            				// request, which would be unusual.
	            				// Note that by removing the request from the map, the client will be
	            				// able to wait for all expected replies, by inspecting the map count
	            				// and waiting for it to hit 0.
	            				LOG.debug("Attempting to correlate reply with request");
	            				LOG.debug("Current outstanding replies: " + correlationMap.size());
	            				ObjectMessage request = correlationMap.remove(reply.getJMSCorrelationID());
	            				if (request != null) {
	            					LOG.debug("Successfully correlated reply");
	            					LOG.info("factorial(" + request.getObject() + ") = " + o);
	            					LOG.debug("Current outstanding replies (after): " + correlationMap.size());
	            				} else {
	            					LOG.warn("Unable to correlate reply with a request initiated by this client");
	            				}
            				} else {
            					LOG.warn("Received unexpected object reply: " + o.getClass().getName());
            				}
            				
            			} catch (JMSException e) {
            				LOG.error("JMS Issue : ",e);
            			}
                    }
            	}
            });

            Random rand = new Random(System.currentTimeMillis());
            for (int i = 1; i <= NUM_MESSAGES_TO_BE_SENT; i++) {
                Integer randomInteger = new Integer(rand.nextInt(RAND_CEILING));
                ObjectMessage message = session.createObjectMessage(randomInteger);
                
                // Set the key JMS message properties that will facilitate the request/reply functionality
                message.setJMSReplyTo(replyQueue);
                message.setJMSCorrelationID(UUID.randomUUID().toString());
                
                // Place the request message in the correlation map, using the JMSCorrelationID
                // as the key.
                correlationMap.put(message.getJMSCorrelationID(), message);
                
                LOG.info("Sending to destination: " + destination.toString() + " a request to compute factorial(" + randomInteger + ")");
                producer.send(message);
                Thread.sleep(MESSAGE_DELAY_MILLISECONDS);
            }
            
            // Wait until all replies have been received. This is the case when the map count
            // hits 0. Try only 5 times, so as to not land in an endless loop. This is typically
            // caused by messages that have expired. Messages that have expired will not be removed
            // from the correlation Map, because they will never get replies.
            int tries = 0;
            while (++tries <= 5 && correlationMap.size() > 0) {
            	LOG.info("Waiting for any outstanding requests to complete");
            	LOG.debug("Current outstanding replies: " + correlationMap.size());
            	try {
            		Thread.sleep(1000 * 5);
            	} catch(InterruptedException e) {
            		LOG.debug("5 second dely complete");
            	}
            }
            
            if (correlationMap.size() > 0) {
            	LOG.warn("There are " + correlationMap.size() + " replies outstanding");
            }
            
            // Cleanup
            producer.close();
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
