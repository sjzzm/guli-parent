package com.atguigu.eduService.service;

import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoBycourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterCourseId(String courseId);
}
