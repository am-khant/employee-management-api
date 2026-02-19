package com.reboot.employeeapi.mapper;

import com.reboot.employeeapi.dto.DepartmentResponse;
import com.reboot.employeeapi.model.Department;

public class DepartmentMapper {
	public static DepartmentResponse toResponse(Department d) {
		return new DepartmentResponse(d.getId(), d.getName());
	}
}
