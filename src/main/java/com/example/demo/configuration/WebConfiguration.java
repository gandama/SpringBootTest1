package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Administrator on 2018/10/26.
 */
@Configuration
public class WebConfiguration {

//    @Bean
//    public WorldFilter getWorldFilter(){
//        return new WorldFilter();
//    }
//
//    @Bean
//    public FilterRegistrationBean testFilterRegistrationBean() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new HelloFilter());
//        registration.addUrlPatterns("/*");
//        registration.setOrder(3);
//        return registration;
//    }


    public class HelloFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String requestURI = request.getRequestURI();
            System.out.println("hello : " + requestURI);
            filterChain.doFilter(request, servletResponse);
        }

        @Override
        public void destroy() {

        }
    }


    public class WorldFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String requestURI = request.getRequestURI();
            System.out.println("world : " + requestURI);
            filterChain.doFilter(request, servletResponse);
        }

        @Override
        public void destroy() {

        }
    }
}
