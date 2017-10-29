package com.springstart.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class GeneralInterceptor extends HandlerInterceptorAdapter {

  private final Logger logger = LoggerFactory.getLogger(GeneralInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.info("First Interceptor - preHandle");

/*    Map<String, String[]> parameterMap = request.getParameterMap();
    for (Map.Entry entry : parameterMap.entrySet()) {
      logger.info("Parameter: " + entry.getKey() + ", Value: " + entry.getValue().toString());
    }*/
/*    logger.info("General Interceptor - preHandle - session creation");
    request.getSession();*/




    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    super.afterCompletion(request, response, handler, ex);
    logger.info("First Interceptor - afterCompletion");
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    super.postHandle(request, response, handler, modelAndView);


    logger.info("First Interceptor - postHandle");



 /*   response.addCookie(new Cookie("MyCookie", "Cookie"));

    Map<String, String[]> parameterMap = request.getParameterMap();
    for (Map.Entry entry : parameterMap.entrySet()) {
      logger.info("Parameter: " + entry.getKey() + ", Value: " + entry.getValue().toString());
    }*/


  }
}
