package com.reboot.employeeapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeUpdateRequest {

	@NotBlank
	private String name;
	@NotNull
	private Long departmentId;
	@NotNull
	@Min(10000)
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

	public void setName(String name) {
		this.name = name;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

}
