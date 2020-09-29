# springcloud-Netflix

# Feign

### 用法

`@EnableFeignClients`  ：可指定包名，不指定默认为注解类的包名。

`@FeignClient`：标记为服务提供者。

### feign注入的步驟

- 注入FeignClientSpecification和**FeignClientFactoryBean** 的BeanDefinition。
- 注入FeignContext，FeignContext是应用配置上下文持有对象NamedContextFactory的实现类。``FeignAutoConfiguration``中进行Bean注入。如果引入了sleuth哪么配置类`TraceFeignClientAutoConfiguration`会优先使用`TraceFeignClient`。
- 构造Feign.Builder。仅feign存在的环境中``FeignClientsConfiguration``会自动注入`Feign.Builder`。
- 生成负载均衡代理类。会接收不同的Client。`Client.DEFAULT`/`LoadBalancerFeignClient`/`TraceLoadBalancerFeignClient`
- 生成默认代理类并注入spring-ioc容器。

### 组件注册

#### 注册入口

> `FeignClientsRegistrar`
>
> ----------------> `org.springframework.context.annotation.ImportBeanDefinitionRegistrar`

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata,
                                    BeanDefinitionRegistry registry) {
    registerDefaultConfiguration(metadata, registry);
    registerFeignClients(metadata, registry);
}
```

#### 组件

`FeignClientSpecification`：规格对象，持有FeignClient的注解的类型和名称

`FeignClientFactoryBean`：工厂对象，最终返回代理对象

``FeignContext``：第三类上下文



`Feign.Builder`

`feign.Client`

核心方法:

```java
Response execute(Request var1, Options var2) throws IOException;
```

实现:

- `feign.Client.Default`

- `org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient`

- ``org.springframework.cloud.sleuth.instrument.web.client.feign.TraceFeignClient``

-  `org.springframework.cloud.sleuth.instrument.web.client.feign.TraceLoadBalancerFeignClient`	

feign在ribbon的基础上再次封装实现请求的接口化调用。`FeignLoadBalancer`中实现对ribbon的调用整合。



`org.springframework.cloud.netflix.feign.Targeter`

实现：

`org.springframework.cloud.netflix.feign.DefaultTargeter`

普通目标创建对象，拦截处理器：`feign.ReflectiveFeign.FeignInvocationHandler#FeignInvocationHandler`

`org.springframework.cloud.netflix.feign.HystrixTargeter` 持有`com.netflix.hystrix.HystrixCommand`用于服务熔断的设置工厂`SetterFactory`用于构建具有容错性质的代理类。具体拦截处理器：`HystrixInvocationHandler`

### FeignContext

​	FeignContext存在与springboot级别上下文中。

​	为每个FeignClient创建一个上下文存放在父类`NamedContextFactory#contexts`属性中。其中上下文注册的对象主要是FeignClient对象相关的配置类。

**FeignClientFactoryBean** 在获取FeignClient对象的过程中，会从FeignContext中注册的配置获取相关对象进行初始化。

ribbon中也会按照`SpringClientFactory`也是类似，按照服务名称创建相关上下文存在配置类。

[参考](https://mp.weixin.qq.com/s?__biz=MzUwOTk1MTE5NQ==&mid=2247483724&idx=1&sn=03b5193f49920c1d286b56daff8b1a09&chksm=f90b2cf8ce7ca5ee6b56fb5e0ffa3176126ca3a68ba60fd8b9a3afd2fd1a2f8a201a2b765803&token=302932053&lang=zh_CN&scene=21#wechat_redirect)

