package com.shsxt.crm.base;


public class BaseQuery {

    /**
     * Layui的数据表格需要的分页参数
     *      page 当前页
     *      limit 每页显示的数量
     */
    private Integer page=1;
    private Integer limit=10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
