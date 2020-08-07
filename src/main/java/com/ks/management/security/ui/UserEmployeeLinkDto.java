package com.ks.management.security.ui;

import com.ks.management.employee.Employee;
import com.ks.management.security.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEmployeeLinkDto {
    private User user;
    private Employee employee;
}
