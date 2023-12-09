package com.blog.payloads;

public class RoleDto {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private int id;
	public RoleDto(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	private String name;
}
