package com.leyou.item.controller;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.utils.PageResult;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //http://api.leyou.com/api/item/spu/page?key=&saleable=true&page=1&rows=5
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                                @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                @RequestParam(value = "key",required = false) String key){
        PageResult<SpuBo> pageResult = goodsService.querySpuByPage(page,rows,saleable,key);
        System.out.println("pageResult前："+pageResult);
        if(null != pageResult && pageResult.getItems()!=null && pageResult.getItems().size() != 0){
            System.out.println("pageResult后："+pageResult);
            return  ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //http://api.leyou.com/api/item/goods
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    //http://api.leyou.com/api/item/spu/detail/195
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if(spuDetail!=null)
            return ResponseEntity.ok(spuDetail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //http://api.leyou.com/api/item/sku/list?id=195
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId){
        List<Sku> skuList = this.goodsService.querySkuBySpuId(spuId);
        if(skuList != null && skuList.size() != 0)
            return ResponseEntity.ok(skuList);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 修改商品信息
     * @param spuBo
     * @return
     */
    //http://api.leyou.com/api/item/goods
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //根据商品的spu的id查询spu
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){
        Spu spu = this.goodsService.querySpuById(spuId);
        if(spu != null)
            return ResponseEntity.ok(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
