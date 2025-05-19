package pl.robertprogramista.r10y_characters.service;

import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;

import java.util.List;

public interface RickAndMortyService {
    /**
     * Downloads characters from the Rick and Morty API.
     * @return List of characters from the Rick and Morty API.
     */
    List<R10yCharacterDto> fetchAllCharacters();
}
