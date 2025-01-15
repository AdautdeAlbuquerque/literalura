# LiterAlura - Catálogo de Livros 📚

## 📋 Descrição
O **LiterAlura** é um projeto desenvolvido como parte do **segundo desafio do programa Oracle ONE**, oferecido pela **Alura**. O objetivo do desafio é construir um **Catálogo de Livros** interativo utilizando **Java** e **Spring Boot**, consumindo uma API externa, manipulando dados JSON e armazenando informações em um banco de dados **PostgreSQL**.  

A aplicação oferece uma interface de interação via console, permitindo que os usuários façam buscas por livros, autores e explorem informações de um vasto catálogo literário.

---

## 🏆 Objetivo do Projeto
Desenvolver um **Catálogo de Livros** que ofereça interação textual com os usuários através do console, proporcionando diferentes opções de consulta e manipulação dos dados. O catálogo deve:

- Consumir informações de uma API pública (Gutendex).
- Manipular dados JSON.
- Persistir os dados em um banco de dados relacional (**PostgreSQL**).
- Oferecer no mínimo 5 opções de interação com o usuário via console.

---
  
## 🖥️ Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot 3.4.1**  
  - Spring Boot Starter Web  
  - Spring Boot Starter Data JPA  
  - Spring Boot DevTools  
- **Jackson Databind 2.15.2**  
- **R2DBC SPI 1.0.0**  
- **PostgreSQL**  
- **Spring Boot Starter Test**  
- **Maven**  
- **IntelliJ IDEA**  

---

## 🛠️ Funcionalidades da Aplicação
A aplicação disponibiliza as seguintes funcionalidades para interação via console:

1. **Buscar livros pelo título**  
2. **Buscar livros por nome do autor**  
3. **Listar livros já registrados no banco de dados**  
4. **Listar livros por idioma**  
5. **Listar autores vivos em um determinado ano**  
6. **Listar autores nascidos em um determinado ano**  
7. **Listar autores por ano de morte**  
8. **Listar todos os autores registrados no banco de dados**  
9. **Exibir o Top 10 livros mais baixados**  
10. **Encerrar o programa**  

---

## 📦 Estrutura do Projeto

A organização do projeto foi feita conforme o padrão MVC, dividindo as responsabilidades em diferentes pacotes e classes. A estrutura final está conforme abaixo:

```
src
└── main
    └── java
        └── desafio.oracleone.literalura
            ├── model
            │   ├── Autor
            │   ├── AutorDTO
            │   ├── Livro
            │   └── LivroDTO
            ├── principal
            │   └── Principal
            ├── repository
            │   └── LivroRepository
            └── service
                ├── ConsumoAPI
                ├── ConverteDados
                ├── IConverteDados
                └── LivroService
    └── resources
        ├── static
        ├── templates
        └── application.properties
```

### 📁 Descrição dos Pacotes e Classes

- **`model`**: Contém as classes que representam os dados principais do projeto.
  - `Autor`: Representa as informações de um autor.
  - `AutorDTO`: DTO (Data Transfer Object) utilizado para transportar dados de autores.
  - `Livro`: Representa as informações de um livro.
  - `LivroDTO`: DTO utilizado para transportar dados de livros.

- **`principal`**: Contém a lógica principal do projeto, responsável pela interação com o usuário.
  - `Principal`: Classe que implementa o menu de opções e gerencia a interação via console.

- **`repository`**: Interface responsável pela comunicação com o banco de dados.
  - `LivroRepository`: Define consultas e operações realizadas no banco de dados **PostgreSQL**.

- **`service`**: Contém os serviços que implementam a lógica de consumo da API e manipulação de dados.
  - `ConsumoAPI`: Realiza o consumo da API Gutendex para buscar informações de livros.
  - `ConverteDados`: Realiza a conversão dos dados recebidos em formato JSON para objetos Java.
  - `IConverteDados`: Interface que padroniza a conversão de dados.
  - `LivroService`: Serviço responsável por operações específicas relacionadas aos livros.

---

## 🗄️ Banco de Dados
O banco de dados utilizado neste projeto é o **PostgreSQL**.

### Configuração do Banco de Dados:
As configurações de conexão estão definidas no arquivo **`application.properties`** localizado em **`src/main/resources`**. As informações de configuração incluem o **host**, **porta**, **nome do banco**, **usuário** e **senha**.

Exemplo de configuração no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📋 Requisitos para Execução
### Pré-requisitos:
- **Java 17+**  
- **Maven**  
- **PostgreSQL**
- **Springboot**   

### Passos para Executar o Projeto:
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/literalura.git
   cd literalura
   ```

2. Configure o banco de dados PostgreSQL conforme as informações do arquivo **`application.properties`**.

3. Compile o projeto com o Maven:
   ```bash
   mvn clean install
   ```

4. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

5. Utilize o console para interagir com o **LiterAlura**.

---

## 🔍 Exemplos de Consultas
Após iniciar a aplicação, será exibido o seguinte menu no console:

```
========================== DESAFIO LITERALURA ==========================

📋 Menu de Opções:

[1] Buscar livros pelo título
[2] Buscar livro por nome do autor
[3] Listar livros já registrados
[4] Listar livros em determinado idioma
[5] Listar autores VIVOS em um determinado ano
[6] Listar autores NASCIDOS em determinado ano
[7] Listar autores por ano de sua morte
[8] Listar todos os autores já registrados
[9] Top 10 livros mais baixados!!!
[0] Sair

========================================================================
Escolha uma opção:
```

Exemplo de busca de livros por título:

```
Digite o título do livro: Alice in Wonderland
Título: Alice's Adventures in Wonderland
Autor: Lewis Carroll
Idioma: Inglês
Downloads: 12345
```

---

## 🌐 Consumo da API
A aplicação consome dados da **API Gutendex**, que oferece informações sobre livros de domínio público. As informações são manipuladas em formato JSON e convertidas para objetos Java.

---

## 🧩 Organização do Projeto no Trello
Durante o desenvolvimento, o projeto foi organizado utilizando o **Trello**, seguindo a metodologia ágil. As colunas utilizadas foram:

| Coluna            | Descrição                                              |
|-------------------|--------------------------------------------------------|
| **Pronto para Iniciar** | Tarefas ainda não iniciadas                       |
| **Desenvolvendo**      | Tarefas em andamento                               |
| **Pausado**            | Tarefas iniciadas, mas temporariamente pausadas    |
| **Concluído**          | Tarefas finalizadas                                |

---

## 🚀 Melhorias Futuras
- Adicionar interface gráfica utilizando **JavaFX** ou **Thymeleaf**.
- Implementar autenticação de usuários.
- Adicionar funcionalidade para avaliação e comentários de livros.
- Salvar histórico de buscas realizadas pelos usuários.

---

## 👨‍💻 Desenvolvedor
- **Nome: Adaut de Albuquerque**  
- **E-mail: adautlima@gmail.com**  
- **LinkedIn: https://www.linkedin.com/in/adaut-sacchi-d-albuquerque-lima-67a314210/**  

---

**Desenvolvido para o Desafio #2 do Programa Oracle ONE pela Alura.**
