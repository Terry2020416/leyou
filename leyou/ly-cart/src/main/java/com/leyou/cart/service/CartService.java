package com.leyou.cart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.interceptors.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String KEY_PREFIX = "ly:cart:uid:";
    public void addCart(Cart cart) {
        try {
            UserInfo userInfo = LoginInterceptor.getLoginUserInfo();
            System.out.println(userInfo.getId());
            System.out.println(userInfo.getUsername());
            //存入redis
            BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
            //根据skuId获取在redis中保存的sku信息
            Object skuObj = boundHashOps.get(cart.getSkuId().toString());
            if(skuObj != null){
                //说明redis中已经存储了相关信息
                //把skuObj转化成Cart对象
                Cart cart1 = JsonUtils.nativeRead(skuObj.toString(), new TypeReference<Cart>() {
                });
                cart1.setNum(cart.getNum()+cart1.getNum());
                //JsonUtils.serialize(cart) 把cart对象转化成json格式字符串存到redis中
            }else{
                //说明是第一次添加
                boundHashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Cart> queryCart() {
        try {
            UserInfo userInfo = LoginInterceptor.getLoginUserInfo();
            BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
            List<Object> skusObj = boundHashOps.values();
            List<Cart> cartList = new ArrayList<>();
            if(skusObj != null){
                for(Object o : skusObj){
                    cartList.add(JsonUtils.nativeRead(o.toString(), new TypeReference<Cart>() {
                    }));
                }
            }
            return cartList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateIncrementCart(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getLoginUserInfo();
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String sku = boundHashOps.get(cart.getSkuId().toString()).toString();

        Cart cart1 = JsonUtils.nativeRead(sku, new TypeReference<Cart>() {
        });
        cart1.setNum(cart1.getNum()+1);

        //修改完后保存到redis中
        boundHashOps.put(cart1.getSkuId().toString(),JsonUtils.serialize(cart1));
    }


    public void deleteGoodsFromCart(Long skuId) {
        UserInfo userInfo = LoginInterceptor.getLoginUserInfo();
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        boundHashOps.delete(skuId+"");
    }
}
