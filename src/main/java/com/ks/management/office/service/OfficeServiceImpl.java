package com.ks.management.office.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ks.management.location.Location;
import com.ks.management.location.dao.JpaLocationDao;
import com.ks.management.security.UserPrincipal;
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
	@Autowired
	private JpaLocationDao jpaLocationDao;
	
	@Override
	public List<Office> getOffices() {
		return jpaOfficeRepo.findAll().stream()
				.filter(o -> o.getCompleted() != null)
				.filter(o -> !o.getCompleted())
				.collect(Collectors.toList());
	}

	@Override
	public Office createOffice(Office office, UserPrincipal userPrincipal) {
		final Integer userId = userPrincipal.getUserId();
		office.setCreatedBy(userId);
		office.setUpdatedBy(userId);

		final Location location = office.getLocation();
		location.setUpdatedBy(userId);
		location.setCreatedBy(userId);
		jpaLocationDao.save(location);

		office.setLocation(location);

		return jpaOfficeRepo.save(office);
	}

	@Override
	public Office getOfficeById(Integer officeId) {
		return jpaOfficeRepo.getOne(officeId);
	}

	@Override
	public Office updateOffice(Office office, UserPrincipal userPrincipal) {
		final Integer userId = userPrincipal.getUserId();
		office.setUpdatedBy(userId);

		final Location location = office.getLocation();;
		jpaLocationDao.save(location);

		office.setLocation(location);

		return jpaOfficeRepo.save(office);
	}

	@Override
	public Office updateOffice(Office office, Integer officeId, UserPrincipal userPrincipal) {
		final Integer userId = userPrincipal.getUserId();
		office.setUpdatedBy(userId);

		final Location location = office.getLocation();;
		jpaLocationDao.save(location);

		office.setLocation(location);

		return jpaOfficeRepo.save(office);
	}

	@Override
	public List<Office> getAllOffices() {
		return jpaOfficeRepo.findAll();
	}

}
