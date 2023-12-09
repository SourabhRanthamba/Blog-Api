package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.Repositories.RoleRepo;
import com.blog.Repositories.UserRepo;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payloads.UserDto;
import com.blog.services.UserService;

@Service
public class userServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private ModelMapper modelmapper;
	@Override
	public UserDto createuser(UserDto userDto) {
		// TODO Auto-generated method stub
	
		User user=this.dtoTouser(userDto);
		
		User savedUser=this.userRepo.save(user);
		return this.usertoDto(savedUser);
	}

	@Override
	public UserDto updateuser(UserDto userDto, Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));	
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());		
		
			User updateUser=this.userRepo.save(user);
		return this.usertoDto(updateUser);
	}

	@Override
	public UserDto getuserById(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));	

		return this.usertoDto(user);
	}

	@Override
	public List<UserDto> getAlluser() {

		List<User> users=this.userRepo.findAll();
		List<UserDto> userDto=users.stream().map(user->this.usertoDto(user)).collect(Collectors.toList());
		return userDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));	
this.userRepo.delete(user);
		

	}

	private User dtoTouser(UserDto userDto) {
//		User user=new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setAbout(userDto.getAbout());
//		user.setPassword(userDto.getPassword());
		
		User user=this.modelmapper.map(userDto,User.class);
		return user;
		
	}
	
	public UserDto usertoDto(User user) {
//		UserDto userDto =new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setAbout(user.getAbout());
//		userDto.setPassword(user.getPassword());

		UserDto userDto=this.modelmapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {

		User user1 = this.modelmapper.map(userDto,User.class);
		
		//encoded the password
		user1.setPassword(this.passwordEncoder.encode(user1.getPassword()));
		
		//roles
		Role role = this.roleRepo.findById(502).get();
		user1.getRoles().add(role);
		
		User save = this.userRepo.save(user1);
		return this.modelmapper.map(save, UserDto.class);
	}
}
