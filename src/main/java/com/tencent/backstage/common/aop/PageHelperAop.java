package com.tencent.backstage.common.aop;//package com.tencent.common.aop;
//
//import com.github.pagehelper.PageHelper;
//import com.tencent.common.utils.PageBean;
//import javassist.ClassClassPath;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.bytecode.CodeAttribute;
//import javassist.bytecode.LocalVariableAttribute;
//import javassist.bytecode.MethodInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Modifier;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Aspect
//@Slf4j
//public class PageHelperAop {
//    //使用线程本地变量
//    private static final ThreadLocal<PageBean> pageBeanContext = new ThreadLocal<>();
//
//    //以WithPage结尾的Controller方法都是需要分页的方法
//    @Before(value = "execution(public * com.tencent.stageserver.api.*.*WithPage(..))")
//    public void controllerAop(JoinPoint joinPoint) throws Exception {
////        log.info("Controller正在执行PageHelperAop");
//        PageBean pageBean =null;
//
//        Object[] args = joinPoint.getArgs();
//        //获取类名
//        String clazzName = joinPoint.getTarget().getClass().getName();
//        //获取方法名称
//        String methodName = joinPoint.getSignature().getName();
//        //通过反射获取参数列表
//        Map<String,Object > nameAndArgs = this.getFieldsName(this.getClass(), clazzName, methodName,args);
//
//        Integer currentPage = 1;
//        Integer pageSize = 10;
//        if(nameAndArgs.get("currentPage") != null){
//            currentPage = (Integer) nameAndArgs.get("currentPage");
//        }
//        if(nameAndArgs.get("pageSize") != null){
//            pageSize = (Integer) nameAndArgs.get("pageSize");
//        }
//        if(pageBean == null){
//            pageBean = new PageBean();
//            pageBean.setCurrentPage(currentPage);
//            pageBean.setPageSize(pageSize);
//        }
//        //将分页参数放置线程变量中
//        pageBeanContext.set(pageBean);
//    }
//
//    @Before(value = "execution(public * com.tencent.serviceimpl.*.*WithPage(..)) || execution(public * com.tencent.serviceimpl.BaseService.*WithPage(..))")
//    public void serviceImplAop(){
////        log.info("Impl正在执行PageHelperAop");
//        PageBean pageBean = pageBeanContext.get();
//        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
//    }
//
////    @AfterReturning(value = "execution(* com.zrl.mapper.*.*WithPage(..))")
////    public void mapperAop(){
////        log.info("mapper正在执行PageHelperAop");
////    }
//
//    /**
//     * 通过反射获取参数列表
//     * @throws Exception
//     */
//    private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
//        Map<String,Object > map=new HashMap<String,Object>();
//
//        ClassPool pool = ClassPool.getDefault();
//        ClassClassPath classPath = new ClassClassPath(cls);
//        pool.insertClassPath(classPath);
//
//        CtClass cc = pool.get(clazzName);
//        CtMethod cm = cc.getDeclaredMethod(methodName);
//        MethodInfo methodInfo = cm.getMethodInfo();
//        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
//        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
//        if (attr == null) {
//            // exception
//            return map;
//        }
//        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
//        for (int i = 0; i < cm.getParameterTypes().length; i++){
//            map.put( attr.variableName(i + pos),args[i]);
//        }
//        return map;
//    }
//}