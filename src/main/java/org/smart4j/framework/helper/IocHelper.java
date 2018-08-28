package org.smart4j.framework.helper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * IOC:通过框架自身来实例化bean,就叫做IOC
 *
 * 代码思路:先通过BeanHelper 获取所有的bean Map(是一个Map<Class<?>,Object>结构,记录类和对象的映射关系).然后遍历这个映射关系,
 * 分别取出bean类和bean实例,进而通过反射获取类中所有的field,进而遍历所有的fields,找到使用Inject注解的field,之后把bean赋给这个field
 * */
public final class IocHelper {
    static{
        //获取所有的Bean类与Bean实例之间的映射关系(Bean Map)
        Map<Class<?>,Object> beanMap=BeanHelper.getbeanMap();
        if (MapUtils.isNotEmpty(beanMap)){
            //遍历Bean Map
            for (Map.Entry<Class<?>,Object> beanEntry:
                 beanMap.entrySet()) {
                //从beanMap中获取Bean类和bean实例
                Class<?> beanClass=beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                //获取Bean类定义的所有的成员变量(简称Bean Field)
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)){
                    //遍历beanFields
                    for (Field field:
                         beanFields) {
                        //判断是否带有Inject注解
                        if ( field.isAnnotationPresent(Inject.class)){
                            //到beanMap中获取Bean Field 对应的实例
                            Class<?> beanFieldClass = field.getType();
                            Object beanFiledInstance = beanMap.get(beanFieldClass);
                            if (beanFiledInstance!=null){
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance,field,beanFiledInstance);
                            }
                        }

                    }
                }

            }
        }
    }
}
