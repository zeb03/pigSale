import request from "@/utils/request";

function getAll(params) {
    return request({
        method: 'get',
        url: '/orders/admin/page',
        // url: '/data/ordersTest.json',
        params
    })
}

function updateStatus(orderId, status) {
    return request({
        method: 'put',
        url: '/orders/status',
        data: {
            id: orderId,
            status,
        }
    })
}

function handleAgree(orderId) {
    return request({
        method: 'put',
        url: '/orders//handle/agree',
        data: {
            id: orderId,
        }
    })
}

function handleDisagree(orderId) {
    return request({
        method: 'put',
        url: '/orders/handle/disagree',
        data: {
            id: orderId,
        }
    })
}



export default { getAll, updateStatus, handleAgree, handleDisagree }