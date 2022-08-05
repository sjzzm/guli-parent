package com.atguigu.eduService.controller;


import com.atguigu.vod.commonutils.R;
import com.atguigu.eduService.entity.subject.OneSubject;
import com.atguigu.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
@RestController
@RequestMapping("/eduService/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传过来的excel
        subjectService.saveSubject(file, subjectService);

        return R.ok();
    }

    //课程分类列表
    @GetMapping("getAllSubject")
    public R getAllsubject(){

       List<OneSubject> list = subjectService.getAllOneTwoSubject();

        return R.ok().data("list", list);
    }

}

