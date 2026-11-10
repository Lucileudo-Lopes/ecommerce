# E-commerce API

API REST de uma loja virtual que fiz com Spring Boot. Tem cadastro e login com JWT, rotas separadas por perfil (usuário comum e ADMIN), produtos, categorias e pedidos. Montei este projeto para praticar o backend completo, desde a segurança até o deploy com Docker.

## O que tem dentro

- Cadastro e login, com token JWT
- Rotas protegidas e rotas que só o ADMIN acessa
- CRUD de categorias e produtos
- Pedidos com itens e status
- Tratamento de erros em um lugar só, para a API sempre responder no mesmo formato
- Swagger para testar tudo pelo navegador
- Banco PostgreSQL e API rodando juntos com Docker Compose

## Tecnologias

Java 21, Spring Boot 4.1.1, Spring Security, Spring Data JPA, PostgreSQL, Maven, JUnit, Docker e Swagger/OpenAPI.

## Como o código está organizado

Dividi por assunto em vez de por camada, então cada pacote tem tudo de um tema:

```
auth        registro e login
category    categorias
config      configuração do Swagger
exceptions  erros de negócio e o handler global
order       pedidos e itens
product     produtos
security    filtro e serviço do JWT, configuração do Spring Security
user        usuário e perfis
```

## Como rodar

Você precisa de Java 21 e Docker.

A aplicação não guarda senha nem chave secreta no código. Antes de rodar, defina duas variáveis de ambiente:

| Variável | O que é |
|---|---|
| `SPRING_DATASOURCE_PASSWORD` | senha do PostgreSQL |
| `JWT_SECRET` | chave que assina os tokens |

Depois, com tudo configurado:

```bash
docker compose up --build
```

Se preferir rodar só a API na sua máquina:

```bash
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

## Testes

```bash
./mvnw test
```

Os testes também leem as duas variáveis acima. Se elas não estiverem definidas, o Spring não sobe o contexto e o teste falha.

## Documentação

Com a API no ar, o Swagger fica em:

```
http://localhost:8080/swagger-ui/index.html
```

Para testar as rotas protegidas, faça login, copie o token e clique em **Authorize**.

## O que aprendi

Este foi o projeto em que entendi de verdade como o Spring Security funciona por dentro: o filtro que lê o token, o serviço que gera e valida o JWT e a configuração que decide quais rotas são públicas e quais exigem perfil de ADMIN. Também aprendi a organizar o código por assunto e a centralizar o tratamento de erros em um handler global, o que deixou os controllers bem mais limpos.

## Dificuldades que tive

A parte que mais me ensinou foi tirar a senha do banco e a chave do JWT de dentro do código. Depois que passei a ler esses valores de variáveis de ambiente, os testes deixaram de subir, porque o Spring não encontrava as variáveis. Precisei configurar o ambiente de execução dos testes no Eclipse para resolver, e foi assim que entendi que configuração sensível não deve ficar no repositório e que cada ambiente (máquina, Docker, testes) precisa receber as suas próprias variáveis.

## Próximos passos

Quero aumentar a cobertura de testes, adicionar paginação na listagem de produtos e fazer o deploy da API na nuvem.

## Contato

Lucileudo Paiva · [github.com/Lucileudo-Lopes](https://github.com/Lucileudo-Lopes)