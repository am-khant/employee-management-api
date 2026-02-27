package com.reboot.employeeapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.reboot.employeeapi.model.Department;
import com.reboot.employeeapi.model.Employee;
import com.reboot.employeeapi.repository.DepartmentRepository;
import com.reboot.employeeapi.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmployeeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	void shouldCreateEmployee() throws Exception {

		Department department = new Department();
		department.setName("IT");
		Department saveDept = departmentRepository.save(department);

		String json = """
				     {
				        "name": "John",
				        "departmentId": %d,
				        "salary": 70000
				    }
				""".formatted(saveDept.getId());

		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());

		assertEquals(1, employeeRepository.count());
	}

	@Test
	void shouldUpdateEmployee() throws Exception {

		Department dept = new Department();
		dept.setName("IT");
		Department savedDept = departmentRepository.save(dept);

		Employee employee = new Employee();
		employee.setName("MK");
		employee.setSalary(60000);
		employee.setDepartment(savedDept);

		Employee savedEmployee = employeeRepository.save(employee);

		String updateJson = """
				{
				    "name": "MK Updated",
				    "departmentId": %d,
				    "salary": 75000
				}
				""".formatted(savedDept.getId());

		mockMvc.perform(
				put("/employees/" + savedEmployee.getId()).contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk());

		Employee updated = employeeRepository.findById(savedEmployee.getId()).get();

		assertEquals("MK Updated", updated.getName());
		assertEquals(75000, updated.getSalary());
	}

	@Test
	void shouldReturn404WhenUpdatingNonExistingEmployee() throws Exception {
		Department dept = new Department();
		dept.setName("IT");
		Department savedDept = departmentRepository.save(dept);

		String updateJson = """
				{
				    "name": "Ghost",
				    "departmentId": %d,
				    "salary": 50000
				}
				""".formatted(savedDept.getId());

		mockMvc.perform(put("/employees/999").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteEmployee() throws Exception {

		// Prepare Department
		Department dept = new Department();
		dept.setName("IT");
		Department savedDept = departmentRepository.save(dept);

		// Prepare Employee
		Employee employee = new Employee();
		employee.setName("MK");
		employee.setSalary(60000);
		employee.setDepartment(savedDept);

		Employee savedEmployee = employeeRepository.save(employee);

		mockMvc.perform(delete("/employees/" + savedEmployee.getId())).andExpect(status().isNoContent());

		org.junit.jupiter.api.Assertions.assertFalse(employeeRepository.existsById(savedEmployee.getId()));
	}

	@Test
	void shouldReturn400WhenNameIsBlank() throws Exception {

		Department dept = new Department();
		dept.setName("IT");
		Department savedDept = departmentRepository.save(dept);

		String invalidJson = """
				{
				    "name": "",
				    "departmentId": %d,
				    "salary": 60000
				}
				""".formatted(savedDept.getId());

		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnEmployee() throws Exception {
		Department dept = new Department();
		dept.setName("IT");
		Department savedDept = departmentRepository.save(dept);

		Employee e = new Employee();
		e.setName("MK");
		e.setDepartment(savedDept);
		e.setSalary(60000);

		Employee savedEmp = employeeRepository.save(e);

		mockMvc.perform(get("/employees/" + savedEmp.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("MK")).andExpect(jsonPath("$.salary").value(60000))
				.andExpect(jsonPath("$.department").value(savedDept.getName()));
	}

	@Test
	void shouldReturn404WhenEmployeeNotFound() throws Exception {

		mockMvc.perform(get("/employees/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").exists());
		;
	}
}
