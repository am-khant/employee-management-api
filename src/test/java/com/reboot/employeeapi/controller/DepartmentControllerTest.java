package com.reboot.employeeapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reboot.employeeapi.dto.DepartmentResponse;
import com.reboot.employeeapi.exception.BusinessException;
import com.reboot.employeeapi.exception.GlobalExceptionHandler;
import com.reboot.employeeapi.exception.ResourceNotFoundException;
import com.reboot.employeeapi.service.DepartmentService;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createDepartment_success_shouldReturn201() throws Exception {

		DepartmentResponse response = new DepartmentResponse(1L, "IT");

		when(service.create(any())).thenReturn(response);

		String requestJson = """
				{
				     "name": "IT"
				}
				""";

		mockMvc.perform(post("/departments").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("IT"));
	}

	@Test
	void createDepartment_validationError_shouldReturn400() throws Exception {

		mockMvc.perform(post("/departments").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "name": ""
				}
				""")).andExpect(status().isBadRequest());
	}

	@Test
	void updateDepartment_success_shouldReturn200() throws Exception {

		DepartmentResponse response = new DepartmentResponse(1L, "HR");

		when(service.update(eq(1L), any())).thenReturn(response);

		mockMvc.perform(put("/departments/1").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "name": "HR"
				}
				""")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("HR"));
	}

	@Test
	void updateDepartment_notFound_shouldReturn404() throws Exception {

		when(service.update(eq(1L), any())).thenThrow(new ResourceNotFoundException("Not found"));

		mockMvc.perform(put("/departments/1").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "name": "HR"
				}
				""")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void deleteDepartment_success_shouldReturn204() throws Exception {

		mockMvc.perform(delete("/departments/1")).andExpect(status().isNoContent());
	}

	@Test
	void deleteDepartment_notFound_shouldReturn404() throws Exception {

		doThrow(new ResourceNotFoundException("Not found")).when(service).delete(1L);

		mockMvc.perform(delete("/departments/1")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void deleteDepartment_businessRule_shouldReturn409() throws Exception {

		doThrow(new BusinessException("Cannot delete")).when(service).delete(1L);

		mockMvc.perform(delete("/departments/1")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void getDepartmentById_success_shouldReturn200() throws Exception {

		DepartmentResponse response = new DepartmentResponse(1L, "IT");

		when(service.getById(1L)).thenReturn(response);

		mockMvc.perform(get("/departments/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("IT"));
	}

	@Test
	void getDepartmentById_notFound_shouldReturn404() throws Exception {

		when(service.getById(1L)).thenThrow(new ResourceNotFoundException("Not found"));

		mockMvc.perform(get("/departments/1")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void getAllDepartments_success_shouldReturn200() throws Exception {

		List<DepartmentResponse> list = List.of(new DepartmentResponse(1L, "IT"), new DepartmentResponse(2L, "HR"));

		when(service.getAll()).thenReturn(list);

		mockMvc.perform(get("/departments")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void getAllDepartments_empty_shouldReturn200() throws Exception {

		when(service.getAll()).thenReturn(List.of());

		mockMvc.perform(get("/departments")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
	}
}
