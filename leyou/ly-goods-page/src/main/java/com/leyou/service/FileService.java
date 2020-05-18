package com.leyou.service;

import com.leyou.page.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Service
public class FileService {

    @Autowired
    private PageService pageService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ly.thymeleaf.destPath}")
    private String destPath;//D:/nginx-1.14.2/html/item

    /**
     * 判断某个商品是否存在
     * @param spuId
     * @return
     */
    public boolean exist(Long spuId){
        File dest = new File(this.destPath);
        if(!dest.exists()){
            dest.mkdirs();
        }
        File file = new File(dest,spuId+".html");
        boolean flag = file.exists();
        return flag;
    }

    /**
     * 异步创建html页面
     * @param spuId
     */
    public void syncCreateHtml(Long spuId){
        ThreadUtils.execute(() -> {
            try {
                createHtml(spuId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void createHtml(Long spuId) throws FileNotFoundException {
        //创建全局上下文对象
        Context context = new Context();
        context.setVariables(pageService.loadData(spuId));//把数据放入到上下文对象

        File file = new File(destPath,spuId+".html");//113.html
        try {
            // 准备输出流
            PrintWriter printWriter = new PrintWriter(file, "UTF-8");
            templateEngine.process("item",context,printWriter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //删除html
    public void deleteHtml(Long id) {
        File file = new File(destPath, id + ".html");
        file.deleteOnExit();
    }
}
