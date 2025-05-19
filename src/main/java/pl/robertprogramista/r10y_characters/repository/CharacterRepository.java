package pl.robertprogramista.r10y_characters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.robertprogramista.r10y_characters.entity.Character;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long>  {
    Optional<Character> findByExternalId(int externalId);

    List<Character> findByNameContainingIgnoreCase(String name);

    boolean existsByExternalId(int externalId);
}
