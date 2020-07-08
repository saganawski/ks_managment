package com.ks.management.staff.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.management.staff.Staff;
import com.ks.management.staff.repository.JpaStaffRepo;
import com.ks.management.staff.repository.StaffDAO;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
	@Autowired
	private StaffDAO staffDao;
	
	@Autowired
	private JpaStaffRepo jpaStaffRepo;
	
	@Override
	public List<Staff> getStaffs() {
		return jpaStaffRepo.findAll();
	}

	@Override
	public Staff update(Staff staff) {
		final Optional<Staff> uStaff =jpaStaffRepo.findById(staff.getId());
		uStaff.ifPresent(s -> jpaStaffRepo.save(staff));
//		final Staff uStaff =jpaStaffRepo.save(staff);
		return uStaff.get();
	}

	@Override
	public Staff create(@Valid Staff staff) {
		return jpaStaffRepo.save(staff);
	}

}
