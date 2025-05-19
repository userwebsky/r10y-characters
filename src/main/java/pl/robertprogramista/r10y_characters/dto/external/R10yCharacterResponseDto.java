package pl.robertprogramista.r10y_characters.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class R10yCharacterResponseDto {
    private Info info;
    private List<R10yCharacterDto> results;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        private int count;
        private int pages;
        private String next;
        private String prev;
    }
}
