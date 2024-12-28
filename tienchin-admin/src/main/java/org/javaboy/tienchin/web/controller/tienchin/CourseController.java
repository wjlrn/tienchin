package org.javaboy.tienchin.web.controller.tienchin;

import org.javaboy.tienchin.activity.domain.vo.ActivityVO;
import org.javaboy.tienchin.channel.domain.Channel;
import org.javaboy.tienchin.channel.domain.vo.ChannelVO;
import org.javaboy.tienchin.common.annotation.Log;
import org.javaboy.tienchin.common.core.controller.BaseController;
import org.javaboy.tienchin.common.core.domain.AjaxResult;
import org.javaboy.tienchin.common.core.page.TableDataInfo;
import org.javaboy.tienchin.common.enums.BusinessType;
import org.javaboy.tienchin.common.utils.poi.ExcelUtil;
import org.javaboy.tienchin.course.domain.Course;
import org.javaboy.tienchin.course.domain.vo.CourseVO;
import org.javaboy.tienchin.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
@RestController
@RequestMapping("/tienchin/course")
public class CourseController extends BaseController {

    @Autowired
    ICourseService courseService;

    /**
     * 获取课程列表
     */
    @PreAuthorize("hasPermission('tienchin:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseVO courseVO) {
        startPage();
        List<Course> list = courseService.selectCourseList(courseVO);
        return getDataTable(list);
    }

    /**
     * 新增课程
     */
    @PreAuthorize("hasPermission('tienchin:course:create')")
    @Log(title = "渠道管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Course course) {
        return courseService.addCourse(course);
    }

    /**
     * 修改保存课程
     */
    @PreAuthorize("hasPermission('tienchin:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    /**
     * 根据课程 ID 查询一个具体的课程
     */
    @PreAuthorize("hasPermission('tienchin:course:edit')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable Long courseId) {
        return AjaxResult.success(courseService.getById(courseId));
    }

    /**
     * 删除课程
     */
    @PreAuthorize("hasPermission('tienchin:course:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds) {
        return toAjax(courseService.deleteCourseByIds(courseIds));
    }

    /**
     * 导出课程
     * @param response
     * @param courseVO
     */
    @Log(title = "课程管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasPermission('tienchin:course:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseVO courseVO) {
        List<Course> list = courseService.selectCourseList(courseVO);
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.exportExcel(response, list, "课程数据");
    }
}
