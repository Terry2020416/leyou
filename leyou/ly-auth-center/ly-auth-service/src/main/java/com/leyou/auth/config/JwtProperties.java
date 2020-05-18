package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
//    secret: ly@Login(Auth}*^31)&hei% # 登录校验的密钥
//        pubKeyPath: D:/rsa/rsa.pub # 公钥地址
//        priKeyPath: D:/rsa/rsa.pri # 私钥地址
//        expire: 30 # token的过期时间,单位分钟
//        cookieName: LY_TOKEN
//        cookieMaxAge: 1800
    private String secret; //密钥
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;//token过期时间
    private String cookieName;
    private Integer cookieMaxAge;//cookie生命周期
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void init(){
        //生成公钥和私钥
         try {
             File pubKey = new File(pubKeyPath);
             File priKey = new File(priKeyPath);
             if(!pubKey.exists() || !priKey.exists()) {
                 RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
             }
             publicKey = RsaUtils.getPublicKey(pubKeyPath);
             privateKey = RsaUtils.getPrivateKey(priKeyPath);
         } catch (Exception e) {
             e.printStackTrace();
         }

    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
