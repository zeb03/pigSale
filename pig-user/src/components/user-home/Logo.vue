<template>
    <div class="header_logo_cantainer ">
        <a class="logo_img" href="#/userHome"></a>
    </div>
</template>

<script>
export default {

}
</script>

<style scoped>
.header_logo_cantainer {
    display: inline-block;
}

.header_logo_cantainer .logo_img {
    width: 150px;
    height: 90px;
    display: block;
    background-image: url('@/assets/img/logo.png');
    background-repeat: no-repeat;
    justify-content: middle;
}
</style>