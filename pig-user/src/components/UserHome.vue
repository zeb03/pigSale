<template>
    <div>
        <top-nav></top-nav>
        <div class="box_shadow">
            <userhome-header :loginID="loginID" @onSearch="searchGoods"></userhome-header>
        </div>
        <div class="userhome_container">
            <userhome-main></userhome-main>
        </div>
        <!-- 返回顶部 -->
        <el-backtop :bottom="100"></el-backtop>
    </div>
</template>

<script>
import InfiniteScrollGoods from './goods/InfiniteScrollGoods.vue';
import TopNav from './user-home/TopNav.vue';
import UserhomeHeader from './user-home/UserhomeHeader.vue';
import UserhomeMain from './user-home/UserhomeMain.vue';

export default {
    data() {
        return {
            loginID: '-1',
            loginName: '用户名',
            isLogin: false
        }
    },
    components: {
        TopNav,
        UserhomeHeader,
        UserhomeMain,
        InfiniteScrollGoods

    },
    created: function () {
        //获得当前登陆用户ID
        const loginID = localStorage.getItem('loginID')
        const loginName = localStorage.getItem('loginName')
        if (!loginID) {
            // this.$message.error('请先登陆')
        } else {
            this.isLogin = true
            this.loginID = loginID
            this.loginName = loginName
        }
    },
    methods: {
        getScroll(e) {
            console.log(e)
        },
        // 搜索
        searchGoods(keyword) {
            this.$router.push('/search/' + keyword)
        },
    }

}
</script>

<style scoped>
.userhome_container {
    width: 1200px;
    margin: 0 auto;
}

.box_shadow {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>