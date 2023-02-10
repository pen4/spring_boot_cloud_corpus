package com.springboot.controller;

import com.springboot.entity.Employee;
import com.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "employee/{id}", method = RequestMethod.GET)
    public Employee findEmployeeById(@PathVariable String id)
    {
        System.out.println("Searching by ID  : " + id);
        return employeeService.getEmployeeByID(id);
    }

    @RequestMapping(value = "employee/{id}/{name}", method = RequestMethod.PUT)
    public Employee findEmployeeById(@PathVariable String id,@PathVariable String name)
    {
        System.out.println("Updating by ID  : " + id);
        return employeeService.updateEmployee(id,name);
    }

    @RequestMapping(value = "employee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id)
    {
        System.out.println("Updating by ID  : " + id);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
