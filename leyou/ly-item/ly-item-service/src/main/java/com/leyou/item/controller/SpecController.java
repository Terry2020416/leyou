package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecService specService;

    @RequestMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        System.out.println("查询... cid为：" + cid);
        List<SpecGroup> specGroups = specService.querySpecGroups(cid);
       // System.out.println("specGroups:" + specGroups);
        if(specGroups!=null && specGroups.size() > 0){
            return ResponseEntity.ok(specGroups);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //根据组名称查询当前组下的信息
    //http://api.leyou.com/api/item/spec/params?gid=2
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                                          @RequestParam(value = "cid",required = false) Long cid,
                                                          @RequestParam(value = "searching",required = false) Boolean searching,
                                                          @RequestParam(value = "generic",required = false) Boolean generic){


        //根据规格组id查询规格参数
        List<SpecParam> SpecParam = specService.querySpecParam(gid,cid,searching,generic);

        if(SpecParam!=null && SpecParam.size() > 0){
            return ResponseEntity.ok(SpecParam);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //添加分组
    //http://api.leyou.com/api/item/spec/group
    @PostMapping("group")
    public ResponseEntity<Void> insertSpecGroup(@RequestBody SpecGroup specGroup){
        System.out.println(specGroup);

        if(specGroup!=null)
            specService.insertSpecGroup(specGroup);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //修改分组
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup){
        System.out.println("修改前："+specGroup);
        if(specGroup != null)
            specService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //删除分组
    //http://api.leyou.com/api/item/spec/group/28
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id){
        System.out.println("要删除的分类id为：" + id);
        specService.deleteSpecGroup(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //在xx组下添加信息
    //http://api.leyou.com/api/item/spec/param
    @PostMapping("param")
    public ResponseEntity<Void> insertSpecParam(@RequestBody SpecParam specParam){
        System.out.println("准备添加的详细参数为："+specParam);
        if(specParam != null)
            this.specService.insertSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //修改详细参数
    //http://api.leyou.com/api/item/spec/param
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam){
        System.out.println("修改前："+specParam);
        if(specParam != null)
            specService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //删除详细参数
    //http://api.leyou.com/api/item/spec/param/33
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id") Long id){
        System.out.println("要删除的详细参数id为：" + id);
        specService.deleteSpecParam(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

