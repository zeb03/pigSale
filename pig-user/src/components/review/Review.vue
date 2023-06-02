<template>
    <div class="user-review-page">
        <!-- 返回 -->
        <el-page-header @back="goBack" content="商品评价" style="margin-bottom: 30px;">
        </el-page-header>
        <!-- 商品信息 -->
        <div class="product-info">
            <div class="product-image">
                <el-image :src="getImgUrl(product.image)" alt="Product Image"
                          style="width: 200px;height: 200px;object-fit: cover;">
                </el-image>
            </div>
            <div class="product-details">
                <div class="product-name">{{ product.productName }}</div>
                <div class="product-price">{{ `￥${product.price}` }}</div>
                <div class="product-description">{{ product.description }}</div>
            </div>
        </div>
        <!-- 评分 -->
        <div class="rating-block">
            <div class="rating-label">评分：
                <el-input v-model="review.rating" placeholder="请输入评分（1~5）" size="mini" type="number"
                          @input="numberChange(arguments[0], 5)" @change="numberChange(arguments[0], 5)"
                          style="width: 50px;"></el-input>
            </div>
            <el-rate v-model="review.rating" show-score text-color="#ff9900">
            </el-rate>
        </div>
        <div class="image-block">
            <div class="image-label">上传图片</div>
            <el-upload ref="upload" action="" :http-request="upload" list-type="picture-card" class="image-upload"
                       :auto-upload="false" :limit="1" :on-success="handlePictureSuccess">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}">
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                    <span class="el-upload-list__item-actions">
                        <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                            <i class="el-icon-zoom-in"></i>
                        </span>
                        <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                            <i class="el-icon-delete"></i>
                        </span>
                    </span>
                </div>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
        </div>
        <!-- 评论输入框 -->
        <div class="comment-block">
            <div class="comment-label">请输入评论内容</div>
            <el-input class="comment-input" type="textarea" :autosize="{ minRows: 4, maxRows: 4 }" placeholder="请输入内容"
                      v-model="review.comment"/>
        </div>
        <!-- 提交按钮 -->
        <el-button class="submit-button" type="primary" plain @click="handelSubmit">提交</el-button>
    </div>
</template>

<script>
    import request from '@/utils/request'

    export default {
        data() {
            return {
                filename: '',
                imageUrl: '',
                dialogImageUrl: '',
                dialogVisible: false,
                disabled: false,
                product: {
                    productId: 7,
                    productName: "小猪",
                    description: "刚断奶的小猪，适合养殖",
                    price: 500,
                    image: "a7.png",
                    orderDetailId: 100,//订单详情id，注意区分
                },
                review: {
                    productId: 7,
                    comment: '',
                    anonymous: 0,
                    rating: 0,
                    image: 'a7.png',//订单详情id，注意区分
                    orderDetailId: 100
                }
            }
        },
        mounted() {
            // 读取路由参数product
            // console.log(product)
            this.product = this.$route.params.product
            this.review.orderDetailId = this.product.orderDetailId
            this.review.productId = this.product.productId
        },
        methods: {
            goBack() {
                this.$router.go(-1)
            },
            numberChange(val, maxNum) {
                val = val.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符 只能输入数字和小数点
                val = val.replace(/\.{2,}/g, "."); // 不能连续输入两个及以上小数点
                val = val
                    .replace(".", "$#$")
                    .replace(/\./g, "")
                    .replace("$#$", "."); // 只保留第一个".", 清除多余的"."
                val = val.replace(
                    /^(-)*(\d+)\.(\d).*$/,
                    "$1$2.$3"
                ); // 只能输入一位小数
                if (
                    val &&
                    val.indexOf(".") < 0 &&
                    val !== ""
                ) {
                    val = parseFloat(val);
                    val = val + "";
                } // 如果没有小数点，首位不能为类似于 01、02的值

                //转换数字类型
                this.review.rating = Number(val)
                //重新渲染
                this.$nextTick(() => {
                    //比较输入的值和最大值，返回小的
                    let num = Math.min(Number(val), maxNum)
                    //输入负值的情况下， = 0（可根据实际需求更该）
                    if (num < 0) {
                        this.review.rating = 0
                    } else if (num > 5) {
                        //反之
                        this.review.rating = 5
                    } else {
                        this.review.rating = num
                    }
                })
            },
            async upload(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                const url = '/common/upload'
                try {
                    const res = await request.post(url, formData).then(res => {
                        console.log('上传图片成功' + res.data)
                        this.filename = res.data
                        this.review.image = this.filename
                        this.submitReview()
                    })
                } catch (error) {
                    console.log('图片上传失败', error)
                }
            },
            handleRemove(file) {
                console.log(file)
                this.$refs.upload.clearFiles();
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url
                this.dialogVisible = true
            },
            handlePictureSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw)
                console.log('imageUrl' + this.imageUrl)
            },
            submitReview() {
                this.$api.review.addReview(this.review)
                    .then(res => {
                            console.log(res.code)
                            if (res.code === 200) {
                                this.$message({
                                    message: '成功发布评论',
                                    type: 'success'
                                })
                            } else {
                                this.$message('发布评论失败')
                            }
                        }
                    )
            },
            getImgUrl(img) {
                return `http://localhost:8080/common/download?filename=${img}`
            },
            handelSubmit() {
                //提交图片和评论
                if (this.imageUrl === '') {
                    this.submitReview()
                }
                this.$refs.upload.submit()
                this.$router.push('/')
            }
        }
    }

</script>

<style scoped>
    .user-review-page {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
    }

    .product-info {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
    }

    .product-image el-image {
        width: 200px;
        height: 200px;
        object-fit: cover;
    }

    .product-details {
        margin-left: 20px;
    }

    .product-name {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .product-price {
        font-size: 20px;
        margin-bottom: 10px;
        color: #0fa3f4;
    }

    .product-description {
        font-size: 16px;
        line-height: 1.5;
        color: #666;
    }

    .rating-block {
        margin-bottom: 20px;
    }

    .rating-label {
        font-size: 16px;
        margin-bottom: 10px;
    }

    .image-block {
        margin-bottom: 20px;
    }

    .image-label {
        font-size: 16px;
        margin-bottom: 10px;
    }

    .image-upload {
        border: 1px dashed #ccc;
        border-radius: 4px;
        padding: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        cursor: pointer;
    }

    .image-upload i {
        font-size: 28px;
        color: #ccc;
        margin-bottom: 10px;
    }

    input[type="number"]::-webkit-outer-spin-button,
    input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    .image-preview img {
        width: 200px;
        height: 200px;
        object-fit: cover;
    }

    .image-preview-overlay i {
        font-size: 24px;
        color: #fff;
        cursor: pointer;
        margin-right: 5px;
    }

    .comment-block {
        margin-bottom: 20px;
    }

    .comment-label {
        font-size: 16px;
        margin-bottom: 10px;
    }

    .comment-input {
        font-size: 16px;
    }

    .submit-button {
        margin-top: 20px;
    }

    .image-preview-dialog img {
        width: 100%;
        height: auto;
    }
</style>
