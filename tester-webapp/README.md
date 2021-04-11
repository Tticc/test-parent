# test-webapp
port:8004

### redis连接
> redis 连接池配置  
**MyLettuceConnectionConfiguration**  
继承 LettuceConnectionConfiguration，自定义连接池配置
---  

### 配置MVC自定义拦截器、servlet
> 1. 使用 MyRegistConfig配置注入
>   1. 见代码。无需添加注解：`@ServletComponentScan`
> 2. 使用注解配置注入
>   1. 启动类添加注解：`@ServletComponentScan`
>   2. 具体实现：`@WebFilter`, `@WebServlet`，`@WebListener`
>      1. `com.tester.testerwebapp.myServlet.MyServlet`
>      2. `com.tester.testerwebapp.myServlet.MyServletListener`
>      3. `com.tester.testerwebapp.myServlet.MyWebFilter`
---  

### webMvc入参测试
> RequestAttributeController、RequestParameterController
> ```
> @RequestAttribute // 这个注解不能与@ResponseBody一起使用
> @PathVariable
> @RequestHeader
> @RequestParam
> @CookieValue
> @RequestBody
> ```
---  

### webMvc配置
> MyWebMvcConfigurer
>> 1. 这里自定义了http请求入参转换器：`com.tester.testerwebapp.config.MyWebMvcConfigurer.addFormatters`  
>也定义了出参转换器：`com.tester.testerwebapp.config.MyWebMvcConfigurer.extendMessageConverters`  
>> 2. 测试类
>>    1. 入参测试：`com.tester.testerwebapp.controller.demo.RequestParameterController.testMyRequestConvert`
>>    2. 出参测试：`com.tester.testerwebapp.controller.demo.RequestParameterController.testMyResponseConvert`

---  

### thymeleaf集成  
> 引入 `spring-boot-starter-thymeleaf`  
> application.yml配置thymeleaf  
> `CooperateController` 效果

---  

### 文件上传  
> `com.tester.testerwebapp.controller.mono.UserController.uploadExcel`
> 拦截配置类： `MultiFileConfig`
---  

### react编程尝试
> `com.tester.testerwebapp.controller.mono.ContentController`  
---  

### 可重入/不可重入 redis锁
> ReentrantCacheLockInterceptor、CacheLockInterceptor  
> 具体实现的工具类在common模块。  
> `@ReentrantCacheLock(key = ConstantList.LOCK_DEFAULT_KEY+"#request.text")`
---  



















































