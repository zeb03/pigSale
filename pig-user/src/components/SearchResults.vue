<template>
    <div>
        <top-nav></top-nav>
        <div class="search_header">
            <logo class="left"></logo>
            <goods-search-one class="search" :initKeyword="keyword" @onSearch="onSearchGoods"></goods-search-one>
        </div>
        <main class="mainer">
            <my-goods ref="goods"></my-goods>
        </main>
        
    </div>
</template>

<script>
import GoodsSearchOne from './goods/GoodsSearchOne.vue'
import Logo from './user-home/Logo.vue'
import TopNav from './user-home/TopNav.vue'
import MyGoods from './goods/MyGoods.vue'

export default {
    components: { TopNav, GoodsSearchOne, Logo, MyGoods },
    data() {
        return {
            keyword: ''
        }
    },
    methods: {
        onSearchGoods(keyword) {
            this.$refs.goods.searchGoods(keyword)
        }
    },
    created: function() {
        const keyword = this.$route.params.keyword
        if (keyword) {
            this.keyword = keyword
        }
    }

}
</script>

<style scoped>
.search_header {
    height: 100px;
    box-shadow: 0px 1px 2px #ccc;
}

.search_header .search{
    margin-top: 30px;
}

.left {
    float: left;
}

.mainer {
    min-width: 1000px;
    padding: 20px;
}

</style>