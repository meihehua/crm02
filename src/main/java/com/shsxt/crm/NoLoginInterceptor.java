package com.shsxt.crm;

import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法访问拦截  拦截器
 *      继承HandlerInterceptorAdapter适配器
 *
 * 实现步骤：
 *      1. 实现拦截器
 *          在拦截器中判断用混是否登录，未登录抛出未登录异常
 *      2. 全局异常处理
 *          在异常处理类中，判断未登录异常，重定向到登录页面
 *      3. 设置拦截器生效
 *          设置需要拦截的资源及放行的资源
 *
 *  判断用户是否是登录状态
 *      判断Cookie中是否有用户ID
 *          如果用户ID不存在，或用户对象不存在，抛出未登录异常
 *
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    /**
     * 在目标方法执行前执行
     *      return true：表示允许目标方法执行
     *      return false：阻止目标方法执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取用户ID （从cookie获取）
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断Cookie中是否有用户ID
        if (null == userId || null == userService.selectByPrimaryKey(userId)) {
            // 抛出未登录异常
            throw  new NoLoginException();
        }
        return true;
    }
}
