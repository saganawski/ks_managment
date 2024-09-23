package com.ks.management.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // return true if the request is for the health-check endpoint
        if(request.getRequestURI().equals("/health-check")){
            return true;
        }
        final String username = request.getUserPrincipal().getName();
        final long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        logger.info("Request from User: {} to path: {}", username, request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // return if the request is for the health-check endpoint
        if(request.getRequestURI().equals("/health-check")){
            return;
        }

        final long startTime = (long) request.getAttribute("startTime");
        final long endTime = System.currentTimeMillis();

        final long duration = endTime - startTime;
        if(duration > 1000){
            logger.warn("Request to path: {}, took an excessive duration of: {} ", request.getRequestURI(),  duration);
        }else {
            logger.info("Request to path: {}, duration: {} ", request.getRequestURI(), duration);
        }
    }
}
