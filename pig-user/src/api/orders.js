import request from "@/utils/request";

function getAll(params) {
    return request({
        method: 'get',
        url: '/orders/user/page',
        params
    })
}

function getUserNotReviewOrder(userId) {
    return request({
        method: 'get',
        url: `/order/detail/review/${userId}`,
    })
}

function submit(addressId, cartItemIds, remark) {
    return request({
        method: 'post',
        url: '/orders/submit',
        data: {
            addressId,
            cartItemIds,
            remark
        }
    })
}

function orderAgain(orderId) {
    return request({
        method: 'post',
        url: '/orders/again',
        data: {
            id: orderId
        }
    })
}

function cancel(orderId) {
    return request({
        method: 'delete',
        url: `/orders/cancel/${orderId}`
    })
}

export default { getAll, getUserNotReviewOrder, submit, orderAgain, cancel }