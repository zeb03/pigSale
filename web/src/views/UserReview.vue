<template>
    <div class="review-box">
        <div class="user-info">
            <!--图片路径后期可能需要更改-->
            <img :src="require(`@/assets${user.image}`)" alt="User Avatar"/>
            <div class="user-name">{{ user.userName }}</div>
        </div>

        <div class="review-option">
            <el-menu class="el-menu-demo" :default-active="'2'" mode="horizontal">
                <el-menu-item index="1" @click="show(1)">{{ `待评价（${ordersLength}）` }}</el-menu-item>
                <el-menu-item index="2" @click="show(2)">{{ `已评价（${reviewLength}）` }}</el-menu-item>
            </el-menu>
        </div>
        <!-- 待评价 -->
        <div v-show="this.index===1">
            <el-row v-for="order in orderDetails" :key="order.id" class="order-box">
                <el-col :span="12" :offset="6">
                    <div class="grid-content bg-purple-light">
                        <div class="order-id">
                            {{ `订单号：${order.orderId}` }}
                        </div>
                        <div class="order-time">
                            {{ order.checkoutTime }}
                        </div>
                        <div class="order-quantity">
                            {{ `数量：${order.quantity}` }}
                        </div>
                    </div>

                    <div class="product-box">
                        <div style="position: relative">
                            <!--跳转到商品详情-->
                            <el-link href="#" class="product-link" :underline="false">
                                <div class="product-info"
                                     style="background-color:#fff; border: #f1f1f1 3px solid;position: relative;width: 700px;height: 100px;margin-bottom: 30px;padding-top: 10px">
                                    <div class="product-image">
                                        <img :src="require(`@/assets${order.image}`)" alt="Product Image"
                                        />
                                    </div>
                                    <div class="product-name">{{ order.productName }}</div>
                                    <div class="product-description">{{ order.description }}</div>
                                    <div class="product-price">
                                        {{ `￥${order.price}` }}
                                    </div>
                                </div>
                            </el-link>

                            <div style="position: absolute;right: 10px;top: 70px">
                                <el-button type="primary" size="small" @click="toReview">评价</el-button>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
        <!-- 已评价 -->
        <div class="review-list" v-show="this.index===2">
            <div class="review-item" v-for="(review, index) in reviews" :key="index">
                <el-card class="box-card" style="width: 700px">
                    <div class="review-info">
                        <el-rate v-model="review.rating" show-score disabled></el-rate>
                        <div class="review-body">{{ review.comment }}</div>
                        <div class="review-images">
<!--                            <img v-if="review.image"-->
<!--                                 :src="require(`@/assets${review.image}`)"-->
<!--                                 :alt="`Review Image upload failed`"/>-->
<!--                            TODO:-->
                            <el-image :src="getImgUrl('ddb51e3f-d8da-4713-bc68-7cad42df3f90.ico')">
                            </el-image>
                        </div>
                        <div class="review-time">{{ review.publishTime }}</div>
                        <div style="width: 10px; margin-left: 580px; margin-top: 0">
                            <el-popconfirm
                                    title="是否确定删除？"
                                    @confirm="deleteReview(review.reviewId)"
                            >
                                <el-button slot="reference">删除</el-button>
                            </el-popconfirm>
                        </div>
                    </div>
                    <el-divider></el-divider>

                    <!-- TODO 点击后跳转到商品详情页 -->
                    <el-link href="#" class="product-link" :underline="false">
                        <div class="product-info">
                            <div class="product-image">
                                <img :src="require(`@/assets${review.productImage}`)" alt="Product Image"
                                />
                            </div>
                            <div class="product-name">{{ review.productName }}</div>
                            <div class="product-description">{{ review.description }}</div>
                        </div>
                    </el-link>
                </el-card>
            </div>
        </div>
    </div>
</template>

