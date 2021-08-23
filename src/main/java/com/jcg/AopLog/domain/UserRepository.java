package com.jcg.AopLog.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	List<User> users;
	
	public UserRepository() {
		users = new ArrayList<>();
		users.add(new User(1, "jcg1", "1234", "01011112222"));
		users.add(new User(2, "jcg2", "1234", "01022222222"));
		users.add(new User(3, "jcg3", "1234", "01033332222"));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User findById(int id) {
		return users.get(id-1);
	}
	
	public void save(JoinReqDto dto) {
		System.out.println("DB에 insert 하기");
	}
	
	public void delete(int id) {
		System.out.println("DB에 삭제하기");
	}
	
	public void update(int id, UpdateReqDto dto) {
		System.out.println("DB에 수정하기");
		throw new IllegalArgumentException("Argument를 잘못 넣음");
	}
}
