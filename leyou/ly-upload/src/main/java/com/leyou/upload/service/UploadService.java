package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;//用来操作fastDFS图片服务器

    public String uploadImage(MultipartFile file) {
        String url = null;
//        //保存图片
//        try {
//            //保存图片
//            /**
//             * 1、生成保存目录
//             */
//            File dir = new File("D:\\workspace1\\upload");
//            if(!dir.exists()){
//                dir.mkdir();
//            }
//            file.transferTo(new File(dir,file.getOriginalFilename()));
//
//            //拼接图片地址
//            //- 图片不能保存在服务器内部，这样会对服务器产生额外的加载负担
//            //- 一般静态资源都应该使用独立域名，这样访问静态资源时不会携带一些不必要的cookie，减小请求的数据量
//            url = "http://image.leyou.com/"+file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //比如 a.png 截取后：png(后缀)
        String ext = StringUtils.substringAfterLast(originalFilename,".");
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),ext,null);
            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
