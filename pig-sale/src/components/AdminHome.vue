<template>
    <div>
        <el-container style="height: 100%">
            <el-header>
                <el-row>
                    <el-col :span="4">
                        <img style="height: 53px" src="../assets/img/adminLogo.png" alt="">
                    </el-col>
                    <el-col :span="15">
                        <h4 style="font-size: 20px;font-weight: bolder;">猪销售后台管理系统</h4>
                    </el-col>
                    <el-col :span="3">
                        <span class="uname">{{ username + " " + loginID }}</span>                      
                    </el-col>
                    <el-col :span="2">
                        <el-button plain @click="onLogout">退出登录</el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router default-active="/adminHome/goods">
                        <el-menu-item index="/adminHome/goods">
                            <i class="el-icon-goods"></i>
                            <span slot="title">商品管理</span>
                        </el-menu-item>
                        <el-menu-item index="/adminHome/category">
                            <i class="el-icon-menu"></i>
                            <span slot="title">分类管理</span>
                        </el-menu-item>
                        <el-menu-item index="/adminHome/orders">
                            <i class="el-icon-document"></i>
                            <span slot="title">订单管理</span>
                        </el-menu-item>
                        <el-submenu index="/admin">
                            <template slot="title">
                                <i class="el-icon-s-finance"></i>
                                <span>账户管理</span>
                            </template>
                            <el-menu-item-group>
                                <el-menu-item index="/adminHome/users">
                                    <i class="el-icon-user"></i>
                                    <span slot="title">用户账号</span>
                                </el-menu-item>
                                <el-menu-item index="/adminHome/admin">
                                    <i class="el-icon-setting"></i>
                                    <span slot="title">管理员账号</span>
                                </el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                        <el-menu-item index="/adminHome/permission">
                            <i class="el-icon-lock"></i>
                            <span slot="title">权限管理</span>
                        </el-menu-item>
                    </el-menu>
                </el-aside>
                <el-main>
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
export default {
    name: 'AdminHome',
    data() {
        return {
            loginID: '登陆ID',
            username: '用户名'
        }
    },
    mounted() {
        this.loginID = localStorage.getItem('loginID')
        this.username = localStorage.getItem('username')
    },
    methods: {
        onLogout() {
            //移除token和loginID
            localStorage.removeItem('token1')
            localStorage.removeItem('loginID')

            this.$router.push('/login')
        }
    }
}
</script>

<style lang="less" scoped>
.el-header,
.el-footer {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
}

.uname {
    display: block;
    font-size: 16px;
    width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.el-aside {
    background-color: #D3DCE6;
    color: #333;
}

.el-main {
    background-color: #E9EEF3;
    color: #333;
}
</style>