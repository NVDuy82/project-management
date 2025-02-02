package com.example.reportservice.configuration;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        String authorization = null;
        if (servletRequestAttributes != null) {
            authorization = servletRequestAttributes.getRequest().getHeader("Authorization");
        }
        
        if (StringUtils.hasText(authorization))
            template.header("Authorization", authorization);
    }
}