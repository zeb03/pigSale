<template>
    <div>
        <el-form class="form_container" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="用户ID：" :label-width="formLabelWidth">
                        <el-input :placeholder="ruleForm.userId" :disabled="true"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="账号类型：" :label-width="formLabelWidth">
                        <el-input :placeholder="ruleForm.role" :disabled="true"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="用户名：" :label-width="formLabelWidth" prop="username">
                        <el-input v-model="ruleForm.username" placeholder="请输入用户名" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性名：" :label-width="formLabelWidth">
                        <el-input v-model="ruleForm.name" placeholder="请输入性名" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="性别：" :label-width="formLabelWidth">
                        <el-select v-model="ruleForm.gender" placeholder="请选择" style="width:80px">
                            <el-option v-for="item in genderOptions" :key="item.value" :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="出生日期：" :label-width="formLabelWidth">
                        <el-date-picker v-model="ruleForm.birthday" type="date" placeholder="选择日期" format="yyyy 年 MM 月 dd 日"
                            value-format="yyyy-MM-dd">
                        </el-date-picker>
                        <!-- <el-input v-model="ruleForm.birthday" placeholder="请输入出生日期" autocomplete="off"></el-input> -->
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="密码：" :label-width="formLabelWidth" prop="password">
                <el-input v-model="ruleForm.password" placeholder="请输入更改后的密码" show-password autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱地址：" :label-width="formLabelWidth" prop="email">
                <el-input v-model="ruleForm.email" placeholder="请输入邮箱地址" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话：" :label-width="formLabelWidth" prop="phone">
                <el-input v-model="ruleForm.phone" placeholder="请输入电话号码" autocomplete="off"></el-input>
            </el-form-item>
            <div style="margin-left: 60%;">
                <el-button @click="onCancel">取 消</el-button>
                <el-button type="primary" @click="submitForm('ruleForm')">立即更改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>

export default {
    name: 'EditAdmin',
    props: {
        adminlist: {
            type: Array
        },
        account: {
            type: Object,
            default: () => {
                return {
                    updateUser: 0, //当前登录用户的userId
                    userId: 0,
                    username: "",
                    name: "",
                    gender: "-1",
                    password: '',
                    birthday: "",
                    role: "管理员",
                    phone: "",
                    email: "",
                }
            }
        }
    },
    computed: {

    },
    data() {
        return {
            genderOptions: [{
                value: '1',
                label: '男'
            }, {
                value: '0',
                label: '女'
            }, {
                value: '-1',
                label: '保密'
            }],
            ruleForm: this.account,
            formLabelWidth: '120px',
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    // { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: ['blur', 'change'] }
                ],
                email: [
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ],
                phone: [
                    { min: 11, max: 11, message: "请输入11位手机号码", trigger: ['blur'] }
                ]

            }
        }
    },
    methods: {
        onCancel() {
            this.$emit('onCancel')
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.updateAdmin()
                }
            })
        },
        updateAdmin() {
            let data = this.ruleForm;
            //获得当前登陆用户ID (无需)
            // let loginID = localStorage.getItem('loginID')
            // if (!loginID){
            //     this.$message.error('请先登陆')
            //     req.updateUser = null;
            // } else {
            //     //添加属性updateUser
            //     req.updateUser = loginID;
            // }
            this.$api.admin.update(data)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功修改信息')
                        //返回
                        this.onCancel()
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        }

    }
}
</script>

<style lang="less" scoped>
.form_container {
    width: 95%;
    margin: 0 auto;
}
</style>