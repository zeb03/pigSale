import request from "@/utils/request";

// 查看商品评价
// currentPage：1
// pageSize：5
// queryWay：1	//1代表查询用户评论、0代表查询商品评论
// userId：10	//若要查询商品评论，则此参数为null
// productId：	//若要查询用户评论，则此参数为null
function getGoodsReview(params) {
    return request({
        method: 'get',
        url: '/review/page',
        params
    })
}

//查看用户评价
function getUserReview(userId) {
    return request({
        method: 'get',
        url: `/review/list/${userId}`,
    })
}

// 发表评论
// {
//     "anonymous": 1,									//1代表匿名评价，0代表不匿名
//     "comment": "This is a commnet",	//评论内容
//     "image": "34534645645",					//照片
//     "productId": 7,									//商品id
//     "rating": 8,										//评分
//   }
function addReview(data) {
    return request({
        method: 'post',
        url: '/review/publish',
        data
    })
}

// 修改评论
// {
//     "reviewId": "2", //不可修改
//     "userId": "10",  //不可修改
//     "productId": "8",//不可修改
//     "rating": 10,
//     "comment": "This is the second commnet",
//     "image": "34534645645",
//     "anonymous": 1,
//     "publishTime": "2023-04-08 14:40:50", //不可修改
// }
function update(data) {
    return request({
        method: 'put',
        url: '/review/edit',
        data
    })
}

// 删除评论
function remove(reviewId) {
    return request({
        method: 'delete',
        url: `/review/remove/${reviewId}`
    })

}

//获取用户信息
function getUser() {
    return request({
        method: 'get',
        url: `/user`
    })
}

export default {getGoodsReview, getUserReview, addReview, update, remove, getUser}