package com.techproof.repository;

import com.techproof.entity.Gender;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenderRepository implements PanacheRepository<Gender> {
}
