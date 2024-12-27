package org.javaboy.tienchin.channel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.javaboy.tienchin.channel.domain.Channel;
import org.javaboy.tienchin.channel.domain.vo.ChannelVO;
import org.javaboy.tienchin.channel.mapper.ChannelMapper;
import org.javaboy.tienchin.channel.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 渠道管理表 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-27
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements IChannelService {

    @Autowired
    ChannelMapper channelMapper;

    @Override
    public List<Channel> selectChannelList(ChannelVO channelVO) {
        return channelMapper.selectChannelList(channelVO);
    }

    @Override
    public AjaxResult addChannel(ChannelVO channelVO) {
        QueryWrapper<Channel> qw = new QueryWrapper<>();
        qw.lambda().eq(Channel::getChannelName, channelVO.getChannelName()).eq(Channel::getDelFlag, 0);
        Channel c = getOne(qw);
        if (c != null) {
            //说明存在一个同名的渠道，并且没有删除
            return AjaxResult.error("存在同名渠道，添加失败");
        }
        Channel channel = new Channel();
        BeanUtils.copyProperties(channelVO, channel);
        channel.setCreateBy(SecurityUtils.getUsername());
        channel.setCreateTime(LocalDateTime.now());
        channel.setDelFlag(0);
        return save(channel)?AjaxResult.success("添加成功"):AjaxResult.error("添加失败");
    }

    @Override
    public AjaxResult updateChannel(ChannelVO channelVO) {
        Channel channel = new Channel();
        BeanUtils.copyProperties(channelVO, channel);
        channel.setUpdateBy(SecurityUtils.getUsername());
        channel.setUpdateTime(LocalDateTime.now());
        //防止前端修改这三个属性
        channel.setCreateTime(null);
        channel.setCreateBy(null);
        channel.setDelFlag(null);
        return updateById(channel)?AjaxResult.success("更新成功"):AjaxResult.error("更新失败");
    }

    @Override
    public boolean deleteChannelByIds(Long[] channelIds) {
        UpdateWrapper<Channel> uw = new UpdateWrapper<>();
        uw.lambda().set(Channel::getDelFlag, 1).in(Channel::getChannelId, channelIds);
        return update(uw);
    }

    @Override
    public boolean importChannel(List<Channel> channelList, boolean updateSupport) {
        if(updateSupport){
            //想更新，根据 ID 去更新
            List<Channel> channels = channelList.stream().map(channel -> {
                channel.setUpdateTime(LocalDateTime.now());
                channel.setUpdateBy(SecurityUtils.getUsername());
                return channel;
            }).collect(Collectors.toList());
            return updateBatchById(channels);
        }else{
            //插入
            List<Channel> channels = channelList.stream().map(channel -> {
                channel.setCreateTime(LocalDateTime.now());
                channel.setCreateBy(SecurityUtils.getUsername());
                channel.setChannelId(null);
                return channel;
            }).collect(Collectors.toList());
            return saveBatch(channels);
        }
    }
}
