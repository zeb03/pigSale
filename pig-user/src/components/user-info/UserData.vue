<template>
    <div class="user_data">
        <div class="user_data_header clearfix">
            <h2>个人资料</h2>
            <el-button class="button" type="primary" size="small" @click="onClickEdit">编辑</el-button>
        </div>
        <div class="user_data_avatar">
            <span class="avatar">头像</span>
            <el-avatar shape="square" :size="150" icon="el-icon-user" :src="getImgUrl(list.image)"></el-avatar>
        </div>
        <el-descriptions class="margin-top" :column="2" border>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user"></i>
                    用户名
                </template>
                {{ list.username }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user-solid"></i>
                    姓名
                </template>
                {{ list.name }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-tickets"></i>
                    性别
                </template>
                <el-tag size="small">{{ genderStr }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-postcard"></i>
                    生日日期
                </template>
                {{ list.birthday }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-mobile-phone"></i>
                    手机号
                </template>
                {{ list.phone }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-message"></i>
                    电子邮箱
                </template>
                {{ list.email }}
            </el-descriptions-item>
        </el-descriptions>

    </div>
</template>

<script>
export default {
    props: {
        loginId: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            list: {
                "userId": "",
                "username": "",
                "password": "",
                "email": "",
                "phone": "",
                "name": "",
                "gender": "",
                "birthday": "",
                "role": 0,
                "image": "avatar.jpg"
            },
            genderStr: "保密",
            userId: this.loginId
        }
    },
    created: function () {
        this.requestUserData()
    },
    methods: {
        getGenderStr(gender) {
            let genderStr = "保密"
            let genderOptions = [{
                value: '1',
                label: '男'
            }, {
                value: '0',
                label: '女'
            }, {
                value: '-1',
                label: '保密'
            }]
            genderOptions.forEach((obj) => {
                if (obj.value === gender) {
                    return genderStr = obj.label
                }
            })
            return genderStr
        },

        // 请求数据
        requestUserData() {
            // 通过路由，获取登陆id
            let userId = this.$route.params.loginID
            // console.log(userId)
            this.$api.userInfo.get(userId)
                .then((response) => {
                    // console.log(response);
                    
                    if (response.code === 200) {
                        this.$message.success("成功")
                        this.list = response.data
                        this.genderStr = this.getGenderStr(response.data.gender)
                    } else {
                        this.$message.error(response.msg)
                    }
                })
        },
        //显示图片，发送请求至服务端
        getImgUrl(img) {
            return `http://localhost:9999/common/download?filename=${img}`
        },
        onClickEdit() {
            this.$router.push("./edit")
        }

    },
   

}
</script>

<style>
.clearfix {
    content: '';
    display: block;
    clear: both;
}

.user_data_header {
    margin-bottom: 50px;
}

.user_data_header h2 {
    float: left;
    font-size: 20px;
    font-weight: bold;
}

.user_data_header .button {
    float: right;
    margin-right: 40px;
}

.user_data_avatar {
    margin-bottom: 30px;
}

.user_data_avatar .avatar {
    float: left;
    font-size: 14px;
    color: #909399;
    margin: 65px 32px;

}
</style>