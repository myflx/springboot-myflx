### 微服务注册与发现之Eureka

#### 服务发现组件应具备的功能

- 服务注册表
- 服务注册与服务发现
- 服务检查

springboot 提供服务发现组件：Eureka,Consul,Zookeeper

#### Eureka原理

​	Eureka提供注册发现功能，同时维护注册服务列表，通过心跳维持服务在注册端的存活（客户端30s，服务端等待90s），通过rest api (feign client)实现远程调用，通过客户端缓存降低服务端压力并提升服务端的可用性。

##### Eureka基本概念

> eureka 中有两个重要概念：Region 、Availability Zone。二者均是AWS概念。Region 代表地理位置，每个地理位置都有许多的Availability Zone ，各个Region 之间互相隔离。spring cloud 中默认的region 是us-east-1.Region 可以理解成一个集群，Availability Zone 可以理解为机房。是对eureka 的分组隔离。
>
> eureka server端本身也是 client，server端作为client在各个server端中间互相注册服务，实现server端的高可用。
>
> server端通过参数配置服务注册与发现：
>
> ``eureka.clent.registerWithEureka``：来控制是否将自身作为client注册到其他eureka server。
>
> ``eureka.clent.fetchRegistry`` ： 控制是否从eureka server中获取注册信息。
>
> 二者默认为true。实际项目通过不同的依赖和注解开关来指定项目的功能。
>
> spring cloud 中的eureka 配置对象主要为：``EurekaInstanceConfigBean``   ``EurekaClientConfigBean``

##### 如何注册服务？

##### 如何发现服务？

#### Eureka用户认证

可用过依赖spring-boot-start-security

同时配置security属性和eureka zone 配置来实现用户认证功能

```yaml
sercurity:
	basic:
		enable: true
	user:
		name: name
         pasword:password123
eureka:
  client:
    serviceUrl:
      defaultZone: http://user:password123localhost:8761/eureka/
```

也可定义实现复杂的用户认证功能

#### Eureka 元数据

> 自定义元数据不改变原服务注册行为，只是传递信息。通过设置属性：eureka.client.metadata-map
>
> 属性存储对象：``EurekaInstanceConfigBean#metadataMap``

#### Eureka Server 的Rest 端点

​		Rest 端点提供了丰富的基于xml，json的rest服务。如服务注册/发现/服务列表等。是eureka针对于非JVM服务的扩展，通过端点接口的调用，实现非JVM服务的整合。

#### 多网卡环境下的IP选择

​	针对于多网卡的服务器，如网卡eth0，eth1，eth2 只有eth1可以联网，如果eureka client 将eth0 或者eth2 注册到eureka server上，其他微服务就无法通过这个ip访问到该微服务的接口。spring cloud 提供这种能力。

- 忽略指定网卡
- 正则指定使用的网络地址
- 只是用本地地址
- 手动指定IP地址

#### Eureka 的自我保护模式

通过配置：``eureka.client.healthcheck.enable.true`` 将auctor的端点 /health 传递到服务端。要是实现更细粒度的检查可以实现接口``com.netflix.appinfo.HealthCheckHandler``

#### Eureka 源码分析