package com.leyou.search.client;

import com.leyou.item.pojo.Brand;
import com.leyou.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "item-service")
public interface BrandClient {

    @GetMapping("brand/page")
    public ResponseEntity<PageResult<Brand>> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                                       @RequestParam(value = "sortBy",required = false) String sortBy,
                                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                                       @RequestParam(value = "key",required = false) String key);

        /**
         * 根据分类信息查询品牌
         * @param cid
         * @return
         */
    @GetMapping("brand/cid/{cid}")
    public List<Brand> queryBrandByCategory(@PathVariable("cid") Long cid);


    //根据品牌id查询品牌名称
    @GetMapping("brand/bid/{bid}")
    public Brand queryBrandById(@PathVariable("bid") Long bid);

}
