import request from "@/utils/request";

function login(username, password) {
    return request({
        method: 'post',
        url: '/user/login',
        data: {
            username,
            password
        }
    })
}

function register(data) {
    return request({
        method: 'post',
        url: '/user/register',
        data
    })
}

function logout() {
    return request({
        method: 'get',
        url: '/user/logout'
    })
}

function remove(userId) {
    return request({
        method: 'delete',
        url: '/user/remove',
        data: {
            userId
        }
    })
}

export default { login, register, logout, remove }