package it.unibo.msenabler.filters;

@Bean
public class FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(){

    FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean 
    = new FilterRegistrationBean<>();
      
  registrationBean.setFilter(new RequestResponseLoggingFilter());
  registrationBean.addUrlPatterns("/users/*");
  registrationBean.setOrder(2);
      
  return registrationBean; 
}
