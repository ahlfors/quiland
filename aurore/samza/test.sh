
#!/usr/bin/env bash
mvn clean package
bin/grid bootstrap
mkdir -p deploy/samza
tar -xvf ./target/hello-samza-0.8.0-dist.tar.gz -C deploy/samza
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/wikipedia-feed.properties

deploy/kafka/bin/kafka-console-consumer.sh \
--zookeeper localhost:2181 --topic marmot-raw