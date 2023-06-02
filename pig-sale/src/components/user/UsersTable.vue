<template>
    <div class="users_container">
        <el-table :data="userslist" height="520" style="width: 100%" stripe>
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="userId" align="center" label="用户ID" width="90">
            </el-table-column>
            <el-table-column align="center" label="图片" prop="image" width="140">
                <template slot-scope="{ row }">
                    <el-image :src="getImgUrl(row.image)">
                        <div slot="error" class="image-slot">
                            <i class="el-icon-picture-outline"></i>
                        </div>
                    </el-image>
                </template>
            </el-table-column>
            <el-table-column prop="username" align="center" label="用户名" width="100">
            </el-table-column>
            <el-table-column prop="name" align="center" label="姓名" width="140">
            </el-table-column>
            <el-table-column prop="phone" align="center" label="电话" width="140">
            </el-table-column>
            <el-table-column prop="email" align="center" label="邮箱地址" width="180">
            </el-table-column>
            <el-table-column prop="gender" align="center" label="性别" width="50">
            </el-table-column>
            <el-table-column prop="birthday" align="center" label="生日" width="140">
            </el-table-column>
            <el-table-column align="center" label="操作" width="140">
                <template slot-scope="scope">
                    <!-- <el-button :type="scope.row.isEdmit ? 'danger' : 'success'" round plain size="small"
                        @click="onEmditClick(scope.row)">
                        {{ scope.row.isEdmit ? '完成' : '编辑' }}
                    </el-button>
                    <el-button @click="onCancelEmdit" type="text" size="small" v-if="scope.row.isEdmit">取消</el-button> -->
                    <el-button @click="onDeleteClick(scope.row)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>

        </el-table>
    </div>
</template>

<script>

export default {
    name: 'UsersTable',
    props: {
        userslist: {
            type: Array
        }
    },
    data() {
        return {
            multipleSelection: [],
            beforeEdmintData: this.userslist,
            edmintData: {}
        }
    },
    methods: {
        getImgUrl(img) {
            return `http://localhost:8080/common/download?filename=${img}`
        },
        onDeleteClick(row) {
            // console.log(row.userId)
            this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteUsers(row.userId)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        deleteUsers(userId) {
            // this.$api.users.get({userId})
            this.$api.users.remove(userId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('删除成功')
                        //刷新数据
                        this.$emit('deleteSuccess')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }
}
</script>

<style lang="less" scoped></style>