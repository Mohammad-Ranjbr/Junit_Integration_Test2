package com.example.spring_boot_testing.service.Impl;

import com.example.spring_boot_testing.exception.ResourceNotFoundException;
import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.example.spring_boot_testing.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> getEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(getEmployee.isPresent()){
            throw new ResourceNotFoundException("Resource already exist with given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

}