package com.atguigu.eduService.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduService.entity.EduSubject;
import com.atguigu.eduService.entity.excel.SubjectData;
import com.atguigu.eduService.service.EduSubjectService;
import com.atguigu.vod.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectListener extends AnalysisEventListener<SubjectData> {

    //SubjectListener这个不交给Spring管理，所以在这儿不能注入其他的实体
    //不能实现数据库操作，
    //

    public EduSubjectService subjectService;

    public SubjectListener() {}
    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }


    //读取excel中的内容，一行一行地读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {

        if(subjectData == null){
            throw new GuliException(20001, "文件数据为空");
        }

        //每次读取两个值，第一个值是一级分类，第二个是二级分类
        //需要做判断，不相同的就加，相同的不加，不能重复添加
        EduSubject oneEduSubject = this.IsExitOneEduSubject(subjectService, subjectData.getOneSubjectName());

        if(oneEduSubject == null){
            oneEduSubject = new EduSubject();
            oneEduSubject.setParentId("0");
            oneEduSubject.setTitle(subjectData.getOneSubjectName());
            //没有相同的一级分类，可以添加
            subjectService.save(oneEduSubject);
        }

        //添加二级分类，判断二级分类是否重复
        String pid=oneEduSubject.getId();

        EduSubject secondEduSubject = this.IsExitSecondEduSubject(subjectService, subjectData.getTwoSubjectName(), pid);

        if(secondEduSubject == null){
            secondEduSubject = new EduSubject();
            secondEduSubject.setParentId(pid);
            secondEduSubject.setTitle(subjectData.getTwoSubjectName());
            //没有相同的一级分类，可以添加
            subjectService.save(secondEduSubject);
        }

    }

    private EduSubject IsExitOneEduSubject(EduSubjectService subjectService, String name){


        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");

        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    private EduSubject IsExitSecondEduSubject(EduSubjectService subjectService, String name, String pid){


        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);

        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
