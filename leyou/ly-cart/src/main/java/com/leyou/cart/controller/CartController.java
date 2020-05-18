package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //http://api.leyou.com/api/cart
    @GetMapping
    public ResponseEntity<List<Cart>> queryCart(){
        List<Cart> cartList = this.cartService.queryCart();
        if(cartList!=null && cartList.size()!=0){
            return ResponseEntity.ok(cartList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //http://api.leyou.com/api/cart/increment
    @PutMapping("increment")
    public ResponseEntity<Void> updateIncrementCart(@RequestBody Cart cart){
        this.cartService.updateIncrementCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //http://api.leyou.com/api/cart/26816294479
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteGoodsFromCart(@PathVariable("skuId") Long skuId){
        this.cartService.deleteGoodsFromCart(skuId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
