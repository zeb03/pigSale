<template>
    <div>
        <div class="product-details-container">
            <el-button type="primary" round size="medium" style="display: flex" icon="el-icon-back"
                       @click="returnPage()">
                返回首页
            </el-button>
            <el-row :gutter="20">
                <el-col :xs="24" :sm="12">
                    <div class="product-image">
                        <el-image :src="getImgUrl(product.image)" alt="`Review Image upload failed`">
                        </el-image>
                    </div>
                </el-col>
                <el-col :xs="24" :sm="12">
                    <h1 class="product-name">{{ product.productName }}</h1>
                    <p class="product-origin">{{ `产地：${product.origin} &nbsp;&nbsp;种类：${product.categoryName}` }}</p>
                    <p class="product-description">{{ `${product.description}` }}</p>
                    <div class="product-price-container">
                        <p class="product-price">{{ product.price }} <span>元</span></p>
                    </div>
                    <p class="product-sales">{{ `销量：${product.sales} 件` }}</p>
                    <p class="product-stock">{{ `库存：${product.stock} 件` }}</p>

                    <el-input-number v-model="cart.quantity" :min="1" :max="product.stock" :step="1"
                                     size="mini"></el-input-number>
                    <el-button type="primary" round icon="el-icon-shopping-cart" size="medium"
                               class="add-to-cart-button"
                               @click="addCart()">
                        加入购物车
                    </el-button>
                </el-col>
            </el-row>

            <h2 class="product-reviews-title">{{ `商品评价（评分：${product.rating} +）` }}</h2>

            <div v-if="reviews.length > 0" class="product-reviews-list">
                <div v-for="review in reviews" :key="review.id" class="product-review">

                    <div class="product-review-left">
                        <p class="product-review-author">{{ review.username }}</p>
                        <el-image :src="getImgUrl(review.avatar)" class="product-review-avatar">
                        </el-image>
                    </div>
                    <div class="product-review-right">
                        <el-rate class="product-review-rating" v-model="review.rating" disabled show-score
                                 text-color="#ff9900"
                                 score-template="{value}"></el-rate>
                        <el-col :xs="36" :sm="36">
                            <el-image :src="getImgUrl(review.image)" class="review-image">
                            </el-image>
                        </el-col>
                        <p class="product-review-content">{{ review.comment }}</p>
                        <p class="product-review-date">{{ review.publishTime }}</p>
                    </div>
                </div>
            </div>
            <p v-else class="product-reviews-empty">暂无评价</p>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                           :current-page="this.currentPage" :page-size="this.pageSize" :total="this.total" background
                           layout="prev, pager, next">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                product: {
                    productId: 7,
                    productName: "小猪",
                    description: "刚断奶的小猪，适合养殖",
                    categoryName: '黑猪',
                    categoryId: '4',
                    price: 500,
                    sales: 100,
                    rating: 3.7,
                    image: "a7.png",
                    origin: "湖南省",
                    stock: 200,
                },
                cart: {
                    productId: 7,
                    quantity: 1
                },
                reviews: [
                    {
                        reviewId: 3,
                        userId: 10,
                        productId: 7,
                        anonymous: 1,
                        comment: "很不错的小猪，质量很好，很灵活",
                        username: "张三",
                        image: "a7.png",
                        rating: 3.5,
                        avatar: "avatar.jpg",
                        publishTime: "2023-04-10"
                    }
                ],
                currentPage: 1,
                pageSize: 5,
                total: 1
            };
        },
        created() {
            this.showProduct()
            this.showReview()
        },
        methods: {
            //显示图片，发送请求至服务端
            getImgUrl(img) {
                return `http://localhost:8080/common/download?filename=${img}`
            },
            showProduct() {
                this.$api.goods.getDetails(this.$route.params.productId).then(res => {
                    console.log(res)
                    const data = res.data
                    if (data) {
                        this.product = data
                        console.log(data)
                    }

                })
            },
            showReview() {
                const params = {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    queryWay: 0,	//1代表查询用户评论、0代表查询商品评论
                    productId: this.$route.params.productId
                }
                this.$api.review.getGoodsReview(params).then(response => {
                    // console.log(res)

                    if (response.code === 200) {
                        this.$message.success('加载成功')
                        let {list, total} = response.data
                        console.log('list:')
                        console.log(list)
                        this.reviews = list
                        this.total = total
                    } else {
                        this.$message.error('加载失败')
                    }
                })

            },
            addCart() {
                this.cart.productId = this.product.productId
                this.$api.cart.updateNum(this.cart.productId, this.cart.quantity)
                    .then(res => {
                        console.log(res)
                        if (res.code === 200) {
                            this.$message({
                                message: "加入购物车成功",
                                type: "success"
                            })
                        }
                    })
            },
            handleSizeChange(val) {
                // 改变每页显示的条数
                this.pageSize = val
                // 注意：在改变每页显示的条数时，要将页码显示到第一页
                this.currentPage = 1
                this.showReview()
            },
            // 显示第几页
            handleCurrentChange(val) {
                // 改变默认的页数
                this.currentPage = val
                this.showReview()
            },
            returnPage() {
                this.$router.push("/")
            }
        }
    };
</script>

<style scoped>
    /* Container */
    .product-details-container {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        margin: 0;
        padding: 20px;
        /*max-width: 100%;*/
        background-color: #f5f5f5;
        border: 1px solid #ccc;
    }

    /* Product Image */
    .product-image {
        width: 100%;
        max-width: 700px;
        /* Updated value */
        margin: 60px auto 20px 70px;
        display: block;
        border: 5px solid #fff;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
    }


    /* Product Name */
    .product-name {
        font-size: 30px;
        margin-bottom: 10px;
        margin-top: 120px;
        color: #444;
        text-align: center;
    }

    /* Product Origin */
    .product-origin {
        font-size: 18px;
        color: #999;
        margin-bottom: 10px;
        text-align: center;
    }

    /* Product Description */
    .product-description {
        font-size: 18px;
        color: #555;
        margin-bottom: 20px;
        text-align: center;
    }

    /* Product Price */
    .product-price {
        font-size: 24px;
        color: #409eff;
        margin-bottom: 20px;
        text-align: center;
    }

    /* 修改 .add-to-cart-button 样式 */
    .add-to-cart-button {
        display: block;
        margin: 20px auto;
        background-color: #409EFF;
        color: #fff;
        font-size: 16px;
        font-weight: bold;
        width: 200px;
        height: 50px;
        border: none;
        padding: 10px 30px;
        border-radius: 25px;
        transition: all 0.3s ease;
        text-transform: uppercase;
        /* 转换为大写 */
        letter-spacing: 1px;
        /* 字母间距 */
    }

    .add-to-cart-button:hover {
        transform: translateY(-5px);
        background-color: #fff;
        color: #409EFF;
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.4);
        border: 2px solid #409EFF;
        cursor: pointer;
    }

    /* Product Reviews Title */
    .product-reviews-title {
        font-size: 24px;
        color: #444;
        margin: 100px 0 20px 0;
        text-align: center;
    }

    /* Product Reviews List */
    .product-reviews-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }

    /* Product Review */
    .product-review {
        display: flex;
        flex-wrap: wrap;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        width: 70%;
        margin: 5px 5px 5px 180px;
    }

    /* Product Review Avatar */
    .product-review-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 10px;
        margin-left: 10px;
    }

    /* Product Review Author */
    .product-review-author {
        font-size: 16px;
        color: #666;
        margin-bottom: 8px;
    }

    .product-review-rating {
        margin-bottom: 40px;
        margin-left: 100px;
        text-align: left;
    }

    /* Product Review Content */
    .product-review-content {
        font-size: 18px;
        color: #333;
        margin-bottom: 20px;
        margin-left: 100px;
        text-align: left;
        width: 200px;
    }

    /* Product Review Date */
    .product-review-date {
        font-size: 14px;
        color: #999;
        margin-top: 10px;
        margin-bottom: 20px;
        margin-left: 100px;
        text-align: left;
    }

    /* Product Reviews Empty */
    .product-reviews-empty {
        font-size: 18px;
        color: #666;
        text-align: center;
        margin: 50px auto;
    }

    .review-image {
        width: 200px;
        margin-left: 100px;
    }


    .product-image {
        margin: 50px auto 20px auto;
    }

    /* Product Name */
    .product-name {
        font-size: 24px;
    }

    /* Product Origin */
    .product-origin {
        font-size: 16px;
    }

    /* Product Description */
    .product-description {
        font-size: 16px;
    }

    /* Product Reviews Title */
    .product-reviews-title {
        font-size: 24px;
    }

    /* Product Review Author */
    .product-review-author {
        font-size: 14px;
    }

    /* Product Review Content */
    .product-review-content {
        font-size: 16px;
    }

    /* Product Review Date */
    .product-review-date {
        font-size: 12px;
    }
</style>
