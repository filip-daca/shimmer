package shimmer.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Encoding filter forces utf-8 characters.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class CharacterEncodingFilter implements Filter {

    private Log log = LogFactory.getLog(CharacterEncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializing CharacterEncodingFilter");
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

    @Override
    public void destroy() {
        log.info("Destroying CharacterEncodingFilter");
    }

}
