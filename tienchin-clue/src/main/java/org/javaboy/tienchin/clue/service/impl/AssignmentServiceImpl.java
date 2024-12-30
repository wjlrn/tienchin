package org.javaboy.tienchin.clue.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.javaboy.tienchin.clue.domain.Assignment;
import org.javaboy.tienchin.clue.mapper.AssignmentMapper;
import org.javaboy.tienchin.clue.service.IAssignmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.common.constant.TienchinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 线索分配表 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements IAssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    @Override
    @Transactional
    public AjaxResult assignClue(Assignment assignment) {
        try {
            //1.先将一个线索的所有分配记录中的 latest 属性设置为 false
            UpdateWrapper<Assignment> uw = new UpdateWrapper<>();
            uw.lambda().set(Assignment::getLatest, false).eq(Assignment::getAssignId, assignment.getAssignId());
            update(uw);
            //2.分配线索
            assignment.setType(TienchinConstants.CLUE_TYPE);
            assignment.setCreateBy(SecurityUtils.getUsername());
            assignment.setCreateTime(LocalDateTime.now());
            assignment.setLatest(true);
            save(assignment);
            return AjaxResult.success("分配线索成功");
        } catch (Exception e) {
            logger.error("assignClue error:", e);
            return AjaxResult.error("分配线索失败", e.getMessage());
        }
    }
}
