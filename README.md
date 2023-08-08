# API de Reservas

Este projeto consiste em uma API RESTful desenvolvida em Java Spring Boot para gerenciar reservas de uma casa de temporada. A API utiliza o banco de dados H2 para armazenamento dos dados. A aplicação possibilita o agendamento de estadias em datas específicas, com informações do hóspede e quantidade de pessoas.

## Começando

1. Clone o repositório: `git clone git@github.com:arthuroaraujo/reservations-backend.git`
2. Navegue até o diretório do projeto: `cd reservations-backend`
3. Certifique-se de ter o Java Spring Boot e o H2 Database configurados.
4. Execute a aplicação.

## Endpoints

A API oferece os seguintes endpoints para gerenciar as reservas:

### Criar uma Reserva

**Método:** POST  
**Endpoint:** `/reservas`  

**Corpo da Requisição (JSON):**
```json
{
    "nomeHospede": "Fulano de Tal",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 4
}
```

**Resposta (JSON):**
```json
{
    "id": 1,
    "nomeHospede": "Fulano de Tal",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 4,
    "status": "CONFIRMADA"
}
```

### Obter Todas as Reservas

**Método:** GET  
**Endpoint:** `/reservas`  

**Resposta (JSON):**
```json
[
    {
        "id": 1,
        "nomeHospede": "Fulano de Tal",
        "dataInicio": "2023-08-10",
        "dataFim": "2023-08-15",
        "quantidadePessoas": 4,
        "status": "CONFIRMADA"
    },
    {
        "id": 2,
        "nomeHospede": "Ciclano da Silva",
        "dataInicio": "2023-09-01",
        "dataFim": "2023-09-05",
        "quantidadePessoas": 2,
        "status": "PENDENTE"
    },
    ...
]
```

### Obter uma Reserva Específica por ID

**Método:** GET  
**Endpoint:** `/reservas/{id}`  

**Resposta (JSON):**
```json
{
    "id": 1,
    "nomeHospede": "Fulano de Tal",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 4,
    "status": "CONFIRMADA"
}
```

### Atualizar uma Reserva Existente

**Método:** PUT  
**Endpoint:** `/reservas/{id}`  

**Corpo da Requisição (JSON):**
```json
{
    "nomeHospede": "Fulano da Silva",
    "dataInicio": "2023-08-12",
    "dataFim": "2023-08-17",
    "quantidadePessoas": 5,
    "status": "PENDENTE"
}
```

**Resposta (JSON):**
```json
{
    "id": 1,
    "nomeHospede": "Fulano da Silva",
    "dataInicio": "2023-08-12",
    "dataFim": "2023-08-17",
    "quantidadePessoas": 5,
    "status": "PENDENTE"
}
```

### Cancelar uma Reserva

**Método:** DELETE  
**Endpoint:** `/reservas/{id}/cancelar`  

**Resposta (JSON):**
```json
{
    "id": 1,
    "nomeHospede": "Fulano da Silva",
    "dataInicio": "2023-08-12",
    "dataFim": "2023-08-17",
    "quantidadePessoas": 5,
    "status": "CANCELADA"
}
```

### Obter Reservas Confirmadas

**Método:** GET  
**Endpoint:** `/reservas/confirmadas`  

**Resposta (JSON):**
```json
[
    {
        "id": 1,
        "nomeHospede": "Fulano de Tal",
        "dataInicio": "2023-08-10",
        "dataFim": "2023-08-15",
        "quantidadePessoas": 4,
        "status": "CONFIRMADA"
    },
    ...
]
```

### Obter Reservas Pendentes

**Método:** GET  
**Endpoint:** `/reservas/pendentes`  

**Resposta (JSON):**
```json
[
    {
        "id": 2,
        "nomeHospede": "Ciclano da Silva",
        "dataInicio": "2023-09-01",
        "dataFim": "2023-09-05",
        "quantidadePessoas": 2,
        "status": "PENDENTE"
    },
    ...
]
```

###

 Obter Reservas Canceladas

**Método:** GET  
**Endpoint:** `/reservas/canceladas`  

**Resposta (JSON):**
```json
[
    {
        "id": 3,
        "nomeHospede": "Beltrano Oliveira",
        "dataInicio": "2023-07-20",
        "dataFim": "2023-07-25",
        "quantidadePessoas": 3,
        "status": "CANCELADA"
    },
    ...
]
```

## Testes
A API foi desenvolvida com foco na qualidade e robustez do código, e por isso foram implementados testes unitários para verificar o funcionamento correto das funcionalidades. A cobertura de testes mede a porcentagem do código que é coberto pelos testes automatizados. Quanto maior a cobertura, mais confiável e seguro é o código. Abaixo está a imagem que mostra a cobertura de testes para este projeto:

![Cobertura de Testes](https://i.imgur.com/pLqO6qt.png)

## Tratativa de Erros no Back End

No Back End, foram implementadas tratativas de erros para garantir que o sistema de reservas de hotel opere de maneira consistente e segura. Abaixo estão as principais tratativas de erros implementadas:

### Criação de Reserva

- "Data indisponível para reserva.": Ao tentar criar uma reserva com datas que já estejam ocupadas por outra reserva, o sistema retorna essa mensagem de erro.

### Atualização de Reserva

- "Data indisponível para atualização.": Semelhante à criação, se a atualização de uma reserva for tentada com datas que já estejam ocupadas por outra reserva, essa mensagem de erro é retornada.
- "Não é possível atualizar uma reserva cancelada.": Se uma tentativa de atualização for feita em uma reserva que já foi cancelada, essa mensagem de erro é retornada.
- "Não é possível mudar o status para CANCELADA através desta operação.": Se tentar atualizar uma reserva para CANCELADA pelo método de atualização, essa mensagem de erro é retornada.

### Outras Restrições

- A data de início da viagem não pode ser posterior à data de fim da viagem.
- Não é possível adicionar ou atualizar reservas com datas no passado.
- "Reserva não encontrada com o ID: " + id: Caso uma reserva não seja encontrada pelo ID fornecido, o sistema retornará essa mensagem de erro.

## Tecnologias Utilizadas

- Java Spring Boot
- H2 Database
