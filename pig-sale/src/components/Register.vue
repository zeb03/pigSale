<template>
    <div class="mask">
        <div id="login-form">
            <div class="login_close"><img src="../assets/img/close.png" alt="关闭" @click="onCloseClick"></div>
            <form action="#" method="post">
                <h2 id="form-title">欢迎注册</h2>
                <div>
                    <label for="username">用户名<span class="red">*</span>：</label>
                    <input type="text" name="userTel" id="username" placeholder="请设置用户名" v-model="username" required
                        @keyup="userNameBlur($event)">
                    <div id="userNameWarn"></div>
                </div>
                <div>
                    <label for="tel">手机号<span class="red">*</span>：</label>
                    <input type="text" name="userTel" id="tel" placeholder="请输入手机号" v-model="phone" required
                        @keyup="userTelBlur($event)">
                    <div id="userTelWarn"></div>
                </div>
                <div>
                    <label for="userPassword">密码<span class="red">*</span>：</label>
                    <input type="password" name="userPassword" id="userPassword" v-model="password" placeholder="请设置登陆密码"
                        required @focus="passwordFocus" @keyup="passwordKeyup($event)" @blur="passwordBlur($event)">
                    <div id="passwordWarn"></div>
                </div>
                <div class="form_register">
                    <label for="repeatPassword">重复密码<span class="red">*</span>：</label>
                    <input type="password" name="repeatPassword" id="repeatPassword" placeholder="请再次输入登陆密码" required
                        @keyup="repPasswordBlur">
                    <div id="repPasswordWarn"></div>
                </div>
                <div class="form_register">
                    <label for="mail">电子邮箱<span class="red">*</span>：</label>
                    <input type="text" name="userMail" id="mail" v-model="email" placeholder="请设置电子邮箱"
                        @keyup="userMailBlur">
                    <div id="userMailWarn"></div>
                </div>
                <div class="form_button">
                    <button id="login-button" @click.prevent @click="onRegister">注 册</button>
                </div>
            </form>
        </div>


    </div>
</template>

<script>
    import Request from "@/utils/request";
export default {

    data() {
        return {
            username: '',
            password: '',
            email: '',
            phone: ''
        }

    },

    methods: {
        userNameBlur(event) {
            let userNameWarn = document.getElementById("userNameWarn");
            let inputValue = event.target.value
            if (inputValue == "") {
                userNameWarn.className = "error";
                userNameWarn.innerHTML = "用户名不能为空";
            } else {
                userNameWarn.className = "true";
                userNameWarn.innerHTML = "用户名输入正确";
            }

        },
        userTelBlur(event) {
            let userTelWarn = document.getElementById("userTelWarn");
            let regExp = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
            let inputValue = event.target.value
            let test = regExp.test(inputValue);
            if (inputValue == "") {
                userTelWarn.className = "error";
                userTelWarn.innerHTML = "电话号码不能为空";
            } else if (test) {
                userTelWarn.className = "true";
                userTelWarn.innerHTML = "电话号码输入正确";
            } else {
                userTelWarn.className = "error";
                userTelWarn.innerHTML = "电话号码输入错误"
            }
        },
        passwordFocus() {
            let passwordWarn = document.getElementById("passwordWarn");
            passwordWarn.className = "warn";
            passwordWarn.innerHTML = "请设置您的登陆密码<br/>1.由字母和数字组成，不能使用特殊字符<br/>2.必须包含大小写字母和数字，且长度为8~18";
        },
        passwordKeyup(event) {
            let passwordWarn = document.getElementById("passwordWarn");
            let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z0-9]{8,18}$/;
            let inputValue = event.target.value
            let test = regExp.test(inputValue);
            if (test) {
                passwordWarn.className = "true";
                passwordWarn.innerHTML = "密码输入正确";
            }
        },
        passwordBlur(event) {
            let passwordWarn = document.getElementById("passwordWarn");
            let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z0-9]{8,18}$/;
            let inputValue = event.target.value
            let test = regExp.test(inputValue);
            if (inputValue == "") {
                passwordWarn.className = "error";
                passwordWarn.innerHTML = "登陆密码不能为空";
            } else if (test) {
                passwordWarn.className = "true";
                passwordWarn.innerHTML = "密码输入正确";
            } else {
                passwordWarn.className = "error";
                passwordWarn.innerHTML = "密码不符合要求"
            }

        },
        repPasswordBlur(event) {
            let password = document.getElementById("userPassword");
            let repPassword = document.getElementById("repeatPassword");
            let repPasswordWarn = document.getElementById("repPasswordWarn");
            let test = (repPassword.value == password.value);//判断两次输入是否一致

            if (repPassword.value == "") {
                repPasswordWarn.className = "error";
                repPasswordWarn.innerHTML = "重复登陆密码不能为空";
            } else if (test) {
                repPasswordWarn.className = "true";
                repPasswordWarn.innerHTML = "重复密码输入正确";
            } else {
                repPasswordWarn.className = "error";
                repPasswordWarn.innerHTML = "密码输入不一致"
            }

        },
        userMailBlur(event) {
            let userMailWarn = document.getElementById("userMailWarn");
            let regExp = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
            let inputValue = event.target.value
            let test = regExp.test(inputValue);

            if (inputValue == "") {
                userMailWarn.className = "";
                userMailWarn.innerHTML = "";
            } else if (test) {
                userMailWarn.className = "true";
                userMailWarn.innerHTML = "邮箱输入正确";
            } else {
                userMailWarn.className = "error";
                userMailWarn.innerHTML = "请输入正确的邮箱";
            }

        },
        onCloseClick() {
            this.$emit('close')
        },
        onRegister() {
            let userNameWarn = document.getElementById("userNameWarn")
            let userTelWarn = document.getElementById("userTelWarn")
            let passwordWarn = document.getElementById("passwordWarn")
            let repPasswordWarn = document.getElementById("repPasswordWarn");
            let userMailWarn = document.getElementById("userMailWarn")
            function ifTure(item) {
                return (item.className === 'true')
            }

            //当全部输入正确时
            if (ifTure(userNameWarn) && ifTure(userTelWarn) && ifTure(passwordWarn) && ifTure(repPasswordWarn) && ifTure(userMailWarn)) {
                console.log("成功输入")

                Request.post('/user/register', {
                    username: this.username,
                    password: this.password,
                    email: this.email,
                    phone: this.phone
                })
                    .then(function (response) {
                        console.log(response);
                        //状态200时
                        if (response.data.code === 200) {
                            this.$message({
                                message: '注册成功',
                                type: 'success'
                            })
                            this.$router.push('/')
                        } else {
                            this.$message.error(response.data.msg)
                        }
                    }.bind(this))
                    .catch(function (error) {
                        console.log(error);
                        this.$message.error('出错了')
                    }.bind(this));
            } else {
                this.$message.error('填写错误')
            }

        }
    }

}
</script>

