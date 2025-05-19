package pl.robertprogramista.r10y_characters.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.robertprogramista.r10y_characters.entity.Character;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CharacterRepositoryTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void mariaDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private CharacterRepository characterRepository;

    private Character rick;
    private Character morty;

    @BeforeEach
    void setUp() {
        characterRepository.deleteAll();

        rick = new Character(1, "Rick Sanchez", "Alive", "Human", "", "Male",
                "Earth (C-137)", "", "Earth (Replacement Dimension)", "", "rick.png", List.of("abc"), "", OffsetDateTime.now());
        morty = new Character(2, "Morty Smith", "Alive", "Human", "", "Male",
                "Earth (C-137)", "", "Earth (Replacement Dimension)", "", "morty.png", List.of("abc"), "", OffsetDateTime.now());

        characterRepository.save(rick);
        characterRepository.save(morty);
    }

    @Test
    void shouldReturnCharacter() {
        Optional<Character> found = characterRepository.findByExternalId(rick.getExternalId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rick Sanchez");
    }

    @Test
    void shouldReturnEmpty() {
        Optional<Character> found = characterRepository.findByExternalId(999);
        assertThat(found).isNotPresent();
    }

    @Test
    void shouldReturnMatchingCharacters() {
        List<Character> foundRicks = characterRepository.findByNameContainingIgnoreCase("rick");
        assertThat(foundRicks).hasSize(1).extracting(Character::getName).contains("Rick Sanchez");

        List<Character> foundSmiths = characterRepository.findByNameContainingIgnoreCase("smith");
        assertThat(foundSmiths).hasSize(1).extracting(Character::getName).contains("Morty Smith");

        List<Character> foundS = characterRepository.findByNameContainingIgnoreCase("s");
        assertThat(foundS).hasSize(2);
    }

    @Test
    void shouldReturnEmptyList() {
        List<Character> found = characterRepository.findByNameContainingIgnoreCase("NonExistentName");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnTrue() {
        boolean exists = characterRepository.existsByExternalId(morty.getExternalId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalse() {
        boolean exists = characterRepository.existsByExternalId(999);
        assertThat(exists).isFalse();
    }
}