package it.unibo.msenabler.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import it.unibo.enablerCleanArch.supports.ColorsOut;


//@Component
public class MyFilterNaive extends HttpFilter{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647421098823734906L;

	
	@Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
		ColorsOut.out(getFilterName() + " " + request.getAuthType(), ColorsOut.BgGreen);
		//UsernamePasswordToken token = extractUsernameAndPasswordFrom(request);  // (1)
		chain.doFilter(request, response); // (4)
	}
/*         
	private UsernamePasswordToken extractUsernameAndPasswordFrom(HttpServletRequest request) {
        // Either try and read in a Basic Auth HTTP Header, which comes in the form of user:password
        // Or try and find form login request parameters or POST bodies, i.e. "username=me" & "password="myPass"
        return checkVariousLoginOptions(request);
    }


    private boolean notAuthenticated(UsernamePasswordToken token) {
        // compare the token with what you have in your database...or in-memory...or in LDAP...
        return false;
    }

    private boolean notAuthorized(UsernamePasswordToken token, HttpServletRequest request) {
       // check if currently authenticated user has the permission/role to access this request's /URI
       // e.g. /admin needs a ROLE_ADMIN , /callcenter needs ROLE_CALLCENTER, etc.
       return false;
    }
*/    
}
 