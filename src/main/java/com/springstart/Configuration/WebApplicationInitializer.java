package com.springstart.Configuration;

import com.springstart.Filters.CustomFilter;
import com.springstart.Filters.SecondCustomFilter;
import com.springstart.Listener.SessionListener;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{RootConfig.class};
  }

  protected Class<?>[] getServletConfigClasses() {
    return new Class[0];
  }

  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  protected Filter[] getServletFilters() {
    return new Filter[]{new CustomFilter(), new SecondCustomFilter()};
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
 /*   servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));*/
    servletContext.addListener( new HttpSessionEventPublisher());
  }
}
