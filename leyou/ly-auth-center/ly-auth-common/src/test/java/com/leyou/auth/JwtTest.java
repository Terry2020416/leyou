package com.leyou.auth;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    //创建tep/rsa目录
    private static final String pubKeyPath = "D:\\tmp\\rsa\\rsa.pub";
    private static final String priKeyPath = "D:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    //第一步：先产生公钥 私钥 然后注释掉该方法
//    @Test
//    public void testRsa() throws Exception {
//        RsaUtils.generateKey(pubKeyPath,priKeyPath,"1234");
//    }

    //在所有测试执行之前，先加载公钥和私钥
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        //生成token
        String token = JwtUtils.generateToken(new UserInfo(666L,"tom"),privateKey,5);
        System.out.println(token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6NjY2LCJ1c2VybmFtZSI6InRvbSIsImV4cCI6MTU4OTQ0MjMzNn0.be_mwoMn8SFVbuPjd1UfOkEft6LRA3a0uWKdA5MKEgp1o3Lzr6MX2FC9_Xd1-2w_I2mpdRPyiAXztnNtaE4ZnBwNvWSUSVD7HUxqGCMTGRcPjIqxgDwE4myzACjIjsBrXp81WBSpRqfRvN9aN1GW1viF5Zjij6ymN3x5tqcP3nw";
        //解密
        UserInfo userInfo = JwtUtils.getInfoFromToken(token,publicKey);
        System.out.println(userInfo.getId());
        System.out.println(userInfo.getUsername());

    }
}
