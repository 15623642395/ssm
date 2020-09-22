package webProject.log4jFilter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;

/**
 * 项目启动时根据web请求路径设置日志目录
 * @author 56525
 * 
 * 注意过滤器中不能使用logger，因为此时的log4jdir还没初始化，要是这里用到了就会报错的
 * 
 *
 */
public class Log4jFilter implements Filter {

	public static final String log4jdirkey = "log4jdir";
	public static final String traceId = "traceId";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ThreadContext.remove(log4jdirkey);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 设置字符集
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletRequest.setCharacterEncoding("UTF-8");
		httpServletResponse.setCharacterEncoding("UTF-8");
		// 设置log4j2的日志目录
		String url = httpServletRequest.getRequestURI();
		String log4dir = url.substring(0, url.lastIndexOf("."));
		// 根据请求路径每个方法的名称创建一个文件夹
		ThreadContext.put(log4jdirkey, log4dir);
		// 每个请求都给一个uuid，防止日志串联时打印杂乱无章
		ThreadContext.put(traceId, UUID.randomUUID().toString());
		// 调用doFIlter方法,如果还有别的过滤器会自动向下调用
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	@Override
	public void destroy() {
	}

}
