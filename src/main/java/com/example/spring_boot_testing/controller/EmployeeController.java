package com.example.spring_boot_testing.controller;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    // The @ResponseStatus annotation in Spring MVC allows you to assign a specific HTTP status to a controller method or an exception.
    // Using this annotation you can specify what HTTP status a controller method will return when executed correctly.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

}
