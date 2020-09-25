package webProject.emp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 切面
 * 
 * @author 56525
 *
 */
@Aspect
@Component
public class AopException {

	// 抽取公共切入点表达式,定义在controller包里的任意方法的执行
	@Pointcut("execution(* webProject.emp.controller.*.*(..))")
	public void pointCut() {

	}

	/**
	 * 前置通知，用于捕获前台访问入参
	 * 
	 * @param joinPoint
	 */
	@Before("pointCut()")
	public void doBefore(JoinPoint joinPoint) {
		// String methodname = joinPoint.getSignature().getName();
		// List<Object> args = Arrays.asList(joinPoint.getArgs());
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes())
		// .getRequest();
		// String url = request.getRequestURI();
		// logger.info("访问路径:" + url + ",方法名称:" + methodname);
		// logger.info("请求参数:" + args);
	}

	/**
	 * 后置通知，获取返回参数
	 * 
	 * @param joinPoint
	 * @param rtn
	 */
	@AfterReturning(returning = "rtn", pointcut = "pointCut()")
	public void doAfter(JoinPoint joinPoint, Object rtn) {
		// logger.info("返回参数:" + rtn);
	}

	/**
	 * 异常通知
	 * @param throwinfo
	 */
	@AfterThrowing(pointcut = "pointCut()",throwing="e")
	public void throwafter(JoinPoint joinPoint, Exception e) {
//		String methodName = joinPoint.getSignature().getName();
//		System.out.println("The method " + methodName + " occurs excetion:" + e);

	}
}
