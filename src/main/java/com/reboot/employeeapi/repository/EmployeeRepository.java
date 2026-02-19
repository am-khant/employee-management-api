package com.reboot.employeeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reboot.employeeapi.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
