package org.smart4j.framework.bean;

import java.util.Map;

/**
 * 请求参数对象
 * */
public class Param {
     private Map<String,Object> paramMap;
     public Param(Map<String,Object> paramMap){
         this.paramMap=paramMap;
     }
     /**
      * 根据参数名获取long类型的参数值
      * */
     public long getLong(String name){
         return Long.valueOf(String.valueOf(paramMap.get(name)));
     }
     /**
      * 获取所有字段的信息
      * */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
