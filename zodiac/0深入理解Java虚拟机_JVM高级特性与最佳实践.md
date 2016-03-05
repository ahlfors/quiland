VisualVM is available in two distributions: VisualVM available at visualvm.java.net and Java VisualVM available as a JDK tool in Oracle/Sun JDK distributions starting from JDK 6 update 7 and Apple's Java for Mac OS X 10.5 Update 4 (see the Releases Overview page).
http://visualvm.java.net/download.html

## JVM

### 1 程序计数器
程序计数器（Program Counter Register）是一块较小的内存空间，它的作用可以看做是**当前线程所执行的字节码的行号指示器**。在虚拟机的概念模型里（仅是概念模型，各种虚拟机可能会通过一些更高效的方式去实现），字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。

由于Java虚拟机的多线程是通过线程轮流切换并分配处理器执行时间的方式来实现的，在任何一个确定的时刻，一个处理器（对于多核处理器来说是一个内核）只会执行一条线程中的指令。因此，为了线程切换后能恢复到正确的执行位置，每条线程都需要有一个独立的程序计数器，**各条线程之间的计数器互不影响，独立存储，我们称这类内存区域为“线程私有”的内存。
**

如果线程正在执行的是一个Java方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；如果正在执行的是Natvie方法，这个计数器值则为空（Undefined）。此内存区域是唯一一个在Java虚拟机规范中没有规定任何OutOfMemoryError情况的区域。

> 以上描述截取自：
《深入理解Java虚拟机:JVM高级特性与最佳实践》 作者： 周志明


Each thread of a running program has its own pc register, or program counter, which is created when the thread is started. The pc register is one word in size, so it can hold both a native pointer and a returnValue. As a thread executes a Java method, the pc register contains the address of the current instruction being executed by the thread. An "address" can be a native pointer or an offset from the beginning of a method's bytecodes. If a thread is executing a native method, the value of the pc register is undefined.

对于一个运行中的Java程序而言，其中的每一个线程都有它自己的PC（程序计数器），在线程启动时创建。大小是一个字长。因此它既能持有一个本地指针，也能够持有一个returnAddress。当线程执行某个Java方法时，PC的内容总是下一条将被指向指令的“地址”。这里的“地址”可以是一个本地指针，也可以是在方法字节码中相对于该方法起始指令的偏移量。如果该线程正在执行一个本地方法，那么此时PC寄存器的值为”undefined”。

> 以上描述截取自：
《Inside the Java Virtual Machine 2nd Edition》 作者：Bill Venners


### 2 Java虚拟机栈
与程序计数器一样，Java虚拟机栈（Java Virtual Machine Stacks）也是线程私有的，它的生命周期与线程相同。虚拟机栈描述的是Java方法执行的内存模型：**每个方法被执行的时候**都会同时创建一个**栈帧（Stack Frame）用于存储局部变量表、操作栈、动态链接、方法出口等信息**。每一个方法被调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中**从入栈到出栈**的过程。

经常有人把Java内存区分为堆内存（Heap）和栈内存（Stack），这种分法比较粗糙，Java内存区域的划分实际上远比这复杂。这种划分方式的流行只能说明大多数程序员最关注的、与对象内存分配关系最密切的内存区域是这两块。其中所指的“堆”在后面会专门讲述，而所指的“栈”就是现在讲的虚拟机栈，或者说是虚拟机栈中的局部变量表部分。

**局部变量表**存放了:
- 编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）
- 对象引用（reference类型，它不等同于对象本身，根据不同的虚拟机实现，它可能是一个指向对象起始地址的引用指针，也可能指向一个代表对象的句柄或者其他与此对象相关的位置）
- returnAddress类型（指向了一条字节码指令的地址）。

其中64位长度的long和double类型的数据会占用2个局部变量空间（Slot），其余的数据类型只占用1个。局部变量表所需的内存空间在编译期间完成分配，当进入一个方法时，这个方法需要在帧中分配多大的局部变量空间是完全确定的，在方法运行期间不会改变局部变量表的大小。

