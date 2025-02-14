# Account Management 🚀

## Descrição 📝
Account Management é um projeto Backend desenvolvido com Java e Spring Boot para gerenciamento de usuários, incluindo funcionalidades de autenticação e autorização utilizando JWT.

## Tecnologias Utilizadas 🛠️
- Java 17+ ☕
- Spring Boot 🌱
- Spring Security 🔒
- JWT (JSON Web Token) 🔑
- Banco de Dados MySQL 🗄️
- JPA/Hibernate 📦
- Maven 🏗️

## Funcionalidades ✨
1. **Autenticação e Autorização com JWT** 🔐
   - Registro de usuário 🆕
   - Login e geração de token JWT 🔑
   - Proteção de rotas com autenticação 🛡️
   
2. **CRUD da entidade User** 👤
   - Criação de usuário ➕
   - Leitura de informações do usuário 📖
   - Atualização de dados do usuário ✏️
   - Exclusão de usuário ❌

## Como Executar o Projeto ▶️
### Pré-requisitos 🏗️
- Java 21+ ☕
- Maven 🏗️
- Banco de dados configurado com o nome `account_management` 🗄️

### Passos 📌
1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/account-management.git
   ```
2. Acesse o diretório do projeto:
   ```sh
   cd account-management
   ```
3. Configure o banco de dados no `application.properties` ou `application.yml`.
4. Compile e execute o projeto:
   ```sh
   mvn spring-boot:run
   ```

## Endpoints Principais 🌐
### Autenticação 🔑
- `POST /login` - Faz login e retorna um token JWT 🔑
- `POST /auth/refresh-token` - Faz atualização do token 🔑

### Usuários 👤
- `GET /users` - Lista todos os usuários 📋 (requer autenticação)
- `GET /users/{id}` - Obtém um usuário por ID 🔍
- `GET /users/pagination` - Paginação de usuários 📋
- `POST /users` - Registra um novo usuário 🆕
- `PUT /users/{id}` - Atualiza um usuário ✏️
- `DELETE /users/{id}` - Remove um usuário ❌

## Contribuição 🤝
Sinta-se à vontade para contribuir com melhorias para o projeto. Para isso:
1. Fork o repositório 🍴
2. Crie uma branch com sua feature:
   ```sh
   git checkout -b minha-feature
   ```
3. Realize os commits 📝:
   ```sh
   git commit -m "Minha nova feature"
   ```
4. Envie as alterações para o repositório remoto 🚀:
   ```sh
   git push origin minha-feature
   ```
5. Abra um Pull Request 🔄

## Licença 📜
Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

