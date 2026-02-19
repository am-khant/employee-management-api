package com.reboot.employeeapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.reboot.employeeapi.dto.EmployeeCreateRequest;
import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.dto.EmployeeUpdateRequest;

public interface EmployeeService {

	EmployeeResponse create(EmployeeCreateRequest request);

	EmployeeResponse update(Long id, EmployeeUpdateRequest request);

	void delete(Long id);

	EmployeeResponse getById(Long id);

	Page<EmployeeResponse> getAll(Pageable pageable);
}
