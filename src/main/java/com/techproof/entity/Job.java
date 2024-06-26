package com.techproof.entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "JOBS")
public class Job extends PanacheEntity {
    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "SALARY", nullable = false)
    private Long salary;

    public Job(){}

    public Job(String name, Long salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
