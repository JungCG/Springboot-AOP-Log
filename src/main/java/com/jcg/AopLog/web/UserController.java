package com.jcg.AopLog.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcg.AopLog.domain.CommonDto;
import com.jcg.AopLog.domain.JoinReqDto;
import com.jcg.AopLog.domain.UpdateReqDto;
import com.jcg.AopLog.domain.User;
import com.jcg.AopLog.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	// 동작 (행동) 은 Method 로 구별
	// 주소에는 대상만 작성
	
//	private UserRepository userRepository;
	
	// DI 1번
	// IoC 컨테이너에서 UserRepository 타입을 찾는다
	// 메모리에 뜬게 있으면 주입 => DI 의존성 주입
//	public UserController(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
	
	// DI 2번 (final + @RequiredArgsConstructor)
	private final UserRepository userRepository;
	
	// ex) http://localhost:8090/user
	@GetMapping("/user")
	public CommonDto<List<User>> findAll() {
		System.out.println("findAll()");
		return new CommonDto<List<User>>(HttpStatus.OK.value(), userRepository.findAll()); // MessageConverter : JavaObject -> Json String (Spring boot 기본)
	}
	
	// ex) http://localhost:8090/user/1
	@GetMapping("/user/{id}")
	public CommonDto<User> findById(@PathVariable int id) {
		System.out.println("findById() : id : "+id);
		return new CommonDto<User>(HttpStatus.OK.value(), userRepository.findById(id));
	}
	
	@CrossOrigin
	// ex) http://localhost:8090/user
	@PostMapping("/user")
	// x-www-form-urlencoded => request.getParameter()
	// text/plain => @RequestBody 어노테이션
	// application/json => @RequestBody 어노테이션 + 오브젝트로 받기
	public CommonDto<?> save(@Valid @RequestBody JoinReqDto dto,  BindingResult bindingResult) {		
		System.out.println("save()");
		System.out.println("user : "+dto);
		userRepository.save(dto);
		
		return new CommonDto<>(HttpStatus.CREATED.value(), "ok");
	}
	
	// ex) http://localhost:8090/user/1
	@DeleteMapping("/user/{id}")
	public CommonDto delete(@PathVariable int id) {
		System.out.println("delete()");
		userRepository.delete(id);
		return new CommonDto<String>(HttpStatus.OK.value());
	}
	
	// ex) http://localhost:8090/user/1
	@PutMapping("/user/{id}")
	public CommonDto update(@PathVariable int id, @Valid @RequestBody UpdateReqDto dto, BindingResult bindingResult) {
		
		System.out.println("update()");
		userRepository.update(id, dto);
		return new CommonDto<>(HttpStatus.OK.value());
	}
}
