package com.leyou.auth.client;

import com.leyou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("user-service")
public interface UserClient {

    //http://user-service/query
    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password);

}
