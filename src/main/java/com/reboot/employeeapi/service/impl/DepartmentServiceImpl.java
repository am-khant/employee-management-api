package com.reboot.employeeapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reboot.employeeapi.dto.DepartmentCreateRequest;
import com.reboot.employeeapi.dto.DepartmentResponse;
import com.reboot.employeeapi.dto.DepartmentUpdateRequest;
import com.reboot.employeeapi.exception.BusinessException;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.mapper.DepartmentMapper;
import com.reboot.employeeapi.model.Department;
import com.reboot.employeeapi.repository.DepartmentRepository;
import com.reboot.employeeapi.service.DepartmentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentrepository;
	private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	public DepartmentServiceImpl(DepartmentRepository departmentrepository) {
		this.departmentrepository = departmentrepository;
	}

	@Override
	public DepartmentResponse create(DepartmentCreateRequest request) {

		log.info("Creating department with name:{}", request.getName());

		Department department = new Department(request.getName());
		Department save = departmentrepository.save(department);

		log.info("Department created with id: {}", save.getId());
		return DepartmentMapper.toResponse(save);
	}

	@Override
	public DepartmentResponse update(Long id, DepartmentUpdateRequest request) {

		log.info("Updating department with name: {}", request.getName());

		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found to upadate:" + id));

		department.setName(request.getName());
		Department update = departmentrepository.save(department);

		log.info("Department updated with name: {}", update.getName());
		return DepartmentMapper.toResponse(update);
	}

	@Override
	public void delete(Long id) {

		log.info("Attempting to delete department with id : {}", id);
		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

		if (!department.getEmployees().isEmpty()) {
			throw new BusinessException("Cannot delete department with existing employees");
		}

		departmentrepository.deleteById(id);
		log.info("Department {} deleted successfully", id);
	}

	@Override
	public DepartmentResponse getById(Long id) {

		log.info("Getting dpeartment data with id: {} ", id);

		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found:" + id));

		log.info("Department {} data got successfully", department.getName());
		return DepartmentMapper.toResponse(department);
	}

	@Override
	public List<DepartmentResponse> getAll() {
		log.info("Getting all department data");
		return departmentrepository.findAll().stream().map(DepartmentMapper::toResponse).collect(Collectors.toList());
	}

}
