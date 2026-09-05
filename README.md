# Polidesk

Sistema de gestão de chamados (help desk) multiusuário, com controle de acesso por perfil e organização de atendimentos por status e tipo de serviço.

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Perfis de usuário](#perfis-de-usuário)
- [Requisitos funcionais](#requisitos-funcionais)
- [Requisitos não funcionais](#requisitos-não-funcionais)
- [Modelo de dados](#modelo-de-dados)
- [Casos de uso por perfil](#casos-de-uso-por-perfil)
- [Regras de negócio](#regras-de-negócio)
- [Stack sugerida](#stack-sugerida)
- [Roadmap](#roadmap)

## Sobre o projeto

O **Polidesk** é uma plataforma de abertura e acompanhamento de chamados técnicos (instalação, manutenção de software/equipamento, rede e telefonia), pensada para operações de suporte de pequeno e médio porte (até 100 usuários simultâneos). O sistema segue um modelo de **sessão única por tenant**, garantindo isolamento dos dados entre usuários, e conta com controle de acesso baseado em papéis para restringir ações sensíveis a administradores.

## Perfis de usuário

| Perfil | Descrição |
|---|---|
| **Administrador** | Criado diretamente via query no banco de dados. Responsável por criar e inativar usuários, além de todas as permissões do perfil comum. |
| **Comum** | Usuário padrão, responsável por abrir, visualizar, editar e cancelar seus próprios chamados. |
| **Visualizador** | Acesso somente leitura aos chamados, sem permissão de criação ou edição. |

## Requisitos funcionais

| ID | Descrição |
|---|---|
| RF01 | O sistema deve conter sessões *tenant*, onde cada usuário acessa dados independentes com registros próprios. |
| RF02 | O sistema deve conter opções de chamados com status **Em Aberto**, **Andamento**, **Fechado** e **Arquivado**. |
| RF03 | O sistema deve contar com *enum* para classificar usuários por cargo: **Administrador**, **Comum**, **Visualizador**. |
| RF04 | O sistema deve possuir uma única página de login. O usuário administrador é criado via query no banco e possui exclusividade para criar e inativar usuários. |
| RF05 | O sistema deve contar com *enum* para classificar o tipo de atendimento: **Instalação**, **Manutenção de Software**, **Manutenção de Equipamento**, **Rede** e **Telefonia**. |

## Requisitos não funcionais

| ID | Descrição |
|---|---|
| RNF01 | Tela intuitiva de login e menu principal para acesso rápido aos chamados. |
| RNF02 | Criptografia de dados sensíveis utilizando padrão **AES-256**. |
| RNF03 | Desempenho consistente nas operações de consulta, edição e criação de chamados. |
| RNF04 | Suporte a até **100 usuários** simultâneos sem degradação de desempenho. |

## Modelo de dados

### Usuário

| Campo | Tipo |
|---|---|
| id | Long |
| Nome | String |
| Login | String |
| Senha | String (criptografada — AES-256) |
| TipoUsuario | Enum (`ADMINISTRADOR`, `COMUM`, `VISUALIZADOR`) |
| criadoEm | LocalDate |

### Chamado

| Campo | Tipo |
|---|---|
| id | Long |
| Descrição | String (máx. 256 caracteres) |
| Usuário | Relacionamento (FK → Usuário) |
| TipoChamado | Enum (`INSTALACAO`, `MANUTENCAO_SOFTWARE`, `MANUTENCAO_EQUIPAMENTO`, `REDE`, `TELEFONIA`) |
| Status | Enum (`EM_ABERTO`, `ANDAMENTO`, `FECHADO`, `ARQUIVADO`) |
| criadoEm | LocalDateTime |

> **Observação:** o campo `Senha` deve ser armazenado sempre criptografado (AES-256), nunca em texto plano, conforme RNF02.

## Casos de uso por perfil

### Usuário (perfil Comum)

- Abrir chamado
- Visualizar os próprios chamados
- Cancelar chamado
- Editar chamado (somente se **não** estiver em status *Andamento*)
- Editar nome
- Alterar senha
- Anexar arquivos ao chamado

### Administrador

- Todas as ações do perfil Comum
- Criar usuários
- Inativar usuários

### Visualizador

- Visualizar chamados (somente leitura)

## Regras de negócio

- Um chamado **não pode ser editado** enquanto estiver com status **Andamento**.
- Apenas o **Administrador** pode criar ou inativar usuários; não há tela de cadastro pública.
- O primeiro usuário Administrador é provisionado **manualmente via query no banco de dados**, não havendo fluxo de auto-registro.
- Cada usuário enxerga apenas os dados pertencentes à sua sessão/tenant.
- Anexos de arquivos ficam vinculados ao chamado e ao usuário que os enviou.

## Stack sugerida

| Camada | Tecnologia |
|---|---|
| Backend | Java + Spring Boot |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | PostgreSQL |
| Segurança | Spring Security + criptografia AES-256 para dados sensíveis |
| Frontend | React ou Angular (SPA consumindo API REST) |
| Autenticação | JWT (sessão via token) |

> Stack sugerida com base nos requisitos apresentados (tipos `Long`, `LocalDate`, `LocalDateTime` indicam ecossistema Java/Spring). Pode ser adaptada conforme a stack real do time.

## Roadmap

- [ ] Modelagem do banco de dados (Usuário e Chamado)
- [ ] Implementação da autenticação e criação manual do Administrador
- [ ] CRUD de chamados com regras de status
- [ ] Upload e anexo de arquivos aos chamados
- [ ] Criptografia AES-256 para campos sensíveis
- [ ] Testes de carga (100 usuários simultâneos)
- [ ] Tela de login e menu principal
