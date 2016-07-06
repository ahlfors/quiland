## spark

### overview
|IP|hostname|components|
|:--|:--|:--|
|10.101.80.186|m1|mesos-master|
|10.101.86.66|m2|mesos-master|
|10.101.86.136|m3|mesos-master|
|10.101.88.235|s1|mesos-slave spark|
|10.101.91.65|s2|mesos-slave spark|
|10.101.95.23|s3|mesos-slave spark|

### 1 http file server[on m1]
```
cd http_file_server/

ls
spark-1.5.2-bin-hadoop2.6.tgz

nohup python -m SimpleHTTPServer &

http://m1:8000
```

### 2 spark-shell[on s1]
```
cd spark-1.5.2-bin-hadoop2.6

export MESOS_NATIVE_JAVA_LIBRARY=/usr/local/lib/libmesos-0.25.0.so
export SPARK_EXECUTOR_URI=http://10.101.80.186:8000/spark-1.5.2-bin-hadoop2.6.tgz
export MASTER=mesos://zk://m1:2181,m2:2181,m3:2181/mesos

./bin/spark-shell
val data = 1 to 10000
val distData = sc.parallelize(data)
distData.filter(_< 10).collect()

val PATH = "/home/lu.hl/20news-bydate-train"
val path = PATH+"/*"
val rdd = sc.wholeTextFiles(path)
println(rdd.count)
```

### 3 spark-app
#### 3.1 local[on s1]
##### on my-mac-book-pro

```
<build>
    <plugins>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>2.6</version>
            <configuration>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

```
mvn clean install
```

```
scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/target/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar lu.hl@s1:/home/lu.hl
scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/src/main/resources/u1.base lu.hl@s1:/home/lu.hl
```

##### on s1

```
export SPARK_HOME=/Users/erichan/Garden/spark-1.5.1-bin-hadoop2.6

export SPARK_CLASSPATH=$SPARK_CLASSPATH:~/.m2/repository/mysql/mysql-connector-java/5.1.38

$SPARK_HOME/bin/spark-submit --master local[2] --class com.aliyun.firstcloud.transform.TransformWork transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar u1.base

cat my.json.13485666858050/part-00019

{"A":"913","B":"82","C":"3","D":"881368310"}
{"A":"913","B":"83","C":"4","D":"881725904"}
...
```

#### 3.2 mesos client mode[on s1]
```
export MESOS_NATIVE_JAVA_LIBRARY=/usr/local/lib/libmesos-0.25.0.so
export SPARK_EXECUTOR_URI=http://m1:8000/spark-1.5.2-bin-hadoop2.6.tgz
export MASTER=mesos://zk://m1:2181,m2:2181,m3:2181/mesos
export SPARK_HOME=/home/lu.hl/spark-1.5.2-bin-hadoop2.6

$SPARK_HOME/bin/spark-submit --master=$MASTER --class com.aliyun.firstcloud.transform.TransformWork --driver-memory 4G --executor-memory 4G transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar u1.base
```

#### 3.3 mesos cluster mode
##### on cluster(on s1)

```
export SPARK_HOME=/home/lu.hl/spark-1.5.2-bin-hadoop2.6
export MASTER=mesos://zk://m1:2181,m2:2181,m3:2181/mesos
$SPARK_HOME/sbin/start-mesos-dispatcher.sh --master $MASTER
```

##### on client(on my-mac-book-pro)

```
mvn clean install && scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/target/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar lu.hl@m1:/home/lu.hl/http_file_server

scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/src/main/resources/u1.base lu.hl@s1:/tmp
scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/src/main/resources/u1.base lu.hl@s2:/tmp
scp /Users/erichan/sourcecode/alibaba2/fc.es/transform/src/main/resources/u1.base lu.hl@s3:/tmp

cd ~/Garden/spark-1.5.1-bin-hadoop2.6/bin
./spark-submit --master=mesos://s1:7077 --deploy-mode cluster --class com.aliyun.firstcloud.transform.TransformWork --driver-memory 4G --executor-memory 4G http://m1:8000/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar /tmp/u1.base
```

##### analysis from log

下载Jar包

```
I1223 11:45:22.352634   968 fetcher.cpp:414]
Fetcher Info:
{
    "cache_directory": "/tmp/mesos/fetch/slaves/0a66c678-3b16-4b56-be64-0b38f1434fb8-S0/lu.hl",
    "items": [
        {
            "action": "BYPASS_CACHE",
            "uri": {
                "extract": true,
                "value": "http://m1:8000/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
            }
        }
    ],
    "sandbox_directory": "/tmp/mesos/slaves/0a66c678-3b16-4b56-be64-0b38f1434fb8-S0/frameworks/0a66c678-3b16-4b56-be64-0b38f1434fb8-0000/executors/driver-20151223114520-0002/runs/5a95c76f-8625-471d-a2e4-0cc7b85bd524",
    "user": "lu.hl"
}

