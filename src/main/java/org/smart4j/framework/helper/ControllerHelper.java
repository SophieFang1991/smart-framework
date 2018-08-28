package org.smart4j.framework.helper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 * */
public final class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系(简称Action Map)
     * */
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<Request,Handler>();

    //封装一个Action_map用于存放request与Handler之间的映射关系.
    static {
        //获取所有的controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)){
            //遍历这些controller类
            for (Class<?> controllerClass:
                 controllerClassSet) {
                //获取controller类中定义的方法
                Method[] methods=controllerClass.getDeclaredMethods();
                for (Method method:
                     methods) {
                    //判断当前方法是否带有action的注解
                    if (method.isAnnotationPresent(Action.class)){
                        //从action注解中获取url映射规则
                        Action action=method.getAnnotation(Action.class);
                        String mapping=action.value();
                        //验证url映射规则
                        if (mapping.matches("\\w+:/\\w*")){
                            String[] array=mapping.split(":");
                            if (ArrayUtils.isNotEmpty(array)&&array.length==2){
                                //获取请求方法和请求路径
                                String requestMethod=array[0];
                                String requestPath=array[1];
                                Request request=new Request(requestMethod,requestPath);
                                Handler handler=new Handler(controllerClass,method);
                                //初始化Action map
                                ACTION_MAP.put(request,handler);
                            }
                        }
                    }
                }


            }
        }
    }
/**
 * 获取Handler
 * */
public static Handler getHandler(String requestMethod,String requestPath){
    Request request=new Request(requestMethod,requestPath);
    return ACTION_MAP.get(request);
}

}
