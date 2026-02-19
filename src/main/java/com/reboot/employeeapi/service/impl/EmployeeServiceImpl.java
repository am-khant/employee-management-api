package com.reboot.employeeapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.reboot.employeeapi.dto.EmployeeCreateRequest;
import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.dto.EmployeeUpdateRequest;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.mapper.EmployeeMapper;
import com.reboot.employeeapi.model.Department;
import com.reboot.employeeapi.model.Employee;
import com.reboot.employeeapi.repository.DepartmentRepository;
import com.reboot.employeeapi.repository.EmployeeRepository;
import com.reboot.employeeapi.service.EmployeeService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;
	private final DepartmentRepository departmentrepository;
	private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	public EmployeeServiceImpl(EmployeeRepository repository, DepartmentRepository departmentRepository) {
		this.repository = repository;
		this.departmentrepository = departmentRepository;
	}

	public EmployeeResponse create(EmployeeCreateRequest req) {

		log.info("Create employee with name : {}", req.getName());
		Department dept = departmentrepository.findById(req.getDepartmentId()).orElseThrow(
				() -> new ResourceNotFoundException("Department not found with id:" + req.getDepartmentId()));

		Employee e = new Employee(req.getName(), dept, req.getSalary());
		Employee saved = repository.save(e);

		log.info("Employee created  with id: {}", saved.getId());
		return EmployeeMapper.toResponse(saved);
	}

	public EmployeeResponse update(Long id, EmployeeUpdateRequest req) {
		log.info("Update employee with name: {}", req.getName());

		Employee employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found to update: " + id));

		Department dept = departmentrepository.findById(req.getDepartmentId()).orElseThrow(
				() -> new ResourceNotFoundException("Department not found to update with id:" + req.getDepartmentId()));

		employee.setName(req.getName());
		employee.setDepartment(dept);
		employee.setSalary(req.getSalary());

		Employee updated = repository.save(employee);

		log.info("Employee updated with id: {}", updated.getName());

		return EmployeeMapper.toResponse(updated);
	}

	public void delete(Long id) {
		log.info("Attempting to delete employee with id : {}", id);

		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Employee not found to delete: " + id);
		}
		log.info("Employee {} deleted successfully", id);

		repository.deleteById(id);
	}

	public Page<EmployeeResponse> getAll(Pageable pageable) {
		log.info("Getting employees data");
		return repository.findAll(pageable).map(EmployeeMapper::toResponse);
	}

	public EmployeeResponse getById(Long id) {

		log.info("Getting employee data with id: {} ", id);
		Employee employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));

		log.info("Employee {} data got successfully", employee.getName());

		return EmployeeMapper.toResponse(employee);
	}
}
