<template>
    <div>
        <el-card class="box-card" style="position: relative;">
            <div slot="header" class="clearfix">
                <el-button v-if="addressData.isDefault" style="position: absolute; top: -6px; right: 5px;"
                    type="text">默认地址</el-button>
                <el-button v-else style="position: absolute; top: -6px; right: 5px; cursor: default;" type="text"
                    @click="setDefault(addressData.addressId)">设为默认</el-button>
                <div style="margin-top: 10px;">
                    <span style="font-size: 20px; ">
                        {{ addressData.province + " " + addressData.city }}
                        （{{ addressData.recipientName }}收）
                    </span>
                </div>
            </div>
            <div class="text item">
                {{ addressData.district + " " + addressData.recipientPhone }}
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    props: {
        data: {
            type: Object,
            default: () => {
                return {
                    "addressId": "8",
                    "userId": "10",
                    "recipientName": "姓名",
                    "recipientPhone": "123152324",
                    "province": "广东省",
                    "city": "广州市",
                    "district": "仲恺农业工程学院海珠校区",
                    "detail": "8619",
                    "isDefault": null
                }
            }
        }

    },
    data() {
        return {
            addressData: this.data,
        }
    },
    methods: {
        setDefault(addressId) {
            //默认为值1     
            let isDefault = 1
            //设置为默认
            let data = { addressId, isDefault }
            this.update(data)
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
    }

}
</script>

<style scoped>
.text {
    font-size: 14px;
}

.item {
    margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}

.box-card {
    width: 280px;
}
</style>