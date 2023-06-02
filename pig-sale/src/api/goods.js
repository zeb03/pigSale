import request from "@/utils/request";

// { currentPage: 1, pageSize: 1, keyword: null, categoryId: null }
function get(params) {
    return request({
        method: 'get',
        url: '/product/page',
        // url: '/data/test.json',
        params
    })
}

// { productName: '', description: '', categoryId: null, price: 0, image: '', origin: '', stock: 0, }
function add(data) {
    return request({
        method: 'post',
        url: '/product',
        data
    })
}

// { productId, productName, description, categoryId, categoryName, parentId, price, image, origin, stock }
function update(data) {
    return request({
        method: 'put',
        url: '/product',
        data
    })
}

function remove(goodsId) {
    return request({
        method: 'delete',
        url: `/product/${goodsId}`
    })

}
export default { get, add, update, remove }