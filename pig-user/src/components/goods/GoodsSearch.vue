<template>
    <div class="goods_search">
        <!-- 输入框 -->
        <el-input v-model="keyword" clearable placeholder="输入商品名称关键词" :style="{ width: inputWidth + 'px' }"></el-input>
        <!-- 搜索按钮 -->
        <el-button type="primary" @click="searchGoods" plain>搜索</el-button>
    </div>
</template>

<script>
export default {
    name: 'GoodsSearch',
    props: {
        inputWidth: {
            type: Number,
            default: 150
        }
    },
    data() {
        return {
            keyword: null,//搜索内容
        }
    },
    methods: {
        // 搜索
        searchGoods() {
            this.$emit('onSearch', this.keyword)
        },
    }

}
</script>

<style></style>