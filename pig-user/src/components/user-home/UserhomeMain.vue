<template>
    <div>
        <!-- banner部分 -->
        <div class="banner">
            <!-- 左分类 -->
            <div class="w">
                <!-- 轮播图左侧 -->
<!--                <sub-nav style="float: left;"></sub-nav>-->
                <!-- 轮播图模块 -->
                <div id="outer" style="width: 800px;">
                    <el-carousel height="361px" trigger="click">
                        <el-carousel-item v-for="item in 6" :key="item">
                            <a href="#">
                                <img :src="'/images/a' + item + '.jpeg'"
                                     style="object-fit: cover; width: 100%; height: 100%;" alt="">
                            </a>
                        </el-carousel-item>
                    </el-carousel>
                </div>
                <!-- 轮播图右侧 -->
                <user-card style="float: right"></user-card>
            </div>
        </div>
        <!-- 分类推荐模块 -->
        <!-- TODO: 根据分类进行搜索      -->
        <div class="goods w">
            <h3>分类推荐</h3>
            <div v-for="category in categoryList" style="font-size: 16px">
                <ul>
                    <li>
                        <a href="">{{ category.categoryName }}
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- box核心内容区域 -->
        <div class="w">
            <div class="box">
                <div class="box-hd">
                    <h3>精品推荐</h3>
                    <a href="#">查看全部</a>
                </div>
                <!-- <infinite-scroll-goods></infinite-scroll-goods> -->
                <scroll-goods></scroll-goods>
            </div>
        </div>
        <!-- footer模块制作 -->
        <div class="footer">
            <div class="w">
                <div class="copyright">
                    <!-- <img src="images/logo.png" alt=""> -->
                    <p>猪肉销售平台<br>
                        © 2023年xxxxxxxx.保留所有权利。-ICP备xxxxxxx号</p>
                    <a href="#" class="app">下载APP</a>
                </div>
                <div class="links">
                    <dl>
                        <dt><a href="#">猪肉销售平台</a></dt>
                        <dd><a href="#">关于</a></dd>
                        <dd><a href="#">管理团队</a></dd>
                        <dd><a href="#">工作机会</a></dd>
                        <dd><a href="#">客户服务</a></dd>
                        <dd><a href="#">帮助</a></dd>
                    </dl>
                    <dl>
                        <dt><a href="#">新手指南</a></dt>
                        <dd><a href="#">关于</a></dd>
                        <dd><a href="#">管理团队</a></dd>
                        <dd><a href="#">工作机会</a></dd>
                        <dd><a href="#">客户服务</a></dd>
                        <dd><a href="#">帮助</a></dd>
                    </dl>
                    <dl>
                        <dt><a href="#">合作伙伴</a></dt>
                        <dd><a href="#">关于</a></dd>
                        <dd><a href="#">管理团队</a></dd>
                        <dd><a href="#">工作机会</a></dd>
                        <dd><a href="#">客户服务</a></dd>
                        <dd><a href="#">帮助</a></dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import GoodsCard from '../goods/GoodsCard.vue'
    import GoodsShow from '../goods/GoodsShow.vue'

    import SubNav from './SubNav.vue'
    import UserCard from '../user/UserCard.vue'
    import InfiniteScrollGoods from '../goods/InfiniteScrollGoods.vue'
    import ScrollGoods from '../goods/ScrollGoods.vue'
    import request from "@/utils/request";

    export default {
        components: {GoodsCard, GoodsShow, SubNav, UserCard, InfiniteScrollGoods, ScrollGoods},

        data() {
            return {
                categoryList: [{
                    categoryId: 1,
                    categoryName: '猪肉销售'
                }],
            }
        },
        created() {
            this.getCategoryList()
        },
        methods: {
            getCategoryList() {
                request({
                    method: 'get',
                    url: '/category/list',
                }).then(res => {
                    this.categoryList = res.data
                    console.log(this.categoryList)
                })
            },
        }
    }


</script>

<style src="../../assets/css/style.css" scoped/>

<style></style>