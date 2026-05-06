package com.sky.aspect;

import com.fasterxml.jackson.core.ObjectCodec;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类自动填充公共字段
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    private final ObjectCodec objectCodec;

    public AutoFillAspect(ObjectCodec objectCodec) {
        this.objectCodec = objectCodec;
    }

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在切入点方法执行之前执行,用于自动填充公共字段
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("自动填充公共字段...");
        //获取当前拦截方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获取注解的值，即数据库操作类型
        OperationType operationType = autoFill.value();
        //获取当前拦截方法的参数对象
        Object[] args = joinPoint.getArgs();
        if(args==null || args.length==0){
            return;
        }
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();//当前时间
        Long currentUserId = BaseContext.getCurrentId();//当前登录用户id

        //根据数据库操作类型，自动填充公共字段
        if(operationType==OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //反射为实体类的公共字段赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentUserId);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //反射获取实体类的setUpdateTime方法并调用，填充
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //反射为实体类的公共字段赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
