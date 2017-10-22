package com.springstart.Configuration;


import com.springstart.Interceptor.GeneralInterceptor;
import com.springstart.Interceptor.SecondInterceptor;
import com.springstart.Listener.SessionListener;
import com.springstart.Model.sessionbean.SessionBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(basePackages = {"com.springstart"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
@PropertySource(value = "classpath:application.properties")
@Import({SearchContext.class, SecurityConfig.class, PersistenceJPAConfig.class})
public class RootConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("/resources/**")
      .addResourceLocations("/resources/")
      .setCachePeriod(3600)
      .resourceChain(true)
      .addResolver(new PathResourceResolver());
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Bean
  public InternalResourceViewResolver jspViewResolver() {
    InternalResourceViewResolver bean = new InternalResourceViewResolver();
    bean.setPrefix("/");
    bean.setSuffix(".jsp");
    return bean;
  }

  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver getMultipartResolver() {
    return new CommonsMultipartResolver();
  }

  @Bean(name = "messageSource")
  public ReloadableResourceBundleMessageSource getMessageSource() {
    ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
    resource.setBasename("classpath:messages");
    resource.setDefaultEncoding("UTF-8");
    return resource;
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

  @Bean
  public SessionListener getSessionListener() {
    return new SessionListener();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);

    registry.addInterceptor(new GeneralInterceptor()).addPathPatterns("/");
    registry.addInterceptor(new SecondInterceptor()).addPathPatterns("/");
  }

  @Bean
  @Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
  public SessionBean sessionBean() {
    return new SessionBean();
  }


}
