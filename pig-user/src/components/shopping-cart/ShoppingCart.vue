<template>
    <div>
        <!-- 购物车 -->
        <div class="cart_container" v-if="!isSubmit">
            <el-table ref="multipleTable" :data="goodsList" height="300" style="width: 100%" stripe
                @select-all="isFull = !isFull" @selection-change="handleSelectionChange">
                <!-- 选择框 -->
                <el-table-column type="selection" width="55" :aria-checked="isFull"></el-table-column>
                <!-- 图片 -->
                <el-table-column align="center" label="" width="190">
                    <template slot-scope="{ row }">
                        <el-image :src="getImgUrl(row.image)">
                            <div slot="error" class="image-slot">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                    </template>
                </el-table-column>
                <!-- 商品 -->
                <el-table-column align="center" label="商品信息" width="100" prop="name">
                </el-table-column>
                <!-- 单价 -->
                <el-table-column align="center" label="单价" width="140">
                    <template slot-scope="{ row }">
                        ￥{{ row.amount.toFixed(2) }}
                    </template>
                </el-table-column>
                <!-- 数量 -->
                <el-table-column align="center" label="数量" width="140">
                    <template slot-scope="{ row }">
                        <el-input-number v-model="row.quantity" size="mini" :min="1"
                            @change="updateNumber(row.productId, row.quantity)">
                        </el-input-number>
                    </template>
                </el-table-column>
                <!-- 操作 -->
                <el-table-column align="center" label="操作" width="140">
                    <template slot-scope="{ row }">
                        <el-button @click="onDeleteClick(row.productId)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="footer-container">
                <el-checkbox :value="isFull" @change="onSelectAll">全选</el-checkbox>
                <el-button class="button" type="text" @click="onRemoveAll">清空购物车</el-button>
                <div>
                    <span>合计</span>
                    <span class="amount">￥{{ amount.toFixed(2) }}</span>
                </div>
                <el-button class="button" type="primary" round @click="isSubmit = true" :disabled="tatol === 0">
                    提交订单（{{ tatol }}）
                </el-button>
            </div>
        </div>
        <!-- 提交订单页面 -->
        <submit-order v-else :submitList="multipleSelection" :amount="amount" :tatol="tatol"
            @cancel="isSubmit = false" @submitSuccess="onSubmitSuccess"></submit-order>
    </div>
</template>
  
<script>
import SubmitOrder from '../orders/SubmitOrder.vue'


export default {
    components: { SubmitOrder },
    name: 'App',
    data() {
        return {
            goodsList: [],
            multipleSelection: [], //选择的数据
            isFull: false,
            isSubmit: false //是否显示提交订单页面
        }
    },
    computed: {
        amount() {
            let amount = 0
            this.multipleSelection.forEach((val) => {
                amount += val.amount * val.quantity
            })
            return amount
        },
        tatol() {
            let tatol = 0
            this.multipleSelection.forEach((val) => {
                tatol += val.quantity
            })
            return tatol
        },
    },
    created: function () {
        this.requestCart()
    },
    methods: {
        getImgUrl(img) {
            return `http://localhost:8080/common/download?filename=${img}`
        },
        // 勾选选项时，更新选择的数据
        handleSelectionChange(val) {
            this.multipleSelection = val;
            console.log(val)
        },
        // 请求数据
        requestCart() {
            this.$api.cart.get()
                .then((response) => {
                    // console.log(response);
                    if (response.code === 200) {
                        // this.$message.success("成功")
                        this.goodsList = response.data
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        // 选择所有商品
        onSelectAll() {
            this.$refs.multipleTable.toggleAllSelection()
        },
        // 更新数量
        updateNumber(productId, quantity) {
            // console.log(productId, quantity)
            this.$api.cart.updateNum(productId, quantity)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('已更新数据')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        //移除单个商品
        onDeleteClick(productId) {
            this.updateNumber(productId, 0)
            //刷新数据
            this.requestCart()
        },
        onClickSubmit() {

        },
        // 点击清空购物车时
        onRemoveAll() {
            this.$confirm('此操作将清空购物车, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.removeAll()
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },
        // 发送清空请求
        removeAll() {
            this.$api.cart.removeAll()
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('已清空')
                        //刷新数据
                        this.requestCart()
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        // 提交订单成功
        onSubmitSuccess() {
            this.isSubmit = false
            this.requestCart()

        }

    },

}
</script>
  
<style scoped>
.cart_container {
    padding-top: 45px;
    padding-bottom: 50px;
}

.footer-container {
    /* position: fixed; */
    /* bottom: 0; */
    height: 50px;
    width: 90%;
    z-index: 999;
    border-top: 1px solid #efefef;
    padding: 0 10px;
    background-color: white;

    display: flex;
    justify-content: space-between;
    align-items: center;
}

.amount {
    color: red;
    font-weight: bolder;
}
</style>
  