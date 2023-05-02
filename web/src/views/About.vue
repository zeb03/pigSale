<template>
    <div class="product-details-container">
        <div class="product-details-background"></div>
        <el-row :gutter="20">
            <el-col :xs="24" :sm="10">
                <img :src="require(`@/assets/${product.image}`)" alt="" class="product-image" />
            </el-col>
            <el-col :xs="24" :sm="14">
                <h1 class="product-name">{{ product.productName }}</h1>
                <p class="product-origin">{{ `产地：${product.origin}（${product.category}）` }}</p>
                <div class="product-price-container">
                    <p class="product-price">{{ product.price }} <span>元</span></p>
                </div>
                <p class="product-sales">{{ `销量：${product.sales} 件` }}</p>
                <p class="product-stock">{{ `库存：${product.stock} 件` }}</p>
                <p class="product-rating">{{ `评分：${product.rating} 分` }}</p>
                <el-input-number v-model="product.quantity" :min="1" :max="product.stock" :step="1" size="mini"></el-input-number>
                <el-button type="primary" round icon="el-icon-shopping-cart" size="medium" class="add-to-cart-button">加入购物车</el-button>
            </el-col>
        </el-row>
        <h2 class="product-reviews-title">商品评价</h2>
        <div v-if="product.reviews.length > 0" class="product-reviews-list">
            <div v-for="review in product.reviews" :key="review.id" class="product-review">
                <div class="product-review-left">
                    <img :src="require(`@/assets/${review.avatar}`)" alt="" class="product-review-avatar" />
                    <p class="product-review-author">{{ review.author }}</p>
                </div>
                <div class="product-review-right">
                    <p class="product-review-content">{{ review.content }}</p>
                    <p class="product-review-date">{{ review.date }}</p>
                </div>
            </div>
        </div>
        <p v-else class="product-reviews-empty">暂无评价</p>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                product: {
                    productName: "小猪",
                    description: "刚断奶的小猪，适合养殖",
                    category: '黑猪',
                    price: 500,
                    sales: 100,
                    rating: 3.7,
                    image: "image1.jpeg",
                    origin: "湖南省",
                    stock: 200,
                    quantity: 1,
                    reviews: [
                        {
                            id: 1,
                            content: "很不错的小猪，质量很好，很灵活",
                            author: "张三",
                            avatar: "avatar1.png",
                            date: "2023-04-10"
                        },
                        {
                            id: 2,
                            content: "这只小猪很健康",
                            author: "李四",
                            avatar: "avatar1.png",
                            date: "2023-04-12"
                        },
                        {
                            id: 3,
                            content: "买回来很满意",
                            author: "王五",
                            avatar: "avatar1.png",
                            date: "2023-04-14"
                        }
                    ]
                }
            };
        }
    };
</script>

<style>
    /* Container */
    .product-details-container {
        padding: 20px;
        max-width: 100%;
        margin: 0 auto;
        background-color: #f5f5f5;
        border-radius: 10px;
        border: 1px solid #ccc;
    }

    /* Product Image */
    .product-image {
        width: 100%;
        max-width: 700px; /* Updated value */
        margin: 80px auto 20px 50px;
        display: block;
        border: 5px solid #fff;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
    }


    /* Product Name */
    .product-name {
        font-size: 30px;
        margin-bottom: 10px;
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
        color: #f60;
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
        text-transform: uppercase; /* 转换为大写 */
        letter-spacing: 1px; /* 字母间距 */
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
        margin: 40px 0 20px 0;
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
        /*justify-content: space-between;*/
        margin: 10px;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        width: 100%;
    }

    /* Product Review Avatar */
    .product-review-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 10px;
    }

    /* Product Review Author */
    .product-review-author {
        font-size: 16px;
        color: #666;
        margin-left: 10px;
        margin-bottom: 0;
    }

    /* Product Review Content */
    .product-review-content {
        font-size: 18px;
        color: #333;
        margin-bottom: 5px;
        margin-left: 100px;
        text-align: left;
        width: 100px;
    }

    /* Product Review Date */
    .product-review-date {
        font-size: 14px;
        color: #999;
        margin-top: 10px;
        margin-bottom: 40px;
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

    /* Responsive /
    @media (max-width: 767px) {
    / Product Image */
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
