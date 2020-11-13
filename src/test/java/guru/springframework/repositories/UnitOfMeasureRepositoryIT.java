package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByDescription_Teaspoon_returnsIsPresent() {

        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertTrue(uomOptional.isPresent());
        assertEquals("Teaspoon",uomOptional.get().getDescription());
    }

    @Test
    void findByDescription_teaspoon_returnsIsNotPresent() {
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("teaspoon");
        assertFalse(uomOptional.isPresent());
    }
}