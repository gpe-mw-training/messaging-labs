This ActiveMQ exercise shows how to create a request reply implementation by leveraging ActiveMQ, using
ActiveMQ and Maven.

The project has 2 pieces:
a) The server project fires up a JMS Message Consumer that will read messages (i.e. requests)
   as long as it can find one; it also fires up a JMS Message Producer that will write messages (i.e. replies)
   to a reply channel, as specified by the JMSReplyTo property.
b) The client project fires up a JMS Message Producer that will create the given
   amount of JMS Messages (i.e. requests) until it will stop and exit; it also fires up a JMS Message Consumer
   that will read messages (i.e. replies) from a temporary queue as long as it can find one or until the
   process exits.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go to the 'activemq-exercises-request-reply' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the Server First

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P server

3) Fire up the Client

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P client

NOTE:

The send of the Message in the Client Project is artificially slowed down (sleep of 100ms between sends) to enable
the user to interrupt them when desired.
