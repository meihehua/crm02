package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.utils.CookieUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件查询营销机会列表
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryByParams(SaleChanceQuery saleChanceQuery) {
        return saleChanceService.queryByParams(saleChanceQuery);
    }
    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index() {

        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     * @param saleChance
     * @param request
     * @return
     */
    @PostMapping("add")
    @ResponseBody
public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        // 从cookie中获取用户名
        String userName = CookieUtil.getCookieValue(request, "userName");
        saleChance.setCreateMan(userName);
        // 调用Service层
        saleChanceService.addSaleChance(saleChance);
        return success();

}

    /**
     * 进入添加或修改营销机会的页面
     * @return
     */
    @RequestMapping("toAddorUpdatePage")
public String toAddorUpdatePage(){
        return "saleChance/add_update";
}
}
