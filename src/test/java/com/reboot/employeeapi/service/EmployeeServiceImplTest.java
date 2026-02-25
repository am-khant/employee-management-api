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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.reboot.employeeapi.dto.EmployeeCreateRequest;
import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.dto.EmployeeUpdateRequest;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.model.Department;
import com.reboot.employeeapi.model.Employee;
import com.reboot.employeeapi.repository.DepartmentRepository;
import com.reboot.employeeapi.repository.EmployeeRepository;
import com.reboot.employeeapi.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@InjectMocks
	private EmployeeServiceImpl employeeService;

	EmployeeCreateRequest employeeCreateRequest = new EmployeeCreateRequest();
	Long departmentId = 1L;

	EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();

	@BeforeEach
	void setUp() {
		employeeCreateRequest.setName("John");
		employeeCreateRequest.setDepartmentId(departmentId);
		employeeCreateRequest.setSalary(50000);
	}

	@Test
	void createEmployee_success() {

		Department department = new Department("ID");
		department.setId(departmentId);

		Employee savedEmployee = new Employee("John", department, 50000);
		savedEmployee.setId(100L);

		when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
		when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

		EmployeeResponse response = employeeService.create(employeeCreateRequest);

		assertNotNull(response);
		assertEquals("John", response.getName());
		assertEquals(100L, response.getId());

		verify(departmentRepository, times(1)).findById(departmentId);
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}

	@Test
	void createEmployee_departmentNotFound_shouldThrowException() {

		when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.create(employeeCreateRequest);
		});

		verify(departmentRepository, times(1)).findById(departmentId);
		verify(employeeRepository, never()).save(any());
	}

	@Test
	void updateEmployee_success() {

		Long employeeId = 1L;

		Department department = new Department("IT");
		department.setId(departmentId);

		Employee existingEmployee = new Employee("OldName", department, 40000);
		existingEmployee.setId(employeeId);

		EmployeeUpdateRequest request = new EmployeeUpdateRequest();
		request.setName("NewName");
		request.setDepartmentId(departmentId);
		request.setSalary(50000);

		Employee updatedEmployee = new Employee("NewName", department, 50000);
		updatedEmployee.setId(employeeId);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
		when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
		when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);

		EmployeeResponse response = employeeService.update(employeeId, request);

		assertNotNull(response);
		assertEquals("NewName", response.getName());
		assertEquals(50000, response.getSalary());
		assertEquals(department.getName(), response.getDepartment());

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(departmentRepository, times(1)).findById(departmentId);
		verify(employeeRepository, times(1)).save(existingEmployee);
	}

	@Test
	void updateEmployee_employeeNotFound_shouldThrowResourceNotFoundException() {
		Long employeeId = 1L;

		EmployeeUpdateRequest request = new EmployeeUpdateRequest();
		request.setDepartmentId(2L);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.update(employeeId, request);
		});

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(departmentRepository, never()).findById(any());
		verify(employeeRepository, never()).save(any());
	}

	@Test
	void updateEmployee_departmentNotFound_shouldThrowResourceNotFoundException() {
		Long employeeId = 1L;

		Department department = new Department("IT");
		department.setId(departmentId);

		Employee existingEmployee = new Employee("OldName", department, 40000);
		existingEmployee.setId(employeeId);

		EmployeeUpdateRequest request = new EmployeeUpdateRequest();
		request.setDepartmentId(departmentId);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
		when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.update(employeeId, request);
		});

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(departmentRepository, times(1)).findById(departmentId);
		verify(employeeRepository, never()).save(any());
	}

	@Test
	void deleteEmployee_success() {

		Long employeeId = 1L;

		when(employeeRepository.existsById(employeeId)).thenReturn(true);

		employeeService.delete(employeeId);

		verify(employeeRepository, times(1)).existsById(employeeId);
		verify(employeeRepository, times(1)).deleteById(employeeId);
	}

	@Test
	void deleteEmployee_notFound_shouldThrowResourceNotFoundException() {

		Long employeeId = 1L;

		when(employeeRepository.existsById(employeeId)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.delete(employeeId);
		});

		verify(employeeRepository, times(1)).existsById(employeeId);
		verify(employeeRepository, never()).deleteById(any());
	}

	@Test
	void getById_success() {

		Long employeeId = 1L;

		Department department = new Department("IT");
		department.setId(2L);

		Employee employee = new Employee("John", department, 50000);
		employee.setId(employeeId);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

		EmployeeResponse response = employeeService.getById(employeeId);

		assertNotNull(response);
		assertEquals(employeeId, response.getId());
		assertEquals("John", response.getName());

		verify(employeeRepository, times(1)).findById(employeeId);
	}

	@Test
	void getById_notFound_shouldThrowResourceNotFoundException() {

		Long employeeId = 1L;

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.getById(employeeId);
		});

		verify(employeeRepository, times(1)).findById(employeeId);
	}

	@Test
	void getAll_success() {

		Pageable pageable = PageRequest.of(0, 5);

		Department department = new Department("IT");
		department.setId(2L);

		Employee employee = new Employee("John", department, 50000);
		employee.setId(1L);

		Page<Employee> employeePage = new PageImpl<>(List.of(employee));

		when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

		Page<EmployeeResponse> result = employeeService.getAll(pageable);

		assertNotNull(result);
		assertEquals(1, result.getContent().size());
		assertEquals("John", result.getContent().get(0).getName());

		verify(employeeRepository, times(1)).findAll(pageable);
	}

	@Test
	void getAll_emptyPage() {

		Pageable pageable = PageRequest.of(0, 5);

		Page<Employee> emptyPage = new PageImpl<>(List.of());

		when(employeeRepository.findAll(pageable)).thenReturn(emptyPage);

		Page<EmployeeResponse> result = employeeService.getAll(pageable);

		assertNotNull(result);
		assertTrue(result.getContent().isEmpty());

		verify(employeeRepository, times(1)).findAll(pageable);
	}
}
