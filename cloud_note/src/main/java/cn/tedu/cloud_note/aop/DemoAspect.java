package cn.tedu.cloud_note.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import cn.tedu.cloud_note.service.UserNotFoundException;

/**
  * 创建一个切面组件，就是一个普通的javaBean 
 */
@Component
@Aspect
public class DemoAspect {
	
	//声明test方法将在userService的全部方法之前执行
	@Before("bean(userService)")
	public void test() {
		System.out.println("hello world");
	}
	//声明test2方法将在userService的全部方法之后执行
	@After("bean(userService)")
	public void test2() {
		System.out.println("after");
	}
	
	@AfterReturning("bean(userService)")
	public void test3() {
		System.out.println("afterReturning");
	}
	
	@AfterThrowing("bean(userService)")
	public void test4() {
		System.out.println("afterThrowing");
	}
	
	/**
	 * 环绕通知 方法：
	 * 	1、必须有返回值Object
	 * 	2、必须有参数 ProceedingJoinPoint
	 * 	3、必须抛出异常
	 * 	4、必须在方法中调用jp.proceed()
	 * 	5、返回业务方法的返回值
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	//@Around("bean(userService)")
	public Object test5(ProceedingJoinPoint jp) 
			throws Throwable{
		System.out.println("val之前");
		Object val = jp.proceed();
		System.out.println("业务结果"+val);
		throw new UserNotFoundException("没有找到用户123");
	}
}
