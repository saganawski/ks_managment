package com.ks.management.office.dao;

import com.ks.management.location.Location;
import com.ks.management.office.Office;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class JpaOfficeRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaOfficeRepo jpaOfficeRepo;

    private Office testOffice;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        testLocation = new Location("TestState", "TestCity", "123 Test St", "", "12345", 1, 1);
        testLocation = entityManager.persistAndFlush(testLocation);

        testOffice = new Office();
        testOffice.setName("Test Office");
        testOffice.setLocation(testLocation);
        testOffice.setCompleted(false);
        testOffice.setCreatedBy(1);
        testOffice.setUpdatedBy(1);
    }

    @Test
    void findByName_ShouldReturnOfficeWhenExists() {
        Office savedOffice = entityManager.persistAndFlush(testOffice);

        Optional<Office> result = jpaOfficeRepo.findByName("Test Office");

        assertTrue(result.isPresent());
        assertEquals(savedOffice.getId(), result.get().getId());
        assertEquals("Test Office", result.get().getName());
    }

    @Test
    void findByName_ShouldReturnEmptyWhenNotExists() {
        Optional<Office> result = jpaOfficeRepo.findByName("Non-existent Office");

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_ShouldReturnEmptyWhenNameIsNull() {
        Optional<Office> result = jpaOfficeRepo.findByName(null);

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_ShouldBeCaseSensitive() {
        entityManager.persistAndFlush(testOffice);

        Optional<Office> result = jpaOfficeRepo.findByName("test office");

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_ShouldHandleSpecialCharacters() {
        testOffice.setName("Test Office & Co.");
        entityManager.persistAndFlush(testOffice);

        Optional<Office> result = jpaOfficeRepo.findByName("Test Office & Co.");

        assertTrue(result.isPresent());
        assertEquals("Test Office & Co.", result.get().getName());
    }

    @Test
    void save_ShouldPersistOffice() {
        Office savedOffice = jpaOfficeRepo.save(testOffice);

        assertNotNull(savedOffice.getId());
        assertEquals("Test Office", savedOffice.getName());
        assertEquals(testLocation.getId(), savedOffice.getLocation().getId());
    }

    @Test
    void findAll_ShouldReturnAllOffices() {
        Office office1 = new Office();
        office1.setName("Office 1");
        office1.setLocation(testLocation);
        office1.setCompleted(false);
        office1.setCreatedBy(1);
        office1.setUpdatedBy(1);

        Office office2 = new Office();
        office2.setName("Office 2");
        office2.setLocation(testLocation);
        office2.setCompleted(true);
        office2.setCreatedBy(1);
        office2.setUpdatedBy(1);

        entityManager.persistAndFlush(office1);
        entityManager.persistAndFlush(office2);

        List<Office> result = jpaOfficeRepo.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(o -> "Office 1".equals(o.getName())));
        assertTrue(result.stream().anyMatch(o -> "Office 2".equals(o.getName())));
    }

    @Test
    void findById_ShouldReturnOfficeWhenExists() {
        Office savedOffice = entityManager.persistAndFlush(testOffice);

        Optional<Office> result = jpaOfficeRepo.findById(savedOffice.getId());

        assertTrue(result.isPresent());
        assertEquals(savedOffice.getId(), result.get().getId());
        assertEquals("Test Office", result.get().getName());
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        Optional<Office> result = jpaOfficeRepo.findById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_ShouldRemoveOffice() {
        Office savedOffice = entityManager.persistAndFlush(testOffice);
        Integer officeId = savedOffice.getId();

        jpaOfficeRepo.deleteById(officeId);
        entityManager.flush();

        Optional<Office> result = jpaOfficeRepo.findById(officeId);
        assertFalse(result.isPresent());
    }

    @Test
    void update_ShouldModifyOffice() {
        Office savedOffice = entityManager.persistAndFlush(testOffice);
        savedOffice.setName("Updated Office Name");
        savedOffice.setCompleted(true);

        Office updatedOffice = jpaOfficeRepo.save(savedOffice);

        assertEquals("Updated Office Name", updatedOffice.getName());
        assertTrue(updatedOffice.getCompleted());
    }
}