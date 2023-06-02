<template>
  <div id="app" style="min-width: 1200px;">
    <nav>
      <router-link to="/login">Login</router-link>|
      <router-link to="/userHome">User</router-link>
    </nav>
    <router-view style="height:100%">
    </router-view>
  </div>
</template>

<script>

export default {
    methods: {
    }

}
</script>

<style lang="less">


</style>
