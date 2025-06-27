package com.ks.management.office;

import com.ks.management.office.dao.JpaOfficeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
@ActiveProfiles("test")
class OfficeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaOfficeRepo officeRepository;

    @Test
    void testFindByName_NotFound() {
        Optional<Office> foundOffice = officeRepository.findByName("Non-existent Office");
        assertFalse(foundOffice.isPresent());
    }

    @Test
    void testSaveOffice_WithoutRelationships() {
        Office office = new Office();
        office.setName("Simple Test Office");
        office.setCompleted(false);
        office.setCreatedBy(1);
        office.setUpdatedBy(1);
        // Not setting location to avoid relationship issues

        Office savedOffice = officeRepository.save(office);
        entityManager.flush();

        assertNotNull(savedOffice);
        assertEquals("Simple Test Office", savedOffice.getName());
        assertFalse(savedOffice.getCompleted());
    }

    @Test
    void testFindAll_EmptyRepository() {
        assertTrue(officeRepository.findAll().isEmpty());
    }

    @Test
    void testRepositoryExists() {
        assertNotNull(officeRepository);
    }
}