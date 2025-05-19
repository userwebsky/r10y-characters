package pl.robertprogramista.r10y_characters.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class R10yCharacterDto {
    private int id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private R10yCharacterDetailsDto origin;
    private R10yCharacterDetailsDto location;
    @JsonProperty("image")
    private String imageUrl;
    private List<String> episode;
    private String url;
    private OffsetDateTime created;
}
