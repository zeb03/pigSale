<template>
    <div class="users_container">
        <!-- 地址添加和编辑 -->
        <add-edit-address :edit-data="edmintData" @addSuccess="requestAddress"
            @editSuccess="requestAddress"></add-edit-address>
        <!-- 地址表格 -->
        <el-alert :title="'已保存了'+ addressList.length +'条地址'" style="margin-top: 30px;" :closable="false" type="info" show-icon>
        </el-alert>
        <el-table :data="addressList" style="width: 100%; margin-top: 20px;" stripe border>
            <el-table-column prop="recipientName" align="center" label="收货人" width="90">
            </el-table-column>
            <el-table-column align="center" label="所在地区" width="140">
                <template slot-scope="{ row }">
                    {{ row.province + " " + row.city }}
                    <br>
                    {{ row.district }}
                </template>
            </el-table-column>
            <el-table-column prop="detail" align="center" label="详细地址" width="200">
            </el-table-column>
            <el-table-column prop="recipientPhone" align="center" label="电话/手机" width="120">
            </el-table-column>
            <el-table-column align="center" label="操作" width="120">
                <template slot-scope="scope">
                    <el-button type="success" round plain size="small" @click="onEditClick(scope.$index)">编辑</el-button>
                    <el-button @click="onDeleteClick(scope.row)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
            <el-table-column align="center" label="状态">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.isDefault === 1" type="success">默认地址</el-tag>
                    <el-button v-else plain size="mini" @click="setDefault(scope.row)">
                        设为默认
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import AddEditAddress from './AddEditAddress.vue'

export default {
    name: 'AddressTable',
    components: { AddEditAddress },
    props: {
        Addresslist: {
            type: Array
        }
    },
    data() {
        return {
            edmintData: {
                "addressId": "",
                "userId": "",
                "recipientName": "",
                "recipientPhone": "",
                "province": "",
                "city": "",
                "district": "",
                "detail": "",
                "isDefault": false
            },
            addressList: []
        }
    },
    created: function () {
        this.requestAddress()
    },
    methods: {
        onEditClick(index) {
            const item = this.addressList[index]
            // 深度克隆,使传入组件的值不改变本地的值
            this.edmintData = { ...item }
            // 转换isDefault的值
            let isDefault = this.edmintData.isDefault
            this.edmintData.isDefault = (isDefault === 1 ? true : false)
            //浏览器滚动条返回顶部
            window.scrollTo(0, 0)
        },
        setDefault(row) {
            //默认为值1     
            let isDefault = 1
            let { addressId, recipientName, recipientPhone, province, city, district, detail } = row
            //设置为默认
            let data = { addressId, recipientName, recipientPhone, province, city, district, detail, isDefault }
            this.update(data)
        },
        // 请求数据
        requestAddress() {
            this.$api.address.get()
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        // this.$message.success("成功")
                        this.addressList = response.data
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        //发送编辑地址请求
        update(data) {
            // console.log(data)
            this.$api.address.update(data)
                .then((response) => {
                    console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功保存')
                        //刷新数据
                        this.requestAddress()
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        onDeleteClick(row) {
            // console.log(row.addressId)
            this.$confirm('确定删除该条地址吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteAddress(row.addressId)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        deleteAddress(addressId) {
            this.$api.address.remove(addressId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success("删除成功")
                        //刷新数据
                        this.requestAddress()
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
    },

}
</script>

<style lang="less" scoped></style>