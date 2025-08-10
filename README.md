# Spring Boot - API com Segurança JWT

Este é um projeto de estudo desenvolvido para implementar uma API REST com segurança utilizando Spring Boot, Spring Security e JSON Web Tokens (JWT).

O projeto demonstra um fluxo completo de autenticação e autorização, incluindo a criação de usuários, login com geração de token e controle de acesso a endpoints baseado em perfis (Roles).

---

## Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.5.4**
* **Spring Security**
* **Spring Data JPA**
* **JWT (JSON Web Token)** com a biblioteca `jjwt`
* **H2 Database** (Banco de dados em memória)
* **Maven**

---

## Recursos Implementados

* **Criação de Usuários:** Endpoint para registrar novos usuários com senha criptografada (BCrypt).
* **Autenticação:** Endpoint `/login` que valida as credenciais e retorna um token JWT.
* **Autorização:**
    * Filtro JWT que valida o token a cada requisição.
    * Endpoints protegidos com acesso baseado em `Roles` (ex: `USERS`, `MANAGERS`).
* **Banco de Dados H2:** Configurado para rodar em memória, com console web habilitado para fácil visualização dos dados.

---

## Como Executar o Projeto

1.  Clone este repositório.
2.  Abra o projeto em sua IDE de preferência (IntelliJ, Eclipse, VS Code).
3.  Aguarde a IDE baixar todas as dependências do Maven.
4.  Execute a classe principal `SpringtokenApplication.java`.
5.  A API estará rodando em `http://localhost:8080`.

---

## Créditos e Observações

Este projeto foi desenvolvido como parte do [Digital Innovation One (DIO)](https://www.dio.me/).

O código original do curso foi a base para este desenvolvimento. Algumas dependências e configurações, como a da biblioteca **JJWT** e a do **Spring Security**, foram atualizadas para garantir a compatibilidade e o funcionamento correto com as versões mais recentes do **Java 21** e do **Spring Boot 3**.
