package it.unibo.msenabler.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import it.unibo.enablerCleanArch.supports.Colors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//@Component
//@Order(2)
public class MyFiliter2 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
 	 
	        HttpServletRequest req  = (HttpServletRequest) request;
	        HttpServletResponse res = (HttpServletResponse) response;
	        Colors.out( "MyFiliter2 req:"+ req.getMethod() +  " " + req.getRequestURI(), Colors.BgYellow );
	        chain.doFilter(request, response);
	        Colors.out( "MyFiliter2 res:"+  res.getContentType()) ;
		
	}

}
