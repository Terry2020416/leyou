package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import com.leyou.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired(required = false)
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        try {
            //使用feign调用微服务接口，查询用户是否存在
            User user = this.userClient.queryUser(username, password);
            //如果不存在,则返回null
            if(user == null)
                return null;
            //存在:
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());
            //产生taken
            System.out.println("jwtProperties.getPrivateKey:"+jwtProperties.getPrivateKey());
            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getCookieMaxAge());
            return token;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
