import request from "@/utils/request";

function getAll() {
    return request({
        method: 'get',
        // url: '/data/test1.json',
        url: ' /permissions/list',
    })
}

function getOneUserPermission(userId) {
    return request({
        method: 'get',
        url: `/user/permissions/list/${userId}`,
    })
}

function getAllAdminRole() {
    return request({
        method: 'get',
        url: '/role/permissions',
    })
}

function update(data) {
    return request({
        method: 'post',
        url: '/user/permissions/add/batch',
        data
    })
}

function removeUserPermission(id) {
    return request({
        method: 'delete',
        url: `/user/permissions/remove/${id}`
    })

}
export default { getAll, getOneUserPermission, getAllAdminRole, update, removeUserPermission }