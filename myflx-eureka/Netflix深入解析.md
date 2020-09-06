# springcloud-Netflix

## Feign解析

### feign注入的步驟

- 注入FeignClientSpecification和**FeignClientFactoryBean** 的BeanDefinition。
- 注入FeignContext，FeignContext是应用配置上下文持有对象NamedContextFactory的实现类。``FeignAutoConfiguration``中进行Bean注入。如果引入了sleuth哪么配置类`TraceFeignClientAutoConfiguration`会优先使用`TraceFeignClient`。
- 构造Feign.Builder。仅feign存在的环境中``FeignClientsConfiguration``会自动注入`Feign.Builder`。
- 生成负载均衡代理类。会接收不同的Client。`Client.DEFAULT`/`LoadBalancerFeignClient`/`TraceLoadBalancerFeignClient`
- 生成默认代理类并注入spring-ioc容器。



### FeignContext

​	FeignContext存在与springboot级别上下文中。

​	为每个FeignClient创建一个上下文存放在父类`NamedContextFactory#contexts`属性中。其中上下文注册的对象主要是FeignClient对象相关的配置类。

**FeignClientFactoryBean** 在获取FeignClient对象的过程中，会从FeignContext中注册的配置获取相关对象进行初始化。



[参考](https://mp.weixin.qq.com/s?__biz=MzUwOTk1MTE5NQ==&mid=2247483724&idx=1&sn=03b5193f49920c1d286b56daff8b1a09&chksm=f90b2cf8ce7ca5ee6b56fb5e0ffa3176126ca3a68ba60fd8b9a3afd2fd1a2f8a201a2b765803&token=302932053&lang=zh_CN&scene=21#wechat_redirect)

