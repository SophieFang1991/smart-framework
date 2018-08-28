package org.smart4j.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化helper相关的类
        HelperLoader.init();
        //获取ServletContext对象(用于注册Servlet)
        ServletContext servletContext=servletConfig.getServletContext();
        //注册处理jsp的servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认servlet
        ServletRegistration defaultServelt=servletContext.getServletRegistration("default");
        defaultServelt.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    @Override
    public  void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径与请求方法
        String requestMethod=req.getMethod().toLowerCase();
        String requetpath=req.getPathInfo();
        //获取action处理器
        Handler handler=ControllerHelper.getHandler(requestMethod,requetpath);
        if (handler!=null){
            //获取Controller类以及其bean实例
           Class<?> controllerClass= handler.getControllerClass();
            Object controllerBean=BeanHelper.getBean(controllerClass);
            //创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String, Object>();
            Enumeration<String> parameterNames=req.getParameterNames();
            while(parameterNames.hasMoreElements()){
                String paramName=parameterNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body=CodecUtil.decodeURl(StreamUtil.getString(req.getInputStream()));
            if (StringUtils.isNotEmpty(body)){
                String[] params=StringUtils.split(body,"&");
                if (ArrayUtils.isNotEmpty(params)){
                    for (String param:
                         params) {
                        String[] array=StringUtils.split(param,"=");
                        if (ArrayUtils.isNotEmpty(array)&&array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            //调用action方法
            Method actionMethod=handler.getActionMethod();
            Object result=ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            //处理action方法的返回值
            if (result instanceof View){
                //返回jsp页面
                View view=(View)result;
                String path=view.getPath();
                if (StringUtils.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        resp.sendRedirect(req.getContextPath()+path);
                    }else{
                        Map<String,Object> model=view.getModel();
                        for (Map.Entry<String,Object> entry:
                             model.entrySet()) {
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
                    }
                }
            }else if(result instanceof Data){
                //返回json数据
                Data data=(Data)result;
                Object model=data.getModel();
                if (model!=null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer=resp.getWriter();
                    String json=JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }

    }
}
