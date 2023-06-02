<template>
    <div>
        <h4 class="text-center">订单管理</h4>
        <el-row type="flex" justify="space-around" style="margin-bottom:10px">
            <el-input v-model="searchOrdersID" clearable placeholder="请输入订单号" style="width:150px; margin-right: 10px;"
                @keydown.enter.native="searchOrders"></el-input>
            <el-date-picker v-model="date" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss" range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期">
            </el-date-picker>
            <el-button type="primary" plain @click="searchOrders">查询</el-button>
            <el-button type="info" plain @click="requestOrdersData">显示所有</el-button>
        </el-row>
        <!-- 订单表格 -->
        <orders-table :orderslist="list" style="margin-bottom:10px" @cancelOrder="requestOrdersData({})"></orders-table>
        <el-pagination :total="total" :page-size="pageSize" layout="total, jumper, prev, pager, next" background
            @current-change="handleCurrentChange">
        </el-pagination>
    </div>
</template>

<script>
import OrdersTable from './OrdersTable.vue';


export default {
    //TODO: 申请再来一单或取消订单后分页插件页数不会返回1
    components: { OrdersTable },
    name: 'MyOrder',
    data() {
        return {
            currentPage: 1,//当前页码
            total: 0,//列表数量总数
            pageSize: 5,//当前页码的数量
            list: [],//列表数据
            searchOrdersID: '',//搜索的id
            date: ''
        }
    },
    //发送请求，请求第一页
    created: function () {
        this.requestOrdersData({})
    },
    methods: {
        handleCurrentChange(val) {
            // console.log(`当前页: ${val}`);
            this.currentPage = val
            this.requestOrdersData({ currentPage: val })
        },
        searchOrders() {
            let ordersId = this.searchOrdersID
            this.requestOrdersData({ ordersId })
        },
        requestOrdersData({ currentPage = 1, pageSize = this.pageSize, ordersId = null }, successMsg = '成功加载数据') {
            // console.log(currentPage, pageSize)
            const params = {
                currentPage,
                pageSize,
                ordersId,
                beginTime: this.date[0],
                endTime: this.date[1]
            }
            this.$api.orders.getAll(params)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(successMsg)
                        let { list, total } = response.data
                        this.list = list
                        this.total = total
                        // console.log(this.list, this.total);
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        onAddSuccess(msg) {
            this.requestOrdersData({}, msg)
        }

    },

}
</script>

<style lang="less" scoped></style>