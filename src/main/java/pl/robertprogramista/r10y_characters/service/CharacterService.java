package pl.robertprogramista.r10y_characters.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CharacterService {
    /**
     * Saves characters downloaded from Rick and Morty API.
     */
    @Async("taskExecutor")
    @Transactional
    CompletableFuture<Void> saveCharacters();

    /**
     * Retrieves the character by name.
     * @param name Character name.
     * @return List of characters found by name.
     */
    @Transactional(readOnly = true)
    List<CharacterResponseDto> getCharacterByName(String name);
}
