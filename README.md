# zklock-spring-boot-starter

# 特别提示: 
zk锁的timeout时间设置受限于zookeeper(zkServer)服务使用的配置属性, 请根据实际需求修改zk服务端启动配置:
参考zoo.cfg配置项：
minSessionTimeout #最小的session time时间，默认值是2个tick time,客户端设置的session time 如果小于这个值，则会被强制协调为这个最小值
maxSessionTimeout #最大的session time时间，默认值是20个tick time,客户端设置的session time 如果大于这个值，则会被强制协调为这个最大值

简介
该项目主要利用Spring Boot的自动化配置特性来实现快速的将zookeeper分布式锁引入spring boot应用，简化原生使用curator的整合代码。

源码地址
GitHub：https://github.com/dawn9117/zklock-spring-boot-starter

小工具一枚，欢迎使用和Star支持，如使用过程中碰到问题，可以提出Issue，我会尽力完善该Starter

版本基础
curator：2.8.0
如何使用
在该项目的帮助下，我们的Spring Boot可以轻松的引入zk分布式锁，主需要做下面两个步骤：

1. 在pom.xml中引入依赖：
<dependency>
  <groupId>com.github.dawn9117</groupId>
  <artifactId>zklock-spring-boot-starter</artifactId>
  <version>1.0.1</version>
</dependency>

2. 在application.properties中添加以下配置
zk.registryAddress=XXX:2181 desc:(required)
zk.session-timeout=XXX  desc:(optional, default: 30000)
zk.connect-timeout=XXX  desc:(optional, default: 30000)
zk.lock-namespace=XXX   desc:(optional, default: locks)

3. 在需要加分布式锁的方法上增加@ZkLock注解
@ZkLock
public void test(){

}

4. 默认情况下使用不可重入共享锁, 锁名称为: zk.lock-namespace + 类全路径 + 方法名, 例如: /locks/com.github.dawn/test。

@ZkLock更细致的配置内容参考如下：

注解支持配置项全部如下
@ZkLock(path = "/locks/MyLockName", type = ZkSemaphoreLock.class, retry = 5, failedStrategy = RetryLockStrategy.class, timeout = 30, timeunit = TimeUnit.SECONDS)
path: 自定义锁路径
type: 指定锁类型, 可自己拓展(目前支持可重入共享锁, 不可重入共享锁)
retry: 配合RetryLockStrategy使用, 失败重试次数
failedStrategy: 加锁失败策略, 可自定义, 实现LockFailedStrategy接口
timeout: 锁连接超时时长
timeuint: 锁连接超时时间单位