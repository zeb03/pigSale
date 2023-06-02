<template>
    <div>
        <el-select v-model="value" placeholder="请选择收货地址" style="width: 500px">
            <el-option v-for="item in addressMsgList" :key="item.addressId" :label="item.allMsg" :value="item.addressId">
                {{ item.headerMsg }}
                {{ item.bodyMsg }}
                <span v-if="item.isDefault" style="color: #ccc;">
                    默认地址
                </span>
            </el-option>
        </el-select>

    </div>
</template>

<script>
import AddressCard from './AddressCard.vue'
export default {
    components: { AddressCard },
    data() {
        return {
            value: null,
            addressList: []
        }
    },
    computed: {
        addressMsgList: function () {
            let list = []
            this.addressList.forEach((val, index) => {
                list[index] = {}
                let item = list[index]
                item.addressId = val.addressId
                item.headerMsg = `${val.province} ${val.city}（${val.recipientName}收）`
                item.bodyMsg = val.district
                item.allMsg = item.headerMsg + item.bodyMsg
                if(val.isDefault) {
                    item.isDefault = true
                }
            })
            return list
        },
        defaultaddressId: function () {
            let defaultId = null
            this.addressList.forEach((val)=>{
                if(val.isDefault) {
                    defaultId = val.addressId
                }
            })
            return defaultId
        }
    },
    watch: {
        defaultaddressId: function(newVal) {
            this.value = newVal
        },
        value: function(newVal) {
            this.$emit('addressId', newVal)
        }
    },
    created: function () {
        this.requestAddress()
    },
    methods: {
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
    },
    
}
</script>

<style></style>