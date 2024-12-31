import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询渠道列表
export function listChannels() {
    return request({
        url: '/tienchin/business/channels',
        method: 'get'
    })
}

// 查询活动列表
export function listActivity(channelId) {
    return request({
        url: '/tienchin/business/activity/'+channelId,
        method: 'get'
    })
}

// 新增线索
export function addBusiness(data) {
    return request({
        url: '/tienchin/business',
        method: 'post',
        data: data
    })
}

// 查询线索列表
export function listBusiness(query) {
    return request({
        url: '/tienchin/business/list',
        method: 'get',
        params: query
    })
}

// 分配商机
export function assignBusiness(data) {
    return request({
        url: '/tienchin/assignment',
        method: 'post',
        data: data
    })
}

// 根据课程类型查询课程
export function getCourseByType(type) {
    return request({
        url: '/tienchin/business/course/' + type,
        method: 'get'
    })
}

// 根据id查询商机详情
export function getBusinessById(businessId) {
    return request({
        url: '/tienchin/business/' + businessId,
        method: 'get'
    })
}
// 根据id查询商机详情
export function getAllCourse() {
    return request({
        url: '/tienchin/business/all_course',
        method: 'get'
    })
}

// 跟进商机
export function businessFollow(data) {
    return request({
        url: '/tienchin/business/follow',
        method: 'post',
        data: data
    })
}

// 查询线索跟进记录
export function getBusinessRecordByBusinessId(businessId) {
    return request({
        url: '/tienchin/follow/record/business/' + businessId,
        method: 'get'
    })
}

//根据 ID 查询商机摘要信息
export function getBusinessSummaryById(businessId){
    return request({
        url: '/tienchin/business/summary/' + businessId,
        method: 'get'
    })
}

// 修改商机
export function updateBusiness(data) {
    return request({
        url: '/tienchin/business',
        method: 'put',
        data: data
    })
}

// 删除商机
export function deleteBusinessByIds(businessIds) {
    return request({
        url: '/tienchin/business/' + businessIds,
        method: 'delete'
    })
}
