package com.leyou.cart.interceptors;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();
    //构造注入
    public LoginInterceptor(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            //通过request获取cookie
            String token = CookieUtils.getCookieValue(request,jwtProperties.getCookieName());
            if(null==token){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            //解密
            UserInfo userInfo = JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
            //放入本地线程
            threadLocal.set(userInfo);
            return true;
        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空
        threadLocal.remove();
    }

    public static UserInfo getLoginUserInfo(){
        return threadLocal.get();
    }
}
