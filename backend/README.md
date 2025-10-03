# Módulo de Recomendação de Filmes

Este sistema foi desenvolvido como parte da disciplina de Validação e Teste de Software, com o objetivo de aplicar práticas de desenvolvimento como Domain-Driven Design (DDD), Test-Driven Development (TDD) e Behavior-Driven Development (BDD).

O sistema permite que um usuário (cliente) avalie filmes de um catálogo e receba recomendações personalizadas com base em seu histórico de avaliações.

### Raiz do Agregado

* *Cliente*: É o agregado responsável por gerenciar o perfil e as interações de um usuário. Ele encapsula todas as avaliações feitas por aquele cliente e garante regras de negócio. Todas as operações, como adicionar uma nova avaliação, devem passar pelo objeto Cliente.

  Possível implementação:

  ```java
  public class Cliente {
      private final UUID id;
      private String nome;
      private final List<Avaliacao> avaliacoes;
  }
  ```

* *Filme*: Representa um filme no catálogo do sistema. É um agregado mais simples, responsável por manter a consistência de suas próprias informações, como título e gênero.

  Possível implementação:

  ```java
  public class Filme {
      private final FilmeId id;
      private String titulo;
      private Genero genero;
  }
  ```

### Entidade

* *Avaliacao*: É uma entidade interna ao agregado Cliente. Cada avaliação possui uma identidade única e representa a nota que um cliente deu a um filme específico. Ela só existe dentro do contexto de um Cliente.

  Possível implementação:

  ```java
  public class Avaliacao {
      private final UUID id;
      private final FilmeId filmeId;
      private final Nota nota;
      private final LocalDateTime dataDaAvaliacao;
  }
  ```

### Objeto de Valor

* *Nota*: Representa a nota (de 1 a 5) de uma avaliação. Sua lógica interna garante que uma nota inválida (como 0 ou 6) não possa ser criada.

  Possível implementacão:

  ```java
  public class Nota {
      private final int valor;  
  }
  ```

* *FilmeId*: Representa o identificador de um Filme.

  Possível implementação:

  ```java
  public class FilmeId {
      private final UUID valor;
  }
  ```

### Enumerado

* *Genero*: Representa o gênero de um filme.

  Possível implementação:

  ```java
  public enum Genero {
      ACTION,
      ROMANCE,
      HORROR,
      ...
  }
  ```
  
