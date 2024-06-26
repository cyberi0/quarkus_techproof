package com.techproof.repository;

import com.techproof.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {
    public List<Employee> findByJobId(Long jobId) {
        return list("job.id", jobId);
    }

    public Employee findById(Long id) {
        return find("id", id).singleResult();
    }
}
