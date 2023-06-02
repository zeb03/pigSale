<template>
    <div class="orders_container">
        <el-table :data="orderslist" height="520" style="width: 100%" stripe>
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="id" align="center" label="订单号" width="140">
            </el-table-column>
            <el-table-column align="center" label="订单状态" width="140">
                <template slot-scope="{ row }">
                    <!-- 订单状态 -->
                    <orders-status :status-num="row.status"></orders-status>
                </template>
            </el-table-column>
            <el-table-column prop="userName" align="center" label="用户名" width="100">
            </el-table-column>
            <el-table-column prop="phone" align="center" label="电话" width="140">
            </el-table-column>
            <!-- 创建时间 -->
            <el-table-column prop="createTime" align="center" label="创建时间" width="130">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <!-- 更新时间 -->
            <el-table-column prop="checkoutTime" align="center" label="最后更改时间" width="130">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.checkoutTime }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="实付金额" width="150">
                <template slot-scope="{ row }">
                    {{ row.totalPrice.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="140">
                <template slot-scope="{ row }">
                    <el-button @click="onOrderDetailClick(row)" size="mini" style="margin-bottom: 5px;">查看商品</el-button>
                    <handle-order-status :status="row.status" :orderId="row.id"></handle-order-status>
                </template>
            </el-table-column>
        </el-table>
        <!-- 订单信息 -->
        <el-dialog :title="'订单详情(' + orderDetails[0].orderId + ')'" :visible.sync="dialogVisible"
            :before-close="handleClose">
            <div v-for="item in orderDetails" :key="item.id">
                <el-descriptions>
                    <el-descriptions-item label="商品ID">{{ item.productId }}</el-descriptions-item>
                    <el-descriptions-item label="商品价格">{{ item.price }}</el-descriptions-item>
                    <el-descriptions-item label="下单数量">{{ item.quantity }}</el-descriptions-item>
                </el-descriptions>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import HandleOrderStatus from './HandleOrderStatus.vue';
import OrderDetails from './OrderDetails.vue';
import OrdersStatus from './OrdersStatus.vue';

export default {
    components: { OrdersStatus, OrderDetails, HandleOrderStatus },
    name: 'ordersTable',
    props: {
        orderslist: {
            type: Array,
        }
    },
    data() {
        return {
            multipleSelection: [],
            beforeEdmintData: this.orderslist,
            edmintData: {},
            dialogVisible: false,
            orderDetails: [
                {
                    "id": "1",
                    "orderId": "7",
                    "productId": 6,
                    "quantity": 2,
                    "price": 20.8
                },
                {
                    "id": "2",
                    "orderId": "7",
                    "productId": 6,
                    "quantity": 2,
                    "price": 20.8
                },
                {
                    "id": "3",
                    "orderId": "7",
                    "productId": 6,
                    "quantity": 2,
                    "price": 20.8
                }
            ]

        }
    },
    methods: {
        handleClose() {
            this.dialogVisible = false
        },
        onOrderDetailClick(row) {
            this.dialogVisible = true
            this.orderDetails = row.orderDetails
        }
    }
}
</script>

<style lang="less" scoped></style>