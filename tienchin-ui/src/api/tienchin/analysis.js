import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询课程列表
export function clueAnalysisData(query) {
    return request({
        url: '/tienchin/analysis/clue',
        method: 'get',
        params: query
    })
}

// 查询课程列表
export function businessAnalysisData(query) {
    return request({
        url: '/tienchin/analysis/business',
        method: 'get',
        params: query
    })
}

// 查询课程列表
export function channelAnalysisData(query) {
    return request({
        url: '/tienchin/analysis/channel',
        method: 'get',
        params: query
    })
}

// 查询课程列表
export function activityAnalysisData(query) {
    return request({
        url: '/tienchin/analysis/activity',
        method: 'get',
        params: query
    })
}


// 新增课程
export function addCourse(data) {
    return request({
        url: '/tienchin/course',
        method: 'post',
        data: data
    })
}
// 查询课程详细
export function getCourse(courseId) {
    return request({
        url: '/tienchin/course/' + parseStrEmpty(courseId),
        method: 'get'
    })
}
// 修改课程
export function updateCourse(data) {
    return request({
        url: '/tienchin/course',
        method: 'put',
        data: data
    })
}

// 删除课程
export function delCourse(courseIds) {
    return request({
        url: '/tienchin/course/' + courseIds,
        method: 'delete'
    })
}
