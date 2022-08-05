package com.atguigu.eduService.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduService.entity.EduSubject;
import com.atguigu.eduService.entity.excel.SubjectData;
import com.atguigu.eduService.entity.subject.OneSubject;
import com.atguigu.eduService.entity.subject.TwoSubject;
import com.atguigu.eduService.listener.SubjectListener;
import com.atguigu.eduService.mapper.EduSubjectMapper;
import com.atguigu.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {



    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {


        try{

            InputStream inputStream=file.getInputStream();

            EasyExcel.read(inputStream, SubjectData.class, new SubjectListener(subjectService)).sheet().doRead();

        }catch (Exception e){

            e.printStackTrace();

        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //先查询所有的一级分类
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("parent_id", "0");

        List<EduSubject> OneSubjects = baseMapper.selectList(queryWrapper);


        //然后再把所有的二级分类
        QueryWrapper<EduSubject> queryWrapper2 = new QueryWrapper<>();

        queryWrapper2.ne("parent_id", "0");

        List<EduSubject> TwoSubjects = baseMapper.selectList(queryWrapper2);


        //封装一级分类
        List<OneSubject> finalList = new ArrayList<>();
        //查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每一个一级分类对象值；

        for (int i = 0; i < OneSubjects.size(); i++) {
            EduSubject eduSubject = OneSubjects.get(i);

            //把这对象里面的值获取出来，放到
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());

            //这是简便方法，直接复制对象  copyProperties
            BeanUtils.copyProperties(eduSubject, oneSubject);

            finalList.add(oneSubject);

            //查询二级分类，封装二级分类放到一级分类中

            ArrayList<TwoSubject> twoSubjectFinal = new ArrayList<>();

            for (int j = 0; j < TwoSubjects.size(); j++) {
                EduSubject Subject1 = TwoSubjects.get(j);

                if(Subject1.getParentId().equals(eduSubject.getId()) ){

                    TwoSubject twoSubject = new TwoSubject();

                    BeanUtils.copyProperties(Subject1, twoSubject);

                    twoSubjectFinal.add(twoSubject);


                }
            }

            oneSubject.setChildren(twoSubjectFinal);

        }



        //封装二级分类



        return finalList;
    }
}
