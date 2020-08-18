package com.tester.testeraop.aop;

import com.tester.testeraop.controller.CloudOfficeException;
import com.tester.testeraop.controller.StackTraceAnnotation;
import com.tester.testeraop.controller.UserOperationDO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Aspect
@Component
@Order(1)
@Slf4j
public class ServiceAspect {

    @Autowired
    private ApplicationContext applicationContext;


    @Around(value = "execution(* com.tester.testeraop.service.impl.DaoServiceImpl.*(..))")
//    @Around(value = "@annotation(com.tester.testeraop.controller.StackTraceAnnotation)")
    public Object traceBackgroundThread2(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("service aspect was triggered!");
        Object[] args = pjp.getArgs();
        // 1.获取注解
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        StackTraceAnnotation annotation = declaringClass.getAnnotation(StackTraceAnnotation.class);
        if(null == annotation || !checkStackTraceIfNeedOperationLog(annotation)){
            // 如果没有注解，或者调用堆栈中没有需要添加操作记录的service，返回
            return pjp.proceed(args);
        }
        // 2.执行方法
        Object next = null;
        try {
            next = pjp.proceed(args);
        }catch (CloudOfficeException coe){
            throw coe;
        }catch (Throwable ex){
            throw ex;
        }
        // 3.如果没有抛出异常，则开始记录
        // 3.1获取参数，以及执行的sql。
        String methodName = method.getName();
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        String[] parameterNames = ms.getParameterNames();
        StringBuilder sb = new StringBuilder();
        sb.append("methodName: ").append(methodName).append("<br/>");
        sb.append("methodParams: ");
        for (int i = 0; i < parameterNames.length; i++) {
            sb.append(parameterNames[i]).append(":").append(args[i]).append(",");
        }
        sb.append("<br/>").append("sql:").append(getSql(annotation,methodName));
        log.info("request in. content:{}",sb.toString());
        UserOperationDO ope = new UserOperationDO();
        ope//.setOperationType(operationType)
                .setOperationContent(sb.toString())
//                .setOriginalData(null)
//                .setNewData(null)
                .setTraceId(getTraceId())
                .setDeletedReason(0)
                .setOperatorEmployeeId(null)
                .setOperatorPersonId(null)
                .setOperatorWechatid(null)
                .setDataFrom(null);
        setDataAndOperationType(methodName, ope, next);
        System.out.println(ope);
        return next;
    }

    private void deleteLog(){
        // todo deleted log that recorded before. by traceId
    }

    private String getSql(StackTraceAnnotation annotation, String methodName){
        String mapper = annotation.mapper();
        String sql = "";
        if(StringUtils.isEmpty(mapper) || StringUtils.isEmpty(methodName)){
            return sql;
        }
        try{
            sql = "select * from xxx";
        // todo 获取sql
        /*Configuration configuration = sqlSessionFactory.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement("com.aeon.dmc.cloud.office.core.dao.mapper.user.OrganizationMapper.selectByPrimaryKey");
        BoundSql boundSql = mappedStatement.getBoundSql(null);
        String sql = boundSql.getSql();*/
        }catch (Throwable t){

        }
        return sql;
    }

    private String getTraceId(){

        // todo 获取traceId
        return "traceId";
    }


    private void setDataAndOperationType(String methodName,UserOperationDO ope, Object result){
        int operationType = 0;
        if(methodName.startsWith("create")){
            ope.setOriginalData(null);
            ope.setNewData(String.valueOf(result));
            operationType = 1;
            return ;
        }else if(methodName.startsWith("delete")){
            ope.setOriginalData(null);
            ope.setNewData(String.valueOf(result));
            operationType = 2;
            return ;
        }else if(methodName.startsWith("update")){
            ope.setOriginalData(null);
            ope.setNewData(String.valueOf(result));
            operationType = 3;
            return ;
        }else if(methodName.startsWith("list")||methodName.startsWith("get")){
            ope.setOriginalData(null);
            ope.setNewData(null);
            operationType = 4;
            return ;
        }
        ope.setOperationType(operationType);
    }

    private boolean checkStackTraceIfNeedOperationLog(StackTraceAnnotation annotation){
        String mapper = annotation.mapper();
        if(StringUtils.isEmpty(mapper) || annotation.servicesToBeRecorded().length <= 0){
            return false;
        }
        String[] logServices = annotation.servicesToBeRecorded();
        List<Boolean> collect = Stream.of(new Throwable().getStackTrace())
                .map(e -> {
                    for (int i = 0; i < logServices.length; i++) {
                        if (Objects.equals(e.getClassName(), logServices[i])) {
                            return true;
                        }
                    }
                    return false;
                })
                .filter(e -> e)
                .collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(collect)){
            return true;
        }
        return false;
    }
    private void printStackTrace(){
        List<StackTraceElement> traceList = new ArrayList<>();
        boolean needOperationLog = false;
        for (StackTraceElement element : new Throwable().getStackTrace()) {
            traceList.add(element);
            System.out.println(element.getClassName());
            System.out.println(element.getMethodName());
            String[] beanNamesForType = applicationContext.getBeanNamesForType(element.getClass());
            for(int i = 0; i< beanNamesForType.length; i++){
                Object bean = applicationContext.getBean(beanNamesForType[i]);
            }
            Annotation[] annotations = element.getClass().getAnnotations();
            System.out.println("annotations.length:"+annotations.length);
            for(int i = 0; i< annotations.length; i++){
                System.out.println("annotation:"+annotations[i].getClass().getTypeName());
            }
            /*if(null != annotation){
                needOperationLog = true;
                break;
            }*/
        }
        System.out.println(traceList.size());
    }


}
