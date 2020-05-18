package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,@PathVariable("type") Integer type){
        Boolean bool = this.userService.check(data,type);
        if(bool == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(bool);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    //@CrossOrigin(origins = "http://api.leyou.com")
//    @PostMapping("code")
//    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){
//        System.out.println("phone:" + phone);
//        Boolean bool = this.userService.sendVerifyCode(phone);
//        if(bool == null || !bool)
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();//500
//        return ResponseEntity.status(HttpStatus.CREATED).build();//201
//    }

    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(
            @RequestParam("phone")String phone){
        System.out.println("phone:" + phone);
        Boolean boo = this.userService.sendVerifyCode(phone);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //用户注册
    @PostMapping("register")
    public ResponseEntity<Void> createUser(User user,@RequestParam("code") String code){
        Boolean register = this.userService.register(user, code);
        if(register==null || !register)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户是否存在
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password){
        System.out.println("username="+username);
        System.out.println("password="+password);
        User user = this.userService.queryUser(username,password);
        if(user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

