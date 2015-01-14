package com.fusesource.activemq.exercises.requestreply;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Factorial {
	private static final Log LOG = LogFactory.getLog(Server.class);
	
	public static BigDecimal factorial(int n) {
    	long start = System.currentTimeMillis();
    	LOG.debug("factorial(" + n + ")");
    	BigDecimal fact = new BigDecimal(1);
    	for (int i = 1; i <= n; i++) {
    		fact = fact.multiply(new BigDecimal(i));
    	}
    	
    	LOG.debug("Returning " + fact);
    	long end = System.currentTimeMillis();
    	
    	LOG.debug("factorial(" + n + ") took " + (end-start) + " milliseconds");
    	return fact;
    }
}
