package com.example.spring_boot_testing.service;

import com.example.spring_boot_testing.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(long id);
    Employee updateEmployee(Employee updateEmployee);
    void deleteEmployee(long id);

}
