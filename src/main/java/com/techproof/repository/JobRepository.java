package com.techproof.repository;

import com.techproof.entity.Job;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JobRepository implements PanacheRepository<Job> {
}
