package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {

        ResultInfo resultInfo = new ResultInfo();

        /*try {
            // 调用Service的方法，如果有异常则捕获
            UserModel userModel = userService.userLogin(userName,userPwd);
            // 将返回的对象设置resultInfo对象中
            resultInfo.setCode(200);
            resultInfo.setResult(userModel);
        } catch (ParamsException e) {
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
            return resultInfo;
        } catch (Exception e) {
            resultInfo.setCode(500);
            resultInfo.setMsg("系统异常，请重试！");
            return resultInfo;
        }*/

        // 调用Service的方法，如果有异常则捕获
        UserModel userModel = userService.userLogin(userName,userPwd);
        // 将返回的对象设置resultInfo对象中
        resultInfo.setCode(200);
        resultInfo.setResult(userModel);

        return resultInfo;
    }

    /**
     * 修改用户密码
     1. 通过形参接收前台传递的参数
     2. 通过工具类，从cookie中获取当前登录用户的ID
     3. 调用Service层，执行修改密码操作
     4. 如果捕获到异常则设置失败的状态码和提示信息，否则响应成功
     * @param request
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     * @return
     */
    @PostMapping("user/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPwd,
                                         String newPwd, String repeatPwd) {
        ResultInfo resultInfo = new ResultInfo();

       /*try {
           // 通过工具类，从cookie中获取当前登录用户的ID
           Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
           // 调用Service层，执行修改密码操作
           userService.updateUserPwd(userId, oldPwd, newPwd, repeatPwd);

           resultInfo.setCode(200);
       } catch (ParamsException e) {
           e.printStackTrace();
           resultInfo.setCode(e.getCode());
           resultInfo.setMsg(e.getMsg());
           return resultInfo;
       } catch (Exception e) {
           e.printStackTrace();
           resultInfo.setCode(500);
           resultInfo.setMsg("修改密码失败！");
       }*/

        // 通过工具类，从cookie中获取当前登录用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用Service层，执行修改密码操作
        userService.updateUserPwd(userId, oldPwd, newPwd, repeatPwd);

        resultInfo.setCode(200);

        return resultInfo;
    }

    /**
     * 进入修改密码页面
     * @return
     */
    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage() {

        return "user/password";
    }

}