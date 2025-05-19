package pl.robertprogramista.r10y_characters.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false)
    private int externalId;

    @Column(nullable = false)
    private String name;

    private String status;
    private String species;
    private String type;
    private String gender;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "origin_url")
    private String originUrl;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_url")
    private String locationUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "character_url")
    private String characterUrl;

    @Column(name = "external_created_at")
    private OffsetDateTime externalCreatedAt;

    @OneToMany(
            mappedBy = "character",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<EpisodeAppearance> episodeAppearances = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Character(int externalId, String name, String status, String species, String type, String gender,
                     String originName, String originUrl, String locationName, String locationUrl, String imageUrl,
                     List<String> episode, String url, OffsetDateTime created) {
        this.externalId = externalId;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.originName = originName;
        this.originUrl = originUrl;
        this.locationName = locationName;
        this.locationUrl = locationUrl;
        this.imageUrl = imageUrl;
        this.characterUrl = url;
        this.externalCreatedAt = created;

        if (episode != null) {
            episode.forEach(this::addEpisodeAppearance);
        }
    }

    private void addEpisodeAppearance(String episodeUrl) {
        EpisodeAppearance appearance = new EpisodeAppearance(episodeUrl, this);
        this.episodeAppearances.add(appearance);
    }
}
