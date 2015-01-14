This ActiveMQ exercise shows how to Failover is setup and how it looks like using ActiveMQ and Maven.

ATTENTION:

This Exercise needs two specific ActiveMQ Broker which are provided in the 'brokerOne' and 'brokerTwo'
module of this exercise. Therefore you must make sure that you have shut down any other broker.

The project has 4 pieces:
a) The brokerOne project representing the first Message Broker
b) The brokerTwo project representing the second Message Broker
c) The consumer project fires up a JMS Message Consumer that will read messages
   as long as it can find one.
d) The producer project fires up a JMS Message Producer that will create the given
   amount of JMS Messages until it will stop and exit.

How to run the Exercise:

1) Fire up the two Message Brokers

a) open a Command Line Window and go it to the exercise root directory
b) enter this:

    mvn -P brokerOne

This will fire up the first ActiveMQ message broker on port 61616 and display a rolling log file.

a) open a Command Line Window and go it to the exercise root directory
b) enter this:

    mvn -P brokerTwo

This will fire up the first ActiveMQ message broker on port 62626 and display a rolling log file.

2) Fire up the Consumer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer

3) Fire up the Producer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P producer

4) Check the Failover

a) Figure out which broker is the active one (the one with this last near the end:
   'ActiveMQ JMS Message Broker ... started'). Shut this one down with CTRL-C and you will see that the other broker
   will become active as well as the Consumer and Provider telling you that they switched over to the other broker.

NOTE:

The consumer is artificially slowed down so that the user can test the switching back and forth between the Message
Brokers.

ATTENTION:

If both brokers are shutdown but at least one comes back up in a short period of time the message exchange will
proceed. 
