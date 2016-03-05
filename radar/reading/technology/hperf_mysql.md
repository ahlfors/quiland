### MySQL Architecture
- Table locks
	When a client wishes to write to a table (insert, delete,update, etc.), it acquires a write lock.

- Row locks
	implemented in the storage engine, not the server
	is available in the InnoDB and Falcon storage engines, among others

	We often see applications that have been converted from MyISAM to InnoDB but are still using LOCK TABLES. This is no longer necessary because of row-level locking, and it can cause severe performance problems.

- 原一隔持
ACID(Atomicity, Consistency, Isolation, Durability)

  - 脏读取(DirtyReads)：
  事务和B并发执行，B执行更新后，A查询到B没有提交的数据，B事务回滚，则A事务得到的数据不是数据库中的真实数据。

  - 不可重复读取(Non-repeatableReads)：
  事务A和B并发执行，A查询数据，然后B更新该数据并提交，A再次查询时该数据已变化。

  - 幻读(PhantomReads)：也称为幻像(幻影)。
  事务A和B并发执行，A查询数据，B插入或者删除数据并提交，A再次查询,结果集中有以前没有的数据或者以前有的数据消失。

  - 隔离级别
    - 读未提交(Read uncommitted)
    - 读提交(read committed)
    - 可重复读(repeatable read)
    - 串行化(Serializable)

- Multiversion Concurrency Control(MVCC)

- The InnoDB Engine
	default engine in MySQL 5.5

- The MyISAM Engine
	As MySQL’s default storage engine in versions 5.1 and older
	doesn’t support transactions or row-level locks
	a nontransactional, non-crash-safe storage engine.

- Selecting the Right Engine
	Transactions
	Backups
	Crash recovery
	Special features

```ALTER TABLE mytable ENGINE = InnoDB;```

## Benchmarking

Measure
- Throughput
- Response time or latency
- Concurrency
- Scalability

### To gather data

```sh
#!/bin/sh
INTERVAL=5
PREFIX=$INTERVAL-sec-status
mysql -uroot -proot -e 'SHOW GLOBAL VARIABLES' >> mysql-variables
while true; do
  file=$(date +%F_%I)
  sleep=$(date +%s.%N | awk "{print $INTERVAL - (\$1 % $INTERVAL)}")
  sleep $sleep
  ts="$(date +"TS %s.%N %F %T")"
  loadavg="$(uptime)"
  echo "$ts $loadavg" >> $PREFIX-${file}-status
  mysql -uroot -proot -e 'SHOW GLOBAL STATUS' >> $PREFIX-${file}-status &
  echo "$ts $loadavg" >> $PREFIX-${file}-innodbstatus
  mysql -uroot -proot -e 'SHOW ENGINE INNODB STATUS\G' >> $PREFIX-${file}-innodbstatus &
  echo "$ts $loadavg" >> $PREFIX-${file}-processlist
  mysql -uroot -proot -e 'SHOW FULL PROCESSLIST\G' >> $PREFIX-${file}-processlist &
  echo $ts
done
```

### To analyze data
```sh
awk '
  BEGIN {
    printf "#ts date time load QPS";
    fmt = " %.2f";
  }
  /^TS/ { # The timestamp lines begin with TS.
    ts = substr($2, 1, index($2, ".") - 1);
    load = NF - 2;
    diff = ts - prev_ts;
    prev_ts = ts;
    printf "\n%s %s %s %s", ts, $3, $4, substr($load, 1, length($load)-1);
  }
  /Queries/ {
    printf fmt, ($2-Queries)/diff;
    Queries=$2
  }
  ' "$@"

./analyze.sh 5-sec-status-2015-04-11_05-status >> QPS-per-5-seconds
```

### To show result

