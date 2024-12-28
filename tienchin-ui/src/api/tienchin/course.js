import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询课程列表
export function listCourse(query) {
    return request({
        url: '/tienchin/course/list',
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
