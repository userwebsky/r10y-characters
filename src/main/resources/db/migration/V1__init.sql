CREATE TABLE characters
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id         INT UNIQUE   NOT NULL,
    name                VARCHAR(255) NOT NULL,
    status              VARCHAR(50),
    species             VARCHAR(100),
    type                VARCHAR(100),
    gender              VARCHAR(50),
    origin_name         VARCHAR(255),
    origin_url          VARCHAR(512),
    location_name       VARCHAR(255),
    location_url        VARCHAR(512),
    image_url           VARCHAR(512),
    character_url       VARCHAR(512),
    external_created_at TIMESTAMP,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_character_name ON characters (name);

CREATE TABLE episode_appearances
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    character_id BIGINT       NOT NULL,
    episode_url  VARCHAR(512) NOT NULL,
    CONSTRAINT fk_episode_character FOREIGN KEY (character_id) REFERENCES characters (id) ON DELETE CASCADE
);