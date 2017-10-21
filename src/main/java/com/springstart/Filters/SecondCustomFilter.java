package com.springstart.Filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class SecondCustomFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(SecondCustomFilter.class);


  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    logger.info("Second custom filter - doFilter - FIRST Invocation");
    filterChain.doFilter(servletRequest,servletResponse);
    logger.info("Second custom - doFilter - SECOND Invocation");
  }

  public void destroy() {

  }
}
