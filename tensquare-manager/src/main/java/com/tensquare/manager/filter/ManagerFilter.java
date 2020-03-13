package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

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
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String header = request.getHeader("Authorization");

        if(request.getMethod().equals("OPTIONS")){
            return null;
        }

        if (request.getRequestURL().indexOf("login")>0){
            return null;
        }

        if(header!=null && !"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles!= null && roles.equals("admin")){
                        currentContext.addZuulRequestHeader("Authorization",token);
                        return null;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    currentContext.setSendZuulResponse(false);
                }
            }
        }
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
