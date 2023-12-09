package com.blog.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableMethodSecurity
public class UserController {

	@Autowired
	private UserService userservice;
	
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		
		UserDto dto=this.userservice.createuser(userDto);
	return new ResponseEntity<>(dto,HttpStatus.CREATED);
	
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto>updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Integer userId){
		UserDto updateUser=this.userservice.updateuser(userDto, userId);
		
		return ResponseEntity.ok(updateUser);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	
	public ResponseEntity<?> deleteuser(@PathVariable Integer userId){
		
	this.userservice.deleteUser(userId);
		return new ResponseEntity<>(new ApiResponse("user deleted successfully",true),HttpStatus.OK);
	}
	@GetMapping("/")
	
	public ResponseEntity<List<UserDto>> getallUser(){
		
		return ResponseEntity.ok(this.userservice.getAlluser());
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getsingleuser(@PathVariable Integer userId){
		
		return ResponseEntity.ok(this.userservice.getuserById(userId));
	}
	
	
}
