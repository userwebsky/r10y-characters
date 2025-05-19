# R (i c k a n d m o r t) y   characters

## 🚀 Uruchomienie w Docker

### 1. Zbuduj projekt Mavenem (upewnij się, że JAR jest tworzony)
```mvn clean package```

Sprawdź, czy plik target/r10y-characters-0.0.1-SNAPSHOT.jar istnieje.

### 2. Uruchom Docker Compose poleceniem

```docker-compose up --build```

## 📚 API

### 🔁 POST `/api/v1/characters/fetch`
Asynchroniczne pobranie danych z API Rick and Morty.

**curl**:
```bash
curl -X 'POST' \
  'http://localhost:7777/api/v1/characters/fetch' \
  -H 'accept: */*' \
  -d ''
```

### 🔍 GET `/api/v1/characters/search?name=`
Zwraca postacie pasujące do nazwy.

**Przykład odpowiedzi**:
```json
[
  {
    "id": 293,
    "externalId": 293,
    "name": "Rick Sanchez",
    "status": "Dead",
    "species": "Human",
    "type": "",
    "gender": "Male",
    "originName": "Earth (Replacement Dimension)",
    "locationName": "Earth (Replacement Dimension)",
    "imageUrl": "https://rickandmortyapi.com/api/character/avatar/293.jpeg",
    "episodeAppearances": [
      "https://rickandmortyapi.com/api/episode/6"
    ],
    "characterUrl": "https://rickandmortyapi.com/api/character/293",
    "createdAt": "2025-05-19T17:55:59"
  }
]
```

### 📄 Swagger UI
[http://localhost:7777/swagger-ui/index.html](http://localhost:7777/swagger-ui/index.html)

## 💻 Uruchomienie lokalnie

### Wymagania Wstępne
1. Java JDK 21
2. Działająca instancja MariaDB

### Konfiguracja
1. Sklonuj repozytorium

2. Dostosuj konfigurację w pliku ```application.properties```
W szczególności ustaw: 
```yaml
spring.datasource.username=
spring.datasource.password=
```
Ewentualnie zmień port, jeśli jest zajęty:
```yaml
server.port=7777
```

3. Utwórz bazę danych dla aplikacji
```sql
        CREATE DATABASE `r10y-character` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
``` 

4. Utwórz użytkownika bazy danych i nadaj mu uprawnienia do nowo utworzonej bazy.
Zwróć uwagę, aby użytkownik i hasło były takie same jak w pliku ```application.properties```

```sql
        CREATE USER 'user_local'@'localhost' IDENTIFIED BY 'password_local';
        GRANT ALL PRIVILEGES ON `r10y-character`.* TO 'user_local'@'localhost';
``` 

5. Zbuduj projekt i uruchom poleceniem:
```shell
mvn spring-boot:run
```

##  🤔 TODO
1. Autentyfikacja i autoryzacja
2. Optymalizacja pod kątem wydajności