<template>
    <div class="scroll_goods">
        <slot></slot>
        <ul class="list">
            <li v-for="item in goodsList" :key="item.productId" class="list-item">
                <goods-card :product-id="item.productId" :image="item.image" :product-name="item.productName" :price="item.price"
                    :category-name="item.categoryName" :origin="item.origin">
                </goods-card>
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
            isLock: false,     // 是否上锁,决定是否可以发请求
            loading: true,
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
            return this.isLastPage || this.goodsList.length > 40
        },
    },
    watch: {
        noMore: function (val) {
            if (val) {
                this.loading = false
            }
        }
    },
    created: function () {
        //发送请求，请求商品第一页
        this.requestGoodsData({})
    },
    mounted() {
        // 监听页面滚动事件
        window.addEventListener("scroll", this.scrolling);

    },
    beforeDestroy() {
        window.removeEventListener("scroll", this.scrolling);
    },
    methods: {
        scrolling() {
            // 滚动条距文档顶部的距离
            let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            //变量windowHeight是可视区的高度
            let windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
            //变量scrollHeight是滚动条的总高度
            let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
            // console.log(scrollTop, scrollHeight, windowHeight);   
            //滚动条到时，请求数据
            if (!this.isLock && !this.noMore && scrollTop + windowHeight > scrollHeight * 0.9) {
                // 立刻关锁
                this.isLock = true
                // console.log(scrollTop, scrollHeight);
                this.requestNextPageData()
            }
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
                    } else {
                        this.$message.error(response.msg)
                    }
                    // 开锁
                    this.isLock = false
                })
                .catch((error) => {
                    setTimeout(() => {
                        // 两秒后开锁
                        this.isLock = false
                    }, 2000)
                })
        }
    }
}
</script>

<style scoped>
.scroll_goods {
    /* height: 800px; */
}

.list-item {
    display: inline-block;
}

.footer {
    margin: 20px 0px;
}

.scroll_goods p {
    text-align: center;
}
</style>