<template>
    <div class="goods_container">
        <el-table :data="list" height="520" style="width: 100%" stripe @selection-change="handleSelectionChange">
            <!-- 选择框 -->
            <el-table-column type="selection" width="55"></el-table-column>
            <!-- 商品 -->
            <el-table-column align="center" label="商品名称" width="90">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">{{ scope.row.productName }}</span>
                    <el-input v-model="scope.row.productName" placeholder="请输入内容" v-else></el-input>
                </template>
            </el-table-column>
            <!-- 图片 -->
            <el-table-column align="center" label="图片" width="90">
                <template slot-scope="{ row }">
                    <el-image :src="getImgUrl(row.image)">
                        <div slot="error" class="image-slot">
                            <i class="el-icon-picture-outline"></i>
                        </div>
                    </el-image>
                </template>
            </el-table-column>
            <!-- 分类 -->
            <el-table-column align="center" label="分类" width="100">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">{{ scope.row.categoryName }}</span>
                    <!-- 分类选择组件 -->
                    <category-select v-model="scope.row.categoryId" @onSelectLabel="scope.row.categoryName = $event"
                        :init-select-label="scope.row.categoryName" :placeholder="scope.row.categoryName"
                        v-else></category-select>
                </template>
            </el-table-column>
            <!-- 价格 -->
            <el-table-column align="center" label="价格" width="140">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">￥{{ scope.row.price.toFixed(2) }}</span>
                    <el-input-number v-model="scope.row.price" size="mini" :min="0" :precision="2" :step="1"
                        controls-position="right" v-else></el-input-number>
                </template>
            </el-table-column>
            <!-- 库存 -->
            <el-table-column prop="stock" align="center" label="库存" width="140">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">{{ scope.row.stock }}</span>
                    <el-input-number v-model="scope.row.stock" size="mini" :min="0" v-else></el-input-number>
                </template>
            </el-table-column>
            <!-- 创建时间 -->
            <el-table-column prop="createTime" align="center" label="创建时间" width="130">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <!-- 更新时间 -->
            <el-table-column prop="updateTime" align="center" label="最后更改时间" width="130">
                <template slot-scope="scope">
                    <i class="el-icon-time"></i>
                    <span style="margin-left: 10px">{{ scope.row.updateTime }}</span>
                </template>
            </el-table-column>
            <!-- 描述 -->
            <el-table-column prop="description" align="center" label="描述" width="130">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">{{ scope.row.description }}</span>
                    <el-input type="textarea" v-model="scope.row.description" :autosize="{ minRows: 2, maxRows: 4 }"
                        placeholder="请输入商品描述内容" v-else>
                    </el-input>
                </template>
            </el-table-column>
            <!-- 产地 -->
            <el-table-column prop="origin" align="center" label="产地" width="130">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" v-if="!scope.row.isEdit">{{ scope.row.origin }}</span>
                    <el-input v-model="scope.row.origin" placeholder="请输入生产地址" autocomplete="off" v-else></el-input>
                </template>
            </el-table-column>
            <!-- 操作 -->
            <el-table-column align="center" label="操作" width="140">
                <template slot-scope="scope">
                    <el-button :type="scope.row.isEdit ? 'danger' : 'success'" round plain size="small"
                        @click="onEditClick(scope.row)">
                        {{ scope.row.isEdit ? '完成' : '编辑' }}
                    </el-button>
                    <el-button @click="onCancelEdit" type="text" size="small" v-if="scope.row.isEdit">取消</el-button>
                    <el-button @click="onDeleteClick(scope.row)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import categorySelect from '../category/CategorySelect.vue'

export default {
    name: 'GoodsTable',
    components: {
        categorySelect
    },
    props: {
        goodslist: {
            type: Array
        }
    },
    watch: {
        goodslist: function (val) {
            this.list = val
        }
    },
    data() {
        return {
            list: this.goodslist,
            multipleSelection: [], //选择的数据
            beforeEdmintData: this.goodslist,
            edmintData: {},
            url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
        }
    },
    methods: {
        getImgUrl(img) {
            return `http://localhost:9999/common/download?filename=${img}`
        },
        // 勾选选项时，更新选择的数据
        handleSelectionChange(val) {
            this.multipleSelection = val;
            console.log(val)
        },
        // 点击删除时
        onDeleteClick(row) {
            console.log(row.productId)
            this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteGoods(row.productId)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        // 发送删除请求
        deleteGoods(productId) {
            console.log(productId)
            this.$api.goods.remove(productId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('删除成功')
                        //刷新数据
                        this.onCancelEdit()
                    } else {
                        this.$message.error(res.msg)
                    }
                })
        },
        // 点击编辑时
        onEditClick(row) {
            if (!row.isEdit) {
                this.$set(row, 'isEdit', true)
            } else {
                // this.$delete(row, 'isEdit')
                row.isEdit = false
            }
            //点击完成按钮时，发送请求
            if (!row.isEdit) {
                let { productId, productName, description, categoryId = null, categoryName, parentId = null, price, image, origin, stock } = row
                let data = { productId, productName, description, categoryId, categoryName, parentId, price, image, origin, stock }
                this.updateGoods(data)
                // console.log(data)
            }
        },
        // 发送更新数据请求
        updateGoods(data) {
            this.$api.goods.update(data)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('成功修改信息')
                    } else {
                        this.$message.error(res.msg)
                    }
                    //刷新数据
                    this.onCancelEdit()
                })
                .catch(() => {
                    //刷新数据
                    this.onCancelEdit()
                })
        },
        // 取消编辑时
        onCancelEdit() {
            this.$emit('cancelEdit')
        }

    }
}
</script>

<style lang="less" scoped></style>