package pl.robertprogramista.r10y_characters.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterResponseDto;
import pl.robertprogramista.r10y_characters.exception.EmptyResultException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RickAndMortyServiceImpl implements RickAndMortyService {
    private final RestTemplate restTemplate;

    @Value("${rickandmorty.api.url}")
    private String apiUrl;

    public RickAndMortyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<R10yCharacterDto> fetchAllCharacters() {
        List<R10yCharacterDto> allCharacters = new ArrayList<>();
        String nextUrl = apiUrl + "/character";

        while (nextUrl != null && !nextUrl.isEmpty()) {
            try {
                log.info("Fetching characters from URL: {}", nextUrl);
                R10yCharacterResponseDto response = restTemplate.getForObject(nextUrl, R10yCharacterResponseDto.class);

                if (response.getResults() == null || response.getInfo() == null) {
                    throw new EmptyResultException("Empty result from Rick and Morty API.");
                }

                allCharacters.addAll(response.getResults());
                nextUrl = response.getInfo().getNext();
            } catch (RestClientException | EmptyResultException e) {
                log.error("Error fetching characters from API at URL: {}. Error: {}", nextUrl, e.getMessage());
                throw e;
            }
        }

        log.info("Fetched a total of {} characters from the API.", allCharacters.size());
        return allCharacters;
    }
}
