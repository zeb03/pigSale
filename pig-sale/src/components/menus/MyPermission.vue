<template>
    <div>
        <h4 class="text-center">权限管理</h4>
        <div style="width: 340px;display: flex;margin-bottom: 10px;">
            <el-input v-model="search" placeholder="搜索用户" style="margin-left: 10px"></el-input>
            <el-button type="primary" plain icon="el-icon-search" style="margin-left: 10px"
                       @click="requestAdminsData(search)">搜索
            </el-button>
        </div>
        <el-table :data="adminList" height="520" style="width: 100%;margin-bottom: 5px;">

            <el-table-column align="center" prop="username" label="用户名"></el-table-column>
            <el-table-column align="center" prop="role" label="角色">
                <template slot-scope="scope">
                    <el-select v-model="scope.row.role" @change="changeRole(scope.row)"
                               :disabled="scope.row.userId == 1">
                        <el-option v-for="role in roles" :key="role.roleId" :label="role.roleName" :value="role.roleId"
                                   :disabled="role.roleId == 1"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                    <el-button type="primary" size="small" @click="showPermissionsDialog(scope.row.userId)">权限管理
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 页码部分 -->
        <el-pagination :total="total" :page-size="pageSize" layout="total, jumper, prev, pager, next" background
                       @current-change="handleCurrentChange">
        </el-pagination>
        <el-dialog :visible.sync="permissionsDialogVisible">
            <div class="dialog-header">
                <i class="el-icon-user"></i>
                <span>权限修改</span>
            </div>
            <div class="dialog-body">
                <div class="permission-row" v-for="role in roles" :key="role.roleId">
                    <div v-if="role.roleId !== 1">
                        <div class="role-info">
                            <div class="role-name">{{ role.roleName }}</div>
                            <div class="role-desc">{{ role.description }}</div>
                        </div>
                        <div class="permission-list">
                            <el-checkbox-group v-model="checkedValues">
                                <el-checkbox v-for="permission in role.rolePermissions"
                                             :key="permission.permissionId"
                                             :label="permission.permissionId"
                                             :disabled="userId == 1">{{ permission.permissionId }}
                                </el-checkbox>
                            </el-checkbox-group>
                        </div>
                    </div>
                </div>
            </div>

            <div class="dialog-footer">
                <el-button @click="permissionsDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="savePermissions">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import request from '@/utils/request'

    export default {
        data() {
            return {
                search: '',
                adminList: [
                    {
                        userId: 0,
                        username: "",
                        name: "",
                        gender: "-1",
                        birthday: "",
                        role: "1",
                        phone: "",
                        email: "",
                        roleName: '超级管理员',
                        permissions: ['查看', '编辑', '删除']
                    }
                ],
                permissionsDialogVisible: false,
                checkedValues: [],
                roles: [
                    {
                        roleId: 2,
                        roleName: "商品管理员",
                        description: "可以进行商品管理",
                        rolePermissions: [
                            {
                                permissionId: '1',
                            },
                            {
                                permissionId: '2',
                            },
                            {
                                permissionId: '3',
                            }
                        ]
                    }
                ],
                userPermissions: [
                    {
                        permissionId: 5,
                        userId: 10,
                    }
                ],
                userId: 10,
                currentPage: 1,//当前页码
                total: 0,//列表数量总数
                pageSize: 5,//当前页码的数量
            }
        },
        //发送请求，请求第一页
        created() {
            this.requestAdminsData()
            this.showRoleWithPermissions()
        },
        methods: {
            handleCurrentChange(val) {
                //处理分页插件
                console.log(`当前页: ${val}`);
                this.currentPage = val
                this.requestAdminsData()
            },

            requestAdminsData(search) {
                //查询所有管理员
                const params = {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    role: 1,
                    search: this.search
                }
                this.$api.users.get(params)
                    .then(response => {
                        if (response.code === 200) {
                            this.$message.success("成功加载数据")
                            let {list, total} = response.data
                            this.adminList = list
                            this.total = total
                        } else {
                            this.$message.error(response.msg)
                        }
                    })
            },

            changeRole(user) {
                // 修改用户角色
                console.log(`将用户 ${user.userId} 的角色修改为 ${user.role}`)
                user.password = null
                request.put(`/user`, user).then(response => {

                    if (response.code === 200) {
                        this.$message.success("成功修改用户角色")
                    } else {
                        this.$message.error(response.msg)
                    }
                })
            },

            showPermissionsDialog(userId) {
                //显示修改权限的弹窗
                // this.showRoleWithPermissions()
                this.userId = userId
                this.showUserPermission()
                this.permissionsDialogVisible = true
            },

            showRoleWithPermissions() {
                //展示所有角色及其权限
                this.$api.permissions.getAllAdminRole()
                    .then(response => {
                        console.log(response)
                        this.roles = response.data
                        console.log('权限:')
                        console.log(this.roles)

                    })
            },

            showUserPermission() {
                //查询当前用户已有的权限
                this.$api.permissions.getOneUserPermission(this.userId)
                    .then(response => {
                        this.checkedValues = response.data.filter(permission => permission.userId === this.userId).map(permission => permission.permissionId)
                        console.log(this.checkedValues)
                    })
            },

            savePermissions() {
                // 发送请求将用户的权限修改，覆盖以前的数据
                this.permissionsDialogVisible = false
                console.log(this.checkedValues)
                this.userPermissions = this.checkedValues.map(permissionId => ({
                    permissionId,
                    userId: this.userId
                }))
                this.$api.permissions.update(this.userPermissions)
                    .then(response => {

                        if (response.code === 200) {
                            this.$message.success("成功修改用户权限")
                        }
                    })
            }
        },
    }
</script>

<style>
    .dialog-header {
        display: flex;
        align-items: center;
        font-size: 20px;
        padding: 20px;
        background-color: #f5f7fa;
    }

    .dialog-header i {
        font-size: 30px;
        margin-right: 10px;
    }

    .dialog-body {
        padding: 20px;
    }

    .permission-row {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
    }

    .role-info {
        flex: 1;
    }

    .role-name {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 5px;
    }

    .role-desc {
        font-size: 14px;
        color: #909399;
        margin-bottom: 5px;
    }

    .permission-list {
        flex: 3;
    }

    .dialog-footer {
        display: flex;
        justify-content: flex-end;
        padding: 20px;
        background-color: #f5f7fa;
    }
</style>