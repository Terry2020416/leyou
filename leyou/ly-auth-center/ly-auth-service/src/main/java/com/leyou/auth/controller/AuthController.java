package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import com.leyou.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired(required = false)
    private JwtProperties jwtProperties;

    /**
     * 生成token，token经过cookie来返回
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        //登陆并生成token
        String token = this.authService.accredit(username,password);
        if(StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();//401
        }
        //设置cookie，存到用户浏览器里
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(),null,true);
        //如果生成了token则使用cookie来保存token
        return ResponseEntity.ok().build();
    }

    /**
     * 对token进行解密，返回给客户端，展示 欢迎 xx 登陆
     * @param token
     * @param request
     * @param response
     * @return
     */
    //http://api.leyou.com/api/auth/verify
    @GetMapping("verify")
    public ResponseEntity<UserInfo> queryUser(@CookieValue("LY_TOKEN") String token,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        //对token进行解密
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //再次产生token，写入cookie，因为cookie生命周期设置为30分钟
            String token1 = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getCookieMaxAge());
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token1,jwtProperties.getCookieMaxAge(),null,true);
            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();//401
    }
}
