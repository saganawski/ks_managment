package com.ks.management.report.dao;

import com.ks.management.report.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConversionDao extends JpaRepository<Conversion, Integer> {
}
