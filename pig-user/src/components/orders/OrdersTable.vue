<template>
    <div class="orders_container">
        <el-table :data="orderslist" height="520" style="width: 100%" stripe>
            <el-table-column prop="id" align="center" label="订单号" width="180">
            </el-table-column>
            <el-table-column prop="checkoutTime" align="center" label="下单时间" width="170">
            </el-table-column>
            <el-table-column align="center" label="实付金额" width="130">
                <template slot-scope="{ row }">
                    ￥{{ row.orderDetails[0].price.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column align="center" label="订单状态" width="140">
                <template slot-scope="{ row }">
                    <orders-status :status="row.status" :ordersId="row.id"></orders-status>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="140">
                <template slot-scope="{ row }">
                    <el-button v-if="row.status === 3" type="primary" plain @click="confirmOrder(row.id)">确认收货</el-button>
                    <el-button v-else-if="row.status !== 4" type="primary" plain @click="onRemove(row.id)">取消/退款</el-button>
                    <el-button v-else type="primary" plain @click="orderAgain(row.id)">再下一单</el-button>

                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import OrderDetails from './OrderDetails.vue';
import OrdersStatus from './OrdersStatus.vue';

export default {
    components: { OrdersStatus, OrderDetails },
    name: 'ordersTable',
    props: {
        orderslist: {
            type: Array,
        },
        currentPage: 1

    },
    data() {
        return {
            multipleSelection: [],
            beforeEdmintData: this.orderslist,
            edmintData: {},
            currentPage: 1
        }
    },
    methods: {
        onRemove(orderId) {
            this.$confirm('此操作将取消订单, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.remove(orderId)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },
        remove(orderId) {
            this.$api.orders.cancel(orderId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.msg)
                        this.$emit('cancelOrder')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        orderAgain(orderId) {
            this.$api.orders.orderAgain(orderId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.msg)
                        this.$emit('cancelOrder')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        confirmOrder(orderId) {
            this.$api.orders.confirmOrder(orderId)
                .then((response) => {
                    // console.log(response);

                    if (response.code === 200) {
                        this.$message.success(response.msg)
                        this.$emit('cancelOrder')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }
}
</script>

<style lang="less" scoped></style>