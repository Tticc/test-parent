package com.tester.testerspring.app24;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * bean 后置处理器测试
 */
public class Application24 {
    public static void main(String[] args) {
        GenericApplicationContext ctx = new GenericApplicationContext();
        ctx.registerBean("bean241", Bean241.class);
        ctx.registerBean("bean242", Bean242.class);
        ctx.registerBean("bean243", Bean243.class);

        // 处理字符串注入
        ctx.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // @Autowired和@Value生效
        ctx.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        // @Resource，@PostConstruct,@PreDestroy生效
        ctx.registerBean(CommonAnnotationBeanPostProcessor.class);

        // 初始化容器
        ctx.refresh();
        // 执行销毁
        ctx.close();
    }

}
