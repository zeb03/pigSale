<template>
    <div class="goods_show clearfix">
        <goods-card v-for="item in goodsList" :key="item.productId" :product-id="item.productId" :image="item.image"
            :product-name="item.productName" :price="item.price" :category-name="item.categoryName"
            :origin="item.origin">
        </goods-card>
    </div>
</template>

<script>
import GoodsCard from './GoodsCard.vue'
export default {
    components: {
        GoodsCard
    },
    props: {
        list: {
            type: Array,
            default: () => {
                return []
            }
        }
    },
    watch: {
        list: function (val) {
            this.goodsList = val
        }
    },
    data() {
        return {
            goodsList: this.list,//列表数据
        }
    },

}
</script>

<style scoped>
.goods_show {
    min-height: 650px;
}
</style>