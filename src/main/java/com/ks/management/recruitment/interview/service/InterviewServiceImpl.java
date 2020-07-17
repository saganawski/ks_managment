package com.ks.management.recruitment.interview.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private JpaInterview jpaInterview;

    @Autowired
    private JpaEmployeeRepo jpaEmployeeRepo;

    @Override
    public List<Interview> getAllInterviews() {
        return jpaInterview.findAll();
    }

    @Override
    public Interview createInterview(Interview interview) {
        //TODO: find active user as employee and createdBy / updatedBy
        final Employee scheduler = jpaEmployeeRepo.findById(2).orElse(null);
        if(scheduler == null){
            throw  new RuntimeException("Could not find employee to set a scheduler!");
        }
        interview.setScheduler(scheduler);
        interview.setCreatedBy(-1);
        interview.setUpdatedBy(-1);

        return jpaInterview.save(interview);
    }
}
