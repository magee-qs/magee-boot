import request from "@/utils/request"; 

/** notice API */
export function getNoticeList(queryParam) { 
    return request({
        url: '/sys/notice/list',
        method: 'get',
        params: queryParam
    })
}

export function getNotice(noticeId) {
    return request({
        url: '/sys/notice/' + noticeId,
        method: 'get'
    })
}

export function addNotice(form) {
    return request({
        url: '/sys/notice/add',
        method: 'post',
        data: form
    })
}

export function editNotice(form) {
    return request({
        url: '/sys/notice/edit',
        method: 'post',
        data: form
    })
}


export function removeNotice(noticeId) {
    return request({
        url: '/sys/notice/remove/' + noticeId,
        method: 'get'
    })
}

export function changeNoticeStatus(noticeId) {
    return request({
        url: '/sys/notice/status/' + noticeId,
        method: 'get'
    })
}


export function getNoticeContentById(noticeId){
    return request({
        url: '/sys/notice/content/' + noticeId,
        method: 'get'
    })
}

