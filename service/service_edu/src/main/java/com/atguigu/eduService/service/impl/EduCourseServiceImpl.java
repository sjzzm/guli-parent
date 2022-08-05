package com.atguigu.eduService.service.impl;

import com.atguigu.eduService.entity.EduCourse;
import com.atguigu.eduService.entity.EduCourseDescription;
import com.atguigu.eduService.entity.vo.CourseInfoVo;
import com.atguigu.eduService.entity.vo.CoursePublishVo;
import com.atguigu.eduService.mapper.EduCourseMapper;
import com.atguigu.eduService.service.EduChapterService;
import com.atguigu.eduService.service.EduCourseDescriptionService;
import com.atguigu.eduService.service.EduCourseService;
import com.atguigu.eduService.service.EduVideoService;
import com.atguigu.vod.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfo) {
        //1.向课程表添加基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo, eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if(insert <= 0){

            throw new GuliException(20001, "添加课程信息失败");
        }

        String cid = eduCourse.getId();

        //2.向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfo.getDescription());
        eduCourseDescription.setId(cid);
        courseDescriptionService.save(eduCourseDescription);


        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.先查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);

        CourseInfoVo courseInfoVo= new CourseInfoVo();

        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2.查询描述表
        EduCourseDescription description = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(description.getDescription());
        return courseInfoVo;

    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);

        int i = baseMapper.updateById(eduCourse);

        if(i==0){

            throw new GuliException(20001, "修改课程信息失败");

        }
        EduCourseDescription description = new EduCourseDescription();

        description.setId(courseInfoVo.getId());

        description.setDescription((courseInfoVo.getDescription()));

        courseDescriptionService.updateById(description);


    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);



        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {

        //1.根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2.根据课程id删除章节
        chapterService.removeChapterCourseId(courseId);

        //3.根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4.根据课程id删除课程本身
        int i = baseMapper.deleteById(courseId);

        if(i == 0){
            throw new GuliException(20001,"删除失败");
        }

    }

}
