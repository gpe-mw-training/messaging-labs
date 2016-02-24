package com.redhat.gpe.training.messaging.jmx;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.jms.*;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JMXClient {
    private static final Logger LOG = LoggerFactory.getLogger(JMXClient.class);
    private static String QUEUE_TEST = "TEST.QUEUE";
    private static String JMX_PROPERTIES_FILE="management.properties";
    
    public static void main(String args[]) {
    	
    	Properties props = new Properties();
        try {
            props.load(JMXClient.class.getClassLoader().getResourceAsStream(JMX_PROPERTIES_FILE));
 
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        JMXServiceURL url = null;
        try {
            url = new JMXServiceURL(props.getProperty("jmx_uri"));
            Map<String, Object> env = new HashMap<String, Object>();
            env.put(JMXConnector.CREDENTIALS, new String[]{props.getProperty("jmx_userid"), props.getProperty("jmx_password")});
            JMXConnector connector = JMXConnectorFactory.connect(url, env);
            connector.connect();
            
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            ObjectName name = new ObjectName("org.apache.activemq:type=Broker,brokerName=amq");
            BrokerViewMBean mbean = MBeanServerInvocationHandler
                    .newProxyInstance(connection, name, BrokerViewMBean.class, true);
            
            /* Broker Info */
            LOG.info("Broker ID : " + mbean.getBrokerId());
            LOG.info("Broker Name : " + mbean.getBrokerName());
            LOG.info("Broker Version : " + mbean.getBrokerVersion());
            LOG.info("Total Message Count : " + mbean.getTotalMessageCount());
            
            /* Broker Queue */
            
            // Create a queue and send a message
            mbean.addQueue(QUEUE_TEST);
            name = new ObjectName("org.apache.activemq:type=Broker,brokerName=amq,destinationType=Queue,destinationName=" + QUEUE_TEST);
            QueueViewMBean queueMbean = MBeanServerInvocationHandler.newProxyInstance(connection, name, QueueViewMBean.class, true);
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("timeToLive", Long.toString(2000));
            headers.put("JMSDeliveryMode", Integer.toString(DeliveryMode.PERSISTENT));
            for(int i=1; i<101; i++){
                queueMbean.sendTextMessage(headers, "test-" + Integer.toString(i), "admin", "admin");
            }

            
            // Check statistics
            LOG.info("Messages enqueued on TEST.QUEUE : " + queueMbean.getEnqueueCount());
            LOG.info("Queue Size : " + queueMbean.getQueueSize());
            LOG.info("Memory Percent Usage : " + queueMbean.getMemoryPercentUsage() + " %");
            
        } catch (Throwable t) {
            LOG.error("JMX Issue : ", t);
        }
    }
}
