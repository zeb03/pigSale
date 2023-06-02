import request from "@/utils/request";

function get(params ) {
    return request({
        method: 'get',
        // url: '/data/category.json',
        url: '/category/list',
        params
    })
}

function add(data) {
    return request({
        method: 'post',
        url: '/category',
        data
    })
}

function update(data) {
    return request({
        method: 'put',
        url: '/category',
        data
    })
}

function remove(categoryId) {
    return request({
        method: 'delete',
        url: `/category/${categoryId}`
    })

}
export default { get, add, update, remove }