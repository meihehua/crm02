package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService  extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     1. 参数校验
     判断用户名或密码是否为空，如果为空则抛出异常 （用户名称或密码不能为空！）
     2. 调用Dao层的方法，通过用户名查询用户对象，返回用户对象
     3. 判断用户对象是否为空
     如果为空，则抛出异常 （该用户不存在！）
     4. 将前台传递的密码，通过MD5算法加密
     5. 将加密后的密码与数据库中查询用户对象中的密码作比较
     如果密码不相等，则抛出异常 （用户密码不正确！）
     6. 返回User对象
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName, String userPwd) {
        // 判断用户名或密码是否为空，如果为空则抛出异常 （用户名称或密码不能为空！）
        AssertUtil.isTrue(StringUtils.isBlank(userName) || StringUtils.isBlank(userPwd), "用户名称或密码不能为空！");
        // 调用Dao层的方法，通过用户名查询用户对象，返回用户对象
        User user = userMapper.queryUserByName(userName);
        // 判断用户对象是否为空
        AssertUtil.isTrue(user == null, "该用户不存在！");
        // 将前台传递的密码，通过MD5算法加密
        String pwd = Md5Util.encode(userPwd);
        // 将加密后的密码与数据库中查询用户对象中的密码作比较
        AssertUtil.isTrue(!pwd.equals(user.getUserPwd()), "用户密码不正确！");
        // 构建用户模型，并返回
        return buildUserModel(user);
    }

    /**
     * 构建用户模型
     * @param user
     * @return
     */
    private UserModel buildUserModel(User user) {
        UserModel userModel = new UserModel();

        // 将userId进行编码
        // userModel.setUserId(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }

    /**
     * 修改密码
     1. 判断用户ID是否为空
     2. 调用Dao层，通过用户ID查询用户对象
     3. 判断用户对象是否为空
     4. 判断原始密码是否为空
     5. 判断原始密码是否正确
     6. 判断新密码是否为空
     7. 判断新密码是否与原始密码相同
     8. 判断重复密码是否为空
     9. 判断重复密码是否与新密码相等
     10. 执行更新操作，判断受影响的行数
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED )
    public void updateUserPwd(Integer userId, String oldPwd, String newPwd, String repeatPwd) {

        // 判断用户ID
        AssertUtil.isTrue(userId == null, "待更新用户不存在！");
        // 调用Dao层，通过用户ID查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 判断用户对象是否为空
        AssertUtil.isTrue(user == null, "待更新用户不存在！");
        // 判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原始密码不能为空！");
        // 判断原始密码是否正确
        AssertUtil.isTrue(!Md5Util.encode(oldPwd).equals(user.getUserPwd()), "原始密码不正确！");
        // 判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空！");
        // 判断原始密码是否和新密码一致
        AssertUtil.isTrue(newPwd.equals(oldPwd), "新密码不能与原始密码一致！");
        // 判断重复密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "重复密码不能为空！");
        // 判断重复密码与新密码是否一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "重复密码与新密码不一致！");
        // 执行更新操作
        AssertUtil.isTrue(userMapper.updateUserPwd(userId, Md5Util.encode(newPwd)) < 1, "修改密码失败！");

    }
}
