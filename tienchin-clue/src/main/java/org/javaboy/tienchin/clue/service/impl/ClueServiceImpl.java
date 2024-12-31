package org.javaboy.tienchin.clue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.javaboy.tienchin.business.domain.Business;
import org.javaboy.tienchin.business.service.IBusinessService;
import org.javaboy.tienchin.assignment.domain.Assignment;
import org.javaboy.tienchin.clue.domain.Clue;
import org.javaboy.tienchin.follow.domain.FollowRecord;
import org.javaboy.tienchin.clue.domain.vo.ClueDetails;
import org.javaboy.tienchin.clue.domain.vo.ClueSummary;
import org.javaboy.tienchin.clue.domain.vo.ClueVO;
import org.javaboy.tienchin.clue.mapper.ClueMapper;
import org.javaboy.tienchin.assignment.service.IAssignmentService;
import org.javaboy.tienchin.clue.service.IClueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.tienchin.follow.service.IFollowRecordService;
import org.javaboy.tienchin.common.constant.TienchinConstants;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 线索表 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements IClueService {

    private static final Logger logger = LoggerFactory.getLogger(ClueServiceImpl.class);

    @Autowired
    private IAssignmentService assignmentService;

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private IFollowRecordService followRecordService;

    @Autowired
    private IBusinessService businessService;

    @Override
    @Transactional
    public AjaxResult addClue(Clue clue) {
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.lambda().eq(Clue::getPhone, clue.getPhone());
        Clue one = getOne(qw);
        if (one != null) {
            return AjaxResult.error("手机号码重复，线索录入失败");
        }
        clue.setNextTime(LocalDateTime.now().plusHours(TienchinConstants.NEXT_FOLLOW_TIME));
        clue.setCreateBy(SecurityUtils.getUsername());
        clue.setCreateTime(LocalDateTime.now());
        //添加线索
        save(clue);
        //添加线索默认的分配人
        Assignment assignment = new Assignment();
        assignment.setAssignId(clue.getClueId());
        assignment.setLatest(true);
        assignment.setType(TienchinConstants.CLUE_TYPE);
        assignment.setUserName(SecurityUtils.getUsername());
        assignment.setUserId(SecurityUtils.getUserId());
        assignment.setDeptId(SecurityUtils.getDeptId());
        assignment.setCreateBy(SecurityUtils.getUsername());
        assignment.setCreateTime(LocalDateTime.now());
        assignmentService.save(assignment);
        return AjaxResult.success("线索录入成功");
    }

    @Override
    public List<ClueSummary> selectClueList(ClueVO clueVO) {
        return clueMapper.selectClueList(clueVO);
    }

    @Override
    public AjaxResult getClueDetailsByClueId(Integer clueId) {
        ClueDetails cd = clueMapper.getClueDetailsByClueId(clueId);
        return AjaxResult.success(cd);
    }

    @Override
    @Transactional
    public AjaxResult clueFollow(ClueDetails clueDetails) {
        //1.更新 tienchin.clue 表
        Clue clue = new Clue();
        //先将 clueDetails 中的属性拷贝到 clue 中
        BeanUtils.copyProperties(clueDetails, clue);
        clue.setStatus(TienchinConstants.CLUE_FOLLOING);
        updateById(clue);
        //2.更新 tienchin.clue_follow 表
        FollowRecord record = new FollowRecord();
        record.setAssignId(clueDetails.getClueId());
        record.setCreateBy(SecurityUtils.getUsername());
        record.setCreateTime(LocalDateTime.now());
        record.setType(TienchinConstants.CLUE_TYPE);
        record.setInfo(clueDetails.getRecord());
        followRecordService.save(record);
        return AjaxResult.success("线索跟进成功");

    }

    @Override
    @Transactional
    public AjaxResult invalidcClueFollow(ClueDetails clueDetails) {
        //如果已经失败了3次，则这条线索直接变为伪线索
        Clue clue = getById(clueDetails.getClueId());
        if (clue.getFailCount() == 3) {
            //无效线索次数已达极限
            UpdateWrapper<Clue> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Clue::getStatus, TienchinConstants.CLUE_INVALIDATE).eq(Clue::getClueId, clueDetails.getClueId());
            update(updateWrapper);
            return AjaxResult.success("无效线索设置成功");
        }
        //1.首先需要设置线索表中的 fail_count 字段 + 1
        UpdateWrapper<Clue> uw = new UpdateWrapper<>();
        uw.lambda().setSql("fail_count=fail_count+1").eq(Clue::getClueId, clueDetails.getClueId());
        update(uw);
        //2.需要往线索记录表中添加一条记录
        FollowRecord record = new FollowRecord();
        record.setAssignId(clueDetails.getClueId());
        record.setCreateBy(SecurityUtils.getUsername());
        record.setCreateTime(LocalDateTime.now());
        record.setType(TienchinConstants.CLUE_TYPE);
        record.setInfo(clueDetails.getRecord());
        followRecordService.save(record);
        return AjaxResult.success("无效线索设置成功");
    }

    @Override
    public AjaxResult getClueSummaryByClueId(Integer clueId) {
        Clue clue = getById(clueId);
        return AjaxResult.success(clue);
    }

    @Override
    public AjaxResult updateClue(Clue clue) {
        return updateById(clue) ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    @Override
    public AjaxResult deleteClueById(Long[] clueIds) {
        UpdateWrapper<Clue> uw = new UpdateWrapper<>();
        uw.lambda().set(Clue::getDelFlag, 1).in(Clue::getClueId, clueIds);
        return update(uw) ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    @Override
    @Transactional
    public AjaxResult clue2Business(Integer clueId) {
        Clue clue = getById(clueId);
        Business business = new Business();
        BeanUtils.copyProperties(clue, business);
        business.setCreateBy(SecurityUtils.getUsername());
        business.setCreateTime(LocalDateTime.now());
        business.setEndTime(null);
        business.setFailCount(0);
        business.setNextTime(null);
        business.setRemark(null);
        business.setUpdateBy(null);
        business.setUpdateTime(null);
        business.setNextTime(LocalDateTime.now().plusHours(TienchinConstants.NEXT_FOLLOW_TIME));
        business.setStatus(TienchinConstants.BUSINESS_ALLOCATED);
        //1.删除线索
        UpdateWrapper<Clue> uw = new UpdateWrapper<>();
        uw.lambda().set(Clue::getDelFlag, 1).eq(Clue::getClueId, clueId);
        update(uw);
        //2.添加商机
        businessService.save(business);
        //3.默认情况下，将商机分配给 admin，将来由 admin 再将商机分配给不同的客户专员
        Assignment assignment = new Assignment();
        assignment.setUserName(TienchinConstants.ADMIN_USERNAME);
        assignment.setType(TienchinConstants.BUSSINESS_TYPE);
        assignment.setCreateBy(SecurityUtils.getUsername());
        assignment.setCreateTime(LocalDateTime.now());
        assignment.setAssignId(business.getBusinessId());
        assignment.setDeptId(TienchinConstants.ADMIN_DEPT_ID);
        assignment.setUserId(TienchinConstants.ADMIN_ID);
        assignment.setLatest(true);
        assignmentService.save(assignment);
        return AjaxResult.success("线索成功转为商机");
    }

}
