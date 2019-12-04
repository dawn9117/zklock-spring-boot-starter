# zklock-spring-boot-starter

### 特别提示: 
ZK客户端连接超时时间和@ZkLock的timeout受限于zookeeper(zkServer)服务端使用的配置属性, 请根据实际需求修改zk服务端启动配置:<br/>
##### 参考zoo.cfg配置项：
* minSessionTimeout: 最小的session time时间，默认值是2个tick time,客户端设置的session time 如果小于这个值，则会被强制协调为这个最小值
* maxSessionTimeout: 最大的session time时间，默认值是20个tick time,客户端设置的session time 如果大于这个值，则会被强制协调为这个最大值

### 简介
该项目主要利用Spring Boot的自动化配置特性来实现快速的将zookeeper分布式锁引入spring boot应用，简化原生使用curator的整合代码。

### 源码地址
GitHub：https://github.com/dawn9117/zklock-spring-boot-starter

##### 小工具一枚，欢迎使用和Star支持，如使用过程中碰到问题，可以提出Issue，我会尽力完善该Starter

### 版本基础
* spring-boot-starter-parent: 2.1.9.RELEASE
* curator-recipes：2.8.0
* fastjson: 1.2.62

### 如何使用
在该项目的帮助下，我们的Spring Boot可以轻松的引入zk分布式锁，主需要做下面两个步骤：

step1. 在pom.xml中引入依赖：
``` java
<dependency>
  <groupId>com.github.dawn9117</groupId>
  <artifactId>zklock-spring-boot-starter</artifactId>
  <version>1.0.1</version>
</dependency>
```

step2. 在application.properties中添加以下配置
* zk.registryAddress=XXX:2181, desc:(required)
* zk.session-timeout=XXX,  desc:(optional, default: 30000)
* zk.connect-timeout=XXX,  desc:(optional, default: 30000)
* zk.lock-namespace=XXX,   desc:(optional, default: locks)

step3. 在需要加分布式锁的方法上增加@ZkLock注解
``` java
@ZkLock
public void test(){

}
```

##### 锁说明: 默认情况下使用不可重入共享锁, 锁名称为: zk.lock-namespace + 类全路径 + 方法名, 例如: /locks/com.github.dawn/test。

##### 原理: 使用spring aop对添加@ZkLock注解的方法进行环绕处理, 处理逻辑:
* step1.加锁
    * 成功执行下一步
    * 失败执行失败策略
* step2. 执行方法
    * 加锁失败直接抛出异常
    * 加锁成功执行连接的方法
* step3. 释放锁

#### @ZkLock更细致的配置内容参考如下：
* path: 自定义锁路径
* type: 指定锁类型, 可自己拓展(目前支持可重入共享锁, 不可重入共享锁)
* retry: 配合RetryLockStrategy使用, 失败重试次数
* failedStrategy: 加锁失败策略, 可自定义, 实现LockFailedStrategy接口
* timeout: 锁连接超时时长
* timeuint: 锁连接超时时间单位