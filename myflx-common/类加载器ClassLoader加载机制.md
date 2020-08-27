# ClassLoader加载机制

## 类加载的过程

- 加载：类加载过程的一个阶段：通过一个类的完全限定查找此类字节码文件，并利用字节码文件创建一个Class对象
- 验证：目的在于确保Class文件的字节流中包含信息符合当前虚拟机要求，不会危害虚拟机自身安全。主要包括四种验证，文件格式验证，元数据验证，字节码验证，符号引用验证。
- 准备：为类变量(即static修饰的字段变量)分配内存并且设置该类变量的初始值即0(如static int  i=5;这里只将i初始化为0，至于5的值将在初始化时赋值)，这里不包含用final修饰的static，因为final在编译的时候就会分配了，注意这里不会为实例变量分配初始化，类变量会分配在方法区中，而实例变量是会随着对象一起分配到Java堆中。
- 解析：主要将常量池中的符号引用替换为直接引用的过程。符号引用就是一组符号来描述目标，可以是任何字面量，而直接引用就是直接指向目标的指针、相对偏移量或一个间接定位到目标的句柄。有类或接口的解析，字段解析，类方法解析，接口方法解析(这里涉及到字节码变量的引用，如需更详细了解，可参考《深入Java虚拟机》)。
- 初始化：类加载最后阶段，若该类具有超类，则对其进行初始化，执行静态初始化器和静态初始化成员变量(如前面只初始化了默认值的static变量将会在这个阶段赋值，成员变量也将被初始化)。

## 类加载器

- Bootstrap加载器，主要加载的是JVM自身需要的类，C++语言实现的，是虚拟机自身的一部分。它负责将 `<JAVA_HOME>/lib`路径下的核心类库或`-Xbootclasspath`参数指定的路径下的jar包加载到内存中。

- 扩展（Extension）类加载器（```sun.misc.Launcher$ExtClassLoader```），Launcher的静态内部类，它负责加载`<JAVA_HOME>/lib/ext`目录下或者由系统变量-Djava.ext.dir指定位路径中的类库，开发者可以直接使用标准扩展类加载器。

- 系统（System）类加载器 ，`sun.misc.Launcher$AppClassLoader`，它负责加载系统类路径`java -classpath`或`-D java.class.path` 指定路径下的类库，也就是我们经常用到的classpath路径，开发者可以直接使用系统类加载器，一般情况下该类加载是程序中默认的类加载器，通过`ClassLoader#getSystemClassLoader()`方法可以获取到该类加载器。



## 双亲委派模式

当通过一个类加载器加载类的时候会首先会通过父加载器去加载。双亲委派模式使对象随着类加载器呈现了层级结构，避免重复加载，就避免核心类的被覆盖的造成安全问题。

## 双亲委派模型的破坏

​		在Java应用中存在着很多服务提供者接口（Service Provider Interface，SPI），这些接口允许第三方为它们提供实现，如常见的 SPI 有 JDBC、JNDI等，这些 SPI 的接口属于 Java 核心库，一般存在rt.jar包中，由Bootstrap类加载器加载，而 SPI 的第三方实现代码则是作为Java应用所依赖的 jar 包被存放在classpath路径下，由于SPI接口中的代码经常需要加载具体的第三方实现类并调用其相关方法，但SPI的核心接口类是由引导类加载器来加载的，而Bootstrap类加载器无法直接加载SPI的实现类，同时由于双亲委派模式的存在，Bootstrap类加载器也无法反向委托AppClassLoader加载器SPI的实现类。在这种情况下，我们就需要一种特殊的类加载器来加载第三方的类库，而线程上下文类加载器就是很好的选择。

如``java.sql.DriverManager``  通过spi机制将类的加载权交给线程中的加载器。

```java
private static void loadInitialDrivers() {
    //....
    AccessController.doPrivileged(new PrivilegedAction<Void>() {
        public Void run() {
            ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
            Iterator<Driver> driversIterator = loadedDrivers.iterator();
            try{
                while(driversIterator.hasNext()) {
                    driversIterator.next();
                }
            } catch(Throwable t) {
                // Do nothing
            }
            return null;
        }
    });
    //....
}

//默认是AppClassLoader
public static <S> ServiceLoader<S> load(Class<S> service) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return ServiceLoader.load(service, cl);
}
```

## springboot/LaunchedURLClassLoader

springboot启动入口是``JarLauncher``

- 主要主要工作是重新注册了jar的URL协议处理器的位置、清除``URLStreamHandlerFactory``
- 创建`LaunchedURLClassLoader` 并设置到当前线程的上下文线程中。
- 创建`MainRunner` 使用上下文类加载器加载main方法并反射调用。

本次重点关注的是：``LaunchedURLClassLoader``

```java
protected ClassLoader createClassLoader(URL[] urls) throws Exception {
    return new LaunchedURLClassLoader(urls, getClass().getClassLoader());
}
```

​		加载器将fatjar包中的BOOT-INF文件的文件包装成URL[]，并取当前类加载器即`Launcher&AppClassLoader`作为父类。创建完成之后将改加载器设置到线程上下文。这是一种对双亲委派模式的违背。

​		``LaunchedURLClassLoader``作为线程上下文类加载器主要重写了查找资源：`findResource`，`findResources`。和加载类`loadClass`。

​		查资源和加载类都增加了资源不存在时候的failfast机制。加载类在加载之前在ClassLoader中对包进行注册定义避免重复加载。

## tomcat类加载器

![1598507586911](..\doc\image\tomcat-classloader.png)

tomcat为何要自定义类加载器？

- 首先各个webapp中的**class和lib，需要相互隔离**，app之间相同的**lib共享加载**不造成资源浪费。
- 对tomcat的加载的包进行隔离，类似jvm解决安全性的问题。
- **热部署**的问题，tomcat修改配置文件不需要重启。

tomcat classloader的标准实现是`org.apache.catalina.loader.StandardClassLoader`。会按照名字的不同对加载器进行分层级。可查看`org.apache.catalina.startup.Bootstrap#initClassLoaders`方法。

```java
private void initClassLoaders() {
    try {
        commonLoader = createClassLoader("common", null);
        if( commonLoader == null ) {
            // no config file, default to this loader - we might be in a 'single' env.
            commonLoader=this.getClass().getClassLoader();
        }
        catalinaLoader = createClassLoader("server", commonLoader);
        sharedLoader = createClassLoader("shared", commonLoader);
    } catch (Throwable t) {
        handleThrowable(t);
        log.error("Class loader creation threw exception", t);
        System.exit(1);
    }
}
```

同样在tomcat启动初始化的时候将`catalinaLoader`作为线程上下文加载器。然后调用启动类启动方法。



对于每个webapp应用，都会对应唯一的StandContext，在StandContext中会引用WebappLoader，该类又会引用WebappClassLoader，WebappClassLoader就是真正加载webapp的classloader。

`org.apache.catalina.core.StandardWrapper#loadServlet`



[tomcat-classloader参考](https://blog.csdn.net/liweisnake/article/details/8470285)

## ClassLoader分析

并行处理。为当前的ClassLoader实现类 在`ClassLoader`中注册并行处理标记。如果没有可能会导致死锁。因为loadClass方法中在加载类时会获取类加载锁。如果没有注册并行处理，获取的锁为当前对象否则是个新对象。由于没有细粒度的锁当前加载器作为锁，在双亲委托和线程上下文加载器同时存在的情况下可能会出现死锁。

```java
static {
    ClassLoader.registerAsParallelCapable();
}
```



注册本地方法。注册除registerNatives以外的所有本地方法。这段代码很多类如，Object，Class中都能见到。主要是在类初始化阶段将本地方法和虚拟加载的动态文件进行连接。

```java
private static native void registerNatives();
static {
    registerNatives();
}
```



loadClass()。加载类是按照双亲委托机制加载，如果要打破这种机会需要重写该方法。正常自定义类加载器只需要重写findClass()方法。

```java
protected Class<?> loadClass(String name, boolean resolve)
    throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
```





[参考博客](https://blog.csdn.net/javazejian/article/details/73413292)

