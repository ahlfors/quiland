## apache bench
```
ab -V
This is ApacheBench, Version 2.3 <$Revision: 1663405 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/
```

```
ab -c 10 -n 100 'http://10.189.195.163:8200/tab?q=%E6%9B%BE%E5%B0%8F%E7%BB%B8&c=1'
```

```
并发请求数量
Concurrency Level:      10
```

```
完成的请求次数
Complete requests:      100
失败的请求次数
Failed requests:        0
```

```
整个过程的持续时间
Time taken for tests:   22.131 seconds
```

```
整个过程的网络流量
Total transferred:      79600 bytes
整个过程的HTML网络流量
HTML transferred:       47400 bytes
```

```
吞吐率reqs/s 每秒请求数量（平均值）
Requests per second:    4.52 [#/sec] (mean)
```

```
Request per second=Complete requests/Time taken for tests
```

```
每请求延迟（平均值）TPS
Time per request:       2213.073 [ms] (mean)
Time per request:       221.307 [ms] (mean, across all concurrent requests)
```

```
Time per request=Time taken for tests/（Complete requests/Concurrency Level）
```

```
每秒流量
Transfer rate:         3.51 [Kbytes/sec] received
```

```
Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:       29   31   2.1     31      45
Processing:  1156 2143 355.9   2069    2789
Waiting:     1154 2139 353.8   2067    2789
Total:       1187 2175 355.7   2100    2823
```

```
用户请求延迟
Percentage of the requests served within a certain time (ms)
50%   2100
66%   2325
75%   2489
80%   2539
90%   2770
95%   2772
98%   2802
99%   2823
100%   2823 (longest request)
```
