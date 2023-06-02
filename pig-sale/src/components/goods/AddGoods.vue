<template>
    <el-dialog title="添加商品" :visible="dialogVisible" @close="onClose">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" style="width:90%">
            <!-- 图片 -->
            <el-form-item label="图片：" :label-width="formLabelWidth" prop="image">
                <el-upload
                        ref="upload"
                        action=""
                        :http-request="upload"
                        list-type="picture-card"
                        :limit="1"
                        :auto-upload="true"
                        :on-success="handlePictureSuccess">
                    <i slot="default" class="el-icon-plus"></i>
                    <div slot="file" slot-scope="{file}">
                        <img
                                class="el-upload-list__item-thumbnail"
                                :src="file.url" alt=""
                        >
                        <span class="el-upload-list__item-actions">
                        <span
                                class="el-upload-list__item-preview"
                                @click="handlePictureCardPreview(file)"
                        >
                          <i class="el-icon-zoom-in"></i>
                        </span>
                        <span
                                v-if="!disabled"
                                class="el-upload-list__item-delete"
                                @click="handleRemove(file)"
                        >
                          <i class="el-icon-delete"></i>
                        </span>
                      </span>
                    </div>
                </el-upload>
            </el-form-item>
            <el-row>
                <el-col :span="12">
                    <!-- 名称 -->
                    <el-form-item label="名称：" :label-width="formLabelWidth" prop="productName">
                        <el-input v-model="ruleForm.productName" placeholder="请输入商品名称" autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <!-- 分类 -->
                    <el-form-item label="分类：" :label-width="formLabelWidth" prop="categoryId">
                        <!-- 分类选择组件 -->
                        <category-select v-model="ruleForm.categoryId"></category-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <!-- 价格 -->
                    <el-form-item label="价格：" :label-width="formLabelWidth" prop="price">
                        <el-input-number v-model="ruleForm.price" :min="0" :precision="2" :step="1"></el-input-number>
                        <span>&nbsp;&nbsp;元</span>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <!-- 库存 -->
                    <el-form-item label="库存：" :label-width="formLabelWidth" prop="stock">
                        <el-input-number v-model="ruleForm.stock" :min="0" :precision="2" :step="1"></el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- 描述 -->
            <el-form-item label="描述：" :label-width="formLabelWidth" prop="description">
                <el-input type="textarea" v-model="ruleForm.description" placeholder="请输入商品描述"
                          autocomplete="off"></el-input>
            </el-form-item>
            <!-- 产地 -->
            <el-form-item label="产地：" :label-width="formLabelWidth" prop="origin">
                <el-input v-model="ruleForm.origin" placeholder="请输入生产地址" autocomplete="off"></el-input>
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
    import CategorySelect from '../category/CategorySelect.vue';
    import axios from "axios";


    export default {
        props: {
            dialogVisible: {
                type: Boolean,
                default: false
            }
        },
        components: {
            CategorySelect
        },
        data() {
            return {
                disabled: false,
                ruleForm: {
                    productName: '',
                    description: '',
                    categoryId: null,
                    price: 0,
                    image: '',
                    origin: '',
                    stock: 0,
                },
                formLabelWidth: '120px',
                rules: {
                    productName: [
                        {required: true, message: '请输入商品名称', trigger: 'blur'},
                    ],
                }
            };
        },
        methods: {
            //上传图片，手动更改请求路径
            async upload(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                const url = '/common/upload'
                try {
                    await axios.post(url, formData).then(res => {
                        console.log('上传图片成功')
                        console.log(res.data)
                        this.ruleForm.image = res.data.data
                        console.log("image:")
                        console.log(this.ruleForm.image)
                        // this.addGoodsdata()
                    })
                } catch (error) {
                    console.log('图片上传失败', error)
                }
            },
            handleRemove(file) {
                console.log(file)
                this.$refs.upload.clearFiles();
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url
                this.dialogVisible = true
            },
            handlePictureSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw)
                console.log('imageUrl' + this.imageUrl)
            },
            // 重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            // 取消提交
            onClose() {
                this.resetForm('ruleForm')
                this.$emit('close')
            },
            // 提交表单
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.addGoodsdata()
                    }
                })
                // this.$refs.upload.submit
            },
            // 发送添加请求
            addGoodsdata() {
                let data = this.ruleForm
                console.log("data:")
                console.log(this.ruleForm)
                this.$api.goods.add(data)
                    .then((response) => {
                        console.log(data)
                        console.log(response);
                        // 状态200时

                        if (response.code === 200) {
                            this.onClose()
                            this.$emit('addSuccess', '成功添加商品')
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
            }
        }
    }

</script>

<style></style>