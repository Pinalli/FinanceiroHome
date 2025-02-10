# Estudo de Caso: Sistema de Gestão de Contas Domésticas 

# Visão Geral do Sistema

O sistema de gestão de contas domésticas tem como objetivo auxiliar usuários a gerenciarem suas finanças pessoais de forma eficiente. Ele permitirá o controle de contas a pagar e a receber, o registro de compras realizadas com cartão de crédito, a geração de relatórios financeiros e o controle de acesso dos usuários, garantindo a privacidade e segurança das informações.

## Funcionalidades Principais

### 2.1. Módulo de Contas a Pagar
- Permite que o usuário registre todas as suas despesas futuras, como água, luz, internet, aluguel, financiamentos, entre outros.
- O sistema enviará notificações ou lembretes automáticos para os usuários sobre o vencimento de suas contas.
- Cada conta terá os seguintes campos: descrição, valor, data de vencimento, status (paga ou pendente), e categoria (como alimentação, moradia, transporte, etc.).

### 2.2. Módulo de Contas a Receber
- Permite que o usuário registre todas as suas entradas de receita, como salários, recebimentos de aluguéis, rendimentos de investimentos, entre outros.
- Também suporta a criação de receitas recorrentes, como um salário mensal.
- Campos principais: descrição, valor, data de recebimento, status (recebido ou pendente), e categoria.

### 2.3. Módulo de Compras com Cartão de Crédito
- O sistema permitirá que os usuários registrem suas compras feitas com cartão de crédito. Cada compra incluirá a data da compra, valor, descrição, categoria, e cartão utilizado.
- Além disso, o sistema permitirá que os usuários acompanhem o limite disponível de seus cartões e o total de compras em aberto.
- Possibilitará ainda o controle de parcelas para compras parceladas.

### 2.4. Relatórios Financeiros
- O sistema gerará relatórios detalhados para que o usuário possa analisar suas finanças. Exemplos de relatórios incluem:
  - Relatório de despesas mensais por categoria.
  - Relatório de saldo entre contas a pagar e receber.
  - Relatório de transações de cartão de crédito.
  - Gráficos de evolução do saldo ao longo do tempo.
- Os relatórios podem ser exportados para PDF ou Excel para facilitar o compartilhamento e a consulta offline.

### 2.5. Controle de Acesso de Usuário
- O sistema terá um único tipo de usuário, que terá acesso completo às funcionalidades, incluindo:
  - Cadastrar e visualizar suas próprias contas a pagar e a receber.
  - Registrar e acompanhar compras feitas com cartão de crédito.
  - Gerar e visualizar relatórios financeiros.
- Para garantir a segurança dos dados, o sistema contará com um processo de autenticação de usuário com login e senha. A senha será armazenada de forma segura utilizando algoritmos de criptografia.

## Tecnologias Envolvidas
- **Back-end:** - **Java**: Linguagem de programação orientada a objetos.
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Flyway**: Ferramenta para migrações de banco de dados.
- **PostgreSQL**: Banco de dados relacional.
- **Front-end:** Interface desenvolvida na linguagem (a definir), desde que seja uma aplicação web, para fornecer uma experiência interativa e moderna.


## Benefícios do Sistema
- Centralização de todas as informações financeiras domésticas em um único local.
- Melhoria no planejamento financeiro ao ter controle sobre receitas e despesas.
- Redução de inadimplência com lembretes automáticos de vencimento de contas.
- Maior segurança das informações financeiras com controle de acesso robusto.

# Diagrama de Sequência
## Fluxo para inserção Categorias

Frontend -> Backend: GET /api/categorias?tipo=DESPESA<br>
Backend -> Database: SELECT * FROM categorias WHERE tipo = 'DESPESA'<br>
Database -> Backend: Retorna categorias<br>
Backend -> Frontend: Lista de categorias DESPESA<br>
Frontend: Exibe dropdown com categorias<br>
Usuário: Seleciona categoria e preenche formulário<br>
Frontend -> Backend: POST /api/contas-pagar (com categoria_id)<br>
Backend: Valida se categoria_id é DESPESA<br>
Backend -> Database: INSERT INTO conta_pagar (...)<br>
