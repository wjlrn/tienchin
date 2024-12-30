package org.javaboy.tienchin.clue.service;

import org.javaboy.tienchin.clue.domain.FollowRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.javaboy.tienchin.common.core.domain.AjaxResult;

/**
 * <p>
 * 线索跟进记录表 服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
public interface IFollowRecordService extends IService<FollowRecord> {

    AjaxResult getFollowRecordByClueId(Integer clueId);
}