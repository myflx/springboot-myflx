配置类
org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration
为org.springframework.web.client.RestTemplate添加不同的org.springframework.http.client.ClientHttpRequestInterceptor
如负载均衡的拦截器：
org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor


创建请求创建工厂用于客户端请求的创建
org.springframework.http.client.InterceptingClientHttpRequestFactory-->org.springframework.http.client.ClientHttpRequestFactory
org.springframework.http.client.SimpleClientHttpRequestFactory

将客户端请求包装成：org.springframework.http.client.InterceptingClientHttpRequest 
————————————————e——————>org.springframework.http.client.AbstractBufferingClientHttpRequest
————————————————e——————>org.springframework.http.client.AbstractClientHttpRequest
—-------—-------i—----->org.springframework.http.client.ClientHttpRequest

InterceptingClientHttpRequest携带的拦截器（实现）：
org.springframework.http.client.support.BasicAuthenticationInterceptor
org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor
org.springframework.boot.actuate.metrics.web.client.MetricsClientHttpRequestInterceptor
org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor



执行前调用
org.springframework.web.client.RestTemplate.AcceptHeaderRequestCallback
—-------—-------i—----->org.springframework.web.client.RequestCallback
requestCallback.doWithRequest(request);------>设置headers



执行请求核心：
org.springframework.http.client.InterceptingClientHttpRequest#executeInternal
代理执行
org.springframework.http.client.InterceptingClientHttpRequest.InterceptingRequestExecution#execute
最后执行落到：
org.springframework.cloud.client.loadbalancer.LoadBalancerClient#execute(java.lang.String, org.springframework.cloud.client.loadbalancer.LoadBalancerRequest<T>)
