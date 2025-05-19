package pl.robertprogramista.r10y_characters.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;
import pl.robertprogramista.r10y_characters.exception.CharacterNotFoundException;
import pl.robertprogramista.r10y_characters.repository.CharacterRepository;
import pl.robertprogramista.r10y_characters.entity.Character;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {
    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private RickAndMortyService rickAndMortyService;

    @InjectMocks
    private CharacterServiceImpl characterService;

    @Test
    void shouldSaveNewCharacters() throws InterruptedException, ExecutionException {
        R10yCharacterDto apiDto = new R10yCharacterDto();
        apiDto.setId(1);
        apiDto.setName("Rick");

        when(rickAndMortyService.fetchAllCharacters()).thenReturn(Collections.singletonList(apiDto));
        when(characterRepository.existsByExternalId(1)).thenReturn(false);
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompletableFuture<Void> future = characterService.saveCharacters();
        future.get();

        verify(characterRepository, times(1)).save(any(Character.class));
        verify(characterRepository, times(1)).existsByExternalId(1);
    }

    @Test
    void shouldCompleteExceptionally() {
        when(rickAndMortyService.fetchAllCharacters()).thenThrow(new RuntimeException("API error"));

        CompletableFuture<Void> future = characterService.saveCharacters();

        assertThrows(ExecutionException.class, future::get);
        assertTrue(future.isCompletedExceptionally());
    }


    @Test
    void shouldReturnDto() {
        Character entity = new Character();
        entity.setId(1L);
        entity.setExternalId(101);
        entity.setName("Rick Sanchez");

        when(characterRepository.findByNameContainingIgnoreCase("Rick")).thenReturn(Collections.singletonList(entity));

        List<CharacterResponseDto> resultDtos = characterService.getCharacterByName("Rick");

        assertFalse(resultDtos.isEmpty());
        assertEquals("Rick Sanchez", resultDtos.get(0).getName());
        assertEquals(101, resultDtos.get(0).getExternalId());
        verify(characterRepository, times(1)).findByNameContainingIgnoreCase("Rick");
    }

    @Test
    void shouldThrowException() {
        when(characterRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Collections.emptyList());

        assertThrows(CharacterNotFoundException.class, () -> {
            characterService.getCharacterByName("NonExistent");
        });
        verify(characterRepository, times(1)).findByNameContainingIgnoreCase("NonExistent");
    }
}