## SpringIOC实现原理

​	很多人一提到spring认为核心是控制翻转（IOC）、依赖注入（DI），个人认为Spring的核心是spring-bean的生命周期。而ioc仅仅是springbean生命周期的一个小插曲。

**spring ioc指的是控制反转，IOC容器负责实例化、定位、配置应用程序中的对象及建立这些对象间的依赖。交由Spring容器统一进行管理，从而实现松耦合**

> “控制反转”，不是什么技术，而是一种设计思想。DI“依赖注入”是IOC的表达方式如：属性注入，构造器注入。

 

### 属性填充

​	属性填充是springbean生命周期的一部分。从springioc容器中获取bean时会在bean实例化之后，初始化之前（`@PostConstruct`方法/`InitializingBean`）进行属性的填充。DI就发生在这个过程。spring可以允许对象之间的循环依赖，可以从spring典型的循环依赖深刻了解spring的ioc原理。其中处理循环依赖的重要手段是对象的三级缓存。属性填充可参看`org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean`

​	处理循环依赖的核心就是三级缓存、对象工厂和早期对象暴露，循环依赖可以看成是一个递归，而三级缓存、对象工厂和早期对象暴露一起构成了递归的出口很好的解决了循环依赖。

#### 三级缓存

三级缓存主要是用来处理单例对象之间循环依赖的技术手段。

```java
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
    //一级缓存
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    //二级缓存
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    //三级循环依赖
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
```

#### 早期对象暴露

​		在`AbstractAutowireCapableBeanFactory#doCreateBean`中一段代码，当对象是单例，系统允许循环依赖且对象当前处理创建此时就判断为允许早期对象的暴露。暴露的对象是单例对象工厂是个匿名内部类，防止循环引用。（对象工厂是对象暴露的方式，如果此处暴露的不是对象工厂而是早期实例对象会如何？）

```java
boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
                                  isSingletonCurrentlyInCreation(beanName));
if (earlySingletonExposure) {
    if (logger.isDebugEnabled()) {
        logger.debug("Eagerly caching bean '" + beanName +
                     "' to allow for resolving potential circular references");
    }
    //这里是一个匿名内部类，为了防止循环引用，尽早持有对象的引用
    addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
}
```



#### 循环依赖注入过程

所有已经创建的单例对象都放入容器``DefaultSingletonBeanRegistry#Set<String> registeredSingletons``中

所有正在创建的单例对象都放入容器``DefaultSingletonBeanRegistry#Set<String> singletonsCurrentlyInCreation``中

所有对象的最终场景都会走到``org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean``

假设一个循环依赖场景：

```java
@Service
class A{
    @Autowired
    B b;
}
@Service
class B{
    @Autowired
    A b;
}
```

A，B服务会在容器启动的时候解析成``BeanDefinition``并注册到IOC容器中，假设注入的过程中A开始初始化则会经历以下递归的创建过程：

- 对象A实例化，包装A的单例对象工厂放入三级缓存对象。
- 对象A的属性填充，此时需要获取B的实例引用。
- 对象B的实例化
- 对象B的属性填充这时要从容器中获取A的实例。而A在实例的过程中引用已经放入早期暴露对象到三级缓存中。所以在获取A实例的时候发现A也正在创建，那么直接从三级缓存中获取早期暴露对象，如果获取成功从三级缓存中移除而进入二级缓存容器中标记为早期单例对象。
- B数据填充成功进入初始化成功返回B对象，后返回到A的属性注入然后A对象数据填充成功进入A的对象初始化成功进入容器。

​			回想早期编程的对象注入的通过构造器或者setter方法进行注入，如果构造器中出现循环依赖会直接导致StackOverFlow。这种实例化由对象自身进行自我控制。表现在代码上就是需要过多的代码去处理依赖有时候复杂系统还处理不好导致问题。当我们按照一定的规则的时候将实例化，依赖注入完全交给容器处理，开发人员仅仅关注单个服务的开发和服务调用就能就省一大笔开销。spring对于ioc这种编程思想的实现极大的方便了开发任务，使工作更专注。



[参考]()





































