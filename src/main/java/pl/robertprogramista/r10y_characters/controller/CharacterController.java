package pl.robertprogramista.r10y_characters.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;
import pl.robertprogramista.r10y_characters.service.CharacterService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/characters")
@Tag(name = "Character Controller", description = "Endpoints for managing and fetching Rick and Morty characters")
public class CharacterController {
    private final CharacterService characterService;

    @PostMapping("/fetch")
    @Operation(summary = "Fetch characters from The Rick and Morty API",
            description = "Asynchronously fetches character data from the external API and saves new characters to the database. Duplicates are ignored.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Request accepted for processing. Data fetching started asynchronously."),
                    @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
            })
    public ResponseEntity<String> fetchCharactersFromApi() {
        log.info("Received request to fetch characters from API.");
        CompletableFuture<Void> future = characterService.saveCharacters();

        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Async character fetching failed: {}", throwable.getMessage(), throwable);
            } else {
                log.info("Async character fetching completed successfully.");
            }
        });
        return ResponseEntity.accepted().body("Character data fetching process started asynchronously.");
    }

    @GetMapping("/search")
    @Operation(summary = "Get characters by name",
            description = "Retrieves a list of characters from the database whose names contain the provided query string (case-insensitive).",
            parameters = {
                    @Parameter(name = "name", description = "Name (or part of the name) of the character to search for", required = true, example = "Rick Sanchez")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved characters",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "No characters found matching the criteria"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<CharacterResponseDto>> getCharacterByName(
            @RequestParam(required = true) String name) {
        log.info("Received request to search characters by name: {}", name);
        List<CharacterResponseDto> characters = characterService.getCharacterByName(name);
        if (characters.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(characters);
    }
}
