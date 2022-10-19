package com.vishcom.laundry.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nuwan on 10/28/15.
 * Reference :http://stackoverflow.com/questions/25136532/allow-options-http-method-for-oauth-token-request
 * good resource : https://bitbucket.org/thetransactioncompany/cors-filter/src/009abc8483dbdb6ad69844538afda3dc12829c7a/src/main/java/com/thetransactioncompany/cors/CORSFilter.java?at=master&fileviewer=file-view-default#CORSFilter.java-199
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

    @Autowired
    private Environment env;

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "app.auth.corsAllowedOrigins";

    public SimpleCORSFilter() {
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        //response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia,Authorization");

        /*ignore the OPTIONS preflight request for mycse user logging*/
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
        //chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
