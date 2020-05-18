package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.respository.GoodsRepository;
import com.leyou.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(SpuBo spuBo) {
        //SpuBo -> Goods
        Goods goods = new Goods();
        //属性复制
        BeanUtils.copyProperties(spuBo,goods);

        //拼接all(标题+分类)
        //根据spu的分类id查询分类名称
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
        String all = spuBo.getTitle() + " " + StringUtils.join(names," ");
        //华为 G9 Plus 32GB 手机 手机通讯 手机
        goods.setAll(all);

        //根据spu的id查询sku
        List<Sku> skus = goodsClient.querySkuBySpuId(spuBo.getId());

        List<Map<String,Object>> list = new ArrayList<>();
        List<Long> prices = new ArrayList<>();

        for(Sku sku : skus){
            //将sku的价格放入prices集合中
            prices.add(sku.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.isBlank(sku.getImages())?"":sku.getImages().split(",")[0]);
            list.add(map);
        }

        //list变成json字符串
        goods.setSkus(JsonUtils.serialize(list));
        goods.setPrice(prices);
        
        //specs
        Map<String,Object> specsMap = getSpecs(spuBo);
        goods.setSpecs(specsMap);
        return goods;
    }

    //根据spu查询规格参数
    private Map<String, Object> getSpecs(SpuBo spuBo) {
        Map<String,Object> specMap = new HashMap<>();
        //查询spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spuBo.getId());

        //查询规格参数
        List<SpecParam> specParams = specClient.querySpecParam(null, spuBo.getCid3(), true, null);

        //把通用的规格参数变成map结构
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });

        //把特用的规格参数变成map结构
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        for(SpecParam specParam : specParams){
            Long id = specParam.getId();
            String name = specParam.getName();
            Object value = null;
            if(specParam.getGeneric()){
                value = genericMap.get(id);
                //如果是数值类型，则进行分段 比如  5.17-6.15英寸
                if(null != value && specParam.getNumeric()){
                    //把数字变成一段一段的 比如  5.17-6.15英寸(有这么一个范围)
                    value = this.chooseSegment(value.toString(),specParam);
                }
                //特有规格参数
            }else{
                value = specialMap.get(id);
            }
            if(null == value){
                value = "其它";
            }
            specMap.put(name,value);
        }
        return specMap;
    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {//segment:1000-2000
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();//添加单位
                }
                break;
            }
        }
        return result;
    }

    public void createIndex(Long id) {
        // id->spu->spuBo->goods
        //根据id查询spu
        Spu spu = this.goodsClient.querySpuById(id);
        SpuBo spuBo = new SpuBo();
        //把spu属性值拷贝到spuBo
        BeanUtils.copyProperties(spu,spuBo);
        //把spuBo转化为spuBo
        Goods goods = buildGoods(spuBo);
        //保存到索引库
        goodsRepository.save(goods);


    }

    //删除索引
    public void delIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
