<template>
    <el-popover placement="top-start" title="订单详情" width="200" trigger="hover" :content="detailContent">
        <el-button slot="reference">
            <slot></slot>
        </el-button>
    </el-popover>
</template>
<script>

export default {
    props: {
        id: String,
        orderId: String,
        productId: String,
        quantity: Number,
        price: Number
    },
    computed: {
        detailContent: function() {
            return `${this.id} + ${this.orderId} + ${this.productId} + ${this.quantity} + ${this.price}`
        }
    }

}
</script>

<style></style>