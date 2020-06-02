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

​		所有服务端返回给客户端的信息都封装在改对象中，http协议中，信息携带者为头或者体。

##### 4.1 Buffering

​		servlet 容器允许但不强制使用缓冲，缓冲可以提升效率。一般都使用默认，同时也可以设置以下参数来控制其缓冲行为。getBufferSize，setBufferSize， isCommitted， reset， resetBuffer， flushBuffer

``setBufferSize``：servlet可以通过该方法自定义缓冲池的大小。为了保证缓存空间的复用，响应缓冲大小不应低于请求数据的大小。该方法需要再``ServletOutputStream`` /``Writer``使用前使用否则异常``IllegalStateException`` 会被抛出。

``isCommitted`` ：方法返回是否有任何字节的数据写到客户端。

``flushBuffer`` :方法强制将缓冲区的数据写到客户端。

``reset``：方法会在响应未提交之前清除缓冲数据和头部以及状态码。

``resetBuffer``：同reset比，只会清除缓冲区数据。

​		一旦响应发生提交之后缓冲区大小不可改变，如果调用了改变缓冲区的方法如：``setBufferSize``   ,``reset`` 或者``resetBuffer`` 会抛出``IllegalStateException``异常。

##### 4.2 Headers

​		``HttpServletResponse`` 可以通过方法`addHeader`  ` setHeader` 来设置头部信息。

##### 4.3 Convenience Methods

​		``sendRedirect``，``sendError``均通过设置特殊的头部和当前body实现``sendError``  可传入可选参数用content  body。``sendRedirect``会设置一个名字为Location的header对应重定向的绝对路径。相当于一下设置：

```java
response.addHeader("Location","http://localhost:9090/test1/");
response.setStatus(HttpStatus.FOUND.value());
```

##### 4.4 国际化

​		为了更方便准确的和客户端沟通语言类型 响应对象提供该方法``setLocale`` 设置语言默认编码是`ISO-8859-1`

spring-boot 默认utf-8。

##### 4.5 响应关闭

​		响应对象即将关闭节点：service方法结束  `setContentLength`  `sendError`  `sendRedirect` 调用之后。

##### 4.6 响应生命周期

​		同请求对象一样响应对象仅存活与servlet#service ,filter#doFilter方法之中。出于性能考虑容器可能会回收对象，所以响应对象不能存在其他范围内。

#### 5.过滤器

##### 功能

- 请求调用前获取资源
- 请求调用前获取请求对象
- 通过请求包装类处理请求的数据（头，体数据）
- 修改响应的头和响应数据
- 拦截调用的资源
- 过滤器可以不存在，存在多个，可以在一个或者多个Servlet的环境 中运行

​		过滤器种类：认证 ，日志，图像转换，数据压缩，编解码，令牌管理，发送资源接触事件，XML转换，缓存，mimeType过滤链。

​		过滤器抛出``UnavailableException`` 异常，容器可能会重试整个过滤链（如果这个异常没有被标记为永久的permanent= true）。

​		容器会在最后移除过滤器的时候调用destroy 方法，一般用来释放资源。

​		对请求和响应的封装是过滤器的核心概念，提供开发者



#### 6.Sessions	

​		Http协议是基于请求响应的，是无状态的协议。保证系统的有效运行，必须建立客户端和服务器之间的联系。Servlet规范定义了接口`HttpSession` 来跟踪用户的会话。不需要开发人员的额外处理。

##### 会话跟踪机制的实现

- Cookies
- SSL Session
- URL 重写机制 exp：`http://www.myserver.com/catalog/index.html;jsessionid=1234`

Sesstion 可以绑定和解绑attribute通过方法：`valueBound`,`valueUnBound`。可以设置超时时间，最后使用时间。

#### 7.Dispatching Requests

[source code](https://github.com/javaee/servlet-spec/blob/master/src/main/java/javax/servlet/RequestDispatcher.java)



同名带参数的获取请求派发器在处理的过程中优先是什么意思？

Servlet属性负载是在forward ，include 的时候进行设置的。

includ Servlet的RequestDispatcher 是通过getNamedDispatcher获取的included属性不会设置。





参数查询字符串中指定用于创建RequestDispatcher
优先于其他参数相同的名称传递给包括在内
servlet。相关的参数用RequestDispatcher是应用范围
只包括向前或调用的持续时间。

https://blog.csdn.net/weixin_38809962/article/details/79300558

[状态码](https://tools.ietf.org/html/rfc7231#section-6.4.3)











