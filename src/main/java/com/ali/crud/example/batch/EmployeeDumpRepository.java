package com.ali.crud.example.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDumpRepository extends JpaRepository<EmployeeDump, Long> {
}