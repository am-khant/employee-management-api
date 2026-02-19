package com.reboot.employeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeUpdateRequest {

	@NotBlank
	private String name;
	@NotNull
	private Long departmentId;
	@NotNull
	private double salary;

	public String getName() {
		return name;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public double getSalary() {
		return salary;
	}
}
