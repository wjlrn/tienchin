package org.javaboy.tienchin.web.controller.tienchin.clue;

import org.javaboy.tienchin.clue.domain.Assignment;
import org.javaboy.tienchin.clue.service.IAssignmentService;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 线索分配表 前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@RestController
@RequestMapping("/tienchin/assignment")
public class AssignmentController extends BaseController {
    @Autowired
    IAssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasPermission('tienchin:clue:assignment')")
    public AjaxResult assignClue(@Validated @RequestBody Assignment assignment){
        return assignmentService.assignClue(assignment);
    }
}
