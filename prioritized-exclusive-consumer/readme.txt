This ActiveMQ exercise shows how to create a Queue Message Producer and an Prioritized Exclusive Consumer using
ActiveMQ and Maven.

The project has 2 pieces:
a) The consumer project fires up a JMS Message Consumer that will read messages
   as long as it can find one.
b) The producer project fires up a JMS Message Producer that will create the given
   amount of JMS Messages until it will stop and exit.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go it to the 'activemq-exercises-message-broker' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the Several Consumer with different Priorities

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P consumer -Dpriority=<Priority of the Consumer where higher number means higher priority like 1, 2 or 5 etc>

NOTE: make sure that the priorities are different

3) Fire up the Producer

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P producer

NOTE:

The send of the Message in the Producer Project is artificially slowed down (sleep of 200ms between sends) to enable
the user to interrupt them when desired.

NOTE:

There is not limit on how many consumers can be created. One can also experiment with shutting down clients while the
message processing is under (using CTRL-C) and watch what's going on.

NOTE:

You can also use the same priority for several consumers to see how that works

NOTE:

The code is a copy of the 'Simple Queue' project except the Queue name changes.
