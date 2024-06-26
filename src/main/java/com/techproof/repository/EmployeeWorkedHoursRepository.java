package com.techproof.repository;

import com.techproof.entity.EmployeeWorkedHours;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class EmployeeWorkedHoursRepository implements PanacheRepository<EmployeeWorkedHours> {

}
