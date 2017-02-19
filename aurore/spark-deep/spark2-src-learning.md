bin/spark-shell
```
export SPARK_SUBMIT_OPTS
"${SPARK_HOME}"/bin/spark-submit --class org.apache.spark.repl.Main --name "Spark shell" "$@"
```
spark-submit
```
exec "${SPARK_HOME}"/bin/spark-class org.apache.spark.deploy.SparkSubmit "$@"
```
spark-class
```
RUNNER="${JAVA_HOME}/bin/java"
"$RUNNER" -Xmx128m -cp "$LAUNCH_CLASSPATH" org.apache.spark.launcher.Main "$@"
```

spark/**launcher**/src/main/java/org/apache/spark/launcher/Main.java
->
spark/launcher/src/main/java/org/apache/spark/launcher/SparkSubmitCommandBuilder.java

spark/**core**/src/main/scala/org/apache/spark/deploy/SparkSubmit.scala

spark/**repl**/scala-2.11/src/main/scala/org/apache/spark/repl/Main.scala
->
/Users/erichan/cooding/spark/repl/scala-2.11/src/main/scala/org/apache/spark/repl/SparkILoop.scala
```scala
def initializeSpark() {
  intp.beQuietDuring {
    processLine("""
      @transient val spark = if (org.apache.spark.repl.Main.sparkSession != null) {
          org.apache.spark.repl.Main.sparkSession
        }
      @transient val sc = {
        val _sc = spark.sparkContext
      }
      """)
    processLine("import org.apache.spark.SparkContext._")
    processLine("import spark.implicits._")
    processLine("import spark.sql")
    processLine("import org.apache.spark.sql.functions._")
    replayCommandStack = Nil // remove above commands from session history.
  }
}
```
spark/**core**/src/main/scala/org/apache/spark/SparkContext.scala

```scala
def this() = this(new SparkConf())
```
spark/**core**/src/main/scala/org/apache/spark/SparkEnv.scala
spark/**core**/src/main/scala/org/apache/spark/SparkConf.scala

```scala
  private[spark] def createSparkEnv(
      conf: SparkConf,
      isLocal: Boolean,
      listenerBus: LiveListenerBus): SparkEnv = {
    SparkEnv.createDriverEnv(conf, isLocal, listenerBus, SparkContext.numDriverCores(master))
  }
```
- spark/core/src/main/scala/org/apache/spark/scheduler/SchedulerBackend.scala
- spark/core/src/main/scala/org/apache/spark/scheduler/TaskScheduler.scala

```scala
private def createTaskScheduler(
      sc: SparkContext,
      master: String,
      deployMode: String): (SchedulerBackend, TaskScheduler) = {
    import SparkMasterRegex._

    master match {
      case "local"  LOCAL_N_REGEX(threads) LOCAL_N_FAILURES_REGEX(threads, maxFailures) =>
        val scheduler = new TaskSchedulerImpl
        val backend = new LocalSchedulerBackend
        scheduler.initialize(backend)
        (backend, scheduler)

      case SPARK_REGEX(sparkUrl) LOCAL_CLUSTER_REGEX(numSlaves, coresPerSlave, memoryPerSlave) =>
        val scheduler = new TaskSchedulerImpl(sc)
        val backend = new StandaloneSchedulerBackend

      case MESOS_REGEX(mesosUrl) =>
        MesosNativeLibrary.load()
        val scheduler = new TaskSchedulerImpl(sc)
        val backend = 
        new MesosCoarseGrainedSchedulerBackend
        new MesosFineGrainedSchedulerBackend

      case masterUrl => 
          val cm = getClusterManager(masterUrl) match {
          case Some(clusterMgr) => clusterMgr
          case None => throw new SparkException("Could not parse Master URL: '" + master + "'")
        }
          val scheduler = cm.createTaskScheduler(sc, masterUrl)
          val backend = cm.createSchedulerBackend(sc, masterUrl, scheduler)
  }
}
```
spark/core/src/main/scala/org/apache/spark/scheduler/DAGScheduler.scala


