package com.reboot.employeeapi.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentCreateRequest {

	@NotBlank
	private String name;

	public String getName() {
		return name;
	}
}
