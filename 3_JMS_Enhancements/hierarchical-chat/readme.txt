This ActiveMQ exercise shows how to use Policies in the Chat Example using
ActiveMQ and Maven.

NOTE:

The project uses these hierarchical topics to illustrate the concept:
a) World:   sales
b) USA:     sales.usa
c) Cal:     sales.usa.cal

The project has 1 piece:
a) The hierarchicalClient project fires up a Chat Client that will catch the User's input
   from the command line as well as listening for the incoming messages on the chat room topic.

How to run the Exercise:

1) Fire up the Message Broker (if not already done)

a) open a Command Line Window and go it to the 'activemq-exercises-message-broker' project
b) enter this:

    mvn -P broker

This will fire up the ActiveMQ message broker and display a rolling log file.

2) Fire up three Chat Client

a) open another Command Line Window and go into the exercise root directory
b) enter this:

    mvn -P chatter -Dtype=<client type which can be one of these: world, usa, cal> -Dptype="<Policy Type which can be '.*' or '.>' without the quotes>"

NOTE:

The 'type' will define the topic name mentioned above to which is publishes to. It will listen to the topic with
the same topic name, but with the given 'ptype' extension. Therefore:

    mvn -P chatter -Dtype=usa -Dptype=".>"

publishes to:   sales.usa
listens to:     sales.usa.>

ATTENTION:

This exercise is copying selected files to implement the various types of chat clients (world, usa, cal) and so the
exercise could FAIL if there started concurrently. Please make sure that one chat client is up and running BEFORE
you start the next.

NOTE:

Play around with different 'ptype' settings to see what is happening.
