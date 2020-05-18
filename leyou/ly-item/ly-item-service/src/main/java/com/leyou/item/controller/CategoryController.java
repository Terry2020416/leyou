package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询类目表
    // http://api.leyou.com/api/item/category/list?pid=0
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryAllCategoryByPid(@RequestParam("pid") Long id){
        List<Category> categoryList = categoryService.queryAllCategoryByPid(id);
        System.out.println("categoryList:" + categoryList);
        if(null != categoryList && categoryList.size() != 0){
            return ResponseEntity.ok(categoryList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //http://api.leyou.com/api/item/category/bid/2032
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){
        List<Category> categoryList = categoryService.queryByBrandId(bid);

        if(categoryList != null && categoryList.size() != 0){
            return ResponseEntity.ok(categoryList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //http://localhost:9081/category/names?id=1,2,3
    @GetMapping("names")
    public ResponseEntity<List<String>>  queryNamesByIds(@RequestParam("ids") List<Long> ids){
        //根据分类id查询分类名称
        List<String> list = this.categoryService.queryNameByIds(ids);

        if( null != list && list.size() !=0 )
            return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
