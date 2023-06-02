<template>
    <div>
        <el-button @click="Logout">退出</el-button>
    </div>
</template>

<script>
export default {
    methods: {
        Logout() {
            //移除
            localStorage.removeItem('loginID')
            localStorage.removeItem('loginName')
            // 刷新页面
            this.$router.go(0)
            this.$api.user.logout()
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(response.msg)
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        }
    }

}
</script>

<style></style>