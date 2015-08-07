This ActiveMQ exercise shows how to create and use Virtual Destination using ActiveMQ and Maven.

ATTENTION:

This exercise needs a special ActiveMQ broker configuration because the Virtual Destinations are
defined on the Broker. Therefore you need to shut down any broker and run the local one (step 1) below.

The project has 4 pieces:
a) The root project fires up the ActiveMQ Message Broker
b) The consumer project fires up a JMS Message Consumer that will read messages
   as long as it can find one.
c) The producer project fires up a JMS Message Producer that will create the given
   amount of JMS Messages until it will stop and exit.
d) The listener project fires up a JMS Message Listener that will read messages
   as long there is no SHUTDOWN command message sent. It will also create a Message Consumer that will listen
   for the control reply messages like the reports or shutdown message.

How to run the Exercise:

1) Fire up the Message Broker

a) open a Command Line Window and go it to the exercise root directory
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the Two Consumer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer -DdestExt=one

NOTE: this has to be 'one' because this is hard coded inside the virtual destination.

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer -DdestExt=two

3) Fire up the Listener

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer -DdestExt=three

4) Fire up the Producer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P producer

NOTE:

The send of the Message in the Producer Project is artificially slowed down (sleep of 100ms between sends) to enable
the user to interrupt them when desired.
NOTE:

The consumer must be using either destination extension 'one' or 'two'. The listener must be killed using CTRL-C because
it will not exit otherwise.
