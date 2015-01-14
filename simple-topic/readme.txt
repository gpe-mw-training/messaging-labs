This ActiveMQ exercise shows how to create a simple queue message Producer and Consumer using
ActiveMQ and Maven.

The project has 3 pieces:
a) The root project fires up the ActiveMQ Message Broker
b) The listener project fires up a JMS Message Listener that will read messages
   as long there is no SHUTDOWN command message sent. It will also create a Message Consumer that will listen
   for the control reply messages like the reports or shutdown message.
c) The publisher project fires up a JMS Message Producer that will create the given
   amount of JMS Messages in 10 messages batches and then a single REPORT message. At the end it will send a SHUTDOWN
   message as indication for the listener to exit.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go it to the 'activemq-exercises-message-broker' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the Listener First

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P subscriber

3) Fire up the Publisher

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P publisher

NOTE:

The publishing of the messages in the Publisher Project is artificially slowed down (sleep of 100ms between sends) to enable
the user to interrupt them when desired.
