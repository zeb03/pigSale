<template>
    <div class="goods_container">
        <!-- 头部 -->
        <el-row type="flex" justify="space-around" style="margin-bottom:10px">
            <!-- 分类 -->
            <el-col :span="5">
                <category-select placeholder="请选择分类" v-model="categoryId"
                    style="display: inline-block; width: 120px;"></category-select>
                <el-button type="primary" plain :disabled="!categoryId" @click="showCategoryGoods">筛选</el-button>
            </el-col>
            <el-col :span="5">
            </el-col>
            <!-- 显示所有 -->
            <el-col :span="10">
                <el-button plain @click="showAllGoods">显示所有</el-button>
            </el-col>
            <!-- 添加 -->
            <el-col :span="2">
                <!-- <el-button type="success" plain @click="addGoodsVisible = true">添加</el-button> -->
            </el-col>
        </el-row>
        <!-- 商品分页显示 -->
        <goods-show :list="list"></goods-show>
        <!-- 页 -->
        <el-pagination :total="total" :page-size="pageSize" layout="total, jumper, prev, pager, next" background
            @current-change="handleCurrentChange">
        </el-pagination>
    </div>
</template>

<script>
import CategorySelect from '../category/CategorySelect.vue';
import GoodsSearch from './GoodsSearch.vue';
import GoodsSearchOne from './GoodsSearchOne.vue';
import GoodsShow from './GoodsShow.vue';

export default {
    name: 'MyGoods',
    components: {
        CategorySelect,
        GoodsShow,
        GoodsSearch,
        GoodsSearchOne
    },
    data() {
        return {
            addGoodsVisible: false,//是否显示添加页面
            currentPage: 1,//当前页码
            total: 0,//列表数量总数
            pageSize: 20,//当前页码的数量
            categoryId: null,//分类显示的分类ID
            list: [],//列表数据
        }
    },
    created: function () {
        //发送请求，请求商品第一页
        this.requestGoodsData({})
    },
    methods: {
        // 请求当前页码的数据
        handleCurrentChange(index) {
            this.requestGoodsData({ currentPage: index })
        },
        // 搜索
        searchGoods(keyword) {
            this.requestGoodsData({ keyword })
        },
        // 分类显示
        showCategoryGoods() {
            let categoryId = this.categoryId
            this.requestGoodsData({ categoryId })
        },
        // 显示所有
        showAllGoods() {
            // 将分类显示的分类ID = null
            this.categoryId = null
            // 清空搜索内容
            this.keyword = ''
            this.requestGoodsData({})
        },
        // 添加成功时，刷新数据
        onAddSuccess(msg) {
            this.requestGoodsData({}, msg)
        },
        // 请求
        requestGoodsData({ currentPage = 1, pageSize = this.pageSize, keyword = null, categoryId = null }, successMsg = '成功加载数据') {
            // console.log(currentPage, pageSize, keyword, categoryId)
            let params = {
                currentPage,
                pageSize,
                keyword,
                categoryId
            }
            this.$api.goods.get(params)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success(successMsg)
                        let { list, total } = response.data
                        this.list = list
                        this.total = total
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
    }
}
</script>

<style lang="less" scoped></style>