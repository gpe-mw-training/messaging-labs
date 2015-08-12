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
features:addurl mvn:com.redhat.gpe.training/features/0.1-SNAPSHOT/xml/features
````
- Deploy the project

````
features:install service
features:install camel-producer
features:install camel-consumer
````

OR

- Add the features xml repository to the ${JBOSS_HOME}/etc/org.apache.karaf.features.cfg file (end of this file).

````
,\
mvn:com.redhat.gpe.training/features/0.1-SNAPSHOT/xml/features
````

- Next, boot Karaf, clean the previous installation & deploy the features

````
./karaf clean or karaf.bat clean

features:install all
````

- Next copy the CSV file to the ${JBOSS_HOME}/demo folder
