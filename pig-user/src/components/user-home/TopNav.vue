<template>
    <div class="topnav">
        <div class="topnav_container">
            <div class="topnav_left">
                <ul>
                    <li><span>欢迎你！</span></li>
                    <li v-if="isLogin">
                        <!-- <router-link :to="{ name: 'userInfo', params: { loginID } }"> -->
                        <router-link :to="'/userInfo/' + loginID">
                            {{ loginName }}&nbsp;{{ loginID }}
                        </router-link>
                    </li>
                    <li v-else><a href="#/login" target="_blank">请登陆</a></li>
                    <li v-if="!isLogin"><a href="#/register" target="_blank">免费注册</a></li>
                </ul>
            </div>
            <div class="topnav_right">
                <ul>
                    <li>
                        <router-link :to="'/userInfo/' + loginID + '/orders'" target="_blank">我的订单</router-link>
                    </li>
                    <li class="topnav_personalcenter">
                        <router-link :to="'/userInfo/' + loginID" target="_blank">个人中心</router-link>
                        <i class="el-icon-arrow-down"></i>
                        <div class="topnav_personalcenter_menu">
                            <ul>
                                <li>
                                    <router-link :to="'/userInfo/' + loginID + '/data'" target="_blank">账号管理</router-link>
                                </li>
                                <li>
                                    <router-link :to="'/userInfo/' + loginID + '/address'"
                                        target="_blank">收货地址</router-link>
                                </li>
                                <li><a href="">我的收藏夹</a></li>
                            </ul>
                            <ul>
                                <li><a href="">帮助中心</a></li>
                                <li><a href="">售后服务</a></li>
                            </ul>
                        </div>
                    </li>
                    <li class="topnav_cart">
                        <img src="@/assets/img/cart_icon.png">
                        <router-link :to="'/userInfo/' + loginID + '/cart'" target="_blank">购物车</router-link>
                    </li>
                    <li><a href="">联系客服</a></li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loginID: '-1',
            loginName: '用户名',
            isLogin: false
        }
    },
    computed: {

    },
    created: function () {
        //获得当前登陆用户ID
        const loginID = localStorage.getItem('loginID')
        const loginName = localStorage.getItem('loginName')
        if (!loginID) {
            this.isLogin = false
            // this.$message.error('请先登陆')
        } else {
            this.isLogin = true
            this.loginID = loginID
            this.loginName = loginName
        }
    }
}
</script>

<style scoped>
.topnav {
    height: 32px;
    color: #6C6C6C;
    background-color: #f5f5f5;
    border-bottom: 1px solid #eee;
    line-height: 32px;
}

.topnav .topnav_container {
    width: 1190px;
    height: 32px;
    margin: 0 auto;
}

.topnav .topnav_container a {
    color: #6C6C6C;
}

.topnav .topnav_container a:hover {
    color: #409EFF;
    text-decoration: none;

}

.topnav .topnav_container .topnav_left {
    float: left;
}

.topnav .topnav_container .topnav_right {
    float: right;
}

.topnav .topnav_container .topnav_left li,
.topnav .topnav_container .topnav_right li {
    float: left;
    padding: 0 15px;
}

.topnav .topnav_container .topnav_right .el-icon-arrow-down {
    margin-left: 5px;

}


.topnav .topnav_container .topnav_right .topnav_personalcenter {
    position: relative;
    border: 1px solid #f5f5f5;
    border-bottom: none;
}

.topnav .topnav_container .topnav_right .topnav_personalcenter:hover {
    background-color: #fff;
    border: 1px solid #6C6C6C54;
}

.topnav .topnav_container .topnav_right .topnav_personalcenter .topnav_personalcenter_menu {
    display: none;
    width: 95.2px;
    background-color: #fff;
    position: absolute;
    top: 32px;
    left: -1px;
    border: 1px solid #6C6C6C54;
    border-top: none;
    box-shadow: 1px 2px 2px rgba(0, 0, 0, .1);
    z-index: 100;
}

.topnav .topnav_container .topnav_right .topnav_personalcenter:hover .topnav_personalcenter_menu {
    display: block;
}

.topnav .topnav_container .topnav_right .topnav_personalcenter .topnav_personalcenter_menu ul:first-child {
    border-bottom: 1px solid #cccccc61;
}

.topnav .topnav_container .topnav_right .topnav_personalcenter .topnav_personalcenter_menu li {
    float: none;
    text-align: center;
}

.topnav .topnav_container .topnav_right .topnav_cart img {
    vertical-align: middle;
    margin-right: 5px;
    margin-bottom: 2px;

}

/* 添加分割线 */
.topnav .topnav_container .topnav_right .topnav_cart::after {
    content: "";
    padding-right: 18px;
    border-right: 2px solid #6c6c6c54;
}
</style>