在Java虚拟机规范中，对这个区域规定了两种异常状况：如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展（当前大部分的Java虚拟机都可动态扩展，只不过Java虚拟机规范中也允许固定长度的虚拟机栈），当扩展时无法申请到足够的内存时会抛出OutOfMemoryError异常。

>
- 线程对应栈
- 方法对应栈帧

### 3 本地方法栈
本地方法栈（Native Method Stacks）与虚拟机栈所发挥的作用是非常相似的，其区别不过是虚拟机栈为虚拟机执行Java方法（也就是字节码）服务，而本地方法栈则是为虚拟机使用到的**Native方法**服务。虚拟机规范中对本地方法栈中的方法使用的语言、使用方式与数据结构并没有强制规定，因此具体的虚拟机可以自由实现它。甚至有的虚拟机（譬如Sun HotSpot虚拟机）直接就把本地方法栈和虚拟机栈合二为一。与虚拟机栈一样，本地方法栈区域也会抛出StackOverflowError和OutOfMemoryError异常。

### 4 Java堆
对于大多数应用来说，Java堆（Java Heap）是Java虚拟机所管理的内存中最大的一块。Java堆是**被所有线程共享**的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存。这一点在Java虚拟机规范中的描述是：所有的**对象实例以及数组**都要在堆上分配，但是随着JIT编译器的发展与逃逸分析技术的逐渐成熟，栈上分配、标量替换优化技术将会导致一些微妙的变化发生，所有的对象都分配在堆上也渐渐变得不是那么“绝对”了。

Java堆是垃圾收集器管理的主要区域，因此很多时候也被称做“GC堆”（Garbage Collected Heap，幸好国内没翻译成“垃圾堆”）。如果从内存回收的角度看，由于现在收集器基本都是采用的分代收集算法，所以Java堆中还可以细分为：新生代和老年代；再细致一点的有Eden空间、From Survivor空间、To Survivor空间等。如果从内存分配的角度看，线程共享的Java堆中可能划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer，TLAB）。不过，无论如何划分，都与存放内容无关，无论哪个区域，存储的都仍然是对象实例，进一步划分的目的是为了更好地回收内存，或者更快地分配内存。在本章中，我们仅仅针对内存区域的作用进行讨论，Java堆中的上述各个区域的分配和回收等细节将会是下一章的主题。

根据Java虚拟机规范的规定，Java堆可以处于物理上不连续的内存空间中，只要逻辑上是连续的即可，就像我们的磁盘空间一样。在实现时，既可以实现成固定大小的，也可以是可扩展的，不过当前主流的虚拟机都是按照可扩展来实现的（通过-Xmx和-Xms控制）。如果在堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出OutOfMemoryError异常。

### 5 方法区

方法区（Method Area）与Java堆一样，是**各个线程共享**的内存区域，它用于存储**已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码**等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap（非堆），目的应该是与Java堆区分开来。

对于习惯在HotSpot虚拟机上开发和部署程序的开发者来说，很多人愿意把方法区称为“永久代”（Permanent Generation），本质上两者并不等价，仅仅是因为HotSpot虚拟机的设计团队选择把GC分代收集扩展至方法区，或者说使用永久代来实现方法区而已。对于其他虚拟机（如BEA JRockit、IBM J9等）来说是不存在永久代的概念的。即使是HotSpot虚拟机本身，根据官方发布的路线图信息，现在也有放弃永久代并“搬家”至Native Memory来实现方法区的规划了。

