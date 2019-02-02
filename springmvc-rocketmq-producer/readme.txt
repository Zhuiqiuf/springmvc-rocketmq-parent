
1.单机版
QuartzSchedule.java 执行多个任务；
    /* 停止任务：实战中通过controller中暂停，或停止任务；例如：
     * @Autowired SchedulerFactoryBean bean;
     * // 停止指定的任务
     * bean.getScheduler().pauseJob(new JobKey("taskname-jobDetail-1", "group1"));
     * // 停止所有任务
     * bean.stop();
     */

2.集群版实现
QuartzScheduleCluster
执行集群版首先开启配置文件中org.quartz.jobStore.isClustered=true，然后注释掉单机版bean。

3.测试
IDEA在Terminal中spring boot执行打包命令：
mvn clean package
cd tagert
java -jar
java -jar springboot-quartz-0.0.1-SNAPSHOT.jar --server.port 8888
然后添加一个Terminal
java -jar springboot-quartz-0.0.1-SNAPSHOT.jar --server.port 8889

观察输出结果，同一个时刻是否只有一个程序执行同一个任务。

注意：运行spring mvc项目，先访问一个控制器触发servlet，这样会激活quartz.

4、Q&A
1.运行集群注意事项

Q:quartz集群执行任务报错Caused by: java.lang.ClassNotFoundException: com.example.springbootquartz.component.ClusterJob

A:集群分布式执行任务时，调度任务信息都保存在数据库中，在执行任务时，目标任务类的限定名（包名+类名）
也会保存到数据库中，因此各服务节点目标任务类的限定名都要相同，否则运行后不能实现集群功能。





其他
===================================================================
一、log4j2配置：
    当前项目使用了log4j2，配置文件在spring boot下直接可以使用，程序运行后，项目中会生成日志文件；
但是使用spring mvc项目，需要修改log4j2.xml中日志的默认路径，将所有./logs/替换为d:/logs/,否则
在windows环境下不能正常生成日志文件。

测试地址：
jsp主页
http://localhost:8080
登录页
http://localhost:8080/login
商品页
http://localhost:8080/goods/query
http://localhost:8080/goods/choosequery?goodsName=
http://localhost:8080/goods/choosequery?goodsName=%E8%8B%B9%E6%9E%9C

1.使用jdbc实现三层示例代码；
2.实现三层junit单元测试；
3.junit测试事务：
    3.1 声明式事务配合@Transactional注解测试事务
    UserServiceImplTest中addWithTx()方法，使用声明式事务配合@Transactional注解测试事务；

    3.2 声明式事务通过xml配置测试事务
    UserServiceImplTest中addWithTxFromXml()方法，通过spring扫描xml，根据包名和方法前缀
    扫描正则匹配到的方法，加入到事务中，测试事务；执行事务操作，无需使用@Transactional注解。

    3.3 编程式事务
    UserServiceImplTest中addWithTxUsingCode()方法，通过TransactionUtil工具类封装PlatformTransactionManager，
    结果Lambda表达式处理传递的业务方法。

