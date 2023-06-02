<template>
    <el-tag :type="status.type" :effect="status.effect">
        {{ status.label }}
    </el-tag>
</template>
  
<script>
export default {
    props: {
        statusNum: {
            type: Number,
            default: 0
        }
    },
    computed: {
        status: function () {
            return this.statusOptions[this.statusNum]
        }
    },
    data() {
        return {
            statusOptions: [
                { effect: 'plain', label: '未知', type: 'info' },
                { effect: 'light', label: '待付款', type: '' },
                { effect: 'dark', label: '待发货', type: 'warning' },
                { effect: 'dark', label: '已发货', type: 'success' },
                { effect: 'light', label: '已收货', type: 'success' },
                { effect: 'dark', label: '申请退款', type: 'danger' },
                { effect: 'light', label: '已取消', type: 'danger' },
            ],
        }
    },

}
</script>
  