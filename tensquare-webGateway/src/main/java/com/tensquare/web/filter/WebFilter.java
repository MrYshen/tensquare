package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {

    /**
     * 在啥时候执行 pre 之前 post
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 执行顺序，数字越小越前
     * @return
     */
    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     * 是否开启 true表示开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器执行的操作 return任何object的值都表示继续执行
     * setsendzuulRespponse(false)表示不再执行
     * @return
     */
    @Override
    public Object run() throws ZuulException {

        //得到request上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String header = request.getHeader("Authorization");
        if(header !=null && !"".equals(header)){
            requestContext.addZuulRequestHeader("Authorization",header);
        }
        System.out.println("进过后台过滤器了");
        return null;
    }
}
