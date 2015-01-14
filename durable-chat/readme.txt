This ActiveMQ Exercise shows how to create a simple Queue Message Producer and Consumer using
ActiveMQ and Maven.

The project has 3 pieces:
a) The root project fires up the ActiveMQ Message Broker
b) The charClient project fires up a Durable Chat Client that will catch the User's input
   from the command line as well as listening for the incoming messages on the chat room topic.

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


NOTE:

Because this is a chat over a durable subscription you can kill one client (CTRL-C) and keep on chatting on the
other client. When you bring back the killed client later (using the SAME chatName) it will display the missed
messages sent in the mean time.

NOTE:

There is no limited to the number of clients as long as their 'chatName' is unique. Try a three-way chat.
