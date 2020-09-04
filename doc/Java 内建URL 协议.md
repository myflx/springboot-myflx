### 						JAVA内建URL协议及自定义



​		通常的URL协议格式，*<u>**protocol**//[authority]hostname:port/resource?queryString</u>*，通过``java.net.URL`` 解析出 protocol，host，port等信息。更复杂的格式可以解析出更多的信息如，file，authority，path等信息。可以参考核心解析类的字段信息。

### 类与关系

#### [什么是`URL`？](https://baike.baidu.com/item/url/110640?fr=aladdin)

​		通常所说的网址，统一资源定位符（Uniform Resource Locator） ：定位资源的一种方式，规范。



#### ``java.net.URL``

```java
public final class URL implements Serializable {
    static final long serialVersionUID = -7627629688361524110L;
    private static final String protocolPathProp = "java.protocol.handler.pkgs";
    private String protocol;
    private String host;
    private int port;
    private String file;
    private transient String query;
    private String authority;
    private transient String path;
    private transient String userInfo;
    private String ref;
    transient InetAddress hostAddress;
    transient URLStreamHandler handler;
    private int hashCode;
    static URLStreamHandlerFactory factory;
    static Hashtable<String, URLStreamHandler> handlers = new Hashtable();
    private static Object streamHandlerLock = new Object();
	....
}
```



#### 核心类

``java.net.URL``  

``java.net.URLConnection``

``java.net.URLStreamHandler``

``java.net.URLStreamHandlerFactory``



#### 依赖关系

![关系图](http://dl2.iteye.com/upload/attachment/0029/2042/6b1c9d1b-646c-34e6-91d5-5b7f37add93f.jpg)

#### 处理过程

- 通过URL类中的方法     ``java.net.URL#getURLStreamHandler ``      创建  ``URLStreamHandler ``

```java
static URLStreamHandler getURLStreamHandler(String protocol) {

        URLStreamHandler handler = handlers.get(protocol);
        if (handler == null) {

            boolean checkedWithFactory = false;

            // Use the factory (if any)
            if (factory != null) {
                handler = factory.createURLStreamHandler(protocol);
                checkedWithFactory = true;
            }

            // Try java protocol handler
            if (handler == null) {
                String packagePrefixList = null;

                packagePrefixList
                    = java.security.AccessController.doPrivileged(
                    new sun.security.action.GetPropertyAction(
                        protocolPathProp,""));
                if (packagePrefixList != "") {
                    packagePrefixList += "|";
                }

                // REMIND: decide whether to allow the "null" class prefix
                // or not.
                packagePrefixList += "sun.net.www.protocol";

                StringTokenizer packagePrefixIter =
                    new StringTokenizer(packagePrefixList, "|");

                while (handler == null &&
                       packagePrefixIter.hasMoreTokens()) {

                    String packagePrefix =
                      packagePrefixIter.nextToken().trim();
                    try {
                        String clsName = packagePrefix + "." + protocol +
                          ".Handler";
                        Class<?> cls = null;
                        try {
                            cls = Class.forName(clsName);
                        } catch (ClassNotFoundException e) {
                            ClassLoader cl = ClassLoader.getSystemClassLoader();
                            if (cl != null) {
                                cls = cl.loadClass(clsName);
                            }
                        }
                        if (cls != null) {
                            handler  =
                              (URLStreamHandler)cls.newInstance();
                        }
                    } catch (Exception e) {
                        // any number of exceptions can get thrown here
                    }
                }
            }

            synchronized (streamHandlerLock) {

                URLStreamHandler handler2 = null;

                // Check again with hashtable just in case another
                // thread created a handler since we last checked
                handler2 = handlers.get(protocol);

                if (handler2 != null) {
                    return handler2;
                }

                // Check with factory if another thread set a
                // factory since our last check
                if (!checkedWithFactory && factory != null) {
                    handler2 = factory.createURLStreamHandler(protocol);
                }

                if (handler2 != null) {
                    // The handler from the factory must be given more
                    // importance. Discard the default handler that
                    // this thread created.
                    handler = handler2;
                }

                // Insert this handler into the hashtable
                if (handler != null) {
                    handlers.put(protocol, handler);
                }
            }
        }
        return handler;
    }
```

- 通过工厂类 ``URLStreamHandler `` 解析URL，核心是创建``URLConnection``  以及提供一些工具类操作。

  ```java
  
  public abstract class URLStreamHandler {
      
      abstract protected URLConnection openConnection(URL u) throws IOException;
  
      /**
       * 使用代理创建连接
       * @since 1.5
       */
      protected URLConnection openConnection(URL u, Proxy p) throws IOException {
          throw new UnsupportedOperationException("Method not implemented.");
      }
  
      /**
       * 解析URL
       */
      protected void parseURL(URL u, String spec, int start, int limit) {
         	....
          setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
      }
  
      //默认端口 查看子类实现
      protected int getDefaultPort() {
          return -1;
      }
  
      // URL 比较
      protected boolean  hostsEqual(URL u1, URL u2) {
          InetAddress a1 = getHostAddress(u1);
          InetAddress a2 = getHostAddress(u2);
          // if we have internet address for both, compare them
          if (a1 != null && a2 != null) {
              return a1.equals(a2);
          // else, if both have host names, compare them
          } else if (u1.getHost() != null && u2.getHost() != null)
              return u1.getHost().equalsIgnoreCase(u2.getHost());
           else
              return u1.getHost() == null && u2.getHost() == null;
      }
  	
      。。。。。。。
  }
  
  ```


- 从系统中获取参数

  ```java
  public class GetPropertyAction implements PrivilegedAction<String> {
      private String theProp;
      private String defaultVal;
  
      public GetPropertyAction(String var1) {
          this.theProp = var1;
      }
  
      public GetPropertyAction(String var1, String var2) {
          this.theProp = var1;
          this.defaultVal = var2;
      }
  
      public String run() {
          String var1 = System.getProperty(this.theProp);
          return var1 == null ? this.defaultVal : var1;
      }
  }
  ```

- 协议默认端口

  - ftp:21
  - http:80
    
  - https:443
  
- 内建实现的协议

  sun.net.www.protocol 下包即为内建实现：file,ftp,http,https,jar,mailto,netdoc

- 通过``java.net.URLStreamHandlerFactory``   作为 ``java.net.URLStreamHandler``的工厂创建处理器。只有手动

  ​	如果内部缓存中存在协议映射的处理器直接获取对应处理器。

  ​	如果提前设置了工厂类，获取处理器的时候会首先从工厂中获取。

  ​	见``java.net.URL#getURLStreamHandler ``      

- 设置系统参数，协议存在多个位置使用 | 进行分割，其中 sun.net.www.protocol 为内建协议处理器位置。

  ```tex
  -Djava.protocol.handler.pkgs=sun.net.www.protocol|com.castle.net.protocol
  ```

  



### 自定义castle协议

#### ``com.castle.net.protocol.castle.CastleURLConnection``

```java
/**
 * @Author LuoShangLin
 */
public class CastleURLConnection extends URLConnection {
    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected CastleURLConnection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {
        System.out.println("CastleURLConnection#connect....");
    }
}
```

#### ``com.castle.net.protocol.castle.Handler``

```java
/**
 * @Author LuoShangLin
 * com.castle.net.protocol.castle.Handler
 */
public class Handler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) {
        System.out.println("Handler#openConnection...");
        return new CastleURLConnection(u);
    }
}
```

#### ``com.castle.net.protocol.MyURLStreamHandlerFactory``

```java
/**
 * @Author LuoShangLin
 */
public class MyURLStreamHandlerFactory implements URLStreamHandlerFactory {

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        System.out.println("MyURLStreamHandlerFactory#openConnection 运行了");
        System.out.println("URL="+protocol);
        return new Handler();
    }
}
```

#### ``com.castle.net.protocol.TestURL``

​	测试自定义协议

```java
/**
 *
 * -Djava.protocol.handler.pkgs=sun.net.www.protocol|com.castle.net.protocol
 * @Description TODO
 * @Author LuoShangLin
 * @Date 2019/5/31 15:36
 */
public class TestURL {

    public static void main(String[] args) {
        try {
            /*URL.setURLStreamHandlerFactory(new MyURLStreamHandlerFactory());*/
            URL httpURL = new URL(null, "castle://www.baidu.com");
            URLConnection urlConn = httpURL.openConnection();
            urlConn.connect();
            System.out.println(urlConn);
        }catch (Exception e){
            System.err.println("自定义协议可能需要添加虚拟机参数： -Djava.protocol.handler.pkgs=sun.net.www.protocol|com.castle.net.protocol");
            e.printStackTrace();
        }
    }
}

```

#### 		Attention

- castle协议的处理器Handler 必须位于包castle下，否则内部实现找不到处理器会抛出异常：

``java.net.MalformedURLException: unknown protocol: castle``

- 自定义内建协议要将处理器工厂置空：URL.setURLStreamHandlerFactory(null);



### Spring Boot Application

#### **what is it?**

[8. Introducing Spring Boot](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#getting-started-introducing-spring-boot)

> Spring Boot makes it easy to create stand-alone, production-grade Spring-based Applications that you can run. We take an ***opinionated*** view of the Spring platform and third-party libraries, so that you can get started with minimum fuss. Most Spring Boot applications need very little Spring configuration.

> You can use Spring Boot to create Java applications that can be started by using `java -jar` or more traditional war deployments. We also provide a command line tool that runs “spring scripts”.

​		 Spring Boot 是基于spring的的应用，可以简单的创建独立的，接近生产的（部署友好）可运行的应用。提供了对spring项目和第三方库的固化的视图减少启动的烦恼，大部分的Spring Boot 基本不需要配置。

#### Personal

​		Spring Boot 实际是 Spring的一层外壳，使用COC的理念（[convention over configuration](https://baike.baidu.com/item/%E7%BA%A6%E5%AE%9A%E4%BC%98%E4%BA%8E%E9%85%8D%E7%BD%AE/22718049?fr=aladdin)），抛弃xml驱动，通过注解驱动达到尽可能减少各种配置。通过对依赖的封装提供快速的项目依赖。通过不同的依赖创建不同的应用类型的一种微服务框架。

#### **“fat” Jar** 

[91.4 Create an Executable JAR with Maven](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#howto-create-an-executable-jar-with-maven)

继承父依赖：

```xml
<!-- Inherit defaults from Spring Boot -->
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.5.RELEASE</version>
	</parent>
```

构建可执行jars需依赖的maven构建插件

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```

不继承父依赖

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<version>2.1.5.RELEASE</version>
			<executions>
				<execution>
					<goals>
						<goal>repackage</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```



#### **[Launcher Manifest](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#executable-jar-launcher-manifest)** 

**``META-INF/MANIFEST.MF``**

```tsx
Manifest-Version: 1.0
Implementation-Title: url
Implementation-Version: 0.0.1-SNAPSHOT
Start-Class: com.castle.net.UrlApplication
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Build-Jdk-Spec: 1.8
Spring-Boot-Version: 2.1.5.RELEASE
Created-By: Maven Archiver 3.4.0
Main-Class: org.springframework.boot.loader.JarLauncher
```



#### **Question**

- *通过命令 java -jar xxx.jar 执行的时候`JarLauncher `线程和 `UrlApplication` 线程的关系？谁主谁次？*

- *``org.springframework.boot.loader.JarLauncher``   实现原理？*

- *自定义协议，处理器做了哪些工作？*

  

#### JarLauncher

- 引导方法：`org.springframework.boot.loader.JarLauncher#main`

```java
public class JarLauncher extends ExecutableArchiveLauncher {

	static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";

	static final String BOOT_INF_LIB = "BOOT-INF/lib/";

	public JarLauncher() {
	}

	protected JarLauncher(Archive archive) {
		super(archive);
	}

	@Override
	protected boolean isNestedArchive(Archive.Entry entry) {
		if (entry.isDirectory()) {
			return entry.getName().equals(BOOT_INF_CLASSES);
		}
		return entry.getName().startsWith(BOOT_INF_LIB);
	}

	public static void main(String[] args) throws Exception {
		new JarLauncher().launch(args);
	}
}
```

- `org.springframework.boot.loader.Launcher#launch`  运行App主方法

```java
public abstract class Launcher {

	/**
	 * Launch the application. This method is the initial entry point that should be
	 * called by a subclass {@code public static void main(String[] args)} method.
	 * @param args the incoming arguments
	 * @throws Exception if the application fails to launch
	 */
	protected void launch(String[] args) throws Exception {
		JarFile.registerUrlProtocolHandler();
		ClassLoader classLoader = createClassLoader(getClassPathArchives());
		launch(args, getMainClass(), classLoader);
	}
    
    
    protected void launch(String[] args, String mainClass, ClassLoader classLoader)
			throws Exception {
		Thread.currentThread().setContextClassLoader(classLoader);
		createMainMethodRunner(mainClass, args, classLoader).run();
	}
}
```

- `org.springframework.boot.loader.jar.JarFile#registerUrlProtocolHandler` 注册自定义协议

```java
public class JarFile extends java.util.jar.JarFile {
	//...//
	public static void registerUrlProtocolHandler() {
		String handlers = System.getProperty(PROTOCOL_HANDLER, "");
		System.setProperty(PROTOCOL_HANDLER, ("".equals(handlers) ? HANDLERS_PACKAGE
				: handlers + "|" + HANDLERS_PACKAGE));
		resetCachedUrlHandlers();
	}
	//...//
	private static void resetCachedUrlHandlers() {
		try {
			URL.setURLStreamHandlerFactory(null);
		}
		catch (Error ex) {
			// Ignore
		}
	}
    //...//
}
```

- Spring自定义jar 协议

  `org.springframework.boot.loader.jar.Handler`

  `org.springframework.boot.loader.jar.JarURLConnection`

#### Memo

[Spring Boot官方文档](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#getting-started-introducing-spring-boot)

[Spring Cloud 官方文档](https://cloud.spring.io/spring-cloud-static/Greenwich.SR1/single/spring-cloud.html)

[Spring Boot 脚手架]( https://start.spring.io/ )

[spring-boot-loader maven 坐标](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-loader)

```xml
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-loader</artifactId>
<version>2.1.5.RELEASE</version>
<scope>provided</scope>
```
