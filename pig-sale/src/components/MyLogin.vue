<template>
    <div class="login_container">
        <div class="login_form">
            <h2>LOGIN</h2>
            <div class="login_form_input">
                <input type="text" v-model="username" placeholder="请输入用户名">
            </div>
            <div class="login_form_input">
                <input type="password" v-model="password" placeholder="请输入密码">
            </div>
            <button @click="onLogin">登录</button>
            <br>
            <div class="login_form_footer">
                未有帐号？
                <span @click="register = true">注册</span>
            </div>
        </div>
        <Register v-if="register" @close="register = false"></Register>
    </div>
</template>

<script>
    import Register from './Register.vue';
    import Request from "@/utils/request";

    export default {
        name: 'MyLogin',
        components: {Register},
        data() {
            return {
                register: false,
                username: '',
                password: '',
                email: '',
                phone: ''
            }
        },
        methods: {
            onLoginClick() {
                if (this.username === 'admin' && this.password === '123456') {
                    this.$message({
                        message: '成功登陆',
                        type: 'success'
                    })
                    sessionStorage.setItem("token", data);
                    localStorage.setItem('token', 'token')
                    //保存当前登陆用户的ID
                    localStorage.setItem('loginID', 0)
                }
                localStorage.removeItem('token')
            },
            async onLogin() {
                Request.post('/user/login', {
                    username: this.username,
                    password: this.password
                })
                    .then(function (response) {
                        console.log(response);

                        //状态200时
                        if (response.code === 200) {
                            this.$message({
                                message: '成功登陆',
                                type: 'success'
                            })
                            //token验证
                            localStorage.setItem('token', 'token')
                            //保存当前登陆用户的ID
                            localStorage.setItem('loginID', response.data.userId)
                            localStorage.setItem('username', response.data.username)
                            if (response.data.role === 0) {
                                this.$router.push('/userHome')
                            } else {
                                this.$router.push('/adminHome')
                            }

                        } else {
                            this.$message.error(response.data.msg)
                        }
                    }.bind(this))
                    .catch(function (error) {
                        console.log(error);
                        this.$message.error('出错了')
                    }.bind(this));
            }
        }
    }
</script>

<style lang="less" scoped>
  .login_container {
    height: 100%;
    background: url('../assets/img/Login.jpg') no-repeat;
    background-size: 100% 100%;
  }

  .login_form,
  .register_form {
    box-sizing: content-box;
    width: 20%;
    height: 400px;
    position: relative;
    top: 15%;
    background-color: #00000060;
    margin: auto;
    text-align: center;
    border-radius: 10px;
    padding: 50px 50px;
  }

  h2 {
    font-size: 30px;
    color: #ffffff90;
    margin-top: 5%;
  }

  .login_form_input {
    margin-top: 5%;
  }

  span {
    color: #fff;
  }

  input {
    border: 0;
    width: 60%;
    font-size: 15px;
    color: #fff;
    background: transparent;
    border-bottom: 2px solid #fff;
    padding: 5px 10px;
    outline: none;
    margin-top: 10px;
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
    background-image: linear-gradient(to right, #30cfd0, #330867);
  }

  .sign_up {
    margin-top: 45%;
    margin-left: 60%;
  }

  a {
    color: #b94648;
  }

  .login_form_footer {
    color: #9b9b9b;
    margin-top: 50px;
  }

  .login_form_footer :hover {
    cursor: pointer;
  }
</style>