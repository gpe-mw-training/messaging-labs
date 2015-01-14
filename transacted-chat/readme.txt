This ActiveMQ exercise shows how to use transactions to write messages to a Topic or to undo (rollback) them.

The project has 2 pieces:
a) The root project fires up the ActiveMQ Message Broker
b) The listener project fires up a Transacted Chat Client that will catch the User's input
   from the command line as well as listening for the incoming messages on the chat room topic. In this case the
   user has to either 'COMMIT' the set of previous messages to sent them out or can 'CANCEL' it to drop the messages
   without being sent to the other chatters.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go it to the 'activemq-exercises-message-broker' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up the first Chat Client

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P chatter -DchatName=<Any Name you Like, for example: chatterOne>

3) Fire up the second Chat Client

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P chatter -DchatName=<Any Name you Like (except the name used above), for example: chatterTwo>

Now each chatter can add a few messages and only if he/she does enter COMMIT in a new line the pending messages
are sent to the other chatters. If he/she writes CANCEL the pending messages are dropped but the user can continue to write and
send new messages afterwards.