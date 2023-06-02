<template>
    <div class="infinite-list-wrapper" style="overflow:auto">
        <slot></slot>
        <ul class="list" v-infinite-scroll="load" :infinite-scroll-disabled="disabled">
            <li v-for="item in goodsList" :key="item.productId" class="list-item">
            <!-- <li v-for="item in goodsList"  class="list-item"> -->
                <goods-card :image="getImgUrl(item.image)" :product-name="item.productName" :price="item.price"
                    :category-name="item.categoryName" :origin="item.origin"></goods-card>
            </li>
        </ul>
        <div class="footer">
            <p v-if="loading">加载中...</p>
            <p v-else>没有更多了</p>
        </div>
    </div>
</template>
  
<script>
import GoodsCard from './GoodsCard.vue'
export default {
    components: { GoodsCard },
    data() {
        return {
            loading: false,
            goodsList: [],     // 总列表数据
            currentPage: 0,    // 当前页码
            total: 0,          // 列表数量总数
            pageSize: 8,       // 每页显示的最大条数
            categoryId: null,  //分类显示的分类ID
            list: [],          // 当前页的列表数据
            isLastPage: false  //是否为最后一页
        }
    },
    computed: {
        noMore() {
            return this.isLastPage || this.goodsList.length > 50
        },
        disabled() {
            return this.loading || this.noMore
        },
        totalPage() {
            return this.total / this.pageSize
        }
    },
    created: function () {
        //发送请求，请求商品第一页
        this.requestGoodsData({})
    },
    methods: {
        load() {
            this.loading = true
            setTimeout(() => {
                this.requestNextPageData()
                this.loading = false
            }, 1000)

        },
        // 请求当前页码的数据
        handleCurrentChange(index) {
            this.requestGoodsData({ currentPage: index })
        },
        // 请求下一页的数据
        requestNextPageData() {
            if (!this.isLastPage) {
                this.handleCurrentChange(this.currentPage + 1)
            }
        },
        // 分类显示
        showCategoryGoods() {
            let categoryId = this.categoryId
            this.requestGoodsData({ categoryId })
        },
        // 显示所有
        showAllGoods() {
            // 将分类显示的分类ID = null
            this.categoryId = null
            // 清空搜索内容
            this.keyword = ''
            this.requestGoodsData({})
        },
        getImgUrl(img) {
            return `http://localhost:8080/common/download?filename=${img}`
        },
        // 请求
        requestGoodsData({ currentPage = 1, pageSize = this.pageSize, keyword = null, categoryId = null }, successMsg = '成功加载数据') {
            console.log(currentPage, pageSize, keyword, categoryId)
            let params = {
                currentPage,
                pageSize,
                keyword,
                categoryId
            }
            this.$api.goods.get(params)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        // this.$message.success(successMsg)
                        let { list, total, isLastPage } = response.data
                        this.list = list
                        this.total = total
                        this.isLastPage = isLastPage
                        this.currentPage += 1
                        // 连接数组
                        this.goodsList = this.goodsList.concat(this.list)
                        this.loading = false
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
    }
}
</script>

<style scoped>
.infinite-list-wrapper {
    height: 800px;
}

.list-item {
    display: inline-block;
}

.footer {
    margin: 20px 0px;
}

.infinite-list-wrapper p {
    text-align: center;
}
</style>