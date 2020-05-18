package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")

public class BrandController {

    @Autowired
    private BrandService brandService;

    //http://api.leyou.com/api/item/brand/page?page=1&rows=5&sortBy=id&desc=false&key=
    //?后面叫 请求参数 RequestParam ？前面叫路径参数PathVariable

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                                       @RequestParam(value = "sortBy",required = false) String sortBy,
                                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                                       @RequestParam(value = "key",required = false) String key){

        PageResult<Brand> pageResult = brandService.pageQuery(page,rows,sortBy,desc,key);

        if(pageResult != null && pageResult.getItems() != null && pageResult.getItems().size() != 0){
            return ResponseEntity.ok(pageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 添加品牌
     * @param brand
     * @param cids
     * @return
     * http://api.leyou.com/api/item/brand
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.addBrand(brand,cids);

        return ResponseEntity.status(HttpStatus.CREATED).build();//201
    }

    //http://api.leyou.com/api/item/brand

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();//201

    }

    //http://api.leyou.com/api/item/brand/cid/76
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid){
        //根据商品分类的id查询该分类下所有的品牌
        List<Brand> brandList = this.brandService.queryBrandByCategory(cid);
        if(brandList!=null && brandList.size()!=0){
            return ResponseEntity.ok(brandList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //根据品牌id查询品牌名称
    @GetMapping("bid/{bid}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("bid") Long bid){
        Brand brand = this.brandService.queryBrandById(bid);
        if(brand != null)
            return ResponseEntity.ok(brand);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
