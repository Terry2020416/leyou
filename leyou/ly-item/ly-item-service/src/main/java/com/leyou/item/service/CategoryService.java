package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    public List<Category> queryAllCategoryByPid(Long id) {
        Category category = new Category();
        category.setParentId(id);
        return categoryMapper.select(category);
    }

    public List<Category> queryByBrandId(Long bid) {
        //根据品牌id查询所有分类
        return categoryMapper.queryByBrandId(bid);
    }


    public List<String> queryNameByIds(List<Long> cids) {
        List<String> names = new ArrayList<>();
        //select * from tb_category where id in(cid1,cid2,cid3)
        List<Category> categories = this.categoryMapper.selectByIdList(cids);

        categories.forEach(id->{
            names.add(id.getName());
        });

        return names;
    }
}
