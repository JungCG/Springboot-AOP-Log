package com.jcg.AopLog.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

// Exception을 낚아채기
@RestController
@ControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public String 요청잘못(IllegalArgumentException e) {
		return "오류 : "+e.getMessage();
	}
}
