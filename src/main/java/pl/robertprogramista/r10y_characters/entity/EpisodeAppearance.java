package pl.robertprogramista.r10y_characters.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "episode_appearances")
public class EpisodeAppearance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "episode_url")
    private String episodeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    public EpisodeAppearance(String episodeUrl, Character character) {
        this.episodeUrl = episodeUrl;
        this.character = character;
    }
}
