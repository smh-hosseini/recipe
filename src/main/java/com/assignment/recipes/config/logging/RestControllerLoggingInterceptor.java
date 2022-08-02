package com.assignment.recipes.config.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Slf4j
public class RestControllerLoggingInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        return true;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        final var stringBuilder = new StringBuilder();
        stringBuilder
            .append(request.getMethod())
            .append(" ")
            .append(request.getRequestURI())
            .append(getParameters(request));
        log.info("Start request: {}", stringBuilder);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {
        final var stringBuilder = new StringBuilder();
        stringBuilder
            .append(request.getMethod())
            .append(" ")
            .append(request.getRequestURI());
        log.info("End request: {}", stringBuilder);
    }

    private String getParameters(HttpServletRequest request) {
        final var queryParameters = new StringBuilder();
        final var parameterNames = request.getParameterNames();
        if (parameterNames != null && parameterNames.hasMoreElements()) {
            queryParameters.append("?");

            while (parameterNames.hasMoreElements()) {
                if (queryParameters.length() > 1) {
                    queryParameters.append("&");
                }
                final var parameterName = parameterNames.nextElement();
                queryParameters
                    .append(parameterName)
                    .append("=")
                    .append(request.getParameter(parameterName));
            }
        }
        return queryParameters.toString();
    }
}
