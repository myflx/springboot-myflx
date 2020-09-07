### 谈谈你对eureka的认识？

​		这个问题比较模糊。可能面试官一时半会儿对eureka也没有想到好的问题，可能他是想听你直接讲而是纯粹的想看你的了解程度。需要从以下方面去阐述eureka：基本概念，基本组件，并发注册，服务注册原理，服务发现原理，心跳检查，客户端缓存，和其他服务发现组件的对比等六个方面描述。

### 工作原理

#### 基本概念

eureka中有两个概念：Region、Zone。属于AWS概念，在非AWS环境中，Region可以理解为跨机房的集群，Zone可以理解为机房。

#### 基本组件

Eureka有两大组件：eureka server、eureka client。eureka client既是服务的提供者又是服务的消费者。

- Eureka Server 提供服务注册发现的能力，每个微服务启动的时候，会向Eureka Server 注册自身的信息（IP，端口、微服务名称）Eureka Server 会存储这些信息。
- Eureka Client是个Java客户端，负责与Eureka Server 的交互，简化了交互过程。
- 微服务启动后，会周期性的（默认30s）向Eureka Server 发送心跳以续约字节的“租期‘。
- 如果Eureka Server 在一定的时间内没有接收到某个微服务的实例的心跳，Eureka Server 将注销该实例（默认90s）
- 默认的情况下，Eureka Server 同时也是Eureka Client。多个Eureka Server 之间通过复制的方式来实现服务注册表中的数据同步。
- Eureka Client 会缓存注册表中的信息。降低Eureka Server 的压力。 

Eureka 通过心跳检查、客户端缓存机制，提高了系统的灵活性，可伸缩性和可用性。