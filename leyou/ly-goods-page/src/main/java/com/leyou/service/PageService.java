package com.leyou.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;



    public Map<String, Object> loadData(Long spuId) {
    //        let spu = /*[[${spu}]]*/ {};
    //        let spuDetail = /*[[${spuDetail}]]*/ {};
    //        let skus = /*[[${skus}]]*/ {};
    //        let specParams = /*[[${specParams}]]*/ {};
    //        let specGroups = /*[[${specGroups}]]*/ {};
        HashMap<String, Object> map = new HashMap<>();
        //根据spu的id查询spu信息
        //System.out.println("spuId:"+spuId);
        Spu spu = this.goodsClient.querySpuById(spuId);
       // System.out.println("spu:"+spu);
        //放入map
        map.put("spu",spu);
        //根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        map.put("spuDetail",spuDetail);

        //根据spuId查询sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
        map.put("skus",skus);

        //根据spu的三级分类cid3查询特有的规格参数
        //System.out.println("specClient=null?"+specClient == null );
        //System.out.println("specClient?"+specClient.querySpecParam(null, spu.getCid3(), null, false)  );
        List<SpecParam> specParams = this.specClient.querySpecParam(null, spu.getCid3(), null, false);
        Map<Long,Object> spMap = new HashMap<>();
        for(SpecParam specParam : specParams){
            //前台只需要id和名字，所以把需要的字段放入到map里
            spMap.put(specParam.getId(),specParam.getName());
        }
        map.put("specParams",spMap);

        //根据spu的三级分类cid3查询规格组
        List<SpecGroup> specGroups = this.specClient.querySpecGroups(spu.getCid3());
        //System.out.println("specGroups:"+specGroups);
        map.put("specGroups",specGroups);

        return map;
    }
}
