package com.ks.management.security.dao;

import com.ks.management.security.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserEmployeeJpa extends JpaRepository<UserEmployee, Integer> {
    @Modifying
    @Query(value = "DELETE FROM user_employee " +
            "WHERE user_id = ?1 ",
            nativeQuery = true)
    void deleteAllByUserId(Integer activeUser);

    UserEmployee findByUserId(Integer userId);
}
