package pl.robertprogramista.r10y_characters.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CharacterResponseDto {
    private Long id;
    private int externalId;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private String originName;
    private String locationName;
    private String imageUrl;
    private List<String> episodeAppearances;
    private String characterUrl;
    private LocalDateTime createdAt;
}
