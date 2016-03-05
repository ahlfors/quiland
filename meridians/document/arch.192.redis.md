### Installing
http://redis.io/download

	sudo wget http://redis.googlecode.com/files/redis-2.6.13.tar.gz -P /opt
	sudo tar zxvf /opt/redis-2.6.13.tar.gz -C /opt
	cd /opt/redis-2.6.13/
	sudo make
	sudo make install
	ls /usr/local/bin

### Running
https://github.com/antirez/redis

	cd /opt/redis-2.6.13/src/
    ./redis-server

		./redis-server &

### Playing with Redis
	cd /opt/redis-2.6.13/src/
	/opt/redis-2.6.13/src $ ./redis-cli
	redis 127.0.0.1:6379> ping
	PONG
	redis 127.0.0.1:6379> set foo bar
	OK
	redis 127.0.0.1:6379> get foo
	"bar"


### 指定配置文件
配置文件redis.conf在Redis根目录下。

	#修改daemonize为yes，即默认以后台程序方式运行
	daemonize no  
	#可修改默认监听端口  
	port 6379  
	#修改生成默认日志文件位置  
	logfile "/home/futeng/logs/redis.log"  
	#配置持久化文件存放位置  
	dir /home/futeng/data/redisData  

	./redis-server ./redis.conf  

### 开机自启动
启动脚本redis_init_script位于位于Redis的/utils/目录下。

	#大致浏览下该启动脚本，发现redis习惯性用监听的端口名作为配置文件等命名，我们后面也遵循这个约定。  
	#redis服务器监听的端口  
	REDISPORT=6379  
	#服务端所处位置，在make install后默认存放与`/usr/local/bin/redis-server`，如果未make install则需要修改该路径，下同。  
	EXEC=/usr/local/bin/redis-server  
	#客户端位置  
	CLIEXEC=/usr/local/bin/redis-cli  
	#Redis的PID文件位置  
	PIDFILE=/var/run/redis_${REDISPORT}.pid  
	#配置文件位置，需要修改  
	CONF="/etc/redis/${REDISPORT}.conf"  

	mkdir /etc/redis  
	cp redis.conf /etc/redis/6379.conf  
	cp redis_init_script /etc/init.d/redisd  

在启动脚本开头添加如下两行注释以修改其运行级别：
	#!/bin/sh  
	# chkconfig:   2345 90 10  
	# description:  Redis is a persistent key-value database  

设置为开机自启动服务器  
	chkconfig redisd on  

打开服务  
	service redisd start  

关闭服务  
	service redisd stop
