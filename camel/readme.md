# Camel & JBoss A-MQ exercise

## Instructions

- To developp the project, first clone the activemq-labs github repo

````
git clone https://github.com/gpe-mw-training/activemq-labs.git
````

- Move to the activemq-exercises/camel directory & build the project

````
mvn install
````

- Start JBoss AMQ
- Install te features file into JBoss AMQ

````
features:addurl mvn:com.fusesource.activemq.exercises/features/0.1-SNAPSHOT/xml/features
````
- Deploy the project

````
features:install config
features:install camel-producer
features:install camel-consumer
````

OR

- Add the features xml repository to the ${JBOSS_HOME}/etc/org.apache.karaf.features.cfg file (end of this file).

````
,\
mvn:com.fusesource.activemq.exercises/features/0.1-SNAPSHOT/xml/features
````

- Next, boot Karaf & Deploy the features

````
features:install all
````

- Next copy the CSV file to the ${JBOSS_HOME}/demo folder
