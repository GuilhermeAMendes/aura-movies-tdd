# Módulo de Recomendação de Filmes

Este sistema foi desenvolvido como parte da disciplina de Validação e Teste de Software, com o objetivo de aplicar práticas de desenvolvimento como Domain-Driven Design (DDD), Test-Driven Development (TDD) e Behavior-Driven Development (BDD).

O sistema permite que um usuário (cliente) avalie filmes de um catálogo e receba recomendações personalizadas com base em seu histórico de avaliações.

## Conceitos de Domain-Driven Design (DDD) Aplicados

A arquitetura deste projeto é fortemente baseada nos padrões táticos do DDD para criar um modelo de domínio rico, coeso e desacoplado. Abaixo estão os principais conceitos aplicados.

### Raiz do Agregado

Um Agregado é um conjunto de objetos de domínio que são tratados como uma única unidade. A Raiz do Agregado é a entidade principal desse conjunto, servindo como o único ponto de entrada para qualquer modificação dentro do agregado. Ela é responsável por garantir que o estado do agregado como um todo permaneça sempre consistente, validando todas as regras de negócio.

Os dois agragados principais desse projeto são:

* *Cliente*: É o agregado responsável por gerenciar o perfil e as interações de um usuário. Ele encapsula todas as avaliações feitas por aquele cliente e garante regras de negócio. Todas as operações, como adicionar uma nova avaliação, devem passar pelo objeto Cliente.
* *Filme*: Representa um filme no catálogo do sistema. É um agregado mais simples, responsável por manter a consistência de suas próprias informações, como título e gênero.

### Entidade

Uma Entidade é um objeto que não é definido por seus atributos, mas sim por seu identificador. Duas entidades podem ter exatamente os mesmos atributos, mas são distintas se tiverem identificadores diferentes. Elas são mutáveis ao longo do tempo.

Em nosso projeto, temos:

* *Avaliacao*: É uma entidade interna ao agregado Cliente. Cada avaliação possui uma identidade única e representa a nota que um cliente deu a um filme específico. Ela só existe dentro do contexto de um Cliente.

### Objeto de Valor

Um Objeto de Valor é um objeto que descreve uma característica do domínio, sendo definido apenas pelos seus atributos. Diferente das entidades, ele não possui uma identidade conceitual. Value Objects são imutáveis: uma vez criados, não podem ser alterados. Se uma mudança for necessária, um novo objeto é criado para substituir o antigo.

Neste projeto, utilizamos os seguintes Value Objects:

* *Nota*: Representa a nota (de 1 a 5) de uma avaliação. Sua lógica interna garante que uma nota inválida (como 0 ou 6) não possa ser criada.
* *FilmeId*: Representa o identificador de um Filme. Usá-lo como um objeto (ao invés de UUID, por exemplo) adiciona segurança de tipos ao nosso domínio.
* *Genero*: Representa o gênero de um filme. Por ser imutável e definido por seu valor (ex: "Ação"), ele se encaixa perfeitamente como um Value Object.