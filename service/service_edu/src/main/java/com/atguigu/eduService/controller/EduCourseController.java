package com.atguigu.eduService.controller;


import com.atguigu.vod.commonutils.R;
import com.atguigu.eduService.entity.EduCourse;
import com.atguigu.eduService.entity.vo.CourseInfoVo;
import com.atguigu.eduService.entity.vo.CoursePublishVo;
import com.atguigu.eduService.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
@RestController
@RequestMapping("/eduService/edu-course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseServicel;

    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseServicel.list(null);
        return R.ok().data("list",list);

    }


    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfo){

        String courseId = courseServicel.saveCourseInfo(courseInfo);


        return R.ok().data("courseId", courseId);
    }

    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){

        CourseInfoVo courseInfoVo = courseServicel.getCourseInfo(courseId);


        return R.ok().data("courseInfoVo", courseInfoVo);
    }


    //修改课程信息

    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseServicel.updateCourseInfo(courseInfoVo);


        return R.ok();
    }

    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseServicel.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseServicel.updateById(eduCourse);
        return R.ok();
    }

    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){

        courseServicel.removeCourse(courseId);

        return R.ok();
    }


}

