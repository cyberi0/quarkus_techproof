package com.techproof.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEE_WORKED_HOURS")
public class EmployeeWorkedHours extends PanacheEntity {
    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "WORKED_HOURS", nullable = false)
    private Long workedHours;

    @Column(name = "WORKED_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonbDateFormat("yyyy-MM-dd")
    private Date workedDate;

    public EmployeeWorkedHours(){}

    public EmployeeWorkedHours(Employee employee, Long workedHours, Date workedDate) {
        this.employee = employee;
        this.workedHours = workedHours;
        this.workedDate = workedDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(Long workedHours) {
        this.workedHours = workedHours;
    }

    public Date getWorkedDate() {
        return workedDate;
    }

    public void setWorkedDate(Date workedDate) {
        this.workedDate = workedDate;
    }
}
