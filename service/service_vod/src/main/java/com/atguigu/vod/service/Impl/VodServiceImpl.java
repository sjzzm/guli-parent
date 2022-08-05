package com.atguigu.vod.service.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.vod.Utils.ConstantVodUtils;
import com.atguigu.vod.Utils.InitVodCilent;
import com.atguigu.vod.commonutils.R;
import com.atguigu.vod.exceptionHandler.GuliException;
import com.atguigu.vod.service.VodService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideoAly(MultipartFile file) {
        return null;
    }

    @Override
    public void removeMoreAlyVideo(List videoList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id



            String videolist = StringUtils.join(videoList.toArray(), ",");
            request.setVideoIds(videolist);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);

        }catch(Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        list.add("11");
        list.add("22");
        list.add("33");
        list.add("44");

        String join = StringUtils.join(list.toArray(), ",");
        System.out.println(join);

    }
}
