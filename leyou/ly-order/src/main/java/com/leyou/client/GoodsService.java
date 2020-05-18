package com.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "api-gateway", path = "/api/item")
public interface GoodsService {
}
