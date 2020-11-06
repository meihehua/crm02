package com.shsxt.crm;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 *      返回视图
 *      返回JSON
 *  方法的返回有两种：
 *      返回视图
 *      返回JSON
 *  如何判断方法返回的是视图还是JSON？
 *      通过判断方法上是否设置@ResponseBody注解
 *      如果返回值为null，表示返回视图；否则返回JSON
 *
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler, Exception e) {


        /**
         * 判断是否是未登录异常
         *      如果是，则重定向到登录页面
         */
        if (e instanceof NoLoginException) {
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }


        /**
         * 设置默认的异常处理
         */
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg","系统异常，请重试...");

        /**
         * 判断方法的返回值
         */
        if (handler instanceof HandlerMethod) {
            // 转换HandlerMethod类型
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 得到方法上@ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // 判断注解是否为空 如果为空，返回视图；如果不为空，则返回JSON
            if (null == responseBody) {
                /**
                 * 返回视图
                 */
                // 判断自定义异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                } else if (e instanceof NoLoginException) {
                    NoLoginException nl = (NoLoginException) e;
                    modelAndView.addObject("code", nl.getCode());
                    modelAndView.addObject("msg",nl.getMsg());
                }

                return modelAndView;

            } else {
                /**
                 * 返回JSON
                 */
                // 设置默认异常信息
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试！");

                // 处理自定义异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                // 设置响应类型及 编码格式
                httpServletResponse.setContentType("application/json;charset=UTF-8");

                // 得到字符输出流
                PrintWriter writer = null;
                try {
                    writer = httpServletResponse.getWriter();
                    // 将对象（JavaBean、List、Map等）转换成JOSN格式的字符串
                    String json = JSON.toJSONString(resultInfo);
                    // 输出字符串
                    writer.write(json);
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // 关闭流
                    if (writer != null) {
                        writer.close();
                    }
                }

                return null;
            }

        }




        return modelAndView;
    }
}
