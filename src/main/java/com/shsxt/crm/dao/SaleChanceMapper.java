package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.vo.SaleChance;

import java.util.List;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {

    // 多条件查询营销机列表
    List<SaleChance> querySaleChanceByParams(SaleChanceQuery saleChanceQuery);

}