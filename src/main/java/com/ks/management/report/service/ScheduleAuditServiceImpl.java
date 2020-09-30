package com.ks.management.report.service;

import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.dao.JpaScheduleAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleAuditServiceImpl implements ScheduleAuditService {
    @Autowired
    private JpaScheduleAudit jpaScheduleAudit;

    @Override
    public List<ScheduleAudit> getScheduleAudits() {
        return jpaScheduleAudit.findAll();
    }
}
