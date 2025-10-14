import request from "@/utils/request";


/** 权限菜单查询 */
export function menuTreeSelect(){
    return request({
        url: '/sys/permission/menuTree',
        method: 'get'
    })
}


/** 获取角色的操作权限 */
export function getItemPermission(itemId){
    return request({
        url: '/sys/permission/' + itemId,
        method: 'get'
    })
}

/** 保存权限 */
export function saveItemPermission(itemType, itemId, permIds){
    return request({
        url: '/sys/permission/save',
        method: 'post',
        data:{itemType, itemId, permIds}
    })
}

/** 查用菜单列表 */
export function getMenuList(){
    return request({
        url: '/sys/menu/list',
        method: 'get'
    })
}

/** 查询菜单 */
export function getMenuById(menuId){
    return request({
        url: '/sys/menu/' + menuId,
        method: 'get'
    })
}

/** 添加菜单 */
export function addMenu(form){
    return request({
        url: '/sys/menu/add',
        method: 'post',
        data: form
    })
}
/** 修改菜单 */
export function updateMenu(form){
    return request({
        url: '/sys/menu/update',
        method: 'post',
        data: form
    })
}
/** 删除菜单  [111,222,333] */
export function removeMenu(menuId){
    return request({
        url: '/sys/menu/remove',
        params: { menuId},
        method: 'get',
    })
}

