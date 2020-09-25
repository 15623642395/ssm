package webProject.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Component;

/**
 * 1、研究web.xml中监听器上的上下文参数作用
 * 	在容器启动时，在实例化对象之前就会执行
 * 2、debug本类和webProject/log4jFilter/Log4jFilter.java的init()方法
 * 	可以知道监听器比过滤器更先加载
 * 
 * @author 56525
 *
 */
@SuppressWarnings("serial")
@Component
public class MyApplicationListener extends HttpServlet implements ServletContextListener {

	/**
	 * 用于在容器开启时
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("我是监听器，我比过滤器先执行，容器初始化时可以获取web.xml上下文初始化参数test的值：" + sce.getServletContext().getInitParameter("test"));
	}

	/**
	 * 用于在容器关闭时
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("容器销毁时，调用监听器中的销毁方法");
	}

}
