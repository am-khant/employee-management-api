package com.reboot.employeeapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reboot.employeeapi.dto.EmployeeCreateRequest;
import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.dto.EmployeeUpdateRequest;
import com.reboot.employeeapi.service.EmployeeService;
import com.reboot.employeeapi.service.impl.EmployeeServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService service;

	public EmployeeController(EmployeeServiceImpl service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.getById(id));
	}

	@GetMapping
	public Page<EmployeeResponse> getAll(Pageable pageable) {
		return service.getAll(pageable);
	}
}
