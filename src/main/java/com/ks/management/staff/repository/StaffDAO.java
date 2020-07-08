package com.ks.management.staff.repository;

import java.util.List;

import com.ks.management.staff.Staff;

public interface StaffDAO {

	List<Staff> getStaffs();

	Staff findById(Integer id);

}
