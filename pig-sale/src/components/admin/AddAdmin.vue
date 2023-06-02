<template>
    <el-dialog title="添加管理员账号" :visible="dialogVisible" @close="onClose" width="30%">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" style="width:90%">
            <el-form-item label="用户名：" :label-width="formLabelWidth" prop="username">
                <el-input v-model="ruleForm.username" placeholder="请输入用户名" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="角色：" :label-width="formLabelWidth" prop="role">
                <el-select v-model="ruleForm.role">
                    <el-option v-for="role in roles" :key="role.roleId" :label="role.roleName"
                        :value="role.roleId"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="密码：" :label-width="formLabelWidth" prop="password">
                <el-input v-model="ruleForm.password" placeholder="请输入密码" show-password autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="重复密码：" :label-width="formLabelWidth" prop="repeatPassword">
                <el-input v-model="ruleForm.repeatPassword" placeholder="请重复输入密码" show-password
                    autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话：" :label-width="formLabelWidth" prop="phone">
                <el-input v-model="ruleForm.phone" placeholder="请输入电话号码" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱地址：" :label-width="formLabelWidth" prop="email">
                <el-input v-model="ruleForm.email" placeholder="请输入邮箱地址" autocomplete="off"></el-input>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <el-button @click="onClose">取 消</el-button>
            <el-button type="primary" @click="submitForm('ruleForm')">立即添加</el-button>
        </div>
    </el-dialog>
</template>

<script>
export default {
    props: {
        dialogVisible: {
            type: Boolean,
            default: false
        }
    },
    mounted() {
        this.showRoleWithPermissions()
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
                role: null,
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
                role: [
                    { required: true, message: '请选择角色', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: ['blur', 'change'] }
                ],
                repeatPassword: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { validator: validatePass2, trigger: 'change' }
                ],
                phone: [
                    { required: true, min: 11, max: 11, message: "请输入11位手机号码", trigger: ['blur', 'change'] }
                ],
                email: [
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ]


            },
            // 角色
            roles: [
                {
                    roleId: 2,
                    roleName: "商品管理员",
                    description: "可以进行商品管理",
                    rolePermissions: [
                        {
                            permissionId: "1",
                            permissionName: "add_product",
                            description: null
                        },
                        {
                            permissionId: "2",
                            permissionName: "edit_product",
                            description: null
                        },
                        {
                            permissionId: "3",
                            permissionName: "delete_product",
                            description: null
                        }
                    ]
                }
            ],
        };
    },
    methods: {
        onClose() {
            //重置表单
            this.resetForm('ruleForm')
            this.$emit('close')
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.addAdmin()
                }
            })
        },
        //展示所有角色及其权限
        showRoleWithPermissions() {
            this.$api.permissions.getAllAdminRole()
                .then(response => {
                    
                    console.log(res)
                    this.roles = response.data
                })
        },
        addAdmin() {
            let { username, password, email, phone } = this.ruleForm
            let data = { username, password, email, phone }
            // console.log(data)
            this.$api.admin.add(data)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        //关闭
                        this.onClose()
                        this.$emit('addSuccess', '成功添加管理员账号')
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        }
    }
}

</script>

<style></style>