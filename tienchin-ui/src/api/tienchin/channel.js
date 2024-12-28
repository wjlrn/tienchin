import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询渠道列表
export function listChannel(query) {
    return request({
        url: '/tienchin/channel/list',
        method: 'get',
        params: query
    })
}

// 新增渠道
export function addChannel(data) {
    return request({
        url: '/tienchin/channel',
        method: 'post',
        data: data
    })
}
// 查询用户详细
export function getChannel(channelId) {
    return request({
        url: '/tienchin/channel/' + parseStrEmpty(channelId),
        method: 'get'
    })
}
// 修改渠道
export function updateChannel(data) {
    return request({
        url: '/tienchin/channel',
        method: 'put',
        data: data
    })
}

// 删除渠道
export function delChannel(channelIds) {
    return request({
        url: '/tienchin/channel/' + channelIds,
        method: 'delete'
    })
}
