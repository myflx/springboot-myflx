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

##### 如何注册/发现服务？

​		eureka server端既是服务端也是客户端，所以会和多有的客户端具有相同功能，eureka client会在启动后开启定时线程轮询分别用来获取注册信息，检测服务端心跳，更新服务缓存信息。服务端除了具备客户端的功能外。同时会开启定时器剔除无效注册，同时开启定时线程为注册的服务续期，同时会更新对等服务端集群的信息。

##### 官方参考

​	[文档地址](https://cloud.spring.io/spring-cloud-netflix/reference/html/)

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

#### Eureka 配置

[链接](https://cloud.spring.io/spring-cloud-netflix/reference/html/appendix.html)

#### Eureka 源码分析

##### eureka client 

###### 配置装配

主要关注 ``@EnableEurekaClient`` 如何装配及其自动的对象。高版本中只需要添加依赖而不需要注解启动，通过配置控制是否启动client启动。

服务发现自动装配对象

```java
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass(EurekaClientConfig.class)
@Import(DiscoveryClientOptionalArgsConfiguration.class)
@ConditionalOnBean(EurekaDiscoveryClientConfiguration.Marker.class)
@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
@ConditionalOnDiscoveryEnabled
@AutoConfigureBefore({ NoopDiscoveryClientAutoConfiguration.class,
		CommonsClientAutoConfiguration.class, ServiceRegistryAutoConfiguration.class })
@AutoConfigureAfter(name = {
		"org.springframework.cloud.autoconfigure.RefreshAutoConfiguration",
		"org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration",
		"org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration" })
public class EurekaClientAutoConfiguration {
    ....
}
```

核心自动装配对象： ``CloudEurekaClient``

自动装配的对象：

``EurekaClientConfig``:持有配置参数。

``MutableDiscoveryClientOptionalArgs``，``RestTemplateDiscoveryClientOptionalArgs``:均继承抽象对象

``AbstractDiscoveryClientOptionalArgs``。``MutableDiscoveryClientOptionalArgs`` 持有集合对象：

``Collection<ClientFilter>``

服务发现配置对象：

``EurekaDiscoveryClientConfiguration``

健康检查配置对象：

``EurekaHealthCheckHandlerConfiguration``



###### 服务注册流程

​	为了了解服务注册的功能实现在不存在eureka server 的情况下客户端启动类只注解 ``@EnableEurekaClient`` 然后启动服务时，会出现报错信息，从报错信息入手和注解启动入口进行源码分析。

```text
com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException: Connection refused: connect
	at com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(ApacheHttpClient4Handler.java:187) ~[jersey-apache-client4-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.filter.GZIPContentEncodingFilter.handle(GZIPContentEncodingFilter.java:123) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.netflix.discovery.EurekaIdentityHeaderFilter.handle(EurekaIdentityHeaderFilter.java:27) ~[eureka-client-1.9.13.jar:1.9.13]
	at com.sun.jersey.api.client.Client.handle(Client.java:652) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource.handle(WebResource.java:682) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource.access$200(WebResource.java:74) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource$Builder.post(WebResource.java:570) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.netflix.discovery.shared.transport.jersey.AbstractJerseyEurekaHttpClient.register(AbstractJerseyEurekaHttpClient.java:56) ~[eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.MetricsCollectingEurekaHttpClient.execute(MetricsCollectingEurekaHttpClient.java:73) ~[eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.executeOnNewServer(RedirectingEurekaHttpClient.java:118) ~[eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.execute(RedirectingEurekaHttpClient.java:79) ~[eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:120) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:77) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.DiscoveryClient.register(DiscoveryClient.java:847) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.InstanceInfoReplicator.run(InstanceInfoReplicator.java:121) [eureka-client-1.9.13.jar:1.9.13]
	at com.netflix.discovery.InstanceInfoReplicator$1.run(InstanceInfoReplicator.java:101) [eureka-client-1.9.13.jar:1.9.13]
```

​	定位到对象``DiscoveryClient`` 其关联的实例信息复制对象 ``InstanceInfoReplicator`` 该对象是个线程任务，会在一个定时的守护线程中执行。

``DiscoveryClient``:服务发现客户端对象。由对象 ``ApplicationInfoManager``，``EurekaClientConfig``，`AbstractDiscoveryClientOptionalArgs(MutableDiscoveryClientOptionalArgs)`，

`Provider<BackupRegistry>`，``EndpointRandomizer``构建。

``ApplicationInfoManager`` :应用信息管理对象持有配置对象``EurekaInstanceConfig``，实例信息``InstanceInfo``，可选参数``OptionalArgs``，状态变更监听器列表。	

``DiscoveryClient``对象（spring cloud实现类``CloudEurekaClient``）：

​	持有Eureka事件监听器列表：``EurekaEventListener``，当状态变更的时候会发布``StatusChangeEvent``事件，缓存刷新之后会发布 ``CacheRefreshedEvent``事件(会发送应用级别事件``HeartbeatEvent``)。并触发监听器的对事件的监听。

​	持有健康检查处理器回调:``Provider<HealthCheckCallback>``，

​	持有健康检查处理器```Provider<HealthCheckHandler>``

​	持有定时线程``ScheduledExecutorService``用来刷新服务地址，检测心跳，刷新缓存。

​	心跳线程``new HeartbeatThread()``, 缓存刷新线程``new CacheRefreshThread()``;

​	持有对象``InstanceRegionChecker``  -->  其持有的``AzToRegionMapper`` 通过不同配置使用不同实现。

​	``DiscoveryClient`` 在所有准备工作做完之后调用方法:

``com.netflix.discovery.DiscoveryClient#initScheduledTasks``进行初始化工作。主要是

- 启动缓存刷新线程
- 启动心跳线程检测服务端的生命特征并更新检测时间
- 构建实例复制对象 ``InstanceInfoReplicator`` 
- 构建应用状态变更监听器``ApplicationInfoManager.StatusChangeListener``
- 启用 ``InstanceInfoReplicator`` 复制对象的定时线程来刷新实例信息，同时判断如果是脏信息就重新注册并清楚脏信息标记。然后设置下次备份任务（默认延迟40s）

``DiscoveryManager`` 单例对象持有服务发现对象和EurekaClientConfig配置对象。



###### 健康检查

配置对象 ``EurekaHealthCheckHandlerConfiguration`` 

启动参数(默认false)：

```yaml
eureka:
  client:
    healthcheck:
      enabled: true
```

健康检查处理对象：``HealthCheckHandler``(``EurekaHealthCheckHandler``)

可以自行实现接口``HealthCheckHandler``实现健康检查。

###### 多AZ

[链接](https://cloud.spring.io/spring-cloud-netflix/reference/html/#zones)

同一个region下配置多AZ ，服务请求的时候偏向同AZ可以按照如下配置

```
eureka.instance.metadataMap.zone = zone1
eureka.client.preferSameZoneEureka = true
```

###### AZ负载均衡 

[链接](https://cloud.spring.io/spring-cloud-netflix/reference/html/#using-eureka-with-spring-cloud-loadbalancer)

`ZonePreferenceServiceInstanceListSupplier`

##### eureka server

###### EurekaServerAutoConfiguration

​		需要在依赖`spring-cloud-starter-netflix-eureka-server` 的基础上在启动配置上增加注解  

``@EnableEurekaServer``，系统才启动eureka server 的功能。否则系统会只能作为 eureka client。

server启动注解的增加会生效 自动配置的对象 ``EurekaServerAutoConfiguration`` 装配

```java
@Configuration
@Import(EurekaServerInitializerConfiguration.class)
@ConditionalOnBean(EurekaServerMarkerConfiguration.Marker.class)
@EnableConfigurationProperties({ EurekaDashboardProperties.class,
		InstanceRegistryProperties.class })
@PropertySource("classpath:/eureka/server.properties")
public class EurekaServerAutoConfiguration extends WebMvcConfigurerAdapter {
		.....
}
```

``EurekaServerInitializerConfiguration`` 继承了``ServletContextAware`` 对象，获取 `ServletContext` 将Eureka 上下文作为属性关联到``ServletContext``中。

```java
@Override
public void start() {
    new Thread(() -> {
        try {
            //初始化上下文
            eurekaServerBootstrap.contextInitialized(
                EurekaServerInitializerConfiguration.this.servletContext);
            log.info("Started Eureka Server");
			//发布获取eureka配置事件
            publish(new EurekaRegistryAvailableEvent(getEurekaServerConfig()));
            EurekaServerInitializerConfiguration.this.running = true;
            //发布获取eureka 服务端启动完毕事件
            publish(new EurekaServerStartedEvent(getEurekaServerConfig()));
        }
        catch (Exception ex) {
            // Help!
            log.error("Could not initialize Eureka servlet context", ex);
        }
    }).start();
}

public void contextInitialized(ServletContext context) {
    try {
        initEurekaEnvironment();
        initEurekaServerContext();

        context.setAttribute(EurekaServerContext.class.getName(), this.serverContext);
    }
    catch (Throwable e) {
        log.error("Cannot bootstrap eureka server :", e);
        throw new RuntimeException("Cannot bootstrap eureka server :", e);
    }
}
```

###### EurekaController

配置启动时会装配控制器：``EurekaController``用于支持eureka dashboard 。

``EurekaServerAutoConfiguration.RefreshablePeerEurekaNodes`` 作为eureka 高可用节点，会在

``com.netflix.eureka.DefaultEurekaServerContext#initialize`` 中启动线程执行任务

```java
PeerEurekaNodes.this.updatePeerEurekaNodes(PeerEurekaNodes.this.resolvePeerUrls());
```

自动配置对象``EurekaServerAutoConfiguration``会自动装配上下文

(持有节点``RefreshablePeerEurekaNodes``)

```java
@Bean
public EurekaServerContext eurekaServerContext(ServerCodecs serverCodecs,
			PeerAwareInstanceRegistry registry, PeerEurekaNodes peerEurekaNodes) {
		return new DefaultEurekaServerContext(this.eurekaServerConfig, serverCodecs,
				registry, peerEurekaNodes, this.applicationInfoManager);
}
```

###### eureka 实例事件发布

Eureka实例生命周期伴随着事件的发布，主要是通过实例注册对象``InstanceRegistry``  发布事件。

``EurekaInstanceCanceledEvent``,``EurekaInstanceRegisteredEvent``,

``EurekaInstanceRenewedEvent``,``EurekaRegistryAvailableEvent``,

``EurekaServerStartedEvent``





###### 对等节点更新线程

上下文会在@PostConstruct中启动线程来获取对等节点的信息。

```java
@PostConstruct
public void initialize() {
    logger.info("Initializing ...");
    this.peerEurekaNodes.start();
    try {
        this.registry.init(this.peerEurekaNodes);
    } catch (Exception var2) {
        throw new RuntimeException(var2);
    }
    logger.info("Initialized");
}
```

对等节点线程会默认10mins 执行一次，在线程中对等eureka server 端信息会被包装成对等节点对象``PeerEurekaNode`` 、信息存在：``com.netflix.eureka.cluster.PeerEurekaNodes#peerEurekaNodes``



当配置发生变化时会发布事件 ``EnvironmentChangeEvent``  也会触发对等节点的更新。

同时启动线程更新对等eureka node.



###### RenewalThreshold 线程

15min执行一次

```java
private void scheduleRenewalThresholdUpdateTask() {
    this.timer.schedule(new TimerTask() {
        public void run() {
            PeerAwareInstanceRegistryImpl.this.updateRenewalThreshold();
        }
    }, (long)this.serverConfig.getRenewalThresholdUpdateIntervalMs(), (long)this.serverConfig.getRenewalThresholdUpdateIntervalMs());
}
```



###### 无效注册服务剔除Timer

1min执行一次

```java
com.netflix.eureka.registry.AbstractInstanceRegistry.EvictionTas
```

###### eureka server 上下文

​	``EurekaServerContext``  实现类：``DefaultEurekaServerContext``

###### 实例注册对象

``PeerAwareInstanceRegistry`` 实现类:``InstanceRegistry`` 



###### 节点集群

``PeerEurekaNodes``



###### eureka server 上下文

``EurekaServerContext``  实现类 ``DefaultEurekaServerContext``

启动集群节点同步节点：this.peerEurekaNodes.start();

###### eureka server 启动入口

``EurekaServerBootstrap``：初始化eureka 环境，初始化eureka上下文，注册MBean。



###### eureka 使用 archaius 配置库



###### eureka server securing

[link](https://cloud.spring.io/spring-cloud-netflix/reference/html/#securing-the-eureka-server)

