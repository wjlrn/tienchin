import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询渠道列表
export function listChannels() {
    return request({
        url: '/tienchin/clue/channels',
        method: 'get'
    })
}

// 查询活动列表
export function listActivity(channelId) {
    return request({
        url: '/tienchin/clue/activity/'+channelId,
        method: 'get'
    })
}

// 新增线索
export function addClue(data) {
    return request({
        url: '/tienchin/clue',
        method: 'post',
        data: data
    })
}

// 查询线索列表
export function listClue(query) {
    return request({
        url: '/tienchin/clue/list',
        method: 'get',
        params: query
    })
}

// 根据部门查询用户列表
export function listUsers(deptId) {
    return request({
        url: '/tienchin/clue/users/' + deptId,
        method: 'get'
    })
}

// 分配线索
export function assignClue(data) {
    return request({
        url: '/tienchin/assignment',
        method: 'post',
        data: data
    })
}

// 根据id查询线索详情
export function getClueById(clueId) {
    return request({
        url: '/tienchin/clue/' + clueId,
        method: 'get'
    })
}

// 新增线索跟进
export function clueFollow(data) {
    return request({
        url: '/tienchin/clue/follow',
        method: 'post',
        data: data
    })
}

// 查询线索跟进记录
export function getClueRecordByClueId(clueId) {
    return request({
        url: '/tienchin/follow/record/clue/' + clueId,
        method: 'get'
    })
}

//无效线索跟进
export function invalidClue(data) {
    return request({
        url: '/tienchin/clue/invalid',
        method: 'post',
        data: data
    })
}
//根据 ID 查询线索摘要信息
export function getClueSummaryById(clueId){
    return request({
        url: '/tienchin/clue/summary/' + clueId,
        method: 'get'
    })
}
// 修改线索
export function updateClue(data) {
    return request({
        url: '/tienchin/clue',
        method: 'put',
        data: data
    })
}

// 删除线索
export function delClue(clueIds) {
    return request({
        url: '/tienchin/clue/' + clueIds,
        method: 'delete'
    })
}

// 查询线索跟进记录
export function clue2Business(clueId) {
    return request({
        url: '/tienchin/clue/to_business/' + clueId,
        method: 'post'
    })
}
