### DTO
O ParcelaDTO contém apenas os dados necessários para a transferência entre as camadas, evitando a exposição de detalhes internos da entidade Parcela.

### Repository
O ParcelaRepository herda de JpaRepository, fornecendo métodos básicos para CRUD (Create, Read, Update, Delete). Você pode adicionar métodos personalizados para consultas mais complexas.

### Service
O ParcelaService contém a lógica de negócio, como validações, conversões entre DTO e entidade, e chamadas ao repositório.

### Controller
O ParcelaController expõe os endpoints REST para interagir com as parcelas.