#### 1.Servlet Interface

请求的处理方法

• doGet for handling HTTP GET requests
• doPost for handling HTTP POST requests
• doPut for handling HTTP PUT requests
• doDelete for handling HTTP DELETE requests
• doHead for handling HTTP HEAD requests
• doOptions for handling HTTP OPTIONS requests
• doTrace for handling HTTP TRACE requests

##### 1.1 servlet 生命周期

​		servlet 生命周期主要体现在``javax.servlet.Servlet`` api的方法包括初始化 init，提供服务service，销毁阶段destroy。 一般的servlet 要继承 `GenericServlet` or `HttpServlet` 来提供功能。

​		servlet 的加载和初始化又servlet 容器负责。在容器加载阶段或者servlet 需要使用阶段进行加载。servlet engine启动的时候servlet container必须加载所需要的servlet classes。加载可通过本地classloader，local file system，network services。

​		servlet 对象被实例化之后容器调用``javax.servlet.Servlet#init(ServletConfig arg)`` 来初始化对象。

ServletConfig 对象和servlet 一对一，servlet 可通过ServletConfig 获取配置的name-value初始化参数。

#### 2.Servlet Context

```java
容器的提供者负责实现ServletContext
Tomcat服务器实现类为 ：ApplicationContext
Jetty容器的实现类：ApplicationContextFacade
Mock测试解决方案也会实现：MockServletContext,SpringBootMockServletContext
```
#### 3.Request

##### 3.1 HTTP Protocol Parameters

##### 3.2 Attributes

##### 3.3 Headers

##### 3.4 Request Path Elements

​	匹配服务的请求路径由几个重要部分组成（从路径中解析出来）

- Context Path ：如果是默认上下则为空字符串，否则就是以 /开头，但不以/结尾的字符。

- Servlet Path ：匹配Servlet的路径

- PathInfo：其他的路径信息以外的信息

| Context Path            | /catalog      |
| ----------------------- | ------------- |
| Servlet Mapping Pattern | /lawn/*       |
| Servlet                 | LawnServlet   |
| Servlet Mapping Pattern | /garden/*     |
| Servlet                 | GardenServlet |
| Servlet Mapping Pattern | *.jsp         |
| Servlet                 | JSPServlet    |



| Request Path                | Path Elements                                                |
| --------------------------- | ------------------------------------------------------------ |
| /catalog/lawn/index.html    | ContextPath: /catalog<br/>ServletPath: /lawn<br/>PathInfo: /index.html |
| /catalog/garden/implements/ | ContextPath: /catalog<br/>ServletPath: /garden<br/>PathInfo: /implements/ |
| /catalog/help/feedback.jsp  | ContextPath: /catalog<br/>ServletPath: /help/feedback.jsp<br/>PathInfo: null |

##### 3.5 Path Translation Methods

• ServletContext.getRealPath
• HttpServletRequest.getPathTranslated



##### 3.6 **Cookies**

HttpServletRequest提供方法 getCookies 来获取来自客户端的cookies缓存



##### 3.7 SSL Attributes

如果请求是通过一些安全协议传输 例如 HTTPS，HttpServletRequest 可以通过 ServletRequest#isSecure 感知到，同时向开发者暴露以下属性。

Table .Protocol Attributes

| Attribute                 | Attribute Name                     | Java Type |
| ------------------------- | ---------------------------------- | --------- |
| cipher suite              | javax.servlet.request.cipher_suite | String    |
| bit size of the algorithm | javax.servlet.request.key_size     | Integer   |

如果请求中有SSL certificate证书会被包装成 ``java.security.cert.X509Certificate`` 数组，然后以 `javax.servlet.request.X509Certificate.` 作为key 绑定在请求中。

##### 3.8 Internationalization

ServletRequest 提供方法判断当前客户端的地区

• getLocale
• getLocales

##### 3.9 请求数据编码

​		Content-Type 未设置字符编码，请求默认使用 `ISO-8859-1` 编码创建reader和解析post数据。同时方法 `getCharacterEncoding` 会返回null 来表明调用者未设置字符编码。

​		为何springboot项目自测的字符编码都为 utf8？

 一个请求在 `org.springframework.web.servlet.FrameworkServlet` 处理之前会经过``org.apache.catalina.core.ApplicationFilterChain`` 管理的一些的一系列`ApplicationFilterConfig` 改对象持有过滤器对不同的请求做不同的处理。其中过滤器

``org.springframework.web.filter.CharacterEncodingFilter#doFilterInternal`` 中将所有的请求默认处理为utf-8。``OrderedCharacterEncodingFilter`` 为springboot 实现，继承`CharacterEncodingFilter` (spring-web实现) ，实现`Ordered` 接口用于排序。``org.springframework.web.util.WebUtils`` 存放了大量和Servlet规范相关的属性枚举。

其中所有的过滤器如下所示：

```java
"ApplicationFilterConfig[name=characterEncodingFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter]"

org.springframework.web.multipart.support.MultipartFilter
//for instanceis to use a normal POST with an additional hidden form field ({@code _method})
"ApplicationFilterConfig[name=hiddenHttpMethodFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter]"
    
"ApplicationFilterConfig[name=formContentFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedFormContentFilter]"
    
"ApplicationFilterConfig[name=requestContextFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter]"
    
"ApplicationFilterConfig[name=productionSecurityFilter, filterClass=com.github.xiaoymin.swaggerbootstrapui.filter.ProductionSecurityFilter]"
    
"ApplicationFilterConfig[name=securityBasicAuthFilter, filterClass=com.github.xiaoymin.swaggerbootstrapui.filter.SecurityBasicAuthFilter]"
    
"ApplicationFilterConfig[name=Tomcat WebSocket (JSR356) Filter, filterClass=org.apache.tomcat.websocket.server.WsFilter]"
```

一个请求处理完会发布 请求处理完成事件 ``org.springframework.web.context.support.ServletRequestHandledEvent`` 404 会重新调度一次。

springboot 的请求使用 ``RequestFacade``包装。

##### 3.10 请求的生命周期

​		一个请求对象仅仅存活于 servlet’s service method或者within the scope of a filter’s doFilter method。

往往容器考虑到性能的开销会回收请求对象。所以开发者要考虑到在作用域以外的区域引用，否则会出现不确定的表现行为。

#### 4.Response











