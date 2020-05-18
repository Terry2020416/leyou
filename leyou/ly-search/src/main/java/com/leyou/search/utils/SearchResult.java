package com.leyou.search.utils;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.search.pojo.Goods;
import com.leyou.utils.PageResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;//商品分类
    private List<Brand> brands;//商品品牌

    private List<Map<String,Object>> specs;//规格参数过滤

    public SearchResult(Long total, long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
