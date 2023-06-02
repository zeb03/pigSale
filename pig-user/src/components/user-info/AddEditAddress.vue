<template>
    <div>
        <h3 style="font-size: 18px; margin-bottom: 5px;">{{ isAdd ? "添加地址" : "编辑地址" }}：</h3>
        <el-form class="form_container" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
            <el-row>
                <el-col :span="8">
                    <el-form-item label="收货人性名：" :label-width="formLabelWidth" prop="recipientName">
                        <el-input v-model="ruleForm.recipientName" placeholder="请输入性名" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="收货人电话：" :label-width="formLabelWidth" prop="recipientPhone">
                        <el-input v-model="ruleForm.recipientPhone" placeholder="请输入电话号码" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="省：" :label-width="formLabelWidth" prop="province">
                        <el-input v-model="ruleForm.province" placeholder="输入省份"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="市：" :label-width="formLabelWidth" prop="city">
                        <el-input v-model="ruleForm.city" placeholder="请输入市" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="区：" :label-width="formLabelWidth" prop="district">
                        <el-input v-model="ruleForm.district" placeholder="请输入区" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="详细地址：" :label-width="formLabelWidth" prop="detail">
                <el-input v-model="ruleForm.detail" placeholder="请输入详细地址信息，如道路、门牌号、小区、楼栋号、单元等信息"
                    autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item :label-width="formLabelWidth">
                <el-checkbox v-model="ruleForm.isDefault">设置为默认收货地址</el-checkbox>
            </el-form-item>
            <div style="margin-left: 70%;">
                <div v-if="isAdd">
                    <el-button @click="clearRuleForm">重置</el-button>
                    <el-button type="primary" @click="addAddress('ruleForm')">添加</el-button>
                </div>
                <div v-else>
                    <el-button @click="onCancelEdit">退出编辑</el-button>
                    <el-button type="primary" @click="editAddress('ruleForm')">保存</el-button>
                </div>

            </div>
        </el-form>
    </div>
</template>
<script>

export default {
    name: 'AddEditAddress.vue',
    props: {
        editData: {
            type: Object,
            default: () => {
                return {
                    "addressId": "",
                    "userId": "",
                    "recipientName": "",
                    "recipientPhone": "",
                    "province": "",
                    "city": "",
                    "district": "",
                    "detail": "",
                    "isDefault": false
                }
            }
        }
    },
    watch: {
        editData: function (val) {
            this.ruleForm = val
            // 当传入的表单信息改变时，切换到编辑模式
            this.isAdd = false
        },
        isAdd: function (isadd) {
            if (isadd) {
                this.clearRuleForm()
            }
        }

    },
    data() {
        return {
            isAdd: true,//为true时为添加功能，false为编辑功能
            ruleForm: this.editData,
            formLabelWidth: '120px',
            rules: {
                recipientName: [
                    { required: true, message: '请输入性名', trigger: 'blur' },
                ],
                recipientPhone: [
                    { required: true, min: 11, max: 11, message: "请输入11位手机号码", trigger: ['blur', 'change'] }
                ],
                detail: [
                    { required: true, min: 5, message: '详细地址长度需要在5个字以上', trigger: ['blur'] }
                ],
            }
        }
    },
    methods: {
        // 清空表单
        clearRuleForm() {
            this.ruleForm = {
                "addressId": "",
                "userId": "",
                "recipientName": "",
                "recipientPhone": "",
                "province": "",
                "city": "",
                "district": "",
                "detail": "",
                "isDefault": false
            }
        },
        // 取消编辑
        onCancelEdit() {
            // 切换到添加模式
            this.isAdd = true
            this.$emit('cancelEdit')
        },
        //添加地址
        addAddress(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //设为默认时，其值为1     
                    let isDefault = this.ruleForm.isDefault ? 1 : 0
                    let { recipientName, recipientPhone, province, city, district, detail } = this.ruleForm
                    let data = { recipientName, recipientPhone, province, city, district, detail, isDefault }
                    this.postAdd(data)
                }
            })
        },
        //发送添加地址请求
        postAdd(data) {
            // console.log(data)
            this.$api.address.add(data)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功添加地址')
                        //emit添加成功事件
                        this.$emit('addSuccess')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        //编辑地址
        editAddress(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //设为默认时，其值为1     
                    let isDefault = this.ruleForm.isDefault ? 1 : 0
                    let { addressId, recipientName, recipientPhone, province, city, district, detail } = this.ruleForm
                    let data = { addressId, recipientName, recipientPhone, province, city, district, detail, isDefault }
                    // console.log(data)
                    this.update(data)
                }
            })

        },
        //发送编辑地址请求
        update(data) {
            // console.log(data)
            this.$api.address.update(data)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功编辑地址')
                        this.$emit('editSuccess')
                        // 转换到添加功能
                        this.isAdd = true
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
    },
}
</script>

<style lang="less" scoped>
.form_container {
    width: 95%;
    margin: 0 auto;
}
</style>