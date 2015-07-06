#!/bin/bash

JB437HOME=/home/student/JB437
AMQHOME=$JB437HOME/software/jboss-a-mq-6.0.0.redhat-024

echo "Setting up broker01..."
cp -r $AMQHOME/bin $JB437HOME/labs/activemq-exercises/failover/broker01/bin
cp -r $AMQHOME/deploy $JB437HOME/labs/activemq-exercises/failover/broker01/deploy
cp -r $AMQHOME/extras $JB437HOME/labs/activemq-exercises/failover/broker01/extras
cp -r $AMQHOME/fabric $JB437HOME/labs/activemq-exercises/failover/broker01/fabric
cp -r $AMQHOME/instances $JB437HOME/labs/activemq-exercises/failover/broker01/instances
cp -r $AMQHOME/lib $JB437HOME/labs/activemq-exercises/failover/broker01/lib
cp -r $AMQHOME/licenses $JB437HOME/labs/activemq-exercises/failover/broker01/licenses
cp -r $AMQHOME/system $JB437HOME/labs/activemq-exercises/failover/broker01/system

echo "Setting up broker02..."
cp -r $AMQHOME/bin $JB437HOME/labs/activemq-exercises/failover/broker02/bin
cp -r $AMQHOME/deploy $JB437HOME/labs/activemq-exercises/failover/broker02/deploy
cp -r $AMQHOME/extras $JB437HOME/labs/activemq-exercises/failover/broker02/extras
cp -r $AMQHOME/fabric $JB437HOME/labs/activemq-exercises/failover/broker02/fabric
cp -r $AMQHOME/instances $JB437HOME/labs/activemq-exercises/failover/broker02/instances
cp -r $AMQHOME/lib $JB437HOME/labs/activemq-exercises/failover/broker02/lib
cp -r $AMQHOME/licenses $JB437HOME/labs/activemq-exercises/failover/broker02/licenses
cp -r $AMQHOME/system $JB437HOME/labs/activemq-exercises/failover/broker02/system

echo "Done."
