import request from "@/utils/request";

function get() {
    return request({
        method: 'get',
        url: '/address'
        // url: '/data/userdatalist.json'
    })
}

function add(data) {
    return request({
        method: 'post',
        url: '/address',
        data
    })
}

function update(data) {
    return request({
        method: 'put',
        url: '/address',
        data
    })
}

function remove(addressId) {
    return request({
        method: 'delete',
        url: `/address/${addressId}`
    })
}

export default { get, add, update, remove }