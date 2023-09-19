# test-swing - EVE工具


### 打包方式
> 1. 运行TesterSwingApplication，生成EasyScript_UI_Main的辅助代码
> 2. maven -> package
>> 2.1 先执行一次test-parent级别的package
>> 2.2 再执行tester-swing的compile，然后再package
> 3. 拿到打包好的jar，复制到C:\Users\18883\Desktop\near2\MyDetect下面
> 4. 启动
>   1. 启动文件见：resources/bootFile
>   2. eve_monitor.bat 为启动本程序脚本
>   3. 欧服_挂机.bat 为启动eve脚本
>
---  

### 结构介绍
> 1. swing包。本包为启动类所在位置，启动方法：EasyScript.start()
>   1. 初始化界面
>   2. 调用EasyScript_UI_Main.start()初始化事件
> 2. 使用注解配置注入
>   1. 启动类添加注解：`@ServletComponentScan`
>   2. 具体实现：`@WebFilter`, `@WebServlet`，`@WebListener`
>      1. `com.tester.testerwebapp.myServlet.MyServlet`
>      2. `com.tester.testerwebapp.myServlet.MyServletListener`
>      3. `com.tester.testerwebapp.myServlet.MyWebFilter`
---  






