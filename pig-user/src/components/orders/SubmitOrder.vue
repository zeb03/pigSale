<template>
    <div>
        <el-page-header @back="goBack" content="提交订单">
        </el-page-header>
        <!-- 地址 -->
        <h2 style="line-height: 55px; font-size: 16px;">收货地址：</h2>
        <select-address @addressId="selectAddressId = $event"
            style="display: inline-block; margin-right: 10px; "></select-address>
        <el-button class="button" type="primary" plain>
            <router-link to="address" style="text-decoration: none;">编辑/添加地址</router-link>
        </el-button>
        <!-- 商品 -->
        <h2 style="line-height: 55px; font-size: 16px;">订单商品：</h2>
        <el-table :data="submitList" height="300" style="width: 100%" stripe>
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
            <el-table-column align="center" label="数量" width="140" prop="quantity">
            </el-table-column>
            <!-- 统计 -->
            <el-table-column align="center" label="该商品合计" width="140">
                <template slot-scope="{ row }">
                    ￥{{ (row.amount * row.quantity).toFixed(2) }}
                </template>
            </el-table-column>
        </el-table>
        <!-- 订单备注 -->
        <h2 style="line-height: 55px; font-size: 16px;">订单备注：</h2>
        <el-input v-model="remark" style="width: 90%;"></el-input>
        <div class="footer-container">
            <div>
                <span>合计</span>
                <span class="amount">￥{{ amount.toFixed(2) }}</span>
            </div>
            <el-button class="button" type="primary" round @click="onClickSubmit">结算（{{ tatol }}）</el-button>
        </div>
    </div>
</template>

<script>
import SelectAddress from '../address/SelectAddress.vue'
export default {
    components: { SelectAddress },
    props: {
        submitList: Array,
        amount: Number,
        tatol: Number

    },
    data() {
        return {
            selectAddressId: null,
            remark: "",//备注

        }
    },
    computed: {
        //购物车中选中的商品id
        cartItemIds: function () {
            let itemIds = []
            this.submitList.forEach((val, index) => {
                itemIds[index] = val.cartId
            })
            return itemIds
        }
    },
    methods: {
        getImgUrl(img) {
            return `http://localhost:8080/common/download?filename=${img}`
        },
        goBack() {
            this.$emit('cancel')
        },
        onClickSubmit() {
            this.$router.push('./cart')
            // 没有地址时
            if (!this.selectAddressId) {
                this.$router.push('./address')
                this.$message("需添加收货地址")
            }
            this.$api.orders.submit(this.selectAddressId, this.cartItemIds, this.remark)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success('提交订单成功')
                        this.$emit('submitSuccess')
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
    }

}
</script>

<style scoped>
.footer-container {
    /* position: fixed; */
    /* bottom: 0; */
    margin-top: 20px;
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
    font-size: 20px;
}
</style>