<style>
    body {
        padding: 0;
        margin: 0;
    }

    .user-info {
        text-align: center;
        flex-direction: column;
        align-items: center;
        margin: 0;
        background-color: #a9b5cb;
        padding-top: 30px;
        padding-bottom: 10px;
    }

    .user-info img {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        margin-bottom: 8px;
    }

    .user-name {
        font-size: 14px;
        font-weight: bold;
    }

    .review-option {
        margin-left: 370px;
    }

    .bg-purple-light {
        /*background: #e5e9f2;*/
        background-color: #f1f1f1;
    }

    .grid-content {
        display: flex;
        font-size: 14px;
        padding-top: 8px;
        margin-top: 20px;
        min-height: 22px;
    }

    .order-box {
        text-align: center;
    }

    .order-id {
        /*margin-left: 394px;*/
        margin-left: 2px;
        margin-right: 50px;
    }

    .order-time {
        font-size: 13px;
        font-weight: bold;
        margin-top: 1px;
        margin-right: 150px;
    }

    .order-quantity {
        margin-right: 63px;
    }

    .product-price {
        position: absolute;
        top: 70px;
        right: 130px;
        font-weight: bold;
    }

    .review-list {
        margin-top: 20px;
        text-align: center;
    }

    .review-item {
        display: flex;
        margin-bottom: 24px;
        margin-left: 370px;
    }

    .review-info {
        flex-grow: 1;
        text-align: left;
    }

    .review-body {
        font-size: 14px;
        margin-bottom: 10px;
        margin-top: 20px;
    }

    .review-time {
        font-size: 12px;
        color: #999;
    }

    .review-images {
        display: flex;
        flex-wrap: wrap;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .review-images img {
        width: 120px;
        height: 120px;
        margin-right: 8px;
        margin-bottom: 8px;
    }

    .product-info {
        padding-left: 10px;
        display: flex;
        background-color: rgb(238 238 238 / 64%);
        width: 700px; /*设置固定宽度*/
        white-space: nowrap; /*禁止文本换行*/
        overflow: hidden; /*隐藏溢出的内容*/
        text-overflow: ellipsis; /*显示省略号*/
    }

    .product-image {
        width: 70px;
        height: 70px;
        padding-top: 10px;
    }

    .product-image img {
        width: 60px;
        height: 60px;
        margin-right: 10px;
    }

    .product-name {
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 8px;
        margin-top: 14px;
        margin-left: 4px;
    }

    .product-description {
        font-size: 14px;
        margin-bottom: 8px;
        margin-top: 14px;
        margin-left: 12px;
    }

    .product-link {
        display: flex;
        align-items: center;
        text-decoration: none;
        color: #000;
    }
</style>

<script>
    export default {
        data() {
            return {
                index: 2,
                user: {
                    userId: 10,
                    userName: "小赵",
                    image: "/img/avatar1.png"
                },
                reviews: [
                    {
                        reviewId: 3,
                        userId: 10,
                        productId: 7,
                        anonymous: 1,
                        comment: "很不错的小猪，质量很好，很灵活",
                        username: "张三",
                        image: "/img/a7.png",
                        rating: 3.5,
                        publishTime: "2023-04-10",
                        productName: "小猪",
                        description: "刚断奶的小猪，适合养殖",
                        productImage: "/img/a7.png",
                    }
                ],
                orderDetails: [
                    {
                        id: "1",
                        orderId: "630987085964115968",
                        productId: "14",
                        quantity: 25,
                        price: 620,
                        checkoutTime: "2023-04-09 21:26:44",
                        image: "/img/a7.png",
                        productName: "武夷山乌猪",
                        description: "肉质鲜美，汁多肉鲜",
                    },
                ],
                reviewLength: 1,
                ordersLength: 1,
            }
        },
        created() {
            this.showReview();
            this.showOrders();
        },
        methods: {
            getImgUrl(img) {
                return `http://localhost:8080/common/download?filename=${img}`
            },
            /* 获取评论信息 */
            showReview() {
                this.$axios.get("/review/list/" + this.user.userId).then(res => {
                    this.reviews = res.data.data
                    this.reviewLength = res.data.data.length
                    console.log('reviews:')
                    console.log(this.reviews)
                })
            },
            /* 获取未评论的订单*/
            showOrders() {
                this.$axios.get("/order/detail/review/" + this.user.userId).then(res => {
                    console.log(res)
                    this.orderDetails = res.data.data
                    this.ordersLength = res.data.data.length
                    console.log("orderDetails:")
                    console.log(this.orderDetails)
                })
            },
            /* 点击未评价或已评价时显示不同信息*/
            show(value) {
                this.index === value ? this.isShow = !this.isShow : this.isShow = true
                this.index = value
            },

            deleteReview(reviewId) {
                this.$axios.delete("/review/remove/" + reviewId).then(res => {
                    console.log(res)
                    this.$message.success('成功删除评论')
                    /* 重新获取评论信息 */
                    this.showReview()
                })
            },
            toReview() {
                //TODO: 跳转到Review.vue页面
            }
        }
    }
</script>