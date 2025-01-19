### DTO
O ParcelaDTO contém apenas os dados necessários para a transferência entre as camadas, evitando a exposição de detalhes internos da entidade Parcela.

### Repository
O ParcelaRepository herda de JpaRepository, fornecendo métodos básicos para CRUD (Create, Read, Update, Delete). Você pode adicionar métodos personalizados para consultas mais complexas.

### Service
O ParcelaService contém a lógica de negócio, como validações, conversões entre DTO e entidade, e chamadas ao repositório.

### Controller
O ParcelaController expõe os endpoints REST para interagir com as parcelas.

### Gerador jwtToken
import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
public static void main(String[] args) {
SecureRandom secureRandom = new SecureRandom();
byte[] key = new byte[32]; // 32 bytes = 256 bits
secureRandom.nextBytes(key);
String base64Key = Base64.getEncoder().encodeToString(key);
System.out.println(base64Key);
}
}
