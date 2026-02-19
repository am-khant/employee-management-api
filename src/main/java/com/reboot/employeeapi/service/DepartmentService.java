package com.reboot.employeeapi.service;

import java.util.List;

import com.reboot.employeeapi.dto.DepartmentCreateRequest;
import com.reboot.employeeapi.dto.DepartmentResponse;
import com.reboot.employeeapi.dto.DepartmentUpdateRequest;

public interface DepartmentService {

	DepartmentResponse create(DepartmentCreateRequest request);

	DepartmentResponse update(Long id, DepartmentUpdateRequest request);

	void delete(Long id);

	DepartmentResponse getById(Long id);

	List<DepartmentResponse> getAll();
}
