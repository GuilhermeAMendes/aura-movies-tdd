# Módulo de Recomendação de Filmes

Este sistema foi desenvolvido como parte da disciplina de Validação e Teste de Software, com o objetivo de aplicar práticas de desenvolvimento como Domain-Driven Design (DDD), Test-Driven Development (TDD) e Behavior-Driven Development (BDD).

O sistema permite que um usuário (cliente) avalie filmes de um catálogo e receba recomendações personalizadas com base em seu histórico de avaliações.

Todos os testes foram feitos pensando em cobertura total de todos os critérios e decisões do package `domain` do backend. Além de testes para matar mutações geradas pela ferramenta Pitest.

### Como rodar a aplicação

Abra a pasta `backend/` no IntelliJ, depois rode o arquivo principal `AuraMoviesTddAPi`. Isto deve subir o backend da aplicação com dados mockados.

Depois, abra a pasta `frontend/` no VSCode, ou qualquer terminal, e digite:

```bash
npm install
```
Para instalar as dependências. Depois:


```bash
npm run dev
```

Pronto! Agora acesse o frontend via localhost:3000.


### Raiz do Agregado

* *User*: É o agregado responsável por gerenciar o perfil e as interações de um usuário. Ele encapsula todas as avaliações feitas por aquele cliente e garante regras de negócio. Todas as operações, como adicionar uma nova avaliação, devem passar pelo objeto User.

  Possível implementação:

  ```java
  public class User {
      private final UUID id;
      private String nome;
      private final List<Rating> ratings;
  }
  ```

* *Movie*: Representa um filme no catálogo do sistema. É um agregado mais simples, responsável por manter a consistência de suas próprias informações, como título e gênero.

  Possível implementação:

  ```java
  public class Movie {
      private final MovieId movieId;
      private String title;
      private Genre genre;
  }
  ```

### Entidade

* *Rating*: É uma entidade interna ao agregado User. Cada avaliação possui uma identidade única e representa a nota que um cliente deu a um filme específico. Ela só existe dentro do contexto de um User.

  Possível implementação:

  ```java
  public class Rating {
      private final MovieId movieId;
      private final Grade grade;
      private final LocalDateTime lastGradedAt;
  }
  ```

### Objeto de Valor

* *Grade*: Representa a nota (de 0 a 5) de uma avaliação. Sua lógica interna garante que uma nota inválida (como -1 ou 6) não possa ser criada.

  Possível implementacão:

  ```java
  public record Grade(int value) {
      // validação: value deve estar entre 0 e 5
  }
  ```

* *MovieId*: Representa o identificador de um Movie.

  Possível implementação:

  ```java
  public class MovieId {
      private final UUID id;
  }
  ```

### Enumerado

* *Genre*: Representa o gênero de um filme.

  Possível implementação:

  ```java
  public enum Genre {
      ACTION,
      ROMANCE,
      HORROR,
      ...
  }
  ```
  
