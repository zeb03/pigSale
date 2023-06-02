<template>
    <div class="user_remove">
        <el-button type="text" @click="onRemove">注销</el-button>
    </div>
</template>

<script>
export default {
    methods: {
        onRemove() {
            this.$confirm('此操作将永久注销您的账号, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.remove()
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },
        remove() {
            const loginID = localStorage.getItem('loginID')
            this.$api.user.remove(loginID)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.msg)
                        //移除
                        localStorage.removeItem('loginID')
                        localStorage.removeItem('loginName')
                        // 跳到首页
                        this.$router.push('/userHome')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }

}
</script>

<style>
.user_remove .el-button--text {
    color: #000;
    font-size: 12px;
}

.user_remove .el-button--text:hover {
    color: rgba(255, 0, 0, 0.819);
}
</style>