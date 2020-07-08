package com.ks.management.office.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
@Service
@Transactional
public class OfficeServiceImpl implements OfficeService {
	@Autowired
	private JpaOfficeRepo jpaOfficeRepo;
	
	@Override
	public List<Office> getOffices() {
		System.out.println("in impl");
		return jpaOfficeRepo.findAll();
	}

}
