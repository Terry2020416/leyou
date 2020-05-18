package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired(required = false)
    private BrandMapper brandMapper;


    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //select count(*) from tb_brand where name like "%"+iphone+"%"
        //select * from tb_brand where name like "%" + iphone + "%" order by id Asc limit 0,5

        //开启分页助手（拦截sql，进行拼接sortBy和desc/asc，并且发出count(*)）
        PageHelper.startPage(page,rows);

        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNoneBlank(key)){
            //创建查询条件的构造对象
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
            //name like "%"+iphone+"%"
        }
        //判断排序字段不为空
        if(StringUtils.isNoneBlank(sortBy)){
            //如果不为空，则排序
            example.setOrderByClause(sortBy+(desc? " DESC":" ASC"));//注意空格
            // order by sortBy(id) desc/asc
        }
        //查询结果并转化分页条件结果
        Page<Brand> brandPage = (Page<Brand>)brandMapper.selectByExample(example);
        PageResult pageResult = new PageResult(brandPage.getTotal(),new Long(brandPage.getPages()),brandPage.getResult());

        return pageResult;
    }

    @Transactional //事务(除了查询，都要添加)
    public void addBrand(Brand brand, List<Long> cids) {
        //新增品牌信息
        brandMapper.insertSelective(brand);//返回一个brand id
        for(Long cid : cids){
            //往品牌表和分类表的中间表插入数据
            brandMapper.insertBrandCategory(brand.getId(),cid);
        }
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        //更新brand表
        brandMapper.updateByPrimaryKey(brand);

        //更新中间表 tb_category_brand ,先删除，再添加
        brandMapper.deleteBrandCategory(brand.getId());

        cids.forEach(cid->{
            brandMapper.insertBrandCategory(brand.getId(),cid);
        });
    }


    public List<Brand> queryBrandByCategory(Long cid) {
        return this.brandMapper.queryBrandByCategory(cid);
    }

    public Brand queryBrandById(Long bid) {
        return this.brandMapper.selectByPrimaryKey(bid);
    }
}
