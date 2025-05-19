package pl.robertprogramista.r10y_characters.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;
import pl.robertprogramista.r10y_characters.exception.CharacterNotFoundException;
import pl.robertprogramista.r10y_characters.service.CharacterService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CharacterController.class)
class CharacterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CharacterService characterService;

    @Test
    void shouldReturnAccepted() throws Exception {
        when(characterService.saveCharacters()).thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(post("/api/v1/characters/fetch"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Character data fetching process started asynchronously."));
    }

    @Test
    void shouldReturnCharacterList() throws Exception {
        CharacterResponseDto characterDto = CharacterResponseDto.builder().name("Rick Sanchez").build();
        List<CharacterResponseDto> characters = Collections.singletonList(characterDto);

        when(characterService.getCharacterByName("Rick Sanchez")).thenReturn(characters);

        mockMvc.perform(get("/api/v1/characters/search").param("name", "Rick Sanchez"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Rick Sanchez"));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        when(characterService.getCharacterByName(anyString())).thenThrow(new CharacterNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/characters/search").param("name", "NonExistent"))
                .andExpect(status().isNotFound());
    }
}