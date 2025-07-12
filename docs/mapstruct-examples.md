# MapStruct - Exemplos de Implementação

Este documento mostra a migração dos mappers manuais para MapStruct.

## Antes - Mapper Manual

```java
@Component
public class ClientMapper {
    
    public Client toDomain(ClientModel model) {
        if (model == null) {
            return null;
        }
        
        return new Client(
            model.getId(),
            model.getName(),
            model.getEmail(),
            model.getCreatedAt()
        );
    }
    
    public ClientModel toModel(Client domain) {
        if (domain == null) {
            return null;
        }
        
        ClientModel model = new ClientModel();
        model.setId(domain.getId());
        model.setName(domain.getName());
        model.setEmail(domain.getEmail());
        model.setCreatedAt(domain.getCreatedAt());
        
        return model;
    }
    
    public void updateModel(Client domain, ClientModel model) {
        if (domain == null || model == null) {
            return;
        }
        
        model.setName(domain.getName());
        model.setEmail(domain.getEmail());
        model.setCreatedAt(domain.getCreatedAt());
    }
}
```

**Problemas:**
- ❌ Muito código boilerplate
- ❌ Verificações manuais de null
- ❌ Mapeamento manual campo por campo
- ❌ Propenso a erros
- ❌ Difícil manutenção

## Depois - MapStruct

```java
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientMapper {
    
    Client toDomain(ClientModel model);
    
    ClientModel toModel(Client domain);
    
    void updateModel(Client domain, @MappingTarget ClientModel model);
}
```

**Vantagens:**
- ✅ Código limpo e conciso
- ✅ Verificações de null automáticas
- ✅ Mapeamento automático por convenção
- ✅ Type-safe em tempo de compilação
- ✅ Performance otimizada
- ✅ Integração com Spring Boot

## Exemplo Complexo - OrderMapper

### Antes (70+ linhas)
```java
@Component
@RequiredArgsConstructor
public class OrderMapper {
    
    private final OrderItemMapper orderItemMapper;
    private final ClientMapper clientMapper;
    
    public Order toDomain(OrderModel model) {
        if (model == null) {
            return null;
        }
        
        Order domain = new Order();
        domain.setId(model.getId());
        domain.setOrderCode(model.getOrderCode());
        domain.setClientId(model.getClientId());
        domain.setClient(clientMapper.toDomain(model.getClient()));
        domain.setTotal(model.getTotal());
        domain.setCreatedAt(model.getCreatedAt());
        
        if (model.getItems() != null) {
            domain.setItems(model.getItems().stream()
                .map(orderItemMapper::toDomain)
                .collect(Collectors.toList()));
        }
        
        return domain;
    }
    
    // ... mais 40+ linhas de código similar
}
```

### Depois (15 linhas)
```java
@Mapper(
    componentModel = "spring",
    uses = {OrderItemMapper.class, ClientMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    
    Order toDomain(OrderModel model);
    
    @Mapping(target = "client", ignore = true)
    OrderModel toModel(Order domain);
    
    @Mapping(target = "client", ignore = true)
    void updateModel(Order domain, @MappingTarget OrderModel model);
    
    @AfterMapping
    default void setOrderReference(@MappingTarget OrderModel orderModel) {
        if (orderModel.getItems() != null) {
            orderModel.getItems().forEach(item -> item.setOrder(orderModel));
        }
    }
}
```

## Configuração Maven

```xml
<!-- MapStruct -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>

<!-- Plugin de compilação -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

## Benefícios da Migração

### Performance
- **Código gerado**: MapStruct gera código Plain Java em tempo de compilação
- **Zero reflection**: Não usa reflection em runtime
- **Otimizado**: Código gerado é otimizado para performance

### Manutenibilidade
- **Menos código**: 80% menos código para manter
- **Type-safe**: Erros detectados em tempo de compilação
- **Convenção**: Mapeamento automático por nome dos campos

### Desenvolvimento
- **Produtividade**: Desenvolvimento mais rápido
- **Menos bugs**: Menor chance de erros manuais
- **IDE Support**: Auto-complete e navegação

## Conclusão

A migração para MapStruct trouxe:
- **Redução de código**: De 200+ linhas para 50 linhas
- **Melhor performance**: Código compilado vs reflection
- **Maior confiabilidade**: Type-safety garantido
- **Facilidade de manutenção**: Menos código para manter

O MapStruct é uma excelente escolha para projetos que precisam de mapeamento eficiente e confiável entre objetos. 