```
sudo apt-get update
sudo apt-get install mysql-server

sudo nano /etc/mysql/my.cnf
	bind-address=192.168.0.200

mysql -uroot -proot
mysql> grant all privileges on *.* to root@"%" identified by "root" with grant option;
mysql> flush privileges;
mysql> quit

sudo service mysql restart

mysql -V
mysql  Ver 14.14 Distrib 5.5.29, for debian-linux-gnu (x86_64) using readline 6.2
```

### root anywhere
```
[lu.hl@v125049070 cnpmjs.org]$ mysql -uroot
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 3
Server version: 5.0.77 Source distribution

Type 'help;' or '\h' for help. Type '\c' to clear the buffer.

mysql> SET PASSWORD FOR 'root'@'localhost' = PASSWORD('root');
Query OK, 0 rows affected (0.00 sec)

mysql> grant all privileges on *.* to root@"%" identified by "root" with grant option;
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

mysql> quit
```