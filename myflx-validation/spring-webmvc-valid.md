

#### spring-webmvc 整合JSR303原理

`org.springframework.web.servlet.DispatcherServlet#doDispatch`

`...`

`org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument`

在该方法里边组装关键对象：`org.springframework.web.bind.WebDataBinder` 主要通过工厂类 `org.springframework.web.bind.support.WebDataBinderFactory` 构造。方法实现类：`org.springframework.web.bind.support.DefaultDataBinderFactory#createBinder`













