package pl.robertprogramista.r10y_characters.mapper;

import org.springframework.stereotype.Component;
import pl.robertprogramista.r10y_characters.dto.CharacterResponseDto;
import pl.robertprogramista.r10y_characters.dto.external.R10yCharacterDto;
import pl.robertprogramista.r10y_characters.entity.Character;
import pl.robertprogramista.r10y_characters.entity.EpisodeAppearance;

@Component
public class CharacterMapper {
    private CharacterMapper() {}

    public static Character mapToEntity(R10yCharacterDto dto) {
        return new Character(
                dto.getId(),
                dto.getName(),
                dto.getStatus(),
                dto.getSpecies(),
                dto.getType(),
                dto.getGender(),
                dto.getOrigin() != null ? dto.getOrigin().getName() : null,
                dto.getOrigin() != null ? dto.getOrigin().getUrl() : null,
                dto.getLocation() != null ? dto.getLocation().getName() : null,
                dto.getLocation() != null ? dto.getLocation().getUrl() : null,
                dto.getImageUrl(),
                dto.getEpisode(),
                dto.getUrl(),
                dto.getCreated()
        );
    }

    public static CharacterResponseDto mapToResponseDto(Character entity) {
        return CharacterResponseDto.builder()
                .id(entity.getId())
                .externalId(entity.getExternalId())
                .name(entity.getName())
                .status(entity.getStatus())
                .species(entity.getSpecies())
                .type(entity.getType())
                .gender(entity.getGender())
                .originName(entity.getOriginName())
                .locationName(entity.getLocationName())
                .imageUrl(entity.getImageUrl())
                .episodeAppearances(entity.getEpisodeAppearances().stream().map(EpisodeAppearance::getEpisodeUrl).toList())
                .characterUrl(entity.getCharacterUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
