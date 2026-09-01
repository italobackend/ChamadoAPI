# Análise de requisitos - Polidesk

- Requisitos funcionais
- Requisitos não funcionais

## `Requisitos funcionais:`
1. O sistema deve conter **sessões tenant**, onde cada usuário acessa dados independentes com registros próprios.
2. O sistema deve conter opções de chamados com status de **em aberto**, **andamento**, **fechado** e **arquivado**.
3. O sistema deve contar com enums para classificar os usuários por cargos, tais como: **administrador**, **comum**, **visualizador**.
4. O sistema deve ter uma página de login apenas, o usuário administrador deve ser criado via query no banco de dados, ele terá acesso para criar, inativar usuários.
5. O sistema deve contar com enums para classificar o tipo de atendimento, tais como: **instalação**, **manutenção de software**, **manutenção de equipamento**, **rede** e **telefonia**.

## `Requisitos não funcionais:`
1. Tela intuitiva de login e menu principal para acessar os chamados.
2. Criptografia de dados sensíveis (**criptografia padrão AES-256**)
3. Desempenho significativo ao realizar consultas, edições e criações de chamados.
4. Sistema suportar até 100 usuários sem queda de desempenho do sistema.

### Tabelas necessárias:

| **Usuário**          | **Chamado**                    |
|----------------------|--------------------------------|
| Nome (String)        | Descrição (String - 256 chars) |
| Login (String)       | Usuário (Relacionamento)       | 
| Senha (String)       | id (Long)                      |
| TipoUsuario (enum)   | TipoChamado(enum)              |
| id (Long)            | Status (enum)                  | 
| criadoEm (LocalDate) | criadoEm (LocalDateTime)       |

### Métodos

**`Usuário`**
* Abrir chamado
* Visualizar os próprios chamados
* Cancelar chamado
* Editar chamado (Caso não esteja em andamento)
* Editar nome
* Alterar senha
* Anexar arquivos
* Criar usuários (Role ADM)


