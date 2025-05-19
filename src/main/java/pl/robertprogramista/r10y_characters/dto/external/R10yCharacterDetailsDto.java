package pl.robertprogramista.r10y_characters.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class R10yCharacterDetailsDto {
    private String name;
    private String url;
}
