# Nexus Data Platform

![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

Nexus é uma plataforma de monitoramento ambiental em tempo real, projetada para coletar, processar, visualizar e gerar insights por meio de sensores IoT. Na V1, o sistema centraliza informações de humidade, temperatura, consumo de energia e qualidade do ar, fornecendo insights valiosos através de uma interface intuitiva.

## 🎯 Objetivo do Projeto

O objetivo principal do Nexus é fornecer uma solução robusta, escalável e de baixo custo para o monitoramento de ambientes, permitindo que usuários tomem decisões informadas para otimizar a segurança, o conforto e a eficiência energética de qualquer espaço.

## ✨ Funcionalidades Principais

* **Coleta de Dados:** Recebe dados de múltiplos sensores via protocolo MQTT (ou HTTP, etc.).
* **Processamento em Tempo Real:** Interpreta e armazena os dados recebidos em um banco de dados.
* **API Robusta:** Expõe endpoints RESTful para que o frontend ou outros serviços possam consumir os dados.
* **Visualização:** Envia dados processados para uma interface de frontend para exibição em gráficos e dashboards.
* **Geração de Insights** Recebe os dados e, baseado em situações predefinidas ou nos hábitos do usuário, gera as melhores sugestões para ajudar no dia a dia.

## 🛠️ Tecnologias Utilizadas

Este é o stack tecnológico planejado para o backend:

* **Linguagem:** Java 
* **Framework:** Spring Boot
* **Banco de Dados:** PostgreSQL
* **Comunicação com Sensores:** MQTT
* **Outras Ferramentas:** Docker,

## 🚀 Como Executar o Projeto

Siga as instruções abaixo para configurar e executar o backend da aplicação em seu ambiente local.

### **Pré-requisitos**

Antes de começar, você precisará ter as seguintes ferramentas instaladas em sua máquina:
* [Git](https://git-scm.com)
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) - Versão 17 ou superior.
* [PostgreSQL](https://www.postgresql.org/download/) - Um banco de dados relacional.

### **Configuração**

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SViniQ/nexuslink.git](https://github.com/SViniQ/nexuslink.git)
    cd nexuslink
    ```
    *(Lembre-se de substituir `seu-usuario` pelo seu nome de usuário do GitHub)*

2.  **Configure o Banco de Dados:**
    * Inicie seu serviço do PostgreSQL.
    * Crie um novo banco de dados. Você pode nomeá-lo como `nexuslink_db`, por exemplo.

3.  **Configure as Variáveis de Ambiente da Aplicação:**
    * Navegue até o diretório `src/main/resources/`.
    * Você encontrará o arquivo `application.yml`. Ele contém as configurações da aplicação.
    * Abra o arquivo e ajuste a seção `datasource` com as suas credenciais do PostgreSQL.

    **Exemplo do `application.yml`:**
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/nexuslink_db  # Mude se o nome do seu banco for diferente
        username: postgres                                    # Seu usuário do PostgreSQL
        password: sua_senha_aqui                              # Sua senha do PostgreSQL
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true
        database-platform: org.hibernate.dialect.PostgreSQLDialect
    ```

### **Executando a Aplicação**

O projeto utiliza o Maven Wrapper, que permite executar a aplicação sem precisar instalar o Maven globalmente.

1.  Abra um terminal na raiz do projeto (`nexuslink`).
2.  Execute o seguinte comando:

    * No Windows:
        ```bash
        mvnw.cmd spring-boot:run
        ```
    * No Linux ou macOS:
        ```bash
        ./mvnw spring-boot:run
        ```

### **Verificação**

Após a execução, a API estará rodando em `http://localhost:8080` (ou a porta que você configurou).

Você pode testar se a API está funcionando enviando uma requisição para um dos endpoints. Por exemplo, usando uma ferramenta como o Postman, Insomnia ou o comando `curl`:

```bash
curl http://localhost:8080/api/users
```

Se tudo estiver correto, você receberá uma resposta em formato JSON com uma lista (provavelmente vazia) de usuários.

## 📝 Estrutura do Projeto

O projeto segue a arquitetura padrão de uma aplicação Spring Boot, separando as responsabilidades em diferentes camadas para manter o código organizado e escalável.

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── nexuslink # (ou o nome do seu pacote principal, ex: com.sensetrack)
│   │   │           ├── config         # Classes de configuração (CORS, Segurança, etc.)
│   │   │           ├── controller     # Controladores REST: recebem as requisições HTTP e definem as rotas da API.
│   │   │           ├── dto            # Data Transfer Objects: objetos que moldam os dados enviados e recebidos pela API.
│   │   │           ├── model          # Entidades JPA: classes que representam as tabelas do banco de dados.
│   │   │           ├── repository     # Repositórios: interfaces do Spring Data JPA para interagir com o banco de dados.
│   │   │           ├── service        # Serviços: onde fica a lógica de negócio da aplicação.
│   │   │           └── SenseTrackApplication.java # Ponto de entrada principal da aplicação.
│   │   └── resources
│   │       └── application.yml    # Arquivo de configuração da aplicação (banco de dados, servidor, etc.).
│   └── test
│       └── java                   # Testes unitários e de integração.
├── .gitignore                     # Arquivos e pastas a serem ignorados pelo Git.
├── mvnw                           # Maven Wrapper para Linux/macOS.
├── mvnw.cmd                       # Maven Wrapper para Windows.
└── pom.xml                        # Arquivo de configuração do projeto Maven (dependências e build).
```

### **Descrição das Camadas**

* **`controller`**: Responsável por expor os endpoints da API. Ele recebe as requisições, chama a camada de serviço apropriada e retorna uma resposta HTTP (geralmente em JSON).
* **`service`**: Onde a mágica acontece. Contém as regras de negócio, validações e orquestra as operações, fazendo a ponte entre os controllers e os repositórios.
* **`repository`**: Define a comunicação com o banco de dados. Utiliza o Spring Data JPA para simplificar as operações de CRUD (Create, Read, Update, Delete), sem a necessidade de escrever SQL manualmente.
* **`model` / `entity`**: Classes que são um espelho das tabelas do banco de dados. O JPA as utiliza para mapear os dados.
* **`dto` (Data Transfer Object)**: Objetos "limpos" usados para transferir dados entre as camadas e, principalmente, como contratos de entrada e saída da API, evitando expor as entidades do banco diretamente.
* **`config`**: Centraliza as configurações da aplicação, como segurança, CORS, e a configuração de beans customizados.

## 🤝 Como Contribuir

Atualmente, o projeto está em fase inicial de desenvolvimento. Informações sobre como contribuir serão disponibilizadas no futuro.

---

*Projeto iniciado em julho de 2024.*