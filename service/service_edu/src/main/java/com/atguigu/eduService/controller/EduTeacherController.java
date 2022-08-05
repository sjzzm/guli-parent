package com.atguigu.eduService.controller;


import com.atguigu.vod.commonutils.R;
import com.atguigu.eduService.entity.EduTeacher;
import com.atguigu.eduService.entity.vo.TeacherQuery;
import com.atguigu.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Zhiming
 * @since 2022-07-26
 */
@RestController
@EnableSwagger2
@CrossOrigin
@RequestMapping("/eduService/teacher")
public class EduTeacherController {

    //注入service
    //访问地址： http://localhost:8001/eduService/teacher/findAll
    @Autowired
    private EduTeacherService teacherService;


    //查询讲师表中的所有数据
    //rest风格
    @GetMapping("findAll")
    public R findAll(){

        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id){

        boolean b = teacherService.removeById(id);

        if(b){
            return R.ok();
        }else return R.error();

    }

    //分页查询讲师的方法
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit){

        Page<EduTeacher> page = new Page<>(1, 3);

        //所有数据都会被封装到page对象里面
        teacherService.page(page, null);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();


//        Map map = new HashMap<>();
//        map.put("total", total);
//        map.put("records", records);
//        return R.ok().data(map);


        return R.ok().data("total", total).data("rows", records);
    }


    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherQuery(@PathVariable long current,
                              @PathVariable long limit,
                              @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> page = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();

        //多条件查询
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();

        if(!StringUtils.isEmpty(name)){

            queryWrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(level)){

            queryWrapper.eq("level", level);
        }

        if(!StringUtils.isEmpty(begin)){

            queryWrapper.ge("gmt_create", begin);
        }

        if(!StringUtils.isEmpty(end)){

            queryWrapper.le("gmt_create", end);
        }



        queryWrapper.orderByDesc("gmt_create");

        //调用方法实现查询分页
        teacherService.page(page, queryWrapper);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total", total).data("rows", records);



    }

    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher teacher){
        boolean save = teacherService.save(teacher);

        if(save){

            return R.ok();
        }else return R.error();
    }

    //根据id查询讲师
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){

        EduTeacher byId = teacherService.getById(id);

        return R.ok().data("teacher", byId);
    }

    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher teacher){

        boolean b = teacherService.updateById(teacher);

        if(b){
            return R.ok();
        }else return R.error();
    }

}