……

I1223 11:45:25.502022   968 fetcher.cpp:446]
Fetched 'http://m1:8000/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar'
to '/tmp/mesos/slaves/0a66c678-3b16-4b56-be64-0b38f1434fb8-S0/frameworks/0a66c678-3b16-4b56-be64-0b38f1434fb8-0000/executors/driver-20151223114520-0002/runs/5a95c76f-8625-471d-a2e4-0cc7b85bd524/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar'
```
在s2上启动Spark Driver

```
I1223 11:45:25.661803   965 exec.cpp:134] Version: 0.25.0
I1223 11:45:25.665387   975 exec.cpp:208] Executor registered on slave 0a66c678-3b16-4b56-be64-0b38f1434fb8-S0

15/12/23 11:45:35 INFO Remoting: Starting remoting
15/12/23 11:45:35 INFO Remoting: Remoting started; listening on addresses :[akka.tcp://sparkDriver@10.101.91.65:56059]
15/12/23 11:45:35 INFO Utils: Successfully started service 'sparkDriver' on port 56059.

15/12/23 11:45:35 INFO SparkEnv: Registering MapOutputTracker
15/12/23 11:45:35 INFO SparkEnv: Registering BlockManagerMaster
15/12/23 11:45:35 INFO DiskBlockManager: Created local directory at /tmp/blockmgr-689e8bb6-c21d-4ecd-83f6-e9800b84a53b
15/12/23 11:45:35 INFO MemoryStore: MemoryStore started with capacity 2.1 GB
15/12/23 11:45:35 INFO HttpFileServer: HTTP File server directory is /tmp/spark-4f3cf098-ed9f-43be-ae7c-a1379893590a/httpd-cf76b838-4aa4-47c1-bd34-73251dcecf92
15/12/23 11:45:35 INFO HttpServer: Starting HTTP Server
15/12/23 11:45:35 INFO Utils: Successfully started service 'HTTP file server' on port 60795.
15/12/23 11:45:36 INFO SparkEnv: Registering OutputCommitCoordinator
15/12/23 11:45:36 INFO Utils: Successfully started service 'SparkUI' on port 4040.
15/12/23 11:45:36 INFO SparkUI: Started SparkUI at http://10.101.91.65:4040
15/12/23 11:45:36 INFO SparkContext: Added JAR file:
/tmp/mesos/slaves/0a66c678-3b16-4b56-be64-0b38f1434fb8-S0/frameworks/0a66c678-3b16-4b56-be64-0b38f1434fb8-0000/executors/driver-20151223114520-0002/runs/5a95c76f-8625-471d-a2e4-0cc7b85bd524/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar
at http://10.101.91.65:60795/jars/transform-0.0.1-SNAPSHOT-jar-with-dependencies.jar with timestamp 1450842336698
```

Driver启动zk client

```
15/12/23 11:45:36 WARN MetricsSystem: Using default name DAGScheduler for source because spark.app.id is not set.
2015-12-23 11:45:37,081:980(0x7f2521513700):ZOO_INFO@log_env@712: Client environment:zookeeper.version=zookeeper C client 3.4.5
2015-12-23 11:45:37,081:980(0x7f2521513700):ZOO_INFO@log_env@716: Client environment:host.name=s2
2015-12-23 11:45:37,081:980(0x7f2521513700):ZOO_INFO@log_env@723: Client environment:os.name=Linux
2015-12-23 11:45:37,081:980(0x7f2521513700):ZOO_INFO@log_env@724: Client environment:os.arch=3.10.0-123.4.2_2.alios7.x86_64
2015-12-23 11:45:37,081:980(0x7f2521513700):ZOO_INFO@log_env@725: Client environment:os.version=#1 SMP Wed Jan 14 16:43:34 CST 2015
2015-12-23 11:45:37,082:980(0x7f2521513700):ZOO_INFO@log_env@733: Client environment:user.name=(null)
2015-12-23 11:45:37,082:980(0x7f2521513700):ZOO_INFO@log_env@741: Client environment:user.home=/home/lu.hl
2015-12-23 11:45:37,082:980(0x7f2521513700):ZOO_INFO@log_env@753: Client environment:user.dir=
/tmp/mesos/slaves/0a66c678-3b16-4b56-be64-0b38f1434fb8-S0/frameworks/0a66c678-3b16-4b56-be64-0b38f1434fb8-0000/executors/driver-20151223114520-0002/runs/5a95c76f-8625-471d-a2e4-0cc7b85bd524
2015-12-23 11:45:37,082:980(0x7f2521513700):ZOO_INFO@zookeeper_init@786: Initiating client connection,
host=m1:2181,m2:2181,m3:2181
sessionTimeout=10000
watcher=0x7f2584f8eca0
sessionId=0
sessionPasswd=<null>
context=0x7f257047d740
flags=0
```

通过zk找到mesos master在m1

```
I1223 11:45:37.082895  1074 sched.cpp:164] Version: 0.25.0
2015-12-23 11:45:37,091:980(0x7f251eb05700):ZOO_INFO@check_events@1703: initiated connection to server [10.101.80.186:2181]
2015-12-23 11:45:37,100:980(0x7f251eb05700):ZOO_INFO@check_events@1750: session establishment complete on server [10.101.80.186:2181], sessionId=0x151cce755860007, negotiated timeout=10000
I1223 11:45:37.100886  1072 group.cpp:331] Group process (group(1)@10.101.91.65:60550) connected to ZooKeeper
I1223 11:45:37.100930  1072 group.cpp:805] Syncing group operations: queue size (joins, cancels, datas) = (0, 0, 0)
I1223 11:45:37.100949  1072 group.cpp:403] Trying to create path '/mesos' in ZooKeeper
I1223 11:45:37.104478  1072 detector.cpp:156] Detected a new leader: (id='623')
I1223 11:45:37.104615  1072 group.cpp:674] Trying to get '/mesos/json.info_0000000623' in ZooKeeper
I1223 11:45:37.106422  1069 detector.cpp:481] A new leading master (UPID=master@10.101.80.186:5050) is detected
```

注册Framework

```
I1223 11:45:37.106519  1069 sched.cpp:262] New master detected at master@10.101.80.186:5050
I1223 11:45:37.106854  1069 sched.cpp:272] No credentials provided. Attempting to register without authentication
I1223 11:45:37.108855  1072 sched.cpp:641] Framework registered with 0a66c678-3b16-4b56-be64-0b38f1434fb8-0001
```

注册BlockManager

```
15/12/23 11:45:37 INFO MesosSchedulerBackend: Registered as framework ID 0a66c678-3b16-4b56-be64-0b38f1434fb8-0001
15/12/23 11:45:37 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 39090.
15/12/23 11:45:37 INFO NettyBlockTransferService: Server created on 39090
15/12/23 11:45:37 INFO BlockManagerMaster: Trying to register BlockManager
15/12/23 11:45:37 INFO BlockManagerMasterEndpoint: Registering block manager 10.101.91.65:39090 with 2.1 GB RAM, BlockManagerId(driver, 10.101.91.65, 39090)
15/12/23 11:45:37 INFO BlockManagerMaster: Registered BlockManager
15/12/23 11:45:38 INFO MemoryStore: ensureFreeSpace(130448) called with curMem=0, maxMem=2222739947
15/12/23 11:45:38 INFO MemoryStore: Block broadcast_0 stored as values in memory (estimated size 127.4 KB, free 2.1 GB)
15/12/23 11:45:38 INFO MemoryStore: ensureFreeSpace(14276) called with curMem=130448, maxMem=2222739947
15/12/23 11:45:38 INFO MemoryStore: Block broadcast_0_piece0 stored as bytes in memory (estimated size 13.9 KB, free 2.1 GB)
15/12/23 11:45:38 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on 10.101.91.65:39090 (size: 13.9 KB, free: 2.1 GB)
```

执行DAG任务

```
15/12/23 11:45:38 INFO SparkContext: Created broadcast 0 from textFile at TransformWork.java:91
15/12/23 11:45:39 INFO FileInputFormat: Total input paths to process : 1
15/12/23 11:45:39 INFO SparkContext: Starting job: saveAsTextFile at TransformWork.java:105
15/12/23 11:45:39 INFO DAGScheduler: Got job 0 (saveAsTextFile at TransformWork.java:105) with 20 output partitions
15/12/23 11:45:39 INFO DAGScheduler: Final stage: ResultStage 0(saveAsTextFile at TransformWork.java:105)
15/12/23 11:45:39 INFO DAGScheduler: Parents of final stage: List()
15/12/23 11:45:39 INFO DAGScheduler: Missing parents: List()
15/12/23 11:45:39 INFO DAGScheduler: Submitting ResultStage 0 (MapPartitionsRDD[3] at saveAsTextFile at TransformWork.java:105), which has no missing parents
15/12/23 11:45:39 INFO MemoryStore: ensureFreeSpace(129632) called with curMem=144724, maxMem=2222739947
15/12/23 11:45:39 INFO MemoryStore: Block broadcast_1 stored as values in memory (estimated size 126.6 KB, free 2.1 GB)
15/12/23 11:45:39 INFO MemoryStore: ensureFreeSpace(43856) called with curMem=274356, maxMem=2222739947
15/12/23 11:45:39 INFO MemoryStore: Block broadcast_1_piece0 stored as bytes in memory (estimated size 42.8 KB, free 2.1 GB)
15/12/23 11:45:39 INFO BlockManagerInfo: Added broadcast_1_piece0 in memory on 10.101.91.65:39090 (size: 42.8 KB, free: 2.1 GB)
```

```
15/12/23 11:45:39 INFO SparkContext: Created broadcast 1 from broadcast at DAGScheduler.scala:861
15/12/23 11:45:39 INFO DAGScheduler: Submitting 20 missing tasks from ResultStage 0 (MapPartitionsRDD[3] at saveAsTextFile at TransformWork.java:105)
15/12/23 11:45:39 INFO TaskSchedulerImpl: Adding task set 0.0 with 20 tasks
15/12/23 11:45:39 INFO TaskSetManager: Starting task 0.0 in stage 0.0 (TID 0, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:39 INFO TaskSetManager: Starting task 1.0 in stage 0.0 (TID 1, s3, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:39 INFO TaskSetManager: Starting task 2.0 in stage 0.0 (TID 2, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:39 INFO TaskSetManager: Starting task 3.0 in stage 0.0 (TID 3, s3, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:39 INFO TaskSetManager: Starting task 4.0 in stage 0.0 (TID 4, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:39 INFO TaskSetManager: Starting task 5.0 in stage 0.0 (TID 5, s3, PROCESS_LOCAL, 2217 bytes)

15/12/23 11:45:43 INFO BlockManagerMasterEndpoint: Registering block manager s1:43030 with 2.1 GB RAM, BlockManagerId(0a66c678-3b16-4b56-be64-0b38f1434fb8-S1, s1, 43030)
15/12/23 11:45:48 INFO BlockManagerMasterEndpoint: Registering block manager s3:59907 with 2.1 GB RAM, BlockManagerId(0a66c678-3b16-4b56-be64-0b38f1434fb8-S2, s3, 59907)
```

s1执行任务

```
15/12/23 11:45:49 INFO BlockManagerInfo: Added broadcast_1_piece0 in memory on s1:43030 (size: 42.8 KB, free: 2.1 GB)
15/12/23 11:45:49 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on s1:43030 (size: 13.9 KB, free: 2.1 GB)
15/12/23 11:45:50 INFO TaskSetManager: Finished task 2.0 in stage 0.0 (TID 2) in 11123 ms on s1 (1/20)
15/12/23 11:45:50 INFO TaskSetManager: Finished task 4.0 in stage 0.0 (TID 4) in 11124 ms on s1 (2/20)
15/12/23 11:45:50 INFO TaskSetManager: Finished task 0.0 in stage 0.0 (TID 0) in 11155 ms on s1 (3/20)
15/12/23 11:45:51 INFO TaskSetManager: Starting task 6.0 in stage 0.0 (TID 6, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:51 INFO TaskSetManager: Starting task 7.0 in stage 0.0 (TID 7, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:51 INFO TaskSetManager: Starting task 8.0 in stage 0.0 (TID 8, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:51 INFO TaskSetManager: Finished task 8.0 in stage 0.0 (TID 8) in 147 ms on s1 (4/20)
15/12/23 11:45:51 INFO TaskSetManager: Finished task 6.0 in stage 0.0 (TID 6) in 150 ms on s1 (5/20)
15/12/23 11:45:51 INFO TaskSetManager: Finished task 7.0 in stage 0.0 (TID 7) in 150 ms on s1 (6/20)
15/12/23 11:45:52 INFO TaskSetManager: Starting task 9.0 in stage 0.0 (TID 9, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:52 INFO TaskSetManager: Starting task 10.0 in stage 0.0 (TID 10, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:52 INFO TaskSetManager: Starting task 11.0 in stage 0.0 (TID 11, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:52 INFO TaskSetManager: Finished task 9.0 in stage 0.0 (TID 9) in 120 ms on s1 (7/20)
15/12/23 11:45:52 INFO TaskSetManager: Finished task 10.0 in stage 0.0 (TID 10) in 121 ms on s1 (8/20)
15/12/23 11:45:52 INFO TaskSetManager: Finished task 11.0 in stage 0.0 (TID 11) in 121 ms on s1 (9/20)
```

s3执行任务

```
15/12/23 11:45:53 INFO BlockManagerInfo: Added broadcast_1_piece0 in memory on s3:59907 (size: 42.8 KB, free: 2.1 GB)
15/12/23 11:45:54 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on s3:59907 (size: 13.9 KB, free: 2.1 GB)
15/12/23 11:45:54 INFO TaskSetManager: Starting task 12.0 in stage 0.0 (TID 12, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:54 INFO TaskSetManager: Starting task 13.0 in stage 0.0 (TID 13, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:54 INFO TaskSetManager: Starting task 14.0 in stage 0.0 (TID 14, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:54 INFO TaskSetManager: Finished task 12.0 in stage 0.0 (TID 12) in 80 ms on s1 (10/20)
15/12/23 11:45:54 INFO TaskSetManager: Finished task 13.0 in stage 0.0 (TID 13) in 87 ms on s1 (11/20)
15/12/23 11:45:54 INFO TaskSetManager: Finished task 14.0 in stage 0.0 (TID 14) in 95 ms on s1 (12/20)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 5.0 in stage 0.0 (TID 5) in 15472 ms on s3 (13/20)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 1.0 in stage 0.0 (TID 1) in 15483 ms on s3 (14/20)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 3.0 in stage 0.0 (TID 3) in 15477 ms on s3 (15/20)
15/12/23 11:45:55 INFO TaskSetManager: Starting task 15.0 in stage 0.0 (TID 15, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:55 INFO TaskSetManager: Starting task 16.0 in stage 0.0 (TID 16, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:55 INFO TaskSetManager: Starting task 17.0 in stage 0.0 (TID 17, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 16.0 in stage 0.0 (TID 16) in 87 ms on s1 (16/20)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 17.0 in stage 0.0 (TID 17) in 88 ms on s1 (17/20)
15/12/23 11:45:55 INFO TaskSetManager: Finished task 15.0 in stage 0.0 (TID 15) in 90 ms on s1 (18/20)
15/12/23 11:45:56 INFO TaskSetManager: Starting task 18.0 in stage 0.0 (TID 18, s3, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:56 INFO TaskSetManager: Starting task 19.0 in stage 0.0 (TID 19, s1, PROCESS_LOCAL, 2217 bytes)
15/12/23 11:45:56 INFO TaskSetManager: Finished task 19.0 in stage 0.0 (TID 19) in 54 ms on s1 (19/20)
15/12/23 11:45:56 INFO TaskSetManager: Finished task 18.0 in stage 0.0 (TID 18) in 105 ms on s3 (20/20)
```

```
15/12/23 11:45:56 INFO DAGScheduler: ResultStage 0 (saveAsTextFile at TransformWork.java:105) finished in 16.728 s
15/12/23 11:45:56 INFO TaskSchedulerImpl: Removed TaskSet 0.0, whose tasks have all completed, from pool
15/12/23 11:45:56 INFO DAGScheduler: Job 0 finished: saveAsTextFile at TransformWork.java:105, took 16.929373 s
```

善后

```
15/12/23 11:45:56 INFO SparkContext: Invoking stop() from shutdown hook
15/12/23 11:45:56 INFO SparkUI: Stopped Spark web UI at http://10.101.91.65:4040
15/12/23 11:45:56 INFO DAGScheduler: Stopping DAGScheduler
I1223 11:45:56.449374  1098 sched.cpp:1771] Asked to stop the driver
I1223 11:45:56.449519  1070 sched.cpp:1040] Stopping framework '0a66c678-3b16-4b56-be64-0b38f1434fb8-0001'
15/12/23 11:45:56 INFO MesosSchedulerBackend: driver.run() returned with code DRIVER_STOPPED
15/12/23 11:45:56 INFO MapOutputTrackerMasterEndpoint: MapOutputTrackerMasterEndpoint stopped!
15/12/23 11:45:56 INFO MemoryStore: MemoryStore cleared
15/12/23 11:45:56 INFO BlockManager: BlockManager stopped
15/12/23 11:45:56 INFO BlockManagerMaster: BlockManagerMaster stopped
15/12/23 11:45:56 INFO OutputCommitCoordinator$OutputCommitCoordinatorEndpoint: OutputCommitCoordinator stopped!
15/12/23 11:45:56 INFO SparkContext: Successfully stopped SparkContext
15/12/23 11:45:56 INFO ShutdownHookManager: Shutdown hook called
15/12/23 11:45:56 INFO ShutdownHookManager: Deleting directory /tmp/spark-4f3cf098-ed9f-43be-ae7c-a1379893590a
```
