### 8.4 集群编排

`nano orchestration.sh`

```
echo "stop and clean container, and remove unused images..."
sudo docker kill $(sudo docker ps -q)
sudo docker rm $(sudo docker ps -a -q)
sudo docker images | grep "<none>" | awk '{print $3}' | xargs sudo docker rmi

HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')

echo "done."
sleep 1
echo "launch zookeeper cluster..."

sudo docker run -d \
--name=zk1 \
--net=host \
-e SERVER_ID=1 \
-e APPEND_1=server.1=${HOST_IP}:2888:3888 \
-e APPEND_2=server.2=${HOST_IP}:2889:3889 \
-e APPEND_3=server.3=${HOST_IP}:2890:3890 \
-e APPEND_4=clientPort=2181 \
feuyeux/zookeeper

sudo docker run -d \
--name=zk2 \
--net=host \
-e SERVER_ID=2 \
-e APPEND_1=server.1=${HOST_IP}:2888:3888 \
-e APPEND_2=server.2=${HOST_IP}:2889:3889 \
-e APPEND_3=server.3=${HOST_IP}:2890:3890 \
-e APPEND_4=clientPort=2182 \
feuyeux/zookeeper

sudo docker run -d \
--name=zk3 \
--net=host \
-e SERVER_ID=3 \
-e APPEND_1=server.1=${HOST_IP}:2888:3888 \
-e APPEND_2=server.2=${HOST_IP}:2889:3889 \
-e APPEND_3=server.3=${HOST_IP}:2890:3890 \
-e APPEND_4=clientPort=2183 \
feuyeux/zookeeper

echo "done"
echo "launch kafka cluster..."

K_PORT=9091
sudo docker run --name=k1 \
-p ${K_PORT}:${K_PORT} \
-e BROKER_ID=1 \
-e HOST_IP=${HOST_IP} \
-e PORT=${K_PORT} \
-e ZK=${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 \
-d feuyeux/kafka

K_PORT=9092
sudo docker run --name=k2 \
-p ${K_PORT}:${K_PORT} \
-e BROKER_ID=2 \
-e HOST_IP=${HOST_IP} \
-e PORT=${K_PORT} \
-e ZK=${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 \
-d feuyeux/kafka

K_PORT=9093
sudo docker run --name=k3 \
-p ${K_PORT}:${K_PORT} \
-e BROKER_ID=3 \
-e HOST_IP=${HOST_IP} \
-e PORT=${K_PORT} \
-e ZK=${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 \
-d feuyeux/kafka

echo "done"
echo "launch spark cluster..."

master=sk1
sudo docker run -d \
-p 18080:8080 -p 17077:7077 -p 16066:6066 \
--name ${master} \
-h ${master} \
-e SPARK_MASTER_HOST=${master} \
-e HOST_IP=${HOST_IP} \
feuyeux/spark master

master=sk2
sudo docker run -d \
-p 28080:8080 -p 27077:7077 -p 26066:6066 \
--name ${master} \
-h ${master} \
-e SPARK_MASTER_HOST=${master} \
-e HOST_IP=${HOST_IP} \
feuyeux/spark master

sudo docker run -d \
--link sk1 \
--link sk2 \
--name sw1 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
feuyeux/spark worker

sudo docker run -d \
--link sk1 \
--link sk2 \
--name sw2 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
feuyeux/spark worker

echo "done"
sleep 1
echo "Now, let's look at the containers:"
sudo docker ps
echo "Test spark communicate with kafka"
echo ""

sudo docker run -it --rm \
--link sk1 --link sk2 --link sw1 --link sw2 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
-P \
feuyeux/spark \
bin/spark-submit \
--master spark://sk1:7077,sk2:7077 \
--class org.apache.spark.examples.streaming.JavaKafkaWordCount \
/usr/local/spark/lib/spark-examples-*hadoop*.jar \
${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 \
my-consumer-group \
topic1,topic2 \
1
```

```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')
sudo docker run -ti --rm feuyeux/kafka /opt/kafka/bin/kafka-topics.sh --list --zookeeper ${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183
sudo docker run -ti --rm feuyeux/kafka /opt/kafka/bin/kafka-topics.sh --create --zookeeper ${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 --replication-factor 1 --partitions 1 --topic x  
sudo docker run -ti --rm feuyeux/kafka /opt/kafka/bin/kafka-console-producer.sh --broker-list ${HOST_IP}:9091,${HOST_IP}:9092,${HOST_IP}:9093 --topic x  
```

