<template>
    <div>
        <el-page-header @back="goBack" content="编辑资料" >
        </el-page-header>
        <el-form class="form_container" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="账号类型：" :label-width="formLabelWidth">
                        <el-input :placeholder="role" :disabled="true"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="用户ID：" :label-width="formLabelWidth">
                        <el-input :placeholder="ruleForm.userId" :disabled="true"></el-input>
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
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="邮箱地址：" :label-width="formLabelWidth" prop="email">
                <el-input v-model="ruleForm.email" placeholder="请输入邮箱地址" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话：" :label-width="formLabelWidth" prop="phone">
                <el-input v-model="ruleForm.phone" placeholder="请输入电话号码" autocomplete="off"></el-input>
            </el-form-item>
            <div style="margin-left: 70%;">
                <el-button type="primary" @click="submitForm('ruleForm')">立即更改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>

export default {
    name: 'EditUserData',
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
            role: "普通用户",
            ruleForm: {
                userId: 0,
                username: "",
                name: "",
                gender: "-1",
                password: '',
                birthday: "",
                role: "普通用户",
                phone: "",
                email: "",
            },
            formLabelWidth: '120px',
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                email: [
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ],
                phone: [
                    { min: 11, max: 11, message: "请输入11位手机号码", trigger: ['blur', 'change'] }
                ]
            }
        }
    },
    created: function () {
        this.requestUserData()
    },
    methods: {
        goBack() {
            this.$router.go(-1)
        },
        getGenderStr(gender) {
            let genderStr = "保密"
            let genderOptions = [{
                value: '1',
                label: '男'
            }, {
                value: '0',
                label: '女'
            }, {
                value: '-1',
                label: '保密'
            }]
            genderOptions.forEach((obj) => {
                if (obj.value === gender) {
                    return genderStr = obj.label
                }
            })
            return genderStr
        },

        // 请求数据
        requestUserData() {
            // 通过路由，获取登陆id
            let userId = this.$route.params.loginID
            // console.log(userId)
            this.$api.userInfo.get(userId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success("成功")
                        this.ruleForm = response.data
                        this.genderStr = this.getGenderStr(response.data.gender)
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.update()
                }
            })
        },
        update() {
            let data = this.ruleForm;
            // console.log(data)
            this.$api.userInfo.update(data)
                .then((response) => {
                    console.log(response);
                    //状态200时
                    
                    if (response.code === 200) {
                        this.$message.success('成功修改信息')
                        //返回
                        this.onCancel()
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    },

}
</script>

<style lang="less" scoped>
.form_container {
    width: 95%;
    margin: 0 auto;
    margin-top: 50px;
}
</style>