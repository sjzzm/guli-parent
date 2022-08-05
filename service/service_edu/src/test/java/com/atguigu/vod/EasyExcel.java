package com.atguigu.vod;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EasyExcel {

    @ExcelProperty("学生编号")
    private Integer sno;

    @ExcelProperty("学生姓名")
    private String name;
}
