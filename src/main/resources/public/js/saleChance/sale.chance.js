layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 营销机会数据列表
     */
    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url : ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth : 95,
        page : true, // 开启分页
        height : "full-125",
        limits : [10,15,20,25], // 可以选择每页显示的数量
        limit : 10, // 默认每页显示的数量
        toolbar: "#toolbarDemo", // 头部工具栏
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"}, // 复选框
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            // 行工具栏
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value){
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }

    }

    /**
     * 表格重载
     *  营销机会的条件搜索
     */
    $(".search_btn").click(function () {
        tableIns.reload({
            where: { // 设定异步数据接口的额外参数，任意设
                // 客户名
                customerName:$("#customerName").val(),
                // 创建人
                createMan:$("[name='createMan']").val(),
                // 分配状态
                state:$("#state").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });
    /**
     * 绑定头部工具栏
     *  table.on('toolbar(表格的lay-filter属性值)', function(obj){
     *
     *  });
     */
    table.on('toolbar(saleChances)',function (obj) {
        //console.log(obj);
        // 判断事件类型
        switch (obj.event) {
            case "add": // 添加操作
                openAddSaleChanceDialog();
                break;
            case "del": // 删除操作
                break;

        }
    });
    /**
     * 打开添加营销机会对话框
     */
function openAddSaleChanceDialog() {
  var title="营销机会管理——机会添加"
        layui.layer.open({
            type:2,//iframe层
            title:title,//标题
            area:['380px','90%'],//弹出框的宽高
            maxmin:true,// 可最大化最小化
            content:ctx+'/sale_chance/toAddorUpdatePage'//iframe的url// url地址
        });
    }
});
