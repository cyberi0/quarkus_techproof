package com.techproof.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import com.techproof.entity.EmployeeWorkedHours;
@Entity
@Table(name = "EMPLOYEES")
public class Employee extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeSeq")
    @SequenceGenerator(name = "employeeSeq", sequenceName = "EMPLOYEES_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GENDER_ID")
    public Gender gender;

    @ManyToOne
    @JoinColumn(name = "JOB_ID")
    public Job job;

    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "LAST_NAME", nullable = false)
    public String lastName;

    @Column(name = "BIRTHDATE", nullable = false)
    public LocalDate birthDate;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<EmployeeWorkedHours> workedHours;

    public Employee() {
    }

    public Employee(Long id, Gender gender, Job job, String name, String lastName, LocalDate birthDate, List<EmployeeWorkedHours> workedHours) {
        this.id = id;
        this.gender = gender;
        this.job = job;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.workedHours = workedHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<EmployeeWorkedHours> getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(List<EmployeeWorkedHours> workedHours) {
        this.workedHours = workedHours;
    }
}
