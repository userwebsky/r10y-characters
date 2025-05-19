package pl.robertprogramista.r10y_characters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import okhttp3.mockwebserver.MockWebServer;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterResponseDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RickAndMortyServiceTest {
    private static MockWebServer mockWebServer;
    private RickAndMortyService rickAndMortyApiService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        rickAndMortyApiService = new RickAndMortyServiceImpl(new RestTemplate());
        try {
            java.lang.reflect.Field apiUrlField = RickAndMortyServiceImpl.class.getDeclaredField("apiUrl");
            apiUrlField.setAccessible(true);
            apiUrlField.set(rickAndMortyApiService, baseUrl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnCharactersAndHandlePagination() throws Exception {
        R10yCharacterResponseDto page1Response = new R10yCharacterResponseDto();
        R10yCharacterResponseDto.Info info1 = new R10yCharacterResponseDto.Info();
        info1.setNext(mockWebServer.url("/api/character?page=2").toString());
        R10yCharacterDto char1 = new R10yCharacterDto();
        char1.setId(1);
        char1.setName("Rick Sanchez");
        page1Response.setInfo(info1);
        page1Response.setResults(Collections.singletonList(char1));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(page1Response))
                .addHeader("Content-Type", "application/json"));

        R10yCharacterResponseDto page2Response = new R10yCharacterResponseDto();
        R10yCharacterResponseDto.Info info2 = new R10yCharacterResponseDto.Info();
        info2.setNext(null);
        R10yCharacterDto char2 = new R10yCharacterDto();
        char2.setId(2);
        char2.setName("Morty Smith");
        page2Response.setInfo(info2);
        page2Response.setResults(Collections.singletonList(char2));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(page2Response))
                .addHeader("Content-Type", "application/json"));

        List<R10yCharacterDto> characters = rickAndMortyApiService.fetchAllCharacters();

        assertEquals(2, characters.size());
        assertEquals("Rick Sanchez", characters.get(0).getName());
        assertEquals("Morty Smith", characters.get(1).getName());
    }

    @Test
    void shouldReturnEmptyListOrHandleError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        Exception exception = assertThrows(
                HttpServerErrorException.InternalServerError.class,
                () -> rickAndMortyApiService.fetchAllCharacters());

        assertTrue(exception.getMessage().contains("500 Server Error on GET request"));
    }
}