package org.javaboy.tienchin.follow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.tienchin.follow.domain.FollowRecord;
import org.javaboy.tienchin.follow.mapper.FollowRecordMapper;
import org.javaboy.tienchin.follow.service.IFollowRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.constant.TienchinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 线索跟进记录表 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@Service
public class FollowRecordServiceImpl extends ServiceImpl<FollowRecordMapper, FollowRecord> implements IFollowRecordService {

    @Override
    public AjaxResult getFollowRecordByClueId(Integer clueId) {
        QueryWrapper<FollowRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(FollowRecord::getType, TienchinConstants.CLUE_TYPE).eq(FollowRecord::getAssignId, clueId).orderByDesc(FollowRecord::getCreateTime);
        return AjaxResult.success(list(qw));
    }

    @Override
    public AjaxResult getFollowRecordByBusinessId(Integer businessId) {
        QueryWrapper<FollowRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(FollowRecord::getType, TienchinConstants.BUSSINESS_TYPE).eq(FollowRecord::getAssignId, businessId).orderByDesc(FollowRecord::getCreateTime);
        return AjaxResult.success(list(qw));
    }
}
