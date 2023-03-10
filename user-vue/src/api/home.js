// 首先引入我们自定义的axios对象
import request from '@/utils/request'
// 导出对象
const queryUser = data =>{
    return request.post({
        url:'/queryUser',
        data:data
    })
}
// 删除
const deleteUser = data =>{
    return request.deletes({
        url:'/deleteUser',
        data:data
    })
}
// 撤销
const undo = data =>{
    return request.post({
        url:'/undo',
        data:data
    })
}
// 新增
const addNew = data =>{
    return request.post({
        url:'/insertUser',
        data:data
    })
}
// 编辑
const editData = data =>{
    return request.post({
        url:'/updateUser',
        data:data
    })
}
export default{
    queryUser,
    deleteUser,
    undo,
    addNew,
    editData
}