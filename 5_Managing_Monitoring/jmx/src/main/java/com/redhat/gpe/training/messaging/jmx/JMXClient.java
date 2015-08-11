package com.redhat.gpe.training.messaging.jmx;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
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

    private static String JMX_URI = "service:jmx:rmi:///jndi/rmi://localhost:1099/karaf-root";

    public static void main(String args[]) {
        JMXServiceURL url = null;
        try {
            url = new JMXServiceURL(JMX_URI);
            Map<String, Object> env = new HashMap<String, Object>();
            env.put(JMXConnector.CREDENTIALS, new String[]{"admin", "admin"});
            JMXConnector connector = JMXConnectorFactory.connect(url, env);
            connector.connect();
            
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            ObjectName name = new ObjectName("org.apache.activemq:type=Broker,brokerName=amq");
            BrokerViewMBean mbean = MBeanServerInvocationHandler
                    .newProxyInstance(connection, name, BrokerViewMBean.class, true);
            
            LOG.info("Broker ID : " + mbean.getBrokerId() + " - " + mbean.getBrokerId());
            LOG.info("Broker Name : " + mbean.getBrokerName() + " - " + mbean.getBrokerName());
            
        } catch (Throwable t) {
            LOG.error("JMX Issue : ", t);
        }
    }
}
