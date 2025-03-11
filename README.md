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

# Diagrama de Classes das Tabelas

#### Tabelas e Relacionamentos

![Image](https://github.com/user-attachments/assets/868043f2-0400-45b2-ba48-616c443a0674)


### **1. Objetivo do Projeto**  
Desenvolver um sistema para ajudar usuários a gerenciar finanças pessoais, incluindo:  
- Registro de **contas a pagar** e **contas a receber**.  
- Controle de **compras com cartão de crédito** (à vista ou parceladas).  
- Geração de **relatórios financeiros** (despesas por categoria, saldo mensal, etc.).  
- Notificações automáticas de vencimento.  
- Autenticação segura de usuários.  

---

### **2. Tecnologias Utilizadas**  
- **Backend**:  
  - Java 17 + Spring Boot 3.1.  
  - Spring Data JPA (PostgreSQL).  
  - Spring Security + JWT para autenticação.  
  - Lombok para redução de boilerplate.  
  - Docker para containerização.  
- **Banco de Dados**:  
  - PostgreSQL (relacional).  
  - Migrações via arquivos SQL (ex: `V1_create_table_usuario.sql`).  
- **Outras Ferramentas**:  
  - Maven para gerenciamento de dependências.  
  - Hibernate Validator para validações.  
  - JSON Web Tokens (JWT) para autenticação.  

---

### **3. Funcionalidades Principais**  
1. **Módulo de Contas**:  
   - Registro de contas a pagar/receber com categorias.  
   - Notificações de vencimento.  
   - Atualização de status (PENDENTE, PAGA, RECEBIDA).  

2. **Módulo de Cartão de Crédito**:  
   - Registro de compras (à vista ou parceladas).  
   - Acompanhamento de limite disponível.  
   - Controle de parcelas e seus status.  

3. **Relatórios Financeiros**:  
   - Exportação para PDF/Excel.  
   - Filtros por período, categoria e tipo de transação.  

4. **Autenticação**:  
   - Cadastro/login de usuários.  
   - Senhas criptografadas com BCrypt.  

---

### **4. Estrutura do Banco de Dados**  
- **Tabelas Principais**:  
  - `usuario`: Dados de autenticação e perfil.  
  - `categoria`: Categorias de transações (DESPESA, RECEITA, CARTAO).  
  - `contas`: Unifica contas a pagar/receber (tipo: PAGAR/RECEBER).  
  - `cartao_credito`: Limite total, datas de fechamento/vencimento.  
  - `compra_cartao`: Compras com cartão + relacionamento com parcelas.  
  - `parcela_compra`: Parcelas de compras (status: PENDENTE, PAGA, ATRASADA).  

- **Decisões de Modelagem**:  
  - **Normalização**: Tabela `categoria` separada para evitar redundância.  
  - **Unificação**: Tabela única `contas` para simplificar consultas.  
  - **Cálculo Dinâmico**: Limite disponível do cartão calculado via JPQL, sem armazenar no banco.  

---

### **5. Implementações-Chave**  
- **Entidades JPA**:  
  - Uso de `@Enumerated(EnumType.STRING)` para status (ex: `StatusConta.PENDENTE`).  
  - Relacionamentos `@OneToMany` e `@ManyToOne` (ex: `CompraCartao` → `ParcelaCompra`).  
- **DTOs (Data Transfer Objects)**:  
  - Conversão de `String` para enums (ex: `TipoConta.valueOf(dto.getTipo())`).  
  - Validações com `@NotBlank`, `@Positive`, `@Pattern`.  
- **Serviços**:  
  - `ParcelaCompraService`: Geração automática de parcelas com validação de quantidade.  
  - `CartaoCreditoService`: Cálculo do limite disponível via JPQL.  
  - Transações com `@Transactional` para operações atômicas.  

---

### **6. Validações e Segurança**  
- **Validações em Três Camadas**:  
  1. **DTO**: Anotações como `@NotBlank` e `@Email`.  
  2. **Serviço**: Regras de negócio (ex: categoria compatível com tipo de conta).  
  3. **Banco de Dados**: `CHECK` constraints (ex: `quantidade_parcelas >= 1`).  
- **Segurança**:  
  - Autenticação JWT com Spring Security.  
  - Criptografia de senhas com BCrypt.  
  - Controle de acesso baseado em roles (usuário só acessa seus dados).  

---

