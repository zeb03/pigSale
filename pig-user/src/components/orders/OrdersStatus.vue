<template>
    <div>
        {{ statusStr }}
    </div>
</template>
  
<script>
export default {
    props: {
        status: {
            type: Number,
            default: 1
        },
        ordersId: {
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
        value: function (newVal) {
            this.updateOrdersStatus(this.ordersId, newVal)
        }
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
        updateOrdersStatus(ordersId, status) {
            console.log(ordersId, status)
            this.$api.orders.updateStatus(ordersId, status)
        }
    }
}
</script>
  