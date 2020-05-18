package com.leyou.upload.controller;

import com.leyou.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
        String url = uploadService.uploadImage(file);
        if (StringUtils.isBlank(url))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        //正确 返回url路径
        return  ResponseEntity.ok(url);
    }
}
