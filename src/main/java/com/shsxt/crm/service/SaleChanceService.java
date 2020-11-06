package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 根据条件查询营销机会
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> queryByParams(SaleChanceQuery saleChanceQuery) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        // 开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        // 得到营销机会列表
        List<SaleChance> list = saleChanceMapper.querySaleChanceByParams(saleChanceQuery);
        // 得到当前的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(list);
        // 总记录数
        map.put("count", pageInfo.getTotal());
        // 当前页的列表
        map.put("data",pageInfo.getList());

        return map;
    }
    /**添加营销机会
     * 1. 非空判断
     *              customerName客户名  非空
     *              linkMan联系人       非空
     *              linkPhone联系号码   非空且格式正确
     *          2. 判断是否设置了指派人
     *              如果指派人为空 （未指派）
     *                  assignTime分配时间  设置为null
     *                  state分配状态       设置为未分配   1=已分配，0=未分配
     *                  devResult开发状态   设置为未开发   0=未开发，1=开发中，2=开发成功，3=开发失败
     *              如果指派人不为空 （已指派）
     *                  assignTime分配时间  设置为当前时间
     *                  state分配状态       设置为已分配   1=已分配，0=未分配
     *                 devResult开发状态   设置为开发中   0=未开发，1=开发中，2=开发成功，3=开发失败
     *          3. 设置默认值
     *              isValid是否有效     设置为1  1=有效，0=无效
     *              createDate创建时间  设置为当前时间
     *              updateDate修改时间  设置为当前时间
     *          4. 执行添加操作，判断受影响的行数
     *
     */
    @Transactional(propagation = Propagation.REQUIRED )
    public void addSaleChance(SaleChance saleChance){
        //非空判断
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //判断是否设定了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            //如果指派人为空 （未指派）
         //assignTime分配时间  设置为null
            saleChance.setAssignTime(null);
          //state分配状态       设置为未分配   1=已分配，0=未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
          //devResult开发状态   设置为未开发   0=未开发，1=开发中，2=开发成功，3=开发失败
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else{
            //如果指派人不为空 （已指派）
        //               assignTime分配时间  设置为当前时间
            saleChance.setAssignTime(new Date());
          // state分配状态       设置为已分配   1=已分配，0=未分配
            saleChance.setState(StateStatus.STATED.getType());
         //devResult开发状态   设置为开发中   0=未开发，1=开发中，2=开发成功，3=开发失败
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //设置默认值
               //  isValid是否有效     设置为1  1=有效，0=无效
        saleChance.setIsValid(1);
               //   createDate创建时间  设置为当前时间
        saleChance.setCreateDate(new Date());
                //    updateDate修改时间  设置为当前时间
        saleChance.setUpdateDate(new Date());
        // 4. 执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会数据添加失败！");
    }
    /**
     * 营销机会跟新
     * 营销机会修改
     *     1. 参数校验
     *         id主键     非空，且id对应的数据存在
     *         客户名     非空
     *         联系人     非空
     *         手机号     非空，且格式正确
     *     2. 设置默认值
     *         修改时间    设置为当前系统时间
     *     3. 设置相关字段的值
     *         原来的数据，指派人为空
     *             修改后，指派人为空
     *
     *             修改后，指派人非空
     *                 assignTime分配时间  设置为当前时间
     *                 state分配状态       设置为已分配   1=已分配，0=未分配
     *                 devResult开发状态   设置为开发中   0=未开发，1=开发中，2=开发成功，3=开发失败
     *         原来的数据，指派人非空
     *             修改后，指派人为空
     *                 assignTime分配时间  设置为null
     *                 state分配状态       设置为未分配   1=已分配，0=未分配
     *                 devResult开发状态   设置为未开发   0=未开发，1=开发中，2=开发成功，3=开发失败
     *             修改后，指派人非空
     *                 当修改前后，指派人不一致时，修改指派时间
     *     4. 执行修改操作，判断受影响的行数
     */
    public void updateSaleChance(SaleChance saleChance){
        //参数检验
        //通过Id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断Id是否为空
        AssertUtil.isTrue(temp == null,"待更新用户不存在");


    }

    /**
     * 判断必填参数是否为空
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
    //客户名
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
        //联系人
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        //联系号码
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系号码不能为空");
        //手机号是否合法
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系号码格式不正确");
    }

}