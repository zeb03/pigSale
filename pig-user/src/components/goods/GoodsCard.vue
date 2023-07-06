<template>
    <div class="goods_card">
        <a :href="'#/goods/' + productId" target="_blank">
            <div style="width:228px;height: 150px;">
                <el-image :src="getImgUrl(image)" style="width:228px;height: 150px;"></el-image>
            </div>
            <div>
                <h4>{{ productName }} &nbsp;</h4>
                <div class="price">￥{{ price.toFixed(2) }}</div>
            </div>
            <div class="info">
                <span>{{ categoryName }}</span>
                • {{ origin }}
            </div>
        </a>
    </div>
</template>

<script>
    export default {
        props: {
            productId: {
                type: String,
                default: "1",
            },
            productName: {
                type: String,
                default: "商品名称",
            },
            price: {
                type: Number,
                default: 0,
            },
            categoryName: {
                type: String,
                default: "分类",
            },
            origin: {
                type: String,
                default: "商品产地",
            },
            image: {
                type: String,
                default: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
            }
        },
        data() {
            return {}
        },
        methods: {
            getImgUrl(img) {
                return `http://localhost:9999/common/download?filename=${img}`
            },
        }

    }
</script>

<style scoped>
    .goods_card {
        position: relative;
        display: inline-block;
        width: 228px;
        height: 270px;
        margin-right: 60px;
        margin-bottom: 15px;
        box-shadow: 2px 1px 5px #ccc;
        background-color: #fff;
    }

    .goods_card:hover {
        background-color: #eeeeee80;
    }

    .goods_card a {
        /* display: inline; */
        text-decoration: none;
    }

    .goods_card img {
        width: 228px;
        height: 151px;
    }

    .goods_card h4 {
        display: inline-block;
        width: 120px;
        height: 30px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        margin: 20px 10px 10px 25px;
        font-size: 17px;
        color: #050505;
        font-weight: 400;
    }

    .goods_card .price {
        position: absolute;
        right: 20px;
        bottom: 32px;
        color: #409EFF;
        font-size: 18px;
    }

    .goods_card .info {
        margin: 0 20px 0 25px;
        font-size: 12px;
        color: #999;
    }

    .goods_card .info span {
        color: #ff7c2d;
    }
</style>