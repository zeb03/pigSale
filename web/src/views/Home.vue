<template>
    <div>
        <div>
            <el-image :src="getImgUrl(this.imageUrl)">
            </el-image>
        </div>
        <div>
            <el-container style="border: 1px solid #eee">
                <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                    <el-menu>
                        <el-submenu index="1">
                            <el-menu-item-group>
                                <template slot="title">分组</template>
                                <el-menu-item index="1-1">
                                    <router-link tag="div" to="/user/review">1</router-link>
                                </el-menu-item>
                                <el-menu-item index="1-2">
                                    <router-link tag="div" to="/review">2</router-link>
                                </el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                    </el-menu>
                </el-aside>
            </el-container>
        </div>
        <router-view/>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                imageUrl: 'a7.png'
            }
        },
        methods: {
            getImgUrl(img) {
                return `http://localhost:8080/common/download?filename=${img}`
            }
        }
    }
</script>