Java虚拟机规范对这个区域的限制非常宽松，除了和Java堆一样不需要连续的内存和可以选择固定大小或者可扩展外，还可以选择不实现垃圾收集。相对而言，垃圾收集行为在这个区域是比较少出现的，但并非数据进入了方法区就如永久代的名字一样“永久”存在了。这个区域的内存回收目标主要是针对常量池的回收和对类型的卸载，一般来说这个区域的回收“成绩”比较难以令人满意，尤其是类型的卸载，条件相当苛刻，但是这部分区域的回收确实是有必要的。在Sun公司的BUG列表中，曾出现过的若干个严重的BUG就是由于低版本的HotSpot虚拟机对此区域未完全回收而导致内存泄漏。

根据Java虚拟机规范的规定，当方法区无法满足内存分配需求时，将抛出OutOfMemoryError异常。

### 6 运行时常量池

运行时常量池（Runtime Constant Pool）是**方法区的一部分**。Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是**常量池（Constant Pool Table）**，用于存放**编译期生成的各种字面量和符号引用**，这部分内容将在类加载后存放到方法区的运行时常量池中。

Java虚拟机对Class文件的每一部分（自然也包括常量池）的格式都有严格的规定，每一个字节用于存储哪种数据都必须符合规范上的要求，这样才会被虚拟机认可、装载和执行。但对于运行时常量池，Java虚拟机规范没有做任何细节的要求，不同的提供商实现的虚拟机可以按照自己的需要来实现这个内存区域。不过，一般来说，除了保存Class文件中描述的**符号引用**外，还会把翻译出来的直接引用也存储在运行时常量池中。

运行时常量池相对于Class文件常量池的另外一个重要特征是具备动态性，Java语言并不要求常量一定只能在编译期产生，也就是并非预置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被开发人员利用得比较多的便是**String类的intern()方法**。

既然运行时常量池是方法区的一部分，自然会受到方法区内存的限制，当常量池无法再申请到内存时会抛出OutOfMemoryError异常。

### 7 直接内存

直接内存（Direct Memory）并不是虚拟机运行时数据区的一部分，也不是Java虚拟机规范中定义的内存区域，但是这部分内存也被频繁地使用，而且也可能导致OutOfMemoryError异常出现，所以我们放到这里一起讲解。

在JDK 1.4中新加入了NIO（New Input/Output）类，引入了一种基于通道（Channel）与缓冲区（Buffer）的I/O方式，它可以使用Native函数库直接分配堆外内存，然后通过一个存储在Java堆里面的DirectByteBuffer对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在Java堆和Native堆中来回复制数据。

显然，本机直接内存的分配不会受到Java堆大小的限制，但是，既然是内存，则肯定还是会受到本机总内存（包括RAM及SWAP区或者分页文件）的大小及处理器寻址空间的限制。服务器管理员配置虚拟机参数时，一般会根据实际内存设置-Xmx等参数信息，但经常会忽略掉直接内存，使得各个内存区域的总和大于物理内存限制（包括物理上的和操作系统级的限制），从而导致动态扩展时出现OutOfMemoryError异常。

## 监控与故障处理

```sh
[lu.hl@v125193052 bin]$ java -version
java version "1.7.0_51"
Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
OpenJDK (Alibaba) 64-Bit Server VM (build 24.45-b08-internal, mixed mode)

[lu.hl@v125193052 ~]$ echo $JAVA_HOME
/usr/alibaba/java

[lu.hl@v125193052 ~]$ cd /usr/alibaba/java/bin/
[lu.hl@v125193052 bin]$ ls
jconsole jvisualvm
jinfo jps jstat jmap jstatd jhat jstack ……
```

### 1 jps
虚拟机进程状态工具

- 本地虚拟机唯一ID（LVMID, Local Virtual Machine Identifier）
- 操作系统进程ID（PID, Process Identifier）

