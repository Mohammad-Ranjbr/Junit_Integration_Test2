package com.example.spring_boot_testing.controller;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
        Employee employee = employeeService.getEmployeeById(employeeId);
        if(employee != null){
            return new ResponseEntity<>(employee,HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable("id") long employeeId){
         Employee savedEmployee = employeeService.getEmployeeById(employeeId);
         if(savedEmployee != null){
             savedEmployee.setFirstName(employee.getFirstName());
             savedEmployee.setLastName(employee.getLastName());
             savedEmployee.setEmail(employee.getEmail());
             return new ResponseEntity<>(employeeService.updateEmployee(employee),HttpStatus.OK);
         } else {
             return ResponseEntity.notFound().build();
         }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Employee deleted successfully!",HttpStatus.OK);
    }

}
