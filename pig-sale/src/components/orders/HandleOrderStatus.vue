<template>
    <div>
        <el-popconfirm v-if="status === 2" title="确定修改订单状态吗？" @confirm="updateOrdersStatus(3)">
            <el-button slot="reference" size="mini" type="warning" plain>发货</el-button>
        </el-popconfirm>
        <el-popconfirm v-else-if="status === 3" title="确定完成订单吗？" @confirm="updateOrdersStatus(4)">
            <el-button slot="reference" size="mini" type="success" plain>完成</el-button>
        </el-popconfirm>
        <el-popconfirm v-else-if="status === 5" title="确定同意退款吗？" confirm-button-text='同意' cancel-button-text='不同意'
            @confirm="onHandleAgreeClick()" @cancel="onHandleDisagreeClick()">
            <el-button slot="reference" size="mini" type="danger" plain>退款</el-button>
        </el-popconfirm>
    </div>
</template>
  
<script>
export default {
    props: {
        status: {
            type: Number,
            default: 1
        },
        orderId: {
            type: String,
        }
    },
    computed: {
        statusStr: function () {
            let statusStr = '状态未知'
            this.options.forEach((val) => {
                if (val.value === this.status) {
                    statusStr = val.label
                }
            })
            return statusStr
        }
    },
    watch: {

    },
    data() {
        return {
            options: [{
                value: 1,
                label: '待付款'
            }, {
                value: 2,
                label: '待发货'
            }, {
                value: 3,
                label: '已发货'
            }, {
                value: 4,
                label: '已收货'
            }, {
                value: 5,
                label: '申请退款'
            }, {
                value: 6,
                label: '已取消'
            }],
            value: this.status
        }
    },
    methods: {
        // TODO: 点击后应该重新刷新页面
        onHandleAgreeClick() {
            this.$api.orders.handleAgree(this.orderId)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.data)
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
        },
        onHandleDisagreeClick() {
            this.$api.orders.handleDisagree(this.orderId)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.data)
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
        },
        updateOrdersStatus(status) {
            console.log(this.orderId)
            this.$api.orders.updateStatus(this.orderId, status)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.data)
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
        }
    }
}
</script>
  