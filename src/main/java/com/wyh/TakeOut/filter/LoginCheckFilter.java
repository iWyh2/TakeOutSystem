package com.wyh.TakeOut.filter;

import com.alibaba.fastjson.JSON;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持匹配通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = request.getRequestURI();
        //定义无需处理的URLS
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login",//移动端登录
                "/doc.html",//Swagger生成的api文档放行路径
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        //判断本次请求是否需要处理
        boolean check = checkURL(urls, requestURI);

        //如果匹配上，那么就是无需处理的，直接放行
        if (check) {
            filterChain.doFilter(request,response);
            return;
        }

        //如果需要处理，先查看登录状态 登陆了就直接放行
        if (request.getSession().getAttribute("employee") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

        //看移动端登录状态
        if (request.getSession().getAttribute("user") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            return;
        }

        //未登录就返回未登录结果给前端页面，让她去登陆
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }


    public boolean checkURL(String[] urls, String URL) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,URL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
