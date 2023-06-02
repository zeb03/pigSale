<template>
    <div class="userhome_header">
        <logo class="header_logo"></logo>
        <div id="search" :class="{ search_fix: isFixed }">
            <div class="container">
                <a href="#">
                    <div class="fix_logo"></div>
                </a>
                <goods-search-one class="header_search" @onSearch="searchGoods"></goods-search-one>
                <div class="header_cart">
                    <router-link :to="'/userInfo/' + loginID + '/cart'">
                        <div class="cart_container">
                            <div class="el-icon-shopping-cart-full"></div>
                            <span>购物车</span>
                        </div>
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import GoodsSearchOne from '../goods/GoodsSearchOne.vue'
import Logo from './Logo.vue'
export default {
    components: { GoodsSearchOne, Logo },
    name: "UserhomeHeader",
    props: {
        loginID: {
            type: String,
            default: '-1'
        }
    },
    data() {
        return {
            isFixed: false
        }
    },
    methods: {
        searchGoods(keyword) {
            console.log(keyword)
            this.$emit('onSearch', keyword)
        }
    },
    created: function () {
        //监听滚动条位置，使其固定定位
        window.addEventListener('scroll', () => {
            let scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
            if (scrollTop > 100) {
                this.isFixed = true
            } else {
                this.isFixed = false
            }
        })

    }

}
</script>

<style scoped>
.userhome_header {
    width: 1190px;
    height: 100px;
    margin: 0 auto;
    padding-top: 23px;
    box-sizing: border-box;
    position: relative;
}

.header_logo {
    position: absolute;
    top: 5px;
    left: 0;
    margin-left: 17px;
    z-index: 30;
}

/* 固定定位样式 */
.search_fix {
    position: fixed;
    width: 100%;
    top: 0;
    left: 0;
    background-color: #fff;
    z-index: 99999;
    box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

.search_fix .fix_logo {
    float: left;
    margin-left: 80px;
    margin-top: 11px;
    height: 41px;
    width: 71px;
    background-image: url('../../assets/img/logo.png');
    background-size: contain;
    background-repeat: no-repeat;

}

.userhome_header .search_fix .container .header_search {
    margin-left: 80px;
}

.userhome_header .container {
    width: 1190px;
    margin: 0 auto;
    position: relative;
}

.userhome_header .container .header_search {
    margin-left: 240px;
    padding: 8px 0;
}

.userhome_header .container .header_cart {
    height: 45px;
    width: 120px;
    position: absolute;
    top: 10px;
    right: 170px;
    cursor: pointer;
}

.userhome_header .container .header_cart a:hover {
    text-decoration: none;
}

.userhome_header .container .cart_container {
    border: 2px solid #409eff9e;
    border-radius: 40px;
    height: 35px;
    padding: 0 15px;
    line-height: 35px;

}

.userhome_header .container .cart_container:hover {
    background-color: #40a0ff10;
    color: #409EFF;

}



.userhome_header .container .cart_container span {
    font-size: 15px;
    vertical-align: middle;
}

.userhome_header .container .el-icon-shopping-cart-full {
    margin-right: 5px;
    color: #409EFF;
    font-size: 28px;
    font-weight: 700;
    vertical-align: middle;
}
</style>