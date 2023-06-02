<template>
    <div class="goods_search">
        <div class="header_search">
            <input class="search_input" v-model="keyword" @keydown.enter="searchGoods" type="text" placeholder="请输入关键字搜索">
            <input class="search_submit"  @click="searchGoods" type="submit" value="搜索">
        </div>
    </div>
</template>

<script>
export default {
    name: 'GoodsSearchOne',
    props: {
        initKeyword: {
            type: String,
            default: ''
        },
        inputWidth: {
            type: Number,
            default: 150
        }
    },
    watch: {
        initKeyword: function(val) {
            console.log(val)
            this.keyword = val
        }
    },
    data() {
        return {
            keyword: this.initKeyword,//搜索内容
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

<style scoped>
.goods_search {
    display: inline-block;
}

.header_search .search_input {
    width: 450px;
    height: 18px;
    padding: 10px;
    padding-left: 40px;
    border: 2px solid #409eff9e;
    border-right: none;
    border-radius: 20px 0px 0px 20px;
    vertical-align: middle;
}

.header_search .search_input:hover {
    border-color: #409EFF;
}

.header_search .search_input:focus {
    border-color: #409EFF;
    outline: none;
}

.header_search .search_submit {
    color: white;
    font-size: 17px;
    font-weight: bold;
    height: 41px;
    padding: 0 20px;
    border: hidden;
    border-radius: 0px 20px 20px 0px;
    background-color: #409EFF;
    vertical-align: middle;
    cursor: pointer
}
</style>