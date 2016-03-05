```
sudo netstat -lpn | grep 8080

netstat - Print network connections, routing tables, interface statistics, masquerade connections, and multicast memberships

   --numeric , -n
       Show numerical addresses instead of trying to determine symbolic host, port or user names.

   -o, --timers
       Include information related to networking timers.

   -p, --program
       Show the PID and name of the program to which each socket belongs.

   -l, --listening
       Show only listening sockets.  (These are omitted by default.)

   -a, --all
       Show both listening and non-listening sockets.  With the --interfaces option, show interfaces that are not up
```

inux下netstat常用命令和参数如: 

```
# netstat -tunpl 
-t   tcp 
-u udp 
-n 只显示数字类地址,这将大大加快netstat的速度 
-l  正在监听 
```

而bsd或者macos下的netstat: 

```
#  netstat -f inet -n -p tcp 
-f address_family,  可以使inet, inet6 , unix 
-n Show network addresses as numbers 
-p protocol, 协议的名字可以在/etc/protocols中找到,通常用的有tcp,udp等
```