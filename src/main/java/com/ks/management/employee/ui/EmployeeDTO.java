package com.ks.management.employee.ui;

import com.ks.management.employee.Employee;
import com.ks.management.employee.EmployeeNote;
import com.ks.management.office.Office;
import com.ks.management.position.Position;
import com.ks.management.recruitment.training.TrainingConfirmationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    private Employee employee;

    private Set<Office> officeOptions = new HashSet<>();
    private Set<Position> positionOptions = new HashSet<>();
}
