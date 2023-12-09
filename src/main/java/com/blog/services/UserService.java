package com.blog.services;

import java.util.List;

import com.blog.payloads.UserDto;

public interface UserService {

	UserDto registerNewUser(UserDto user);
	UserDto createuser(UserDto user);
	UserDto updateuser(UserDto user,Integer userId);
	List<UserDto> getAlluser();
	void deleteUser(Integer userId);
	UserDto getuserById(Integer userId);

}
