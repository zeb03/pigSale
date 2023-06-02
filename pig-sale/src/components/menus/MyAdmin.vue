<template>
    <div>
        <h4 class="text-center">管理员账号</h4>
        <el-row type="flex" justify="space-around" style="margin-bottom:10px">
            <el-col :span="18">
                <!-- <el-input v-model="keyword" clearable placeholder="请输入搜索关键词" style="width:180px"
                    @keydown.enter.native="searchAdmin"></el-input> -->
                <!-- <el-button type="primary" plain @click="searchAdmin">搜索</el-button> -->
                <el-button type="info" plain @click="requestAdminData">显示所有</el-button>
            </el-col>
            <el-col :span="2">
                <el-button type="success" plain @click="addAdminVisible = true">添加</el-button>
            </el-col>
        </el-row>
        <!-- 用户表格 -->
        <admin-table v-bind:adminlist="list" style="margin-bottom:10px" @onEdit="onEdit"></admin-table>
        <!-- 页码部分 -->
        <el-pagination :total="total" :page-size="pageSize" layout="total, jumper, prev, pager, next" background
            @current-change="handleCurrentChange">
        </el-pagination>
        <!-- 添加用户 -->
        <add-admin v-bind:dialogVisible="addAdminVisible" @close="addAdminVisible = false"
            @addSuccess="onAddSuccess"></add-admin>
        <!-- 编辑用户 -->
        <el-dialog title="编辑账号信息" :visible.sync="ifShowEdit">
            <adit-admin v-bind:account="editData" v-if="ifShowEdit" @onCancel="ifShowEdit = false"></adit-admin>
        </el-dialog>
    </div>
</template>

<script>
import AdminTable from '../admin/AdminTable.vue'
import AddAdmin from '../admin/AddAdmin.vue'
import AditAdmin from '../admin/EditAdmin.vue'

export default {
    components: { AdminTable, AddAdmin, AditAdmin },
    name: 'MyAdmin',
    data() {
        return {
            addAdminVisible: false,
            currentPage: 1,//当前页码
            total: 0,//列表数量总数
            pageSize: 5,//当前页码的数量
            list: [],//列表数据
            editData: {},//编辑的数据
            ifShowEdit: false//是否显示编辑页
        }
    },
    //发送请求，请求第一页
    created: function () {
        this.requestAdminData({})
    },
    methods: {
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.requestAdminData({ currentPage: val })
        },
        searchAdmin() {
            let keyword = this.keyword
            this.requestAdminData({ keyword })
        },
        requestAdminData({ currentPage = 1, pageSize = this.pageSize, role = 1 }, successMsg = '成功加载数据') {
            console.log(currentPage, pageSize)
            const params = {
                currentPage,
                pageSize,
                role
            }
            this.$api.admin.get(params)
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
            this.requestAdminData({}, msg)
        },
        onEdit(index) {
            //让密码隐藏
            this.list[index].password = ''
            //保存编辑数据
            this.editData = this.list[index]
            this.ifShowEdit = true
        }
    },

}
</script>

<style lang="less" scoped></style>