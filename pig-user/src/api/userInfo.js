import request from "@/utils/request";

function get(userId) {
    return request({
        method: 'get',
        // url: `/user/${userId}`
        url: `/user`
        // url: '/data/userdata.json'
    })
}

function update(data) {
    return request({
        method: 'put',
        url: '/user',
        data
    })
}

export default { get, update }