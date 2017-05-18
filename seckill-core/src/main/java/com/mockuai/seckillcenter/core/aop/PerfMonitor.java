package com.mockuai.seckillcenter.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 监控service接口的运行时间
 * <功能详细描述>
 *
 * @author Administrator
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Aspect
@Component
public class PerfMonitor {

	private static final Logger logger = LoggerFactory.getLogger("perfLogger");

	/**
	 * 监控com.mockuai.marketingcenter.core.manager包及其子包的所有public方法
	 * <功能详细描述>
	 *
	 * @see [类、类#方法、类#成员]
	 */
	@Pointcut("execution(* com.mockuai.seckillcenter.core.manager..*.*(..))")
	private void pointCutMethod() {
	}

	@Pointcut("execution(* com.mockuai.seckillcenter.core.dao..*.*(..))")
	private void pointCutMethodDAO() {
	}

	//声明环绕通知
	@Around("pointCutMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long begin = System.nanoTime();
		Object o = pjp.proceed();
		long end = System.nanoTime();
		logger.info("{}:{}", pjp.getTarget().getClass() + "." + pjp.getSignature().getName(), (end - begin) / 1000000);
		return o;
	}

	//声明环绕通知
	@Around("pointCutMethodDAO()")
	public Object doAroundDAO(ProceedingJoinPoint pjp) throws Throwable {
		long begin = System.nanoTime();
		Object o = pjp.proceed();
		long end = System.nanoTime();
		logger.info("{}:{}", pjp.getTarget().getClass() + "." + pjp.getSignature().getName(), (end - begin) / 1000000);
		return o;
	}
}