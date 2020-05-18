package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {

    @Select("select * from tb_category tc left join tb_category_brand cb on tc.id = cb.category_id where cb.brand_id = #{bid}")
    List<Category> queryByBrandId(Long bid);
}