- [gnuplot-5.0.0](http://jaist.dl.sourceforge.net/project/gnuplot/gnuplot/5.0.0/gnuplot-5.0.0.tar.gz)
- ```sudo apt-get install -y gnuplot```

```sh
gnuplot

plot "QPS-per-5-seconds" using 5 w lines title "QPS"
```

### Full-Stack Tools
- [ab](http://httpd.apache.org/docs/2.0/programs/ab.html)
- [http_load](http://www.acme.com/software/http_load/)
- [jmeter](http://jakarta.apache.org/jmeter/)

### Single-Component Tools
- [mysqlslap](http://dev.mysql.com/doc/refman/5.1/en/mysqlslap.html)
- sql-bench
- [Super Smack](http://vegan.net/tony/supersmack/)
- dbt2
- [sysbench](https://launchpad.net/sysbench)

### sysbench
```sh
sudo apt-get install sysbench
```
#### CPU
```sh
cat /proc/cpuinfo

model name	: Intel(R) Core(TM) i7-4850HQ CPU @ 2.30GHz
stepping	: 1
cpu MHz		: 2294.000
cache size	: 6144 KB
```

```sh
sysbench --test=cpu --cpu-max-prime=20000 run
sysbench 0.4.12:  multi-threaded system evaluation benchmark

Running the test with following options:
Number of threads: 1

Doing CPU performance benchmark

Threads started!
Done.

Maximum prime number checked in CPU test: 20000


Test execution summary:
    total time:                          26.4700s
    total number of events:              10000
    total time taken by event execution: 26.4680
    per-request statistics:
         min:                                  2.28ms
         avg:                                  2.65ms
         max:                                  7.07ms
         approx.  95 percentile:               3.77ms

Threads fairness:
    events (avg/stddev):           10000.0000/0.00
    execution time (avg/stddev):   26.4680/0.00
```

#### IO

```sh
sysbench --test=fileio --file-total-size=150G prepare
sysbench --test=fileio --file-total-size=150G --file-test-mode=rndrw/ --init-rng=on --max-time=300 --max-requests=0 run
sysbench --test=fileio --file-total-size=150G cleanup
```

#### OLTP

```sh
sysbench --test=oltp --oltp-table-size=1000000 --mysql-db=test/ --mysql-user=root prepare
```

## Profiling

pt-query-digesttool --processlist
pt-query-digest --type=tpcdump

### Using SHOW PROFILE

```sql
mysql> SET profiling = 1;
```
> disabled by default

```sql
SELECT * FROM pivot_da_sync_message_log;

mysql> SHOW PROFILES;
```

||||
|--:|--:|--:|
| Query_ID | Duration   | Query                                   |
|        2 | 0.05062200 | select * from pivot_da_sync_message_log |

```sh
mysql> SHOW PROFILE FOR QUERY 2;
```

| Status               | Duration |
|--:|--:|
| starting             | 0.000129 |
| checking permissions | 0.000033 |
| Opening tables       | 0.000058 |
| System lock          | 0.000018 |
| Table lock           | 0.000022 |
| init                 | 0.000034 |
| optimizing           | 0.000017 |
| statistics           | 0.000029 |
| preparing            | 0.000028 |
| executing            | 0.000017 |
| Sending data         | 0.048796 |
| end                  | 0.000024 |
| query end            | 0.000015 |
| freeing items        | 0.001368 |
| logging slow query   | 0.000019 |
| cleaning up          | 0.000015 |

16 rows in set (0.01 sec)


```sql
SET @query_id = 2;

SELECT STATE, SUM(DURATION) AS Total_R,
  ROUND(
    100 * SUM(DURATION) /
    (SELECT SUM(DURATION)
     FROM INFORMATION_SCHEMA.PROFILING
     WHERE QUERY_ID = @query_id
    ), 2) AS Pct_R,
  COUNT(*) AS Calls,
  SUM(DURATION) / COUNT(*) AS "R/Call"
FROM INFORMATION_SCHEMA.PROFILING
WHERE QUERY_ID = @query_id
GROUP BY STATE
ORDER BY Total_R DESC;
```

| STATE                | Total_R  | Pct_R | Calls | R/Call       |
|--:|--:|--:|--:|
| Sending data         | 0.048796 | 96.39 |     1 | 0.0487960000 |
| freeing items        | 0.001368 |  2.70 |     1 | 0.0013680000 |
| starting             | 0.000129 |  0.25 |     1 | 0.0001290000 |
| Opening tables       | 0.000058 |  0.11 |     1 | 0.0000580000 |
| init                 | 0.000034 |  0.07 |     1 | 0.0000340000 |
| checking permissions | 0.000033 |  0.07 |     1 | 0.0000330000 |
| statistics           | 0.000029 |  0.06 |     1 | 0.0000290000 |
| preparing            | 0.000028 |  0.06 |     1 | 0.0000280000 |
| end                  | 0.000024 |  0.05 |     1 | 0.0000240000 |
| Table lock           | 0.000022 |  0.04 |     1 | 0.0000220000 |
| logging slow query   | 0.000019 |  0.04 |     1 | 0.0000190000 |
| System lock          | 0.000018 |  0.04 |     1 | 0.0000180000 |
| executing            | 0.000017 |  0.03 |     1 | 0.0000170000 |
| optimizing           | 0.000017 |  0.03 |     1 | 0.0000170000 |
| cleaning up          | 0.000015 |  0.03 |     1 | 0.0000150000 |
| query end            | 0.000015 |  0.03 |     1 | 0.0000150000 |

16 rows in set (0.01 sec)

### Using SHOW STATUS
```sql
mysql> FLUSH STATUS;

mysql> select * from pivot_da_sync_message_log;

mysql> SHOW STATUS WHERE Variable_name LIKE 'Handler%'
    -> OR Variable_name LIKE 'Created%';
```

| Variable_name              | Value  |
|--|--|
| Created_tmp_disk_tables    | 0      |
| Created_tmp_files          | 171968 |
| Created_tmp_tables         | 6      |
| Handler_commit             | 2      |
| Handler_delete             | 0      |
| Handler_discover           | 0      |
| Handler_prepare            | 0      |
| Handler_read_first         | 2      |
| Handler_read_key           | 20     |
| Handler_read_next          | 0      |
| Handler_read_prev          | 0      |
| Handler_read_rnd           | 16     |
| Handler_read_rnd_next      | 761    |
| Handler_rollback           | 0      |
| Handler_savepoint          | 0      |
| Handler_savepoint_rollback | 0      |
| Handler_update             | 0      |
| Handler_write              | 610    |

18 rows in set (0.00 sec)

```sql
SHOW GLOBAL STATUS  WHERE Variable_name = 'Slow_queries';
```

| Variable_name       | Value   |
|--|--|
| Slow_queries        | 2290518 |

2 rows in set (0.00 sec)

### Using the slow query log

```sh
mysql -upivot -ppivot -h10.125.59.180 -e 'SHOW GLOBAL VARIABLES' >> pivot_mysql_variables
```
- back_log        100
- basedir /usr/alibaba/install/mysql-5.1.40sp1/
- datadir /data/mysqldata1/mydata/
- innodb_data_home_dir    /data/mysqldata1/innodb_ts
- general_log_file        /data/mysqldata1/sock/mysql.log
- log_error       /data/mysqldata1/log/error.log
- slow_query_log  ON
- slow_query_log_file     /data/mysqldata1/log/slow-query.log
- innodb_buffer_pool_size	3221225472

innodb_buffer_pool_pages_flushed

```
sudo awk '/^# Time:/{print $3, $4, c;c=0}/^# User/{c++}' /data/mysqldata1/log/slow-query.log
```

### Using SHOW GLOBAL STATUS

queries per second, Threads_connected, and Threads_running
```sh
mysqladmin -upivot -ppivot -h10.125.59.180 ext -i1 | awk '
/Queries/{q=$4-qp;qp=$4}
/Threads_connected/{tc=$4}
/Threads_running/{printf "%5d %5d %5d\n", q, tc, $4}'
```

### Using SHOW PROCESSLIST
```sh
mysql -upivot -ppivot -h10.125.59.180 -e 'SHOW PROCESSLIST\G' | grep State: | sort | uniq -c | sort -rn
```

```
[lu.hl@v125059180 ~]$ df -h
Filesystem            Size  Used Avail Use% Mounted on
/dev/xvda1            243G   44G  187G  19% /
tmpfs                 4.0G  184K  4.0G   1% /dev/shm
```
