<template>
    <div>
        <!-- 顶部导航条 -->
        <top-nav @getLoginId="loginId = $event"></top-nav>
        <header class="header">
            <div class="container">
                <a href="#/userHome">
                    <div class="header_logo"></div>
                </a>
                <div class="header_ul">
                    <ul class="clearfix">
                        <li><a href="#/userHome">首页</a></li>
                        <li class="header_li_active"><a href="#">账号管理</a></li>
                    </ul>
                </div>
            </div>
        </header>
        <div class="main">
            <div class="container clearfix">
                <div class="menu">
                    <div class="menu_item">
                        <h2 class="menu_title">我的购物</h2>
                        <ul>
                            <li><router-link to="cart">购物车</router-link></li>
                            <li><router-link to="orders">订单</router-link></li>
                            <li><router-link to="address">收货地址</router-link></li>
                            <li><router-link to="review">评价</router-link></li>
                        </ul>
                    </div>
                    <div class="menu_item">
                        <h2 class="menu_title">个人资料</h2>
                        <ul>
                            <li><router-link to="data">查看</router-link></li>
                            <li><router-link to="edit">编辑</router-link></li>
                            <li><user-remove style=""></user-remove></li>
                        </ul>
                    </div>
                </div>
                <div class="main_body">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import TopNav from './user-home/TopNav.vue'
import UserRemove from './user/UserRemove.vue'
export default {
    components: { TopNav, UserRemove },
    data() {
        return {
            loginId: null
        }
    },
    created: function () {
        // console.log(this.$route.params)
    }
}
</script>

<style scoped>
/* 清除浮动 */
.clearfix::after {
    content: "";
    display: block;
    clear: both;
}

.header {
    height: 80px;
    background: #409EFF;
}

.header .container,
.main .container {
    width: 1190px;
    height: 100%;
    margin: 0 auto;
}

.header .container .header_logo,
.header .container .header_ul {
    float: left;
}

.header .container .header_logo {
    margin-left: 80px;
    margin-right: 20px;
    margin-top: 11px;
    height: 59px;
    width: 95px;
    background-image: url('../assets/img/logo.png');
    background-size: contain;
    background-repeat: no-repeat;
}

.header .container .header_ul li {
    height: 55px;
    line-height: 80px;
    float: left;
    padding: 0 5px;
    margin-left: 50px;
}

.header .container .header_ul li a {
    font-size: 14px;
    color: #fff;
}

.header .container .header_ul li a:hover {
    text-decoration: none;
}

.header .container .header_ul li.header_li_active{
    border-bottom: 2px solid #eee;
}

.main {
    background-color: #f5f5f5;
    padding-top: 25px;
}

.main .container {
    border: 1px solid #40a0ffa2;
    border-radius: 25px;
    padding: 20px;
}

.main .menu,
.main .main_body {
    background-color: #fff;
    border: 1px double #40a0ffa2;
    border-radius: 10px;
}

.main .menu {
    float: left;
    width: 70px;
    padding: 15px;

}

.main .menu a:hover {
    color: #409EFF;
}

.main .menu .menu_item {
    margin-bottom: 30px;
}

.main .menu .menu_item li {
    padding-left: 10px;
    padding-top: 5px;
}

.main .menu .menu_title {
    font-size: 15px;
    padding-bottom: 5px;
    margin-bottom: 5px;
    border-bottom: 1px solid #ccc;
}

.main .main_body {
    margin-left: 40px;
    padding: 50px;
    float: left;
    min-height: 433px;
    width: 800px;
}
</style>