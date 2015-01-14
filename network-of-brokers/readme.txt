This ActiveMQ exercise shows how to create a simple queue message Producer and Consumer using
ActiveMQ and Maven.

The project has 2 pieces:
a) The consumer project fires up a JMS Message Consumer that will read messages
   as long as it can find one.
b) The producer project fires upa JMS Message Producer that will create the given
   amount of JMS Messages until it will stop and exit.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go to the 'activemq-exercises-message-broker' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the Consumer First

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer

3) Fire up the Producer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P producer

NOTE:

The send of the Message in the Producer Project is artificially slowed down (sleep of 100ms between sends) to enable
the user to interrupt them when desired.
