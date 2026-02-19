package com.reboot.employeeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reboot.employeeapi.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
