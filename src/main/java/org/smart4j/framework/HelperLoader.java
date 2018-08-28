package org.smart4j.framework;
//用于加载ClassHelper,BeanHelper,IOCHelper,controllerHelper,实际上是加载他的静态代码块
/**
 * 加载相应的helper类
 *
 */

import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;
import org.smart4j.framework.util.ClassUtil;

public class HelperLoader {
    public static void init(){
        Class<?>[] classList={ ClassHelper.class, BeanHelper.class, IocHelper.class, ControllerHelper.class };
        for (Class<?> cls:
             classList) {
            ClassUtil.loadClass(cls.getName(),false);
        }

    }
}
