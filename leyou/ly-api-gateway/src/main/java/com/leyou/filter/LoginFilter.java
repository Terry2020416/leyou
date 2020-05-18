package com.leyou.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.leyou.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired(required = false)
    private JwtProperties jwtProperties;

    @Autowired(required = false)
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //请求头之前，查看请求参数
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        //在白名单内的无需拦截
        //获取当前请求上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //api/auth/accredit
        String requestURI = request.getRequestURI();

        //从FilterProperties中获取白名单
        List<String> allowPaths = filterProperties.getAllowPaths();
        System.out.println(allowPaths);
        for (String allow : allowPaths){
            if(requestURI.contains(allow)){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //从请求中获取cookie，然后进行解密
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        try {
            //从request中获取cookie
            String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
            //解密
            JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            //说明没有登陆，不给予通过
            requestContext.setSendZuulResponse(false);
            //返回401响应码
            requestContext.setResponseStatusCode(401);
        }
        return null;
    }
}
