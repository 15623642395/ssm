package webProject.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

/**
 * 自定义全局异常处理器
 * 	注意：他会捕获所有的异常，例如：redis没有启动起来，前台调用redis服务是是会抛出异常的，此时特就会通过GlobalExceptionHandler
 * 			捕获这个异常
 * @author 56525
 *
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver, Ordered {
	private static final String SUCCESS_STATUS = "0";// 失败

	private static final String SUCCESS_MESSAGE = "交易失败";

	/**
	 * 处理自定义异常，并将异常以json形式返回给调用页面
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		CodeException codeException = null;
		if (ex instanceof CodeException) {
			codeException = (CodeException) ex;
		} else if (ex instanceof HttpMessageNotReadableException) {
			codeException = new CodeException(SUCCESS_STATUS, "参数异常");
		} else {
			codeException = new CodeException(SUCCESS_STATUS, SUCCESS_MESSAGE);
		}
		ModelAndView mv = new ModelAndView();
		/* 使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常 */
		FastJsonJsonView view = new FastJsonJsonView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		Map<String, String> rspHead = new HashMap<String, String>();
		rspHead.put("SUCCESS_STATUS", SUCCESS_STATUS);
		rspHead.put("SUCCESS_CODE", codeException.getCode());
		rspHead.put("SUCCESS_MESSAGE", codeException.getMessage());
		attributes.put("rspHead", rspHead);
		view.setAttributesMap(attributes);
		mv.setView(view);
		return mv;
	}

	/**
	 * 异常处理先后顺序.
	 *
	 * @see org.springframework.core.Ordered#getOrder()
	 */

	@Override
	public int getOrder() {
		return 2;
	}

}
