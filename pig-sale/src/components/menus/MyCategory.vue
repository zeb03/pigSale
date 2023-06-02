<template>
    <div>
        <h4 class="text-center">分类管理</h4>
        <!-- 添加 -->
        <el-card class="box-card" style="margin-top: 20px;">
            <div slot="header" class="clearfix">
                <span style="font-size: 16px;">添加分类</span>
            </div>
            <div>
                <el-input v-model="addCategoryName" placeholder="请输入分类名称" style="width:221px"></el-input>
                <el-button @click="addCategory" type="primary" :disabled="addCategoryName === ''">添加</el-button>
            </div>
        </el-card>
        <el-divider></el-divider>
        <!-- 编辑 -->
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span style="font-size: 16px;">编辑分类</span>
            </div>
            <div>
                <!-- 分类选择组件 -->
                <category-select v-model="categoryId" style="margin-bottom: 20px;"></category-select>
                <el-input v-model="newCategoryName" placeholder="请输入新分类名称" :disabled="!categoryId"
                    style="width:221px"></el-input>
                <el-button @click="editCategory" type="primary" :disabled="newCategoryName === ''">保存</el-button>
                <el-button @click="onDeleteClick" type="danger" icon="el-icon-delete" circle
                    :disabled="!categoryId"></el-button>
            </div>
        </el-card>

    </div>
</template>
  
<script>
import CategorySelect from '../category/CategorySelect.vue'

export default {
    components: { CategorySelect },
    name: 'MyCategory',
    data() {
        return {
            addCategoryName: '',
            newCategoryName: '',
            categoryId: null
        }
    },
    methods: {
        addCategory() {
            const data = {
                categoryName: this.addCategoryName,
                parentId: 0
            }
            // 发送请求
            this.$api.category.add(data)
                .then((response) => {
                    
                    if (response.code === 200) {
                        this.$message.success('添加成功')
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        },
        editCategory() {
            const data = {
                categoryId: this.categoryId,
                categoryName: this.newCategoryName,
                parentId: 0
            }
            // 发送请求
            this.$api.category.update(data)
                .then((response) => {
                    
                    if (response.code === 200) {
                        this.$message.success('编辑成功')
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        },
        onDeleteClick() {
            this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteCategory()
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        deleteCategory() {
            this.$api.category.remove(this.categoryId)
                .then((response) => {
                    
                    if (response.code === 200) {
                        this.$message.success('删除成功')
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        }
    }
}
</script>
  
<style lang="less" scoped></style>