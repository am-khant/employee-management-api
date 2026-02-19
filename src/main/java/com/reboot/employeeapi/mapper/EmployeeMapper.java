package com.reboot.employeeapi.mapper;

import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.model.Employee;

public class EmployeeMapper {

	public static EmployeeResponse toResponse(Employee e) {
		return new EmployeeResponse(e.getId(), e.getName(), e.getSalary(), e.getDepartment().getName());
	}
}
