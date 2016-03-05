

### 8.3.2 zookeeper

#### 1 zookeeper镜像制作
使用`nano Dockerfile`命令编辑如下脚本：

```dockerfile
# Version: 0.0.1
FROM index.tenxcloud.com/docker_library/java
MAINTAINER Eric Han "feuyeux@gmail.com"
# RUN apt-get update && apt-get install -y wget unzip git
# RUN wget http://apache.fayea.com/zookeeper/zookeeper-3.4.8/zookeeper-3.4.8.tar.gz
COPY zookeeper-3.4.8.tar.gz /tmp/
RUN tar -xzf /tmp/zookeeper-3.4.8.tar.gz -C /opt
RUN cp /opt/zookeeper-3.4.8/conf/zoo_sample.cfg /opt/zookeeper-3.4.8/conf/zoo.cfg
RUN mv /opt/zookeeper-3.4.8 /opt/zookeeper
RUN rm -f /tmp/zookeeper-3.4.8.tar.gz

ADD entrypoint.sh /opt/entrypoint.sh
RUN chmod 777 /opt/entrypoint.sh

EXPOSE 2181 2888 3888
WORKDIR /opt/zookeeper
VOLUME ["/opt/zookeeper/conf", "/tmp/zookeeper"]
CMD ["/opt/entrypoint.sh"]
```
#### 2 容器启动脚本
使用`nano entrypoint.sh`命令编辑如下脚本：

```bash
#!/bin/sh
ZOO_CFG="/opt/zookeeper/conf/zoo.cfg"
echo "server id (myid): ${SERVER_ID}"
echo "${SERVER_ID}" > /tmp/zookeeper/myid

echo "${APPEND_1}" >> ${ZOO_CFG}
echo "${APPEND_2}" >> ${ZOO_CFG}
echo "${APPEND_3}" >> ${ZOO_CFG}
echo "${APPEND_4}" >> ${ZOO_CFG}
echo "${APPEND_5}" >> ${ZOO_CFG}
echo "${APPEND_6}" >> ${ZOO_CFG}
echo "${APPEND_7}" >> ${ZOO_CFG}
echo "${APPEND_8}" >> ${ZOO_CFG}
echo "${APPEND_9}" >> ${ZOO_CFG}
echo "${APPEND_10}" >> ${ZOO_CFG}
/opt/zookeeper/bin/zkServer.sh start-foreground
```
#### 3 构建镜像
```
sudo docker build -t feuyeux/zookeeper .
```

#### 4 单主机启动容器
```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')

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
```

观察容器配置文件：

```
sudo docker exec -ti zk1 cat /opt/zookeeper/conf/zoo.cfg
sudo docker exec -ti zk1 cat /tmp/zookeeper/myid
```

在PC机上，使用`zkCli`测试zookeeper集群

```
HOST_IP=192.168.3.103
zkCli -server ${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183
```

在PC机上，使用程序测试

```java
public interface Conf {
    String zkConnect = "192.168.3.103:2181,192.168.3.103:2182,192.168.3.103:2183";
    String kafkaServerList = "192.168.3.103:9091,192.168.3.103:9092,192.168.3.103:9093";
    String topic = "ktalk";
    int zkTimeout = 10000;
}

public static void main(String[] args) throws Exception {
    ZooKeeper zk = new ZooKeeper(Conf.zkConnect, Conf.zkTimeout, new Watcher() {
        public void process(WatchedEvent event) {
            System.out.println("Event:" + event.getType());
        }
    });

    zk.create("/my_path", "root_data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    zk.create("/my_path/my_branch", "branch_data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

    System.out.println("my_path data:" + new String(zk.getData("/my_path", false, null)));
    System.out.println("my_branch data:" + new String(zk.getData("/my_path/my_branch", false, null)));
    System.out.println("my_path path:" + zk.getChildren("/my_path", true));

    zk.setData("/my_path/my_branch", "branch_new_data".getBytes(), -1);
    System.out.println("my_branch data:" + new String(zk.getData("/my_path/my_branch", false, null)));

    zk.delete("/my_path/my_branch", -1);
    System.out.println("my_path path:" + zk.getChildren("/my_path", true));
    zk.delete("/my_path", -1);
}
```

输出：

```
Event:None
my_path data:root_data
my_branch data:branch_data
my_path path:[my_branch]
my_branch data:branch_new_data
Event:NodeChildrenChanged
my_path path:[]
Event:NodeDeleted
```

#### 5 多主机启动容器
```
docker run -d \
 --net=host \
 -e SERVER_ID=1 \
 -e APPEND_1=server.1=zk1:2888:3888 \
 -e APPEND_2=server.2=zk2:2888:3888 \
 -e APPEND_3=server.3=zk3:2888:3888 \
 feuyeux/zookeeper

docker run -d \
 --net=host \
 -e SERVER_ID=2 \
 -e APPEND_1=server.1=zk1:2888:3888 \
 -e APPEND_2=server.2=zk2:2888:3888 \
 -e APPEND_3=server.3=zk3:2888:3888 \
 feuyeux/zookeeper

docker run -d \
 --net=host \
 -e SERVER_ID=3 \
 -e APPEND_1=server.1=zk1:2888:3888 \
 -e APPEND_2=server.2=zk2:2888:3888 \
 -e APPEND_3=server.3=zk3:2888:3888 \
 feuyeux/zookeeper
```

### 8.3.3 Kafka
#### 1 Kafka镜像制作
使用`nano Dockerfile`命令编辑如下脚本：

```
# Version: 0.0.1
FROM index.tenxcloud.com/docker_library/java
MAINTAINER Eric Han "feuyeux@gmail.com"
#RUN apt-get update
RUN apt-get install -y wget unzip git
RUN wget -q http://apache.fayea.com/kafka/0.9.0.1/kafka_2.10-0.9.0.1.tgz
RUN tar -xzf kafka_2.10-0.9.0.1.tgz -C /opt
RUN mv /opt/kafka_2.10-0.9.0.1 /opt/kafka
ENV KAFKA_HOME /opt/kafka
ADD start-kafka.sh /usr/bin/start-kafka.sh
RUN chmod 777 /usr/bin/start-kafka.sh
CMD start-kafka.sh
```

#### 2 容器启动脚本
使用`nano start-kafka.sh`命令编辑如下脚本：

```
cp $KAFKA_HOME/config/server.properties $KAFKA_HOME/config/server.properties.bk
sed -r -i "s/(zookeeper.connect)=(.*)/\1=${ZK}/g" $KAFKA_HOME/config/server.properties
sed -r -i "s/(broker.id)=(.*)/\1=${BROKER_ID}/g" $KAFKA_HOME/config/server.properties
sed -r -i "s/(log.dirs)=(.*)/\1=\/tmp\/kafka-logs-${BROKER_ID}/g" $KAFKA_HOME/config/server.properties
sed -r -i "s/#(advertised.host.name)=(.*)/\1=${HOST_IP}/g" $KAFKA_HOME/config/server.properties
sed -r -i "s/#(port)=(.*)/\1=${PORT}/g" $KAFKA_HOME/config/server.properties
sed -r -i "s/(listeners)=(.*)/\1=PLAINTEXT:\/\/:${PORT}/g" $KAFKA_HOME/config/server.properties
if [ "$KAFKA_HEAP_OPTS" != "" ]; then
    sed -r -i "s/^(export KAFKA_HEAP_OPTS)=\"(.*)\"/\1=\"$KAFKA_HEAP_OPTS\"/g" $KAFKA_HOME/bin/kafka-server-start.sh
fi
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
```

#### 3 构建镜像
```
sudo docker build -t feuyeux/kafka .
```

#### 4 启动容器
```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')

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
```

观察容器配置文件：

```
sudo docker exec -ti k1 cat /opt/kafka/config/server.properties
```

在PC机上，使用程序测试kafka集群

```java
public static void main(String[] args) {
    Properties props = new Properties();
    props.put("metadata.broker.list", Conf.kafkaServerList);
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("key.serializer.class", "kafka.serializer.StringEncoder");
    ProducerConfig config = new ProducerConfig(props);
    Producer<String, String> producer = new Producer<String, String>(config);

    props = new Properties();
    props.put("zookeeper.connect", Conf.zkConnect);
    props.put("group.id", "my_group");
    props.put("zookeeper.session.timeout.ms", "100");
    props.put("zookeeper.sync.time.ms", "100");
    props.put("auto.commit.interval.ms", "100");
    props.put("auto.offset.reset", "smallest");
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    ConsumerConfig consumerConfig = new ConsumerConfig(props);
    ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);

    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(Conf.topic, new Integer(1));
    StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
    StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
    Map<String, List<KafkaStream<String, String>>> consumerMap =
            consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
    KafkaStream<String, String> stream = consumerMap.get(Conf.topic).get(0);

    KeyedMessage<String, String> data = new KeyedMessage<String, String>(Conf.topic, "k", "vvvv");
    producer.send(data);
    producer.close();

    ConsumerIterator<String, String> it = stream.iterator();
    while (it.hasNext()) {
        MessageAndMetadata<String, String> data0 = it.next();
        System.out.println(data0.key() + ":" + data0.message());
    }
    consumer.shutdown();
}
```

输出：

```
k:vvvv
```

