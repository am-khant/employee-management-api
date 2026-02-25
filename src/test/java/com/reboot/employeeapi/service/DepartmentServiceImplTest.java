package com.reboot.employeeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reboot.employeeapi.dto.DepartmentCreateRequest;
import com.reboot.employeeapi.dto.DepartmentResponse;
import com.reboot.employeeapi.dto.DepartmentUpdateRequest;
import com.reboot.employeeapi.exception.BusinessException;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.model.Department;
import com.reboot.employeeapi.model.Employee;
import com.reboot.employeeapi.repository.DepartmentRepository;
import com.reboot.employeeapi.service.impl.DepartmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

	@Mock
	private DepartmentRepository departmentRepository;
	@InjectMocks
	private DepartmentServiceImpl departmentService;

	@BeforeEach
	void setUp() {

	}

	@Test
	void createDepartment_success() {

		DepartmentCreateRequest request = new DepartmentCreateRequest();
		request.setName("IT");

		Department saved = new Department("IT");
		saved.setId(1L);

		when(departmentRepository.save(any(Department.class))).thenReturn(saved);

		DepartmentResponse response = departmentService.create(request);

		assertNotNull(response);
		assertEquals(1L, response.getId());
		assertEquals("IT", response.getName());

		verify(departmentRepository, times(1)).save(any(Department.class));
	}

	@Test
	void updateDepartment_success() {

		Long id = 1L;

		Department existing = new Department("OldName");
		existing.setId(id);

		DepartmentUpdateRequest request = new DepartmentUpdateRequest();
		request.setName("NewName");

		Department updated = new Department("NewName");
		updated.setId(id);

		when(departmentRepository.findById(id)).thenReturn(Optional.of(existing));

		when(departmentRepository.save(existing)).thenReturn(updated);

		DepartmentResponse response = departmentService.update(id, request);

		assertNotNull(response);
		assertEquals("NewName", response.getName());

		verify(departmentRepository, times(1)).findById(id);
		verify(departmentRepository, times(1)).save(existing);
	}

	@Test
	void updateDepartment_notFound_shouldThrowResourceNotFoundException() {

		Long id = 1L;

		DepartmentUpdateRequest request = new DepartmentUpdateRequest();
		request.setName("NewName");

		when(departmentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.update(id, request);
		});

		verify(departmentRepository, times(1)).findById(id);
		verify(departmentRepository, never()).save(any());
	}

	@Test
	void deleteDepartment_success() {
		Long id = 1L;
		Department department = new Department("IT");
		department.setId(id);
		department.setEmployees(new ArrayList<>());

		when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
		departmentService.delete(id);

		verify(departmentRepository, times(1)).findById(id);
		verify(departmentRepository, times(1)).deleteById(id);
	}

	@Test
	void deleteDepartment_notFound_shouldThrowResourceNotFoundException() {
		Long id = 1L;

		when(departmentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.delete(id);
		});

		verify(departmentRepository, times(1)).findById(id);
		verify(departmentRepository, never()).deleteById(any());
	}

	@Test
	void deleteDepartment_withEmployees_shouldThrowBusinessException() {

		Long id = 1L;

		Department department = new Department("IT");
		department.setId(id);

		Employee employee = new Employee();
		department.setEmployees(List.of(employee)); // not empty

		when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

		assertThrows(BusinessException.class, () -> {
			departmentService.delete(id);
		});

		verify(departmentRepository, times(1)).findById(id);
		verify(departmentRepository, never()).deleteById(any());
	}

	@Test
	void getById_success() {

		Long id = 1L;

		Department department = new Department("IT");
		department.setId(id);

		when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

		DepartmentResponse response = departmentService.getById(id);

		assertNotNull(response);
		assertEquals("IT", response.getName());

		verify(departmentRepository, times(1)).findById(id);
	}

	@Test
	void getById_notFound_shouldThrowException() {

		Long id = 1L;

		when(departmentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.getById(id);
		});

		verify(departmentRepository, times(1)).findById(id);
	}

	@Test
	void getAll_success() {

		Department department = new Department("IT");
		department.setId(1L);

		when(departmentRepository.findAll()).thenReturn(List.of(department));

		List<DepartmentResponse> result = departmentService.getAll();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("IT", result.get(0).getName());

		verify(departmentRepository, times(1)).findAll();
	}

	@Test
	void getAll_empty() {

		when(departmentRepository.findAll()).thenReturn(List.of());

		List<DepartmentResponse> result = departmentService.getAll();

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(departmentRepository, times(1)).findAll();
	}

}
