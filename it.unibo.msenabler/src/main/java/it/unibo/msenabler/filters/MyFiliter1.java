package it.unibo.msenabler.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import it.unibo.enablerCleanArch.supports.ColorsOut;

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
//@Order(1)
public class MyFiliter1 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
 	 
	        HttpServletRequest req  = (HttpServletRequest) request;
	        HttpServletResponse res = (HttpServletResponse) response;
	        ColorsOut.out( "MyFiliter1 req: "+ req.getMethod() +  " " + req.getRequestURI(), ColorsOut.BgCyan );
	        chain.doFilter(request, response);
	        ColorsOut.out( "MyFiliter1 res: "+  res.getContentType()) ;
		
	}

}
