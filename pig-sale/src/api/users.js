import request from "@/utils/request";

function get(params) {
    return request({
        method: 'get',
        // url: '/data/test1.json',
        url: '/user/page',
        params
    })
}

function add(data) {
    return request({
        method: 'post',
        url: '/user',
        data
    })
}

function update(data) {
    return request({
        method: 'put',
        url: '/user',
        data
    })
}

function remove(userId) {
    return request({
        method: 'delete',
        url: `/user/${userId}`
    })

}
export default { get, add, update, remove }