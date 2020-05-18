package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand (brand_id,category_id) values(#{id},#{cid})")
    //id 品牌id cid 分类id
    void insertBrandCategory(Long id,Long cid);

    @Delete("delete from tb_category_brand where brand_id = #{id}")
    void deleteBrandCategory(Long id);

    @Select("select b.* from tb_brand b left join tb_category_brand c " +
            "on b.id = c.brand_id where c.category_id = #{cid}")
    List<Brand> queryBrandByCategory(@Param("cid") Long cid);
}
