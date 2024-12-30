package org.javaboy.tienchin.web.controller.tienchin.clue;

import io.swagger.models.auth.In;
import org.aspectj.weaver.loadtime.Aj;
import org.javaboy.tienchin.activity.service.IActivityService;
import org.javaboy.tienchin.channel.service.IChannelService;
import org.javaboy.tienchin.clue.domain.Clue;
import org.javaboy.tienchin.clue.domain.vo.ClueDetails;
import org.javaboy.tienchin.clue.domain.vo.ClueSummary;
import org.javaboy.tienchin.clue.domain.vo.ClueVO;
import org.javaboy.tienchin.clue.service.IClueService;
import org.javaboy.tienchin.common.annotation.Log;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.page.TableDataInfo;
import org.javaboy.tienchin.common.enums.BusinessType;
import org.javaboy.tienchin.common.validator.CreateGroup;
import org.javaboy.tienchin.common.validator.EditGroup;
import org.javaboy.tienchin.course.domain.Course;
import org.javaboy.tienchin.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 线索表 前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@RestController
@RequestMapping("/tienchin/clue")
public class ClueController extends BaseController {
    @Autowired
    private IClueService clueService;

    @Autowired
    private IChannelService channelService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 新增线索
     */
    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @Log(title = "线索管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated(CreateGroup.class) @RequestBody Clue clue) {
        return clueService.addClue(clue);
    }

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/channels")
    public AjaxResult getAllChannels() {
        return AjaxResult.success(channelService.list());
    }

    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/activity/{channelId}")
    public AjaxResult getAtivityByChannelId(@PathVariable("channelId") Integer channelId) {
        return activityService.getActivityByChannelId(channelId);
    }

    /**
     * 获取线索列表
     */
    @PreAuthorize("hasPermission('tienchin:clue:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClueVO clueVO) {
        startPage();
        List<ClueSummary> list = clueService.selectClueList(clueVO);
        return getDataTable(list);
    }

    @GetMapping("/users/{deptId}")
    @PreAuthorize("hasPermission('tienchin:clue:assignment')")
    public AjaxResult getUsersByDeptId(@PathVariable Long deptId){
        return sysUserService.getUsersByDeptId(deptId);
    }

    @GetMapping("/{clueId}")
    @PreAuthorize("hasAnyPermissions('tienchin:clue:view', 'tienchin:clue:follow')")
    public AjaxResult getClueDetailsByClueId(@PathVariable Integer clueId){
        return clueService.getClueDetailsByClueId(clueId);
    }

    @PreAuthorize("hasPermission('tienchin:clue:follow')")
    @PostMapping("/follow")
    public AjaxResult clueFollow(@RequestBody ClueDetails clueDetails) {
        return clueService.clueFollow(clueDetails);
    }

    @PreAuthorize("hasPermission('tienchin:clue:follow')")
    @PostMapping("/invalid")
    public AjaxResult invalidcClueFollow(@RequestBody ClueDetails clueDetails) {
        return clueService.invalidcClueFollow(clueDetails);
    }

    @GetMapping("/summary/{clueId}")
    @PreAuthorize("hasPermission('tienchin:clue:edit')")
    public AjaxResult getClueSummaryByClueId(@PathVariable Integer clueId){
        return clueService.getClueSummaryByClueId(clueId);
    }

    /**
     * 修改保存线索
     */
    @PreAuthorize("hasPermission('tienchin:clue:edit')")
    @PutMapping
    public AjaxResult updateClue(@Validated(EditGroup.class) @RequestBody Clue clue) {
        return clueService.updateClue(clue);
    }

    /**
     * 删除线索
     */
    @PreAuthorize("hasPermission('tienchin:clue:remove')")
    @DeleteMapping("/{clueIds}")
    public AjaxResult deleteClueById(@PathVariable Long[] clueIds) {
        return clueService.deleteClueById(clueIds);
    }
}
