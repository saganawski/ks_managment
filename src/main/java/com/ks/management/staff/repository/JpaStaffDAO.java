package com.ks.management.staff.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ks.management.staff.Staff;
@Repository
public class JpaStaffDAO  implements StaffDAO {
	
	@PersistenceContext
	private EntityManager entityManger;
	
	@Override
	public List<Staff> getStaffs() {
		try {
			final List<Staff> staffs = (List<Staff>) entityManger
					.createQuery("SELECT s FROM Staff s", Staff.class)
					.getResultList();
			return staffs;
		}catch(Exception e) {
			throw new IllegalArgumentException("Could not find staff", e);
		}
		
	}

	@Override
	public Staff findById(Integer staffId) {
		try {
			final Staff staff = entityManger
					.createQuery("FROM staff WHERE id= :staffId", Staff.class)
					.setParameter("staffId", staffId)
					.getSingleResult();
			return staff;
		}catch(Exception e) {
			throw new RuntimeException("Could not find Staff with UserId: " + staffId,e);
		}
	}

}
