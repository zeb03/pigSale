<template>
    <div class="register_form">
        <h2>欢迎注册</h2>
        <div class="text">
            已有账号？
            <a href="#/login">登陆</a>
        </div>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" style="width:90%">
            <el-form-item label="用户名：" :label-width="formLabelWidth" prop="username">
                <el-input v-model="ruleForm.username" placeholder="请输入用户名" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码：" :label-width="formLabelWidth" prop="password">
                <el-input v-model="ruleForm.password" placeholder="请输入密码" show-password autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="重复密码：" :label-width="formLabelWidth" prop="repeatPassword">
                <el-input v-model="ruleForm.repeatPassword" placeholder="请重复输入密码" show-password
                    autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱地址：" :label-width="formLabelWidth" prop="email">
                <el-input v-model="ruleForm.email" placeholder="请输入邮箱地址" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话：" :label-width="formLabelWidth" prop="phone">
                <el-input v-model="ruleForm.phone" placeholder="请输入电话号码" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <el-button style="width: 228px;" type="primary" @click="submitForm('ruleForm')">注册</el-button>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        dialogVisible: {
            type: Boolean,
            default: true
        }
    },
    data() {
        let validatePass2 = (rule, value, callback) => {
            if (value !== this.ruleForm.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            ruleForm: {
                username: '',
                password: '',
                repeatPassword: '',
                phone: null,
                email: null,
            },
            formLabelWidth: '120px',
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: ['blur', 'change'] }
                ],
                repeatPassword: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { validator: validatePass2, trigger: 'change' }
                ],
                email: [
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ],
                phone: [
                    { min: 11, max: 11, message: "请输入11位手机号码", trigger: ['blur', 'change'] }
                ]

            }
        };
    },
    methods: {
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.register()
                }
            })
        },
        register() {
            let { username, password, email, phone } = this.ruleForm
            let data = { username, password, email, phone }
            // console.log(data)
            this.$api.user.register(data)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功注册')
                        this.$router.push('/login')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }
}

</script>

<style scoped>
.register_form {
    box-sizing: border-box;
    width: 480px;
    height: 550px;
    background-color: #ffffffdc;
    margin-top: 110px;
    margin-right: 139px;
    text-align: center;
    border-radius: 10px;
    padding: 50px 50px;
}


.register_form h2 {
    font-size: 36px;
    font-weight: 600;
    color: #000;
    text-align: left;
}

.register_form .text {
    font-size: 14px;
    margin-bottom: 35px;
    text-align: left;
    color: #9b9b9b;
}

.register_form .text a:hover {
    color: #409BFF;
}
</style>