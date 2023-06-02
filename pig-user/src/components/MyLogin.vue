<template>
    <div class="login_container">
        <div style="float: right;">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import LoginForm from './user/LoginForm.vue';
import MyRegister from './user/MyRegister.vue';

export default {
    name: 'MyLogin',
    components: { MyRegister, LoginForm },
    data() {
        return {

        }
    },

}
</script>

<style lang="less" scoped>
.login_container {
    height: 100%;
    min-height: 800px;
    background: url('../assets/img/login_bg.jpg') no-repeat;
    background-size: 100% 100%;
}
</style>
