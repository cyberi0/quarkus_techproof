package com.techproof.services;

import com.techproof.entity.Employee;
import com.techproof.repository.EmployeeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository employeeRepository;

    @Transactional
    public List<Employee> getEmployeesByJobId(Long jobId) {
        return employeeRepository.findByJobId(jobId);
    }

    public List<Employee> filterAndSortEmployeesByJobId(Long jobId, List<Employee> employees) {
        return employees.stream()
                .filter(employee -> jobId.equals(employee.getJob().id))
                .sorted((e1, e2) -> e1.getLastName().compareTo(e2.getLastName()))
                .collect(Collectors.toList());
    }

    public Map<String, List<Employee>> groupEmployeesByLastName(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getLastName));
    }
}
