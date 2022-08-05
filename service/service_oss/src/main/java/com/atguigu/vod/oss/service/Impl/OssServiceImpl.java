package com.atguigu.vod.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.vod.oss.service.OssService;
import com.atguigu.vod.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        //String fileHost = ConstantPropertiesUtil.FILE_HOST;

        String uploadUrl = null;






        try{
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            //获得实际名称
            String filename = file.getOriginalFilename();

            //在文件名称中添加一个唯一的随机的值，防止相同的文件名，文件被覆盖
            String uuid = UUID.randomUUID().toString().replace("-", "");

            filename = uuid+filename;

            //把文件按照日期分类
            //2020/11/14
            //获取当前日期
            String path = new DateTime().toString("yyyy/MM/dd");

            filename = path+"/"+filename;

            //获取上传文件流
            InputStream InputStream =file.getInputStream();

            ossClient.putObject(bucketName, filename, InputStream);

            ossClient.shutdown();

            //需要把路径拼接返回
            uploadUrl = "http://" + bucketName + "." + endPoint + "/" + filename;

            return uploadUrl;



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        //构建日期路径：avatar/2019/02/26/文件名
//        String filePath = new DateTime().toString("yyyy/MM/dd");
//
//        //文件名：uuid.扩展名
//        String original = file.getOriginalFilename();
//        String fileName = UUID.randomUUID().toString();
//        String fileType = original.substring(original.lastIndexOf("."));
//        String newName = fileName + fileType;
//        String fileUrl = fileHost + "/" + filePath + "/" + newName;
//
//
//
//
//
//




    }
}
