<template>
    <div class="subnav">
        <div class="first">
            <span>
                <a href="#">
                    <div>
                        <span>饲料</span>
                        <div class="gt">&nbsp;</div>
                    </div>
                </a>
            </span>
            <div class="first-next">
                <ul>
                    <li>猪饲料</li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
        </div>
        <div class="first">
            <span>
                <a href="#">
                    <div>
                        <span>兽药</span>
                        <div class="gt">&gt;</div>
                    </div>
                </a>
            </span>
            <div class="first-next">
                <ul>
                    <li>猪饲料</li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>添加剂</a>
                    <div class="gt">&gt;</div>
                </div>
                <div class="clearfix"></div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>猪场设备</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>防非设备</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>温控环控</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>生物制品</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>种猪</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>1<br>
            </div>
        </div>
        <div class="first">
            <span>
                <div>
                    <a>种猪精液</a>
                    <div class="gt">&gt;</div>
                </div>
            </span>
            <div class="first-next">
                猪饲料<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>
                1<br>1<br>
            </div>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style scoped>
/* 轮播图左侧 */
.subnav {
    width: 190px;
    height: 361px;
    background-color: #fff;
    text-align: center;
    border-radius: 10px;
    /* border: 1px solid #409EFF; */
    box-shadow: 2px 3px 3px rgba(0, 0, 0, .1);
}

.first {
    width: 199px;
    height: 28px;
    margin: 10px 0;
    text-indent: 20px;
    line-height: 28px;
    position: relative;
    font-size: 16px;
    font-weight: 400;
    color: #050505;
}

.first:hover {
    /* height: 40px; */
    font-size: 18px;
    background-color: #F7F9FA;
}

.first:hover a {
    color: #409EFF;
    text-decoration: none;
    cursor: pointer;
}

.first .gt {
    float: right;
    color: #666;
    font-size: 22px;
    font-weight: 700;
    padding-right: 20px;
}

.first-next {
    width: 200px;
    height: 300px;
    position: absolute;
    top: 0;
    left: 190px;
    text-align: center;
    border: 1px solid rgb(238, 238, 238);
    color: #050505;
    background-color: #fff;
    display: none;
    z-index: 9999;
}

.first:hover .first-next {
    display: block;
}
</style>