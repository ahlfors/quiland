
### 8.3.4 spark

#### 1 spark镜像制作

```
FROM index.tenxcloud.com/docker_library/java
MAINTAINER Eric Han "feuyeux@gmail.com"
RUN curl -s http://apache.fayea.com/spark/spark-1.6.0/spark-1.6.0-bin-hadoop2.6.tgz | tar -xz -C /usr/local/
RUN cd /usr/local && ln -s spark-1.6.0-bin-hadoop2.6 spark
ENV SPARK_HOME /usr/local/spark

ADD entrypoint.sh /usr/local/entrypoint.sh
RUN chmod 777 /usr/local/entrypoint.sh
ENTRYPOINT ["/usr/local/entrypoint.sh"]
WORKDIR /usr/local/spark
```

#### 2 容器启动脚本
`nano bootstrap.sh`

```
#!/bin/bash
/bin/bash -c "cat /etc/hosts"
/bin/bash -c "hostname"

CMD=${1:-"exit 0"}
if [[ "$CMD" == "master" ]]; then
  export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER -Dspark.deploy.zookeeper.url=${HOST_IP}:2181,${HOST_IP}:2182,${HOST_IP}:2183 -Dspark.deploy.zookeeper.dir=/given-spark"
  /bin/bash -c "$SPARK_HOME/sbin/start-master.sh -h $SPARK_MASTER_HOST"
  #/bin/bash -c "tail -f $SPARK_HOME/logs/spark--org.apache.spark.deploy.master.Master*"
elif [[ "$CMD" == "worker" ]]; then
  /bin/bash -c "$SPARK_HOME/sbin/start-slave.sh $SPARK_MASTER_URL"
  #/bin/bash -c "tail -f $SPARK_HOME/logs/spark--org.apache.spark.deploy.worker.Worker*"
else
  /bin/bash -c "$*"
fi
```

#### 3 构建镜像

```
sudo docker build -t feuyeux/spark .
```

#### 4 启动容器

启动一个master容器。

```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')
master=sk1
sudo docker run -it --rm \
-p 18080:8080 -p 17077:7077 -p 16066:6066 \
--name ${master} \
-h ${master} \
-e SPARK_MASTER_HOST=${master} \
-e HOST_IP=${HOST_IP} \
feuyeux/spark master
```

再启动一个master容器，两者竞选，一个为`ALIVE`，另一个为`STANDBY`。

```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')
master=sk2
sudo docker run -it --rm \
-p 28080:8080 -p 27077:7077 -p 26066:6066 \
--name ${master} \
-h ${master} \
-e SPARK_MASTER_HOST=${master} \
-e HOST_IP=${HOST_IP} \
feuyeux/spark master
```

启动若干个worker容器，连接上述两个master容器。

```
sudo docker run -it --rm \
--link sk1 \
--link sk2 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
feuyeux/spark worker
```

查看zookeeper上的spark状态

```
zkCli -server 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183
Connecting to 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183
Welcome to ZooKeeper!
JLine support is enabled

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[zk: 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183(CONNECTED) 0] ls /
[given-spark, zookeeper]
[zk: 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183(CONNECTED) 1] ls /given-spark
[leader_election, master_status]
[zk: 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183(CONNECTED) 2] ls /given-spark/leader_election
[_c_5c4bca91-0688-4fa6-9d53-89599704b000-latch-0000000000, _c_b5f7392c-06c8-4ecf-9830-66c960d8060f-latch-0000000001]
[zk: 10.211.55.32:2181,10.211.55.32:2182,10.211.55.32:2183(CONNECTED) 3] ls /given-spark/master_status  
[worker_worker-20160224075914-172.17.0.4-42397, worker_worker-20160224080611-172.17.0.6-38943, worker_worker-20160224080527-172.17.0.5-44599]
```
![](img/8-.sk1.png)

![](img/8-.sk2.png)

#### 5 本地计算测试
```
sudo docker run -it --rm \
--link sk1 \
--link sk2 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
feuyeux/spark \
bin/run-example org.apache.spark.examples.SparkPi 13
```

#### 6 集群计算测试
```
sudo docker run -it --rm \
--link sk1 --link sk2 \
-e SPARK_MASTER_URL=spark://sk1:7077,sk2:7077 \
feuyeux/spark \
bin/spark-submit \
--master spark://sk1:7077,sk2:7077 \
examples/src/main/python/pi.py 13
```

![](img/8-.submmit.png)


#### 7 spark+kafka
https://github.com/apache/spark/blob/master/examples/src/main/java/org/apache/spark/examples/streaming/JavaDirectKafkaWordCount.java

```
sudo docker exec -ti worker1 ls /usr/local/spark/lib
datanucleus-api-jdo-3.2.6.jar  datanucleus-core-3.2.10.jar  datanucleus-rdbms-3.2.9.jar  spark-1.6.0-yarn-shuffle.jar  spark-assembly-1.6.0-hadoop2.6.0.jar  spark-examples-1.6.0-hadoop2.6.0.jar
```

```
HOST_IP=$(ip -o -4 addr list eth0 | perl -n -e 'if (m{inet\s([\d\.]+)\/\d+\s}xms) { print $1 }')

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

http://colobu.com/2015/01/05/kafka-spark-streaming-integration-summary/
