package com.reboot.employeeapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reboot.employeeapi.dto.EmployeeResponse;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createEmployee_success_shouldReturn201() throws Exception {

		EmployeeResponse response = new EmployeeResponse(1L, "John", 50000.0, "IT");

		when(service.create(any())).thenReturn(response);

		String requestJson = """
				{
				    "name": "John",
				    "salary": 50000,
				    "departmentId": 1
				}
				""";

		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("John")).andExpect(jsonPath("$.salary").value(50000.0))
				.andExpect(jsonPath("$.department").value("IT"));
	}

	@Test
	void createEmployee_validationFail_shouldReturn400() throws Exception {

		String invalidJson = """
				{
				    "name": "",
				    "salary": 1000
				}
				""";

		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.validationErrors").exists());
	}

	@Test
	void createEmployee_departmentNotFound_shouldReturn404() throws Exception {

		when(service.create(any())).thenThrow(new ResourceNotFoundException("Department not found with id: 999"));

		String requestJson = """
				{
				    "name": "John",
				    "salary": 50000,
				    "departmentId": 999
				}
				""";

		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Department not found with id: 999"));
	}

	@Test
	void updateEmployee_success_shouldReturn200() throws Exception {

		EmployeeResponse response = new EmployeeResponse(1L, "John Updated", 60000.0, "IT");

		when(service.update(eq(1L), any())).thenReturn(response);

		String requestJson = """
				{
				    "name": "John Updated",
				    "salary": 60000,
				    "departmentId": 1
				}
				""";

		mockMvc.perform(put("/employees/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("John Updated")).andExpect(jsonPath("$.salary").value(60000.0))
				.andExpect(jsonPath("$.department").value("IT"));
	}

	@Test
	void updateEmployee_validationFail_shouldReturn400() throws Exception {

		String invalidJson = """
				{
				    "name": "",
				    "salary": 1000,
				    "departmentId": 1
				}
				""";

		mockMvc.perform(put("/employees/1").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.validationErrors").exists());
	}

	@Test
	void updateEmployee_notFound_shouldReturn404() throws Exception {

		when(service.update(eq(1L), any())).thenThrow(new ResourceNotFoundException("Not Found"));

		String requestJson = """
				{
				    "name": "John",
				    "salary": 50000,
				    "departmentId": 999
				}
				""";

		mockMvc.perform(put("/employees/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void deleteEmployee_success_shouldReturn204() throws Exception {

		mockMvc.perform(delete("/employees/1")).andExpect(status().isNoContent());
	}

	@Test
	void deleteEmployee_notFound_shouldReturn404() throws Exception {
		doThrow(new ResourceNotFoundException("Employee not found to delete: 999")).when(service).delete(999L);

		mockMvc.perform(delete("/employees/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Employee not found to delete: 999"));
	}

	@Test
	void getById_success() throws Exception {
		EmployeeResponse response = new EmployeeResponse(1L, "John", 50000, "IT");

		when(service.getById(1L)).thenReturn(response);

		mockMvc.perform(get("/employees/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("John")).andExpect(jsonPath("$.salary").value(50000.0))
				.andExpect(jsonPath("$.department").value("IT"));
	}

	@Test
	void getById_notFound_shouldReturn404() throws Exception {
		when(service.getById(1L)).thenThrow(new ResourceNotFoundException("Employee not found"));

		mockMvc.perform(get("/employees/1")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Employee not found"));
	}

	@Test
	void getAll_success_shouldReturn200() throws Exception {

		Page<EmployeeResponse> page = new PageImpl<>(List.of(new EmployeeResponse(1L, "John", 50000.0, "IT")));

		when(service.getAll(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/employees?page=0&size=5")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(1)).andExpect(jsonPath("$.content[0].name").value("John"))
				.andExpect(jsonPath("$.content[0].salary").value(50000.0))
				.andExpect(jsonPath("$.content[0].department").value("IT"));
	}

	@Test
	void getAll_empty_shouldReturn200WithEmptyList() throws Exception {

		Page<EmployeeResponse> emptyPage = new PageImpl<>(List.of());

		when(service.getAll(any(Pageable.class))).thenReturn(emptyPage);

		mockMvc.perform(get("/employees")).andExpect(status().isOk()).andExpect(jsonPath("$.content").isEmpty());
	}
}
