package com.atguigu.eduService.service.impl;

import com.atguigu.eduService.entity.EduChapter;
import com.atguigu.eduService.entity.EduVideo;
import com.atguigu.eduService.entity.chapter.ChapterVo;
import com.atguigu.eduService.entity.chapter.VideoVo;
import com.atguigu.eduService.mapper.EduChapterMapper;
import com.atguigu.eduService.service.EduChapterService;
import com.atguigu.eduService.service.EduVideoService;
import com.atguigu.vod.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoBycourseId(String courseId) {
        //根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("course_id", courseId);

        List<EduChapter> eduChapterList = baseMapper.selectList(queryWrapper);


        //2.根据课程id查询课程里面的所有小节

        QueryWrapper<EduVideo> queryVideo = new QueryWrapper<>();

        queryVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(queryVideo);

        //创建list集合，用于最终封装数据

        ArrayList<ChapterVo> finalList = new ArrayList<>();

        //创建list集合，用于最终封装数据
        //遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {

            //每个章节
            EduChapter eduChapter = eduChapterList.get(i);

            //eduChapter的值复制到ChapterVO中去
            ChapterVo chapterVo = new ChapterVo();

            BeanUtils.copyProperties(eduChapter, chapterVo);

            //放到最终的返回list中去
            finalList.add(chapterVo);

            //用于封装章节的小节
            List<VideoVo> videoList = new ArrayList<>();

            for (int j = 0; j < eduVideoList.size(); j++) {

                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);

                //判断：小节里面的chapterId和章节里面的id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){


                    VideoVo videoVo = new VideoVo();

                    BeanUtils.copyProperties(eduVideo, videoVo);

                    videoList.add(videoVo);
                }

            }


            chapterVo.setChildren(videoList);


        }


        //4.遍历查询小节list集合，进行封装




        return finalList;
    }

    //删除章节的做法
    @Override
    public boolean deleteChapter(String chapterId) {

        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapterId);
        //List<EduVideo> list = eduVideoService.list(queryWrapper);
        int count = eduVideoService.count(queryWrapper);
        //判断一下count
        if(count>0){//能查询出来小节，不进行删除
            throw new GuliException(20001,"不能进行删除");

        }else {//不能查询出数据，不删除
            int result = baseMapper.deleteById(chapterId);

            return result>0;
        }

    }

    @Override
    public void removeChapterCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("course_id", courseId);

        baseMapper.delete(queryWrapper);
    }
}
