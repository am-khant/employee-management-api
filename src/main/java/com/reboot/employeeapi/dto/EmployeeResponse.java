package com.reboot.employeeapi.dto;

public class EmployeeResponse {

	private Long id;
	private String name;
	private double salary;
	private String department;

	public EmployeeResponse(Long id, String name, double salary, String department) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getSalary() {
		return salary;
	}

	public String getDepartment() {
		return department;
	}
}
