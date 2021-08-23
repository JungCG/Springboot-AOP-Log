package com.jcg.AopLog.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jcg.AopLog.domain.CommonDto;

import io.sentry.Sentry;

// @Configuration : Controller 진입하기 전에 사용, 설정할 때 쓰는 것
// @Component : Controller가 뜨고 나서 뜨는 것
@Component
@Aspect
public class BindingAdvice {
	// log 처리 방법 -> 파일로 남기는 방법
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);
	
	// 함수 : 앞 뒤 제어 @Around
	// 함수 : 앞만 제어 @Before (username이 안들어왔으면 내가 강제로 넣어주고 실행) -> ProceedingJoinPoint 사용 불가 (에러), return은 의미가 없다.
	// 함수 : 뒤만 제어 @After (응답만 관리) -> ProceedingJoinPoint 사용 불가 (에러)

	@Before("execution(* com.jcg.AopLog.web..*Controller.*(..))")
	public void testCheck() {
		// Request 값 처리방법
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : "+request.getRequestURI());
		
	}
	
	@After("execution(* com.jcg.AopLog.web..*Controller.*(..))")
	public void testCheck2() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : "+request.getRequestURI());
		
		System.out.println("후처리 로그를 남겼습니다.");
		
	}
	
	@Around("execution(* com.jcg.AopLog.web..*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		System.out.println("type : "+type);
		System.out.println("method : "+method);
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				// 서비스 : 정상적인 화면 -> 사용자요청
				System.out.println("bindingResult = "+bindingResult.getErrorCount());
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨 error, warn, info, debug
						log.warn(type+"."+method+"() => 필드 : "+error.getField()+", 메시지 : "+error.getDefaultMessage());
//						log.debug(type+"."+method+"() => 필드 : "+error.getField()+", 메시지 : "+error.getDefaultMessage());
						Sentry.captureMessage(type+"."+method+"() => 필드 : "+error.getField()+", 메시지 : "+error.getDefaultMessage());
					}
					
					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed(); // 함수의 스택 실행
	}
}
