package com.ks.management.staff.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.ks.management.staff.Staff;

public interface StaffService {

	List<Staff> getStaffs();

	Staff update(@NotNull Staff staff);

	Staff create(@Valid Staff staff);

}
