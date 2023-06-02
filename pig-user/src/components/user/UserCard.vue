<template>
    <div class="user_card">
        <div class="user_header">
            <img src="images/user.webp" alt="">
        </div>
        <div>
            <p v-if="!isLogin" class="p1">Hi，你好 !</p>
            <p v-else class="p1">Hi {{ loginName }}{{ loginID }}</p>
            <p v-if="!isLogin" class="p2">欢迎逛猪销售电子平台！</p>
        </div>
        <div v-if="!isLogin">
            <a href="#/login">
                <div class="login">
                    登录
                </div>
            </a>
            <a href="#/register">
                <div class="register">
                    注册
                </div>
            </a>
        </div>
        <div v-else style="margin-left: 150px;">
            <!-- <router-link :to="'/userInfo/' + loginID">个人中心</router-link> -->
            <logout></logout>
        </div>
        <div class="user_bottom">
            <img src="images/user1.webp" alt="">
        </div>
    </div>
</template>

<script>
import Logout from './Logout.vue'
export default {
  components: { Logout },
    name: 'UserCard',
    data() {
        return {
            loginID: '-1',
            loginName: '用户名',
            isLogin: false
        }
    },
    created: function () {
        //获得当前登陆用户ID
        const loginID = localStorage.getItem('loginID')
        const loginName = localStorage.getItem('loginName')
        if (!loginID) {
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
.user_card {
    width: 370px;
    height: 361px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 3px 3px rgba(0, 0, 0, .1);
}

.user_header {
    border: 1px solid #409EFF;
    border-radius: 100%;
    overflow: hidden;
    width: 75px;
    height: 75px;
    margin: 30px auto;
}

.user_header img {
    height: 100%;
}

.user_card .p1 {
    text-align: center;
    font-size: 20px;
    font-weight: 400;
}

.user_card .p2 {
    text-align: center;
    font-size: 16px;
    font-weight: 400;
}

.login,
.register {
    width: 80px;
    height: 40px;
    background-color: #409EFF;
    text-align: center;
    border-radius: 20px;
    margin-top: 20px;
    color: #fff;
    font-size: 16px;
}

.login {
    margin-left: 80px;
    display: inline-block;
    line-height: 40px;
}

.register {
    margin-right: 80px;
    float: right;
    line-height: 40px;
}

.user_bottom {
    position: absolute;
    bottom: 0;
    width: 200px;
    height: 100px;
    margin-left: 60px;
    /* border: 1px solid #050505; */
}

.user_bottom img {
    width: 100%;
    height: 100%;
}
</style>