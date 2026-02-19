package com.reboot.employeeapi.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentUpdateRequest {

	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

}
