package com.ks.management.employee.service;

import com.ks.management.employee.EmployeeNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeNoteRepo extends JpaRepository<EmployeeNote, Integer> {
}