<style scoped>
.mask {
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, .5);
    position: fixed;
    top: 0;
    left: 0;
    z-index: 500;

}

.red {
    color: red;
}

#login-form {
    /* display: none; */
    width: 500px;
    height: 554px;
    margin: 0 auto;
    margin-top: 100px;
    position: relative;
    background-color: #fff;
    border: .1rem solid rgba(0, 0, 0, 0.1);
    box-shadow: 10px 10px 10px rgb(0, 0, 0, 0.1);
    border-radius: 11px;
}

.login_close {
    position: absolute;
    top: 12px;
    right: 15px;
}

.login_close:hover {
    cursor: pointer;
}

#login-form h2 {
    margin-top: 14px;
    padding: 17px 0;
    font-size: 36px;
    font-weight: bold;
    color: #000;
}

#login-form form {
    width: 417px;
    margin: 0 auto;
    padding: 0 20px 20px 20px;

    border-radius: 11px;

}

#login-form form>div {
    margin-bottom: 35px;
}

#login-form div>label {
    display: inline-block;
    width: 125px;
    text-align: right;
}

#login-form input[type="text"],
#login-form input[type="password"] {
    box-sizing: border-box;
    width: 250px;
    padding: 5px;
    border: .1rem solid rgba(0, 0, 0, .1);
    border-radius: 5px;
}

#login-form input:focus {
    outline: none;
}

#login-form ul li {
    display: inline;
    width: 179px;
}

.form_button {
    text-align: center;
    width: 328px;
    margin: 0 auto;
}

#login-form button {
    border: 2px solid #66CCFF;
    border-radius: 5px;
    background-color: #66ccFF;
    font-size: 20px;
    font-weight: bolder;
    padding: 5px;
    width: 100%;
}

.warn,
.true,
.error {
    padding: 0.1em 1em;
    margin-left: 162px;
    margin-top: 5px;
    width: 187px;
    position: absolute;
    vertical-align: middle;
    font-size: 13px;
    border-radius: 4px;
}

.warn {
    border: 1px solid #66CCFF;
    position: absolute;
    z-index: 100;
    background-color: white;
}

.true {
    border: 1px solid #6DDB6C;
    background-color: #E9FAE7;
}

.error {
    border: 1px solid #FF8C6C;
    background-color: #FFF1E4;
}
</style>