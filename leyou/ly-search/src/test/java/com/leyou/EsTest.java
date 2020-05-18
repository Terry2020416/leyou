package com.leyou;

import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.respository.GoodsRepository;
import com.leyou.search.service.IndexService;
import com.leyou.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class EsTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        int page = 1;

        while(true){
            //不停地查询数据库，然后把数据导入索引库里
            //利用feign调用各种微服务，它发的就是http请求
            PageResult<SpuBo> pageResult = this.goodsClient.querySpuByPage(page, 50, null, null);

            //如果没有查询到数据
            if(null == pageResult)
                break;
            page++;
            //获取spuBo
            List<SpuBo> items = pageResult.getItems();

            List<Goods> goodsList = new ArrayList<>();
            for(SpuBo spuBo : items){
                Goods goods = indexService.buildGoods(spuBo);
                goodsList.add(goods);
            }
            //保存到索引库
            this.goodsRepository.saveAll(goodsList);
        }
    }
}
