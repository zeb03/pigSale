<template>
    <div>
        <h4 class="text-center">用户账号</h4>
        <el-row type="flex" justify="space-around" style="margin-bottom:10px">
            <el-col :span="18">
                <!-- <el-input v-model="keyword" clearable placeholder="请输入搜索关键词" style="width:180px"
                    @keydown.enter.native="searchUsers"></el-input> -->
                <!-- <el-button type="primary" plain @click="searchUsers">搜索</el-button> -->
                <el-button type="info" plain @click="requestUsersData">显示所有</el-button>
            </el-col>
            <el-col :span="2">
                <!-- <el-button type="success" plain @click="handle">添加</el-button> -->
            </el-col>
        </el-row>
        <!-- 用户表格 -->
        <users-table v-bind:userslist="list" style="margin-bottom:10px"
            @deleteSuccess="requestUsersData({}, '刷新数据')"></users-table>
        <el-pagination :total="total" :page-size="pageSize" layout="total, jumper, prev, pager, next" background
            @current-change="handleCurrentChange">
        </el-pagination>
    </div>
</template>

<script>
import UsersTable from '../user/UsersTable.vue';

export default {
    components: { UsersTable },
    name: 'MyUsers',
    data() {
        return {
            currentPage: 1,//当前页码
            total: 0,//列表数量总数
            pageSize: 5,//当前页码的数量
            list: []//列表数据
        }
    },
    //发送请求，请求第一页
    created: function () {
        this.requestUsersData({})
    },
    methods: {
        handleCurrentChange(val) {
            // console.log(`当前页: ${val}`);
            this.requestUsersData({ currentPage: val })
        },
        searchUsers() {
            let keyword = this.keyword
            this.requestUsersData({ keyword })
        },
        requestUsersData({ currentPage = 1, pageSize = this.pageSize, role = 0 }, successMsg = '成功加载数据') {
            // console.log(currentPage, pageSize)
            const params = {
                currentPage,
                pageSize,
                role
            }
            this.$api.users.get(params)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(successMsg)
                        let { list, total } = response.data
                        this.list = list
                        this.total = total
                        // console.log(this.list, this.total);
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
        },
        onAddSuccess(msg) {
            this.requestUsersData({}, msg)
        }

    },

}
</script>

<style lang="less" scoped></style>