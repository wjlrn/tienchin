package org.javaboy.tienchin.clue.service;

import org.javaboy.tienchin.clue.domain.Assignment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.javaboy.tienchin.common.core.domain.AjaxResult;

/**
 * <p>
 * 线索分配表 服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
public interface IAssignmentService extends IService<Assignment> {

    AjaxResult assignClue(Assignment assignment);
}
