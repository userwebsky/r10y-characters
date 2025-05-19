package pl.robertprogramista.r10y_characters.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;
import pl.robertprogramista.r10y_characters.exception.CharacterNotFoundException;
import pl.robertprogramista.r10y_characters.mapper.CharacterMapper;
import pl.robertprogramista.r10y_characters.repository.CharacterRepository;
import pl.robertprogramista.r10y_characters.entity.Character;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;
    private final RickAndMortyService rickAndMortyService;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> saveCharacters() {
        log.info("Starting asynchronous task to fetch and save characters.");
        try {
            List<R10yCharacterDto> apiCharacters = rickAndMortyService.fetchAllCharacters();
            int newCharactersCount = 0;
            int skippedCharactersCount = 0;

            for (R10yCharacterDto dto : apiCharacters) {
                if (!characterRepository.existsByExternalId(dto.getId())) {
                    Character entity = CharacterMapper.mapToEntity(dto);
                    characterRepository.save(entity);
                    newCharactersCount++;
                } else {
                    skippedCharactersCount++;
                }
            }
            log.info("Finished fetching characters. Saved {} new characters, skipped {} (already exist).",
                    newCharactersCount, skippedCharactersCount);
        } catch (Exception e) {
            log.error("Error during fetching and saving characters: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterResponseDto> getCharacterByName(String name) {
        log.debug("Searching for characters with name containing: {}", name);
        List<Character> entities = characterRepository.findByNameContainingIgnoreCase(name);
        if (entities.isEmpty()) {
            throw new CharacterNotFoundException("No characters found with name containing: " + name);
        }
        return entities.stream()
                .map(CharacterMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }
}
