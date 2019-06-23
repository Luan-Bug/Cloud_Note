package cn.tedu.cloud_note.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
  * 对软件业务层进行业务测试 
 */
@Component
@Aspect
public class TimeAspect {
	//@Around("bean(*Service)")
	public Object test(ProceedingJoinPoint jp) throws Throwable{
		
		long t1 = System.currentTimeMillis();
		Object val = jp.proceed();//目标业务方法
		long t2 = System.currentTimeMillis();
		long t = t2-t1;
		
		//JoinPoint 对象可以获取目标业务方法的详情信息：方法签名，调用参数等
		Signature m = jp.getSignature();
		
		System.out.println(m+"用时："+t);
		return val;
	}
}
