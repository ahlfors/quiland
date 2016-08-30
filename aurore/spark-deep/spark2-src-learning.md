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
        } else {
          org.apache.spark.repl.Main.createSparkSession()
        }
      @transient val sc = {
        val _sc = spark.sparkContext
        _sc.uiWebUrl.foreach(webUrl => println(s"Spark context Web UI available at ${webUrl}"))
        println("Spark context available as 'sc' " +
          s"(master = ${_sc.master}, app id = ${_sc.applicationId}).")
        println("Spark session available as 'spark'.")
        _sc
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

spark/**core**/src/main/scala/org/apache/spark/SparkEnv.scala