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

cp the file located into this folder `features/src/main/resources/poolconfig.cfg` to the etc directory of JBoss-AMQ

````
features:install config
features:install camel-producer
features:install camel-consumer
````