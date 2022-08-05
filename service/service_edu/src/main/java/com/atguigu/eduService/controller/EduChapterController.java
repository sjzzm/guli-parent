package com.atguigu.eduService.controller;


import com.atguigu.vod.commonutils.R;
import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.chapter.ChapterVo;
import com.atguigu.eduService.service.EduChapterService;
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
@RequestMapping("/eduService/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;


    //根据课程id查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list= eduChapterService.getChapterVideoBycourseId(courseId);


        return R.ok().data("allChapterVideo", list);
    }


    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){

        eduChapterService.save(eduChapter);


        return R.ok();
    }

    //根据章节的id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);


        return R.ok().data("chapter", eduChapter);
    }

    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){

        boolean flag = eduChapterService.deleteChapter(chapterId);

        if(flag){
            return R.ok();
        }else
            return R.error();

    }





}

