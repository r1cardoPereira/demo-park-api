
# Projeto de Gerenciamento de Usuários

## Descrição

Este projeto é uma API REST para gerenciamento de usuários. Ele permite criar, buscar e atualizar usuários.

## Pré-requisitos

- Java 17
- Spring Boot 3.2.1 

## Instalação

1. Clone o repositório
2. Execute `mvn install` para instalar as dependências
3. Execute `mvn spring-boot:run` para iniciar o servidor

## Uso

### Criar usuário

POST /api/v1/usuarios

Payload:

```json
{
    "username": "usuario",
    "password": "senha"
}
```

Resposta:

```json
{
    "id": 1,
    "username": "usuario"
}
```

### Obter usuário

GET /api/v1/usuarios/{id}

Resposta:

```json
{
    "id": 1,
    "username": "usuario"
}
```

### Atualizar senha do usuário

PATCH /api/v1/usuarios/{id}

Payload:

```json
{
    "senhaAtual": "senha",
    "novaSenha": "novaSenha",
    "confirmaSenha": "novaSenha"
}
```

Resposta:

204 No Content

### Obter todos os usuários

GET /api/v1/usuarios

Resposta:

```json
[
    {
        "id": 1,
        "username": "usuario"
    },
    {
        "id": 2,
        "username": "usuario2"
    }
]
```

## Testes

Para executar os testes, use o comando `mvn test`.

## Melhores práticas

Durante o desenvolvimento deste projeto, seguimos as melhores práticas de desenvolvimento de software, incluindo:

- Programação orientada a objetos
- Princípios SOLID
- Testes unitários
- Manipulação de exceções
- Documentação de código

## Contribuição

Se você quiser contribuir para este projeto, por favor, faça um fork do repositório e submeta um pull request.

## Licença

Sem licença.



