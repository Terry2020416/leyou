package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired(required = false)
    private SpuMapper spuMapper;

    @Autowired(required = false)
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired(required = false)
    private BrandMapper brandMapper;

    @Autowired(required = false)
    private SkuMapper skuMapper;

    @Autowired(required = false)
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //开启分页助手
        PageHelper.startPage(page,rows);

        //创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNoneBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //上架/下架/全部
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //查询spu表
        //select * from tb_spu where title like "%key%" and saleable = true/false
        List<Spu> spus = this.spuMapper.selectByExample(example);

        //转换成分页助手的page对象
        Page<Spu> spuPage = (Page<Spu>)spus;

        //存放spubo
        List<SpuBo> spuBoList = new ArrayList<>();

        for(Spu spu : spus){

            SpuBo spuBo1 = new SpuBo();

            //属性copy 把spuBo属性复制到spuBo1里，
            BeanUtils.copyProperties(spu,spuBo1);

            //根据spu商品分类的id查询其名称
            List<String> namesList = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

            //根据 / 对每个name后面添加 /  形成 name1/name2/name3 格式(要求)
            String join = StringUtils.join(namesList,"/");

            spuBo1.setCname(join);//设置商品名称

            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

            spuBo1.setBname(brand.getName());//设置品牌名称


            spuBoList.add(spuBo1);
        };

        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),spuBoList);
    }

    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //商品保存 spu表
        //缺失了4个属性
        System.out.println("spuBo:" + spuBo);
        spuBo.setSaleable(true);
        spuBo.setValid(true);

        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());

        this.spuMapper.insertSelective(spuBo);

        //保存spuDetail,由于缺失spuId，所以要加入spuId
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());

        this.spuDetailMapper.insertSelective(spuDetail);

        //保存sku(库存表)
        List<Sku> skus = spuBo.getSkus();
        saveSkus(spuBo,skus);

        //发一条消息
        sendMsg("insert",spuBo.getId());
    }

    /**
     * 保存商品
     * @param spuBo
     * @param skus
     */
    private void saveSkus(SpuBo spuBo, List<Sku> skus) {
        //迭代sku集合保存
        skus.forEach(sku -> {
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            //保存sku
            this.skuMapper.insertSelective(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据detailId查询出商品详情
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 查询sku表的信息 通过spuId
     * @param spuId
     * @return
     */
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = this.skuMapper.select(sku);
        //根据在sku表中查到的每个商品skuId,再向库存表stock查询stock信息
        skuList.forEach(sku1 -> {
            sku1.setStock(this.stockMapper.selectByPrimaryKey(sku1.getId()).getStock());
        });
        return skuList;
    }

    /**
     * 修改商品
     * @param spuBo
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //spu,spuDetail表可以直接更新
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //修改的思路： 先删除当前spu表中对应的所有sku信息，再添加
        //先查询，在删除
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());

        //从数据库查询到skus
        List<Sku> skuList = this.skuMapper.select(sku);

        //根据skuList里面的skuId删除对应信息
        if(!CollectionUtils.isEmpty(skuList)){
            //如果skuList不为空
            List<Long> ids = skuList.stream().map(Sku::getId)
                    .collect(Collectors.toList());
            this.stockMapper.deleteByIdList(ids);
            this.skuMapper.delete(sku);
        }

        //删除完毕后进行添加
        saveSkus(spuBo,spuBo.getSkus());

        //发一条消息
        sendMsg("update",spuBo.getId());
    }
    public void sendMsg(String type,Long id){
        //item.update,id
        amqpTemplate.convertAndSend("item."+type,id);//指定RoutingKey

    }
    public Spu querySpuById(Long spuId) {
       return this.spuMapper.selectByPrimaryKey(spuId);
    }
}
