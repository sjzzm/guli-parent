package com.atguigu.eduService.mapper;

import com.atguigu.eduService.entity.EduCourse;
import com.atguigu.eduService.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {


    public CoursePublishVo getPublishCourseInfo(String courseId);


}
