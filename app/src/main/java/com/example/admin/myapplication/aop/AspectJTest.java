package com.example.admin.myapplication.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by Administrator on 2017/8/9.
 */

@Aspect
public class AspectJTest {
    private static final String TAG = "tag00";

    @Pointcut("execution(@com.example.admin.myapplication.aop.AspectJAnnotation  * *(..))")
    public void executionAspectJ() {

    }

    @Around("executionAspectJ()")
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Log.i(TAG, "bamboo ==  aroundAspectJ(ProceedingJoinPoint joinPoint)");
//        AspectJAnnotation aspectJAnnotation = methodSignature.getMethod().getAnnotation(AspectJAnnotation.class);
//        String permission = aspectJAnnotation.value();
//        Context context = (Context) joinPoint.getThis();
//        Object o = null;
//        if (PermissionManager.getInnerInstance().checkPermission(context,permission)) {
//            o = joinPoint.proceed();
//            Log.i(TAG, "有权限");
//        } else {
//            Log.i(TAG, "没有权限，不给用");
//        }
//        return o;
        return joinPoint.proceed();
    }
}
