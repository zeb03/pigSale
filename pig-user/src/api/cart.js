import request from "@/utils/request";

function get() {
    return request({
        method: 'get',
        url: '/cart/list'
        // url: '/data/cart.json'
    })
}

function add(data) {
    return request({
        method: 'post',
        url: '/cart/add',
        data
    })
}

function updateNum(productId,quantity) {
    return request({
        method: 'put',
        url:'/cart/edit',
        data: {
            productId,
            quantity
        }
    })
}


function removeAll() {
    return request({
        method: 'delete',
        url: `/cart/clean`
    })
}

export default { get, add, updateNum, removeAll }

