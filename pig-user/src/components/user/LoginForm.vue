<template>
    <div class="login_form">
        <h2>LOGIN</h2>
        <div class="login_form_input">
            <el-input class="myinput" v-model="username" placeholder="请输入用户名"></el-input>
        </div>
        <div class="login_form_input">
            <el-input class="myinput" v-model="password" placeholder="请输入密码" show-password></el-input>
        </div>
        <button @click="onLogin">登 录</button><br>
        <div>     
        </div>
        <div class="login_form_footer">
            未有帐号？
            <a href="#/register">注册</a>/
            <a href="#/userHome" target="_blank">首页</a>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            username: '',
            password: '',
        }
    },
    methods: {
        onLoginClick() {
            if (this.username === 'admin' && this.password === '123456') {
                this.$message({
                    message: '成功登陆',
                    type: 'success'
                })
                this.$router.push('/adminHome')
                // return localStorage.setItem('token1', 'ruan')
                //保存当前登陆用户的ID
                localStorage.setItem('loginID', 7647676144)
                //保存当前登陆用户的用户名
                localStorage.setItem('loginName', "测试")
                this.$router.push('/userHome')
            }
            localStorage.removeItem('token')
        },
        onLogin() {
            this.$api.user.login(this.username, this.password)
                .then((response) => {
                    // console.log(response);

                    if (response.code === 200) {
                        this.$message.success('成功登陆')
                        //token验证
                        // return localStorage.setItem('token1', 'ruan')
                        //保存当前登陆用户的ID
                        localStorage.setItem('loginID', response.data.userId)
                        //保存当前登陆用户的用户名
                        localStorage.setItem('loginName', response.data.username)
                        if (response.data.role === 1) {
                            this.$router.push('/adminHome')
                        } else {
                            this.$router.push('/userHome')
                        }
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }
}

</script>

<style scoped>
.login_form {
    box-sizing: border-box;
    width: 480px;
    height: 400px;
    background-color: #00000086;
    margin-top: 25%;
    margin-right: 139px;
    text-align: center;
    border-radius: 10px;
    padding: 50px 50px;
}

.login_form h2 {
    font-size: 30px;
    color: #ffffff90;
    letter-spacing: .2em;
}

.login_form_input {
    margin-top: 5%;
}

.myinput {
    width: 60%;
    font-size: 15px;
    background: transparent;
    border-bottom: 2px solid #fff;
    padding: 5px 10px;
    margin-top: 10px;
    color: #ccc;
}

button {
    margin-top: 50px;
    width: 60%;
    height: 30px;
    border-radius: 10px;
    border: 0;
    color: #fff;
    text-align: center;
    line-height: 30px;
    font-size: 15px;
    cursor: pointer;
    background-image: linear-gradient(to right, #09a4fe, #085467);
}

.sign_up {
    margin-top: 45%;
    margin-left: 60%;
}

.login_form_footer {
    color: #9b9b9b;
    margin-top: 50px;
}

.login_form_footer a {
    color: #fff;
}

.login_form_footer a:hover {
    color: #09a4fe;
    cursor: pointer;
}
</style>
<style>
/* 修改input样式 */
.login_form_input .el-input__inner {
    background-color: transparent;
    border: 0;
    color: #fff;
    height: 16px;
    line-height: 16px;
}
</style>