```sh
[lu.hl@v125193052 bin]$ sudo jps -lvm
22025 ../lib/gaea.env.agent-0.0.1-SNAPSHOT.jar -Dfile.encoding=utf-8
22041 sun.tools.jps.Jps -lvm -Dapplication.home=/opt/taobao/install/jdk-1.7.0_51 -Xms8m
14883 /usr/alibaba/jetty/start.jar --ini=/home/admin/web-deploy/jetty_server/conf/start.ini -Xms2048m -Xmx2048m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m -XX:MaxPermSize=256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Ddatabase.codeset=ISO-8859-1 -Ddatabase.logging=false -Dsun.rmi.transport.tcp.responseTimeout=60000 -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dapplication.codeset=UTF-8 -Djava.util.logging.config.file=/home/admin/web-deploy/conf/general/logging.properties -Dorg.eclipse.jetty.util.URI.charset=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8088,server=y,suspend=n -Djetty.logs=/home/admin/web-deploy/jetty_server/logs -Djetty.home=/usr/alibaba/jetty -Djava.io.tmpdir=/home/admin/web-deploy/jetty_server/tmp
2941 ../lib/gaea.env.agent-0.0.1-SNAPSHOT.jar -Dfile.encoding=utf-8
```

- -l 列出主类名或者jar路径
- -v 列出显式设定的参数
- -m 列出传递给main的参数

```sh
[lu.hl@v125193052 bin]$ sudo ps -ef | grep java
root      2941     1  0  2014 ?        07:05:56 java -Dfile.encoding=utf-8 -jar ../lib/gaea.env.agent-0.0.1-SNAPSHOT.jar
admin    14883     1  1 Apr09 ?        01:10:05 /usr/alibaba/java/bin/java -server -Xms2048m -Xmx2048m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m -XX:MaxPermSize=256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Ddatabase.codeset=ISO-8859-1 -Ddatabase.logging=false -Dsun.rmi.transport.tcp.responseTimeout=60000 -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dapplication.codeset=UTF-8 -Djava.util.logging.config.file=/home/admin/web-deploy/conf/general/logging.properties -Dorg.eclipse.jetty.util.URI.charset=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8088,server=y,suspend=n -Djetty.logs=/home/admin/web-deploy/jetty_server/logs -Djetty.home=/usr/alibaba/jetty -Djava.io.tmpdir=/home/admin/web-deploy/jetty_server/tmp -jar /usr/alibaba/jetty/start.jar --ini=/home/admin/web-deploy/jetty_server/conf/start.ini
lu.hl    22488  5984  0 23:46 pts/0    00:00:00 grep java
```

```sh
[lu.hl@v125193052 bin]$ sudo netstat -lpn|grep 8080 | awk {'print $7}'| awk -F '/' {'print $1'}
14883
```

### 2 jstat
统计监控工具 Java Virtual Machine Statistics Monitoring Tool

#### 1 间隔500毫秒、执行10次，打印类加载、卸载的数量、占用空间和类装载耗时

```sh
[lu.hl@v125193052 bin]$ sudo jstat -class 14883
Loaded  Bytes  Unloaded  Bytes     Time
 13307 25191.0        0     0.0      37.27

[lu.hl@v125193052 bin]$ sudo jstat -class 14883 500 10
Loaded  Bytes  Unloaded  Bytes     Time
 13307 25191.0        0     0.0      37.27
 ……
```

#### 2 间隔10秒、执行5次，打印堆状态

```sh
[lu.hl@v125193052 bin]$ sudo jstat -gcutil 14883 10000 5
  S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT
  0.00  11.57  87.65  21.43  28.94   1283   24.406     0    0.000   24.406
  0.00  11.57  93.01  21.43  28.94   1283   24.406     0    0.000   24.406
  0.00  11.57  97.17  21.43  28.94   1283   24.406     0    0.000   24.406
 11.46   0.00   3.00  21.44  28.94   1284   24.422     0    0.000   24.422
 11.46   0.00   7.22  21.44  28.94   1284   24.422     0    0.000   24.422
```
永久代占用了28.94%的空间

- S0 Survivor区
- S1 Survivor区
- E 新生代-Eden区
- O 老年代-Old区
- P 永久代-Permanent
- YGC 新生代GC
- YGCT 新生代GC耗时秒数
- FGC Full GC
- FGCT Full GC耗时秒数
- GCT 所有GC总耗时秒数

#### 3 打印实时编译的数量和耗时
```sh
[lu.hl@v125193052 bin]$ sudo jstat -compiler 14883
Compiled Failed Invalid   Time   FailedType FailedMethod
    4529      0       0    82.57          0
```

### 3 jinfo
查看和调整参数

#### 1 打印虚拟机参数值

```sh
[lu.hl@v125193052 bin]$ sudo jinfo -J-d64 -flags 14883
Attaching to process ID 14883, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.45-b08-internal

-Xms2048m -Xmx2048m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m -XX:MaxPermSize=256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Ddatabase.codeset=ISO-8859-1 -Ddatabase.logging=false -Dsun.rmi.transport.tcp.responseTimeout=60000 -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dapplication.codeset=UTF-8 -Djava.util.logging.config.file=/home/admin/web-deploy/conf/general/logging.properties -Dorg.eclipse.jetty.util.URI.charset=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8088,server=y,suspend=n -Djetty.logs=/home/admin/web-deploy/jetty_server/logs -Djetty.home=/usr/alibaba/jetty -Djava.io.tmpdir=/home/admin/web-deploy/jetty_server/tmp
```

- 堆初始值 InitialHeapSize
- 堆最大值 MaxHeapSize
- 新生代初始值 NewSize
- 新生代最大值 MaxNewSize
- 永久代初始值 PermSize
- 永久代最大值 MaxPermSize

直接内存 DirectMemorySize

#### 2 打印系统环境变量

```sh
sudo jinfo -J-d64 -sysprops 14883
sudo jinfo -sysprops 14883
```

```
JVM version is 24.45-b08-internal
java.runtime.name = Java(TM) SE Runtime Environment
jna.platform.library.path = /usr/lib64:/lib64
sun.rmi.transport.tcp.responseTimeout = 60000
java.vm.version = 24.45-b08-internal
sun.boot.library.path = /opt/taobao/install/jdk-1.7.0_51/jre/lib/amd64
java.vendor.url = http://java.oracle.com/
java.vm.vendor = Oracle Corporation
path.separator = :
file.encoding.pkg = sun.io
java.vm.name = OpenJDK (Alibaba) 64-Bit Server VM
java.util.logging.config.file = /home/admin/web-deploy/conf/general/logging.properties
sun.os.patch.level = unknown
sun.java.launcher = SUN_STANDARD
user.country = CN
org.eclipse.jetty.util.URI.charset = UTF-8
user.dir = /usr/alibaba/install/jetty-distribution-7.2.0
java.vm.specification.name = Java Virtual Machine Specification
database.logging = false
java.runtime.version = 1.7.0_51-b13
java.awt.graphicsenv = sun.awt.X11GraphicsEnvironment
os.arch = amd64
java.endorsed.dirs = /opt/taobao/install/jdk-1.7.0_51/jre/lib/endorsed
line.separator =

java.io.tmpdir = /home/admin/web-deploy/jetty_server/tmp
java.vm.specification.vendor = Oracle Corporation
os.name = Linux
sun.jnu.encoding = GB18030
java.library.path = /usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
java.class.version = 51.0
java.specification.name = Java Platform API Specification
java.net.preferIPv4Stack = true
jetty.home = /usr/alibaba/install/jetty-distribution-7.2.0
org.eclipse.jetty.server.Request.maxFormContentSize = -1
sun.management.compiler = HotSpot 64-Bit Tiered Compilers
sun.net.client.defaultReadTimeout = 30000
os.version = 2.6.32-220.23.2.ali927.el5.x86_64
application.codeset = UTF-8
user.home = /home/admin
user.timezone = US/Pacific
java.awt.printerjob = sun.print.PSPrinterJob
file.encoding = GB18030
java.specification.version = 1.7
user.name = admin
java.class.path = /home/admin/web-deploy/jetty_server/ext:/home/admin/web-deploy/jetty_server/ext/jetty-server-extended.jar:/home/admin/web-deploy/jetty_server/ext/log4j-1.2.12.jar:/home/admin/web-deploy/jetty_server/ext/slf4j-api-1.4.3.jar:/home/admin/web-deploy/jetty_server/ext/slf4j-log4j12-1.4.3.jar:/home/admin/web-deploy/jetty_server/ext/xercesImpl-2.9.1.jar:/home/admin/web-deploy/jetty_server/ext/xml-apis-1.3.04.jar:/home/admin/web-deploy/jetty_server/ext/xml-resolver-1.2.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-xml-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/servlet-api-2.5.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-http-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-continuation-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-server-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-security-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-servlet-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-webapp-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-deploy-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-servlets-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/com.sun.el_1.0.0.v201004190952.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/ecj-3.6.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/javax.el_2.1.0.v201004190952.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/javax.servlet.jsp_2.1.0.v201004190952.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/javax.servlet.jsp.jstl_1.2.0.v201004190952.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/jetty-jsp-2.1-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/org.apache.jasper.glassfish_2.1.0.v201007080150.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jsp/org.apache.taglibs.standard.glassfish_1.2.0.v201004190952.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-util-7.2.0.v20101020.jar:/usr/alibaba/install/jetty-distribution-7.2.0/lib/jetty-io-7.2.0.v20101020.jar
java.vm.specification.version = 1.7
sun.net.client.defaultConnectTimeout = 30000
sun.arch.data.model = 64
sun.java.command = /usr/alibaba/jetty/start.jar --ini=/home/admin/web-deploy/jetty_server/conf/start.ini
java.home = /opt/taobao/install/jdk-1.7.0_51/jre
user.language = zh
java.specification.vendor = Oracle Corporation
awt.toolkit = sun.awt.X11.XToolkit
java.vm.info = mixed mode
java.version = 1.7.0_51
java.ext.dirs = /opt/taobao/install/jdk-1.7.0_51/jre/lib/ext:/usr/java/packages/lib/ext
sun.boot.class.path = /opt/taobao/install/jdk-1.7.0_51/jre/lib/taobao-patch.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/resources.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/rt.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/sunrsasign.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/jsse.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/jce.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/charsets.jar:/opt/taobao/install/jdk-1.7.0_51/jre/lib/jfr.jar:/opt/taobao/install/jdk-1.7.0_51/jre/classes
database.codeset = ISO-8859-1
java.awt.headless = true
java.vendor = Oracle Corporation
file.separator = /
java.vendor.url.bug = http://bugreport.sun.com/bugreport/
sun.io.unicode.encoding = UnicodeLittle
sun.rmi.transport.connectTimeout = 2000
sun.cpu.endian = little
jetty.logs = /home/admin/web-deploy/jetty_server/logs
sun.cpu.isalist =
```

### 4 jmap和jhat
- jmap用于生成堆转储快照（heapdump或dump文件）（Memory Map for Java）

- jhat用于分析堆转储快照
> 一般较少使用

#### 1 打印堆详情

```sh
[lu.hl@v125193052 bin]$ sudo jmap -J-d64 -heap 14883

Attaching to process ID 14883, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.45-b08-internal

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

Heap Configuration:
   MinHeapFreeRatio = 40
   MaxHeapFreeRatio = 70
   MaxHeapSize      = 2147483648 (2048.0MB)
   NewSize          = 268435456 (256.0MB)
   MaxNewSize       = 268435456 (256.0MB)
   OldSize          = 5439488 (5.1875MB)
   NewRatio         = 2
   SurvivorRatio    = 8
   PermSize         = 268435456 (256.0MB)
   MaxPermSize      = 268435456 (256.0MB)
   G1HeapRegionSize = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 241631232 (230.4375MB)
   used     = 54057888 (51.553619384765625MB)
   free     = 187573344 (178.88388061523438MB)
   22.372061571908056% used
Eden Space:
   capacity = 214827008 (204.875MB)
   used     = 51027632 (48.66374206542969MB)
   free     = 163799376 (156.2112579345703MB)
   23.75289423571919% used
From Space:
   capacity = 26804224 (25.5625MB)
   used     = 3030256 (2.8898773193359375MB)
   free     = 23773968 (22.672622680664062MB)
   11.30514354752445% used
To Space:
   capacity = 26804224 (25.5625MB)
   used     = 0 (0.0MB)
   free     = 26804224 (25.5625MB)
   0.0% used
concurrent mark-sweep generation:
   capacity = 1879048192 (1792.0MB)
   used     = 410628864 (391.606201171875MB)
   free     = 1468419328 (1400.393798828125MB)
   21.853024618966238% used
Perm Generation:
   capacity = 268435456 (256.0MB)
   used     = 77677464 (74.0790023803711MB)
   free     = 190757992 (181.9209976196289MB)
   28.93711030483246% used
```

#### 2 打印堆中live实例直方图

```sh
sudo -uadmin jmap -histo:live 14883 > aegis.pivot.histo.txt
scp aegis.pivot.histo.txt erichan@10.18.212.171:/Users/erichan/快盘/+base/java_jvm/
```

```
num     #instances         #bytes  class name
----------------------------------------------
  1:         26679       49771552  [I
  2:        176098       27523592  [C
  3:         28686       18545832  [B
  4:        125148       18442344  <constMethodKlass>
  5:        125148       17033600  <methodKlass>
  6:         13128       14393768  <constantPoolKlass>
  7:         13128        9013936  <instanceKlassKlass>
  8:         11372        8466208  <constantPoolCacheKlass>
  9:          7903        4534384  <methodDataKlass>
 10:        168631        4047144  java.lang.String
 ……
 ```

#### 3 堆转储快照

```sh
[lu.hl@v125193052 admin]$ sudo -uadmin jmap -dump:format=b,file=aegis.pivot.bin 14883
sudo scp aegis.pivot.bin erichan@10.18.212.171:/Users/erichan/快盘/+base/java_jvm/
aegis.pivot.bin                                                                                                               100%  407MB   8.1MB/s   00:50

jhat -J-Xmx4096m -port 7000 aegis.pivot.bin
http://localhost:7000/histo/
http://localhost:7000/showInstanceCounts/
```

### 5 jstack
用于生成线程快照（threaddump或javacore文件）（Stack Trace for Java）

```sh
[lu.hl@v125193052 admin]$ sudo -uadmin jstack -l 14883 > aegis.pivot.stack.txt
[lu.hl@v125193052 admin]$ sudo scp aegis.pivot.stack.txt erichan@10.18.212.171:/Users/erichan/快盘/+base/java_jvm/
aegis.pivot.stack.txt                                                                                                         100%   62KB  62.3KB/s   00:00  
```

- -l 除队长外，显示关于锁的附加信息。

### 6 可视化工具
#### 1 jConsole

```sh
jconsole
```

#### 2 VisualVM_remote
The remote system must be configured to run the **jstatd daemon**.
http://docs.oracle.com/javase/7/docs/technotes/tools/share/jstatd.html

##### On Server

/home/lu.hl/vm.policy

```
grant codebase "file:${java.home}/../lib/tools.jar" {
   permission java.security.AllPermission;
};
```

```
jstatd -J-Djava.security.policy=/home/lu.hl/vm.policy
```

##### On Client

```sh
[erichan@Han-Yosemite ~]$ jps 10.125.193.52
16881 Jstatd
14883 start.jar
```

```sh
jvisualvm
```

> 信息不全 需要JMX辅助

#### 3 heapdump分析
```
jvisualvm -J-Xmx8192m
```
