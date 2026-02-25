package com.reboot.employeeapi.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentCreateRequest {

	@NotBlank
	private String name;

	public DepartmentCreateRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
