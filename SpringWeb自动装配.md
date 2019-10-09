#### Spring Web 自动装配

​		spring3.1的里程牌不仅仅意义在于提供“模块装配” 能力，还有一项具有象征性意义的能力-----“Web自动装配”。自动装配是spring-boot的三大特性之一，通过应用场景上分为Web应用和非Web应用，高版本的自动装配支持仅限Web应用场景，依赖Servlet3+，如tomcat7.x或Jettty7.x。

##### 理解Web自动装配

​	传统Spring项目的启动一般是在web.xml中配置DispatcherServlet和映射等。实际才Servlet3.0+的spring项目可以不同web.xml而通过编程的手段启动web应用。

```java
public class MyWebApplicationInitializer implements WebApplicationInitializer{
	public void onStartup(ServletContext container){
		ServletRegistration.Dynamic registration = container.addServlet("dispatcer",new DistpatcherServlet());
		registration.setLoadOnStartup(1);
		registration.addMapping("/*");
	}
}
```

``org.springframework.web.WebApplicationInitializer``: 是spring-web 提供的接口确保被Servlet3容器检测到并初始化。spring同时提供了两种不同实现用于不同场景。

``org.springframework.web.servlet.support.AbstractDispatcherServletInitializer`` :用于替换web.xml 中的Servlet配置。

``org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer``:用于提供驱动注解配置类。

``org.springframework.web.context.AbstractContextLoaderInitializer`` :用于替换xml中注册的ContextLoaderListener；

##### 自定义Web自动装配

###### 	tomcat插件、依赖

```xml
<dependency>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.1</version>
    <scope>runtime</scope>
</dependency>
<build>
    <plugins>

        <!-- Maven war 插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <configuration>
                <!-- 忽略错误，当web.xml不存在时 -->
                <failOnMissingWebXml>false</failOnMissingWebXml>
            </configuration>
        </plugin>

        <!-- Tomcat Maven 插件用于构建可执行 war -->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <executions>
                <execution>
                    <id>tomcat-run</id>
                    <goals>
                        <!-- 最终打包成可执行的jar包 -->
                        <goal>exec-war-only</goal>
                    </goals>
                    <phase>package</phase>
                    <configuration>
                        <!-- ServletContext 路径 -->
                        <path>/</path>
                    </configuration>
                </execution>
            </executions>
        </plugin>

    </plugins>
</build>
```

> 注：项目要打包成war格式

###### 控制器

```java
@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping
    public String hello() {
        return "hello";
    }
}
```

###### 	实现``AbstractContextLoaderInitializer``

```java
public class SpringContextLoaderInitializer extends AbstractContextLoaderInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return new AnnotationConfigWebApplicationContext();
    }
}
```

###### 	实现``AbstractAnnotationConfigDispatcherServletInitializer``

```java
public class SpringWebMvcServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    protected Class<?>[] getServletConfigClasses() {
        return of(SpringWebMvcConfiguration.class);
    }

    protected String[] getServletMappings() {
        return of("/*");
    }

    private static <T> T[] of(T... values) {
        return values;
    }
}
```

###### 	定义基础配置类

```java
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.myflx.webmvc.controller")
public class SpringWebMvcConfiguration {
}
```

###### 	启动

通过java -jar spring-webmvc-3.2.x-1.0-SNAPSHOT-war-exec.jar  的方式启动

http://localhost:8080 访问接口即可。

> 注：此处的java -jar 实际还是tomcat的方式运行web应用。java -jar 启动项目不是spring-boot独有的方式是在开源社区的借鉴，重新实现了jar url协议实现的。

为什么继承一下现有的接口就可以实现自动装配，进而启动环境?

##### Web自动装配原理

spring自身并不具备web自动装配的能力，实际需要就是Servlet3.0才能实现。下面探讨装配过程中的Servlet3技术基础

###### Servlet配置方法（Configuration Method）

​	ServletContext接口增加了方法用于动态添加Servlet，Filter，Listener

###### 运行时插拔（runtime pluggability）

​		属于一种运行加载的风格和方式。关注的是配置方法被调用的时机，容器或应用启动时Servlet容器初始化对象``javax.servlet.ServletContainerInitializer#onStartup(Set<Class<?>> c, ServletContext ctx)``会被回调，通过注解``@HandlesTypes`` 关注自己指定类型的对象。 ``ServletContainerInitializer`` 的实现类要通过SPI的方式  在文件夹 META-INF/services 下放置名为`javax.servlet.ServletContainerInitializer` 的文本文件，里边内容为一个或多个实现类。

​		所以我们按照文件名在spring-boot的环境下全局搜索可以看到不同jar中的不同Servlet容器初始化的实现类：

spring-web :

``org.springframework.web.SpringServletContainerInitializer`` 处理类型为``WebApplicationInitializer``



关于Servlet规范是整个java web架构的基础，会单独梳理分享出来。



