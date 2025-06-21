Vou fazer um deep dive completo no projeto e criar um arquivo técnico detalhado com todas as funcionalidades. Primeiro, vou analisar mais profundamente o código para entender todos os detalhes.

Agora vou criar um arquivo técnico detalhado com todas as funcionalidades do projeto:

Agora vou criar um arquivo técnico detalhado com todas as funcionalidades do projeto atual:

```markdown
# Análise Técnica Completa - Sistema de Teatro JavaFX

## 📋 Índice

1. [Visão Geral do Sistema](#visão-geral-do-sistema)
2. [Arquitetura e Padrões de Projeto](#arquitetura-e-padrões-de-projeto)
3. [Modelo de Dados e Entidades](#modelo-de-dados-e-entidades)
4. [Funcionalidades Core do Sistema](#funcionalidades-core-do-sistema)
5. [Sistema de Autenticação e Segurança](#sistema-de-autenticação-e-segurança)
6. [Gerenciamento de Poltronas](#gerenciamento-de-poltronas)
7. [Sistema de Compra de Ingressos](#sistema-de-compra-de-ingressos)
8. [Dashboard e Estatísticas](#dashboard-e-estatísticas)
9. [Interface Gráfica](#interface-gráfica)
10. [Persistência de Dados](#persistência-de-dados)
11. [Sistema de Logging e Observers](#sistema-de-logging-e-observers)
12. [Tratamento de Exceções](#tratamento-de-exceções)
13. [Testes Implementados](#testes-implementados)
14. [Configurações e Dependências](#configurações-e-dependências)

---

## Visão Geral do Sistema

O Sistema de Teatro é uma aplicação desktop desenvolvida em **Java + JavaFX** com persistência em **MySQL**. O sistema gerencia eventos teatrais, sessões, áreas do teatro, poltronas e a venda de ingressos, oferecendo funcionalidades completas para administradores e usuários comuns.

### 🎯 Objetivos do Sistema

-   Gerenciar eventos teatrais e suas sessões
-   Controlar a ocupação de poltronas em tempo real
-   Permitir compra de ingressos com seleção visual de poltronas
-   Gerar estatísticas de vendas e ocupação
-   Fornecer interface intuitiva para diferentes tipos de usuário

---

## Arquitetura e Padrões de Projeto

### ��️ Arquitetura Geral

O sistema segue a arquitetura **MVC (Model-View-Controller)** com separação clara de responsabilidades:
```

┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ View │ │ Controller │ │ Model │
│ (JavaFX) │◄──►│ (Teatro) │◄──►│ (Entidades) │
└─────────────────┘ └─────────────────┘ └─────────────────┘
│
▼
┌─────────────────┐
│ DAO │
│ (MySQL) │
└─────────────────┘

```

### 📦 Estrutura de Pacotes
```

com.teatro/
├── model/ # Entidades de domínio
├── view/ # Interfaces JavaFX
├── controller/ # Controladores
├── dao/ # Acesso a dados
├── service/ # Lógica de negócio
├── database/ # Conexão com banco
├── exception/ # Exceções customizadas
├── util/ # Utilitários
├── observer/ # Padrão Observer
├── factory/ # Padrão Factory
├── state/ # Padrão State
└── builder/ # Padrão Builder

````

### �� Padrões de Projeto Implementados

#### 1. **Singleton**
- **Classe**: `DatabaseConnection`, `Teatro`, `TeatroLogger`
- **Propósito**: Garantir uma única instância de conexão com banco e serviços principais

#### 2. **DAO (Data Access Object)**
- **Classes**: `UsuarioDAO`, `EventoDAO`, `SessaoDAO`, `AreaDAO`, `IngressoDAO`
- **Propósito**: Abstrair a persistência de dados, facilitando mudanças no banco

#### 3. **Factory Method**
- **Classes**: `UsuarioFactory`, `UsuarioAdminFactory`, `UsuarioComumFactory`
- **Propósito**: Criar diferentes tipos de usuário (ADMIN/COMUM) de forma flexível

#### 4. **State Pattern**
- **Classes**: `PoltronaState`, `DisponivelState`, `OcupadaState`, `SelecionadaState`
- **Propósito**: Gerenciar os estados das poltronas (disponível, ocupada, selecionada)

#### 5. **Observer Pattern**
- **Classes**: `Subject`, `Observer`, `NotificacaoSubject`, `VendaLoggerObserver`
- **Propósito**: Notificar sobre vendas de ingressos e gerar logs

#### 6. **Builder Pattern**
- **Classes**: `AbstractBuilder`, `IngressoBuilder`, `UsuarioBuilder`
- **Propósito**: Construir objetos complexos de forma flexível

#### 7. **MVC (Model-View-Controller)**
- **Model**: Entidades no pacote `model`
- **View**: Interfaces JavaFX no pacote `view`
- **Controller**: Classe `Teatro` atuando como fachada

---

## Modelo de Dados e Entidades

### ��️ Entidades Principais

#### 1. **Usuario**
```java
public class Usuario {
    private Long id;
    private String nome;
    private String cpf;           // Único
    private String endereco;
    private String telefone;
    private String email;         // Único
    private String senha;         // Hasheada com SHA-256 + Salt
    private String tipoUsuario;   // "ADMIN" ou "COMUM"
}
````

#### 2. **Evento**

```java
public class Evento {
    private Long id;
    private String nome;          // "Hamlet", "O Fantasma da Opera", "O Auto da Compadecida"
    private List<Sessao> sessoes;
}
```

#### 3. **Sessao**

```java
public class Sessao {
    private Long id;
    private String nome;
    private TipoSessao tipoSessao;  // MANHA, TARDE, NOITE
    private Timestamp data;
    private List<Area> areas;
    private Long eventoId;
}
```

#### 4. **Area**

```java
public class Area {
    private Long id;
    private String nome;          // Plateia A, Plateia B, Camarotes, Frisas, Balcão Nobre
    private double preco;         // R$ 40,00 a R$ 250,00
    private int capacidadeTotal;  // 5 a 100 poltronas
    private List<Boolean> poltronas;  // Estado de cada poltrona
    private double faturamento;
    private Long sessaoId;
}
```

#### 5. **Poltrona**

```java
public class Poltrona {
    private int numero;
    private boolean ocupada;
    private String cpfOcupante;
    private Area area;
    private PoltronaState state;  // Padrão State
}
```

#### 6. **Ingresso**

```java
public class Ingresso {
    private Long id;
    private Long usuarioId;
    private Long sessaoId;
    private Long areaId;
    private int numeroPoltrona;
    private double valor;
    private Timestamp dataCompra;
    private String codigo;        // Código único do ingresso
}
```

### 💰 Configuração de Preços e Capacidades

| Área            | Capacidade        | Preço     |
| --------------- | ----------------- | --------- |
| Plateia A       | 25 poltronas      | R$ 40,00  |
| Plateia B       | 100 poltronas     | R$ 60,00  |
| Camarotes (1-5) | 10 poltronas cada | R$ 80,00  |
| Frisas (1-6)    | 5 poltronas cada  | R$ 120,00 |
| Balcão Nobre    | 50 poltronas      | R$ 250,00 |

---

## Funcionalidades Core do Sistema

### 🔐 Sistema de Autenticação

#### **Login Multi-identificador**

-   **Funcionalidade**: Login por CPF ou email
-   **Implementação**: `UsuarioDAO.autenticar(String identificador, String senha)`
-   **Segurança**: Senhas hasheadas com SHA-256 + Salt

#### **Recuperação de Senha**

-   **Funcionalidade**: Recuperação por CPF + Email
-   **Implementação**: `Teatro.recuperarSenha(Long id, String novaSenha)`
-   **Validação**: Verifica se CPF e email correspondem ao mesmo usuário

#### **Tipos de Usuário**

-   **ADMIN**: Acesso completo ao sistema + dashboard de estatísticas
-   **COMUM**: Acesso limitado (compra e visualização de ingressos)

### 🎭 Gerenciamento de Eventos

#### **Eventos Fixos**

-   Hamlet
-   O Fantasma da Opera
-   O Auto da Compadecida

#### **Sessões por Evento**

-   **Manhã**: 08:00, 09:30, 11:00
-   **Tarde**: 13:00, 14:30, 16:00
-   **Noite**: 18:00, 19:30, 21:00

### �� Sistema de Sessões

#### **TipoSessao Enum**

```java
public enum TipoSessao {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private String descricao;
}
```

#### **Geração de Sessões**

-   Cada evento possui múltiplas sessões
-   Sessões são criadas automaticamente com todas as áreas disponíveis
-   Data e horário são configuráveis

---

## Sistema de Autenticação e Segurança

### 🔒 Hash de Senhas

#### **PasswordHasher**

```java
public class PasswordHasher {
    // Gera salt aleatório de 16 bytes
    public static String generateSalt()

    // Hash com SHA-256 + Salt
    public static String hashPassword(String password, String salt)

    // Verifica senha contra hash armazenado
    public static boolean verifyPassword(String password, String storedHash)

    // Identifica se senha já está hasheada
    public static boolean isHashed(String storedHash)
}
```

#### **Formato de Armazenamento**

-   **Novo formato**: `salt:hash` (separados por `:`)
-   **Compatibilidade**: Suporte a senhas antigas em texto plano
-   **Migração automática**: `PasswordMigration.executarMigracaoSeNecessario()`

### ��️ Validações de Segurança

#### **Validator**

```java
public class Validator {
    public static void validarCpf(String cpf)
    public static void validarEmail(String email)
    public static void validarStringNaoVazia(String valor, String campo)
    public static void validarNumeroPositivo(int numero, String campo)
    public static void validarNaoNulo(Object objeto, String campo)
}
```

#### **Validações Implementadas**

-   **CPF**: Formato e dígitos verificadores
-   **Email**: Formato válido
-   **Senha**: Não vazia (pode ser expandida para força)
-   **Campos obrigatórios**: Nome, endereço, telefone
-   **Unicidade**: CPF e email únicos por usuário

---

## Gerenciamento de Poltronas

### �� Lógica de Ocupação

#### **Chave de Ocupação**

A ocupação de uma poltrona é determinada pela combinação única de:

-   `evento_id`
-   `data_sessao`
-   `horario`
-   `area_id`
-   `poltrona_id`

#### **Verificação de Disponibilidade**

```java
// Teatro.java
public boolean verificarPoltronaDisponivel(Sessao sessao, Area area, int numeroPoltrona) {
    return !ingressoService.poltronaOcupada(sessao.getId(), area.getId(), numeroPoltrona);
}

// IngressoService.java
public boolean poltronaOcupada(Long sessaoId, Long areaId, int numeroPoltrona) {
    return dao.poltronaOcupada(sessaoId, areaId, numeroPoltrona);
}
```

#### **Consulta SQL de Verificação**

```sql
SELECT COUNT(*) FROM ingressos
WHERE sessao_id = ? AND area_id = ? AND numero_poltrona = ?
```

### 🎨 Padrão State para Poltronas

#### **Estados Possíveis**

1. **Disponível**: Poltrona livre para compra
2. **Ocupada**: Poltrona já vendida
3. **Selecionada**: Poltrona temporariamente selecionada pelo usuário

#### **Implementação**

```java
public interface PoltronaState {
    void ocupar(Poltrona poltrona);
    void liberar(Poltrona poltrona);
    void selecionar(Poltrona poltrona);
    String getNomeEstado();
}
```

#### **Transições de Estado**

-   **Disponível → Selecionada**: Usuário clica na poltrona
-   **Selecionada → Ocupada**: Confirmação da compra
-   **Selecionada → Disponível**: Usuário deseleciona
-   **Ocupada → Disponível**: Cancelamento (não implementado)

### 🖥️ Interface Visual de Seleção

#### **SelecionarPoltronaView**

-   **Layout**: Grid de botões representando poltronas
-   **Cores**:
    -   Verde: Disponível
    -   Vermelho: Ocupada
    -   Azul: Selecionada
-   **Validação em tempo real**: Verifica disponibilidade antes de confirmar

#### **Funcionalidades**

-   **Seleção múltipla**: Usuário pode selecionar várias poltronas
-   **Resumo dinâmico**: Mostra quantidade e valor total
-   **Validação**: Verifica se poltronas ainda estão disponíveis na confirmação

---

## Sistema de Compra de Ingressos

### 🛒 Fluxo de Compra

#### **1. Seleção de Evento**

-   Lista de eventos disponíveis
-   Informações básicas (nome, poster)

#### **2. Seleção de Sessão**

-   Data e horário disponíveis
-   Filtro por tipo de sessão (Manhã/Tarde/Noite)

#### **3. Seleção de Área**

-   Lista de áreas com poltronas disponíveis
-   Preço e capacidade mostrados
-   Atualização em tempo real da disponibilidade

#### **4. Seleção de Poltronas**

-   Interface visual com grid de poltronas
-   Seleção múltipla permitida
-   Resumo com quantidade e valor total

#### **5. Confirmação e Pagamento**

-   Resumo da compra
-   Geração de código único do ingresso
-   Persistência no banco de dados

### �� Geração de Ingresso

#### **IngressoModerno**

```java
public class IngressoModerno {
    private Long id;
    private String eventoNome;
    private String horario;
    private String dataSessao;
    private String areaNome;
    private int numeroPoltrona;
    private double valor;
    private Timestamp dataCompra;
    private String codigo;
}
```

#### **Formato de Impressão**

```
=========================================
INGRESSO - TEATRO
=========================================
Evento: %s
Sessão: %s
Área: %s
Poltrona: %d
Valor: R$ %.2f
Data da Compra: %s
=========================================
```

### 🔄 Transações e Concorrência

#### **Problema de Concorrência**

-   Múltiplos usuários podem tentar comprar a mesma poltrona
-   Necessário verificar disponibilidade antes de confirmar

#### **Solução Implementada**

1. **Verificação inicial**: Busca poltronas disponíveis
2. **Verificação na seleção**: Cada clique verifica disponibilidade
3. **Verificação na confirmação**: Última verificação antes de salvar
4. **Rollback automático**: Se alguma poltrona ficar indisponível

#### **Código de Verificação**

```java
// Verifica se todas as poltronas selecionadas ainda estão disponíveis
for (Poltrona poltrona : new ArrayList<>(poltronasSelecionadas)) {
    if (!teatro.verificarPoltronaDisponivel(sessao, area, poltrona.getNumero())) {
        // Remove poltrona da seleção e notifica usuário
        poltronasSelecionadas.remove(poltrona);
        atualizarResumo();
        return;
    }
}
```

---

## Dashboard e Estatísticas

### 📊 Dashboard de Administrador

#### **Estatísticas Disponíveis**

1. **Vendas por Peça**

    - Peça mais vendida
    - Peça menos vendida
    - Total de ingressos por peça

2. **Ocupação por Sessão**

    - Sessão com maior ocupação
    - Sessão com menor ocupação
    - Percentual de ocupação

3. **Faturamento**
    - Peça mais lucrativa
    - Peça menos lucrativa
    - Lucro médio por peça

#### **EstatisticasService**

```java
public class EstatisticasService {
    public Map<String, Object> buscarEstatisticas() {
        // Busca todas as estatísticas usando views SQL
        estatisticas.put("pecaMaisVendida", buscarPecaMaisVendida());
        estatisticas.put("pecaMenosVendida", buscarPecaMenosVendida());
        estatisticas.put("sessaoMaiorOcupacao", buscarSessaoMaiorOcupacao());
        // ... outras estatísticas
    }
}
```

### 🗃️ Views SQL para Estatísticas

#### **estatisticas_vendas_peca**

```sql
CREATE OR REPLACE VIEW estatisticas_vendas_peca AS
SELECT
    e.nome AS nome_peca,
    COUNT(i.id) AS total_ingressos_vendidos,
    SUM(i.valor) AS faturamento_total,
    AVG(i.valor) AS valor_medio_ingresso
FROM eventos e
    LEFT JOIN sessoes s ON s.evento_id = e.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id
GROUP BY e.id, e.nome
ORDER BY total_ingressos_vendidos DESC;
```

#### **estatisticas_ocupacao_sessao**

```sql
CREATE OR REPLACE VIEW estatisticas_ocupacao_sessao AS
WITH total_poltronas AS (
    SELECT s.id AS sessao_id, SUM(a.capacidade_total) AS total_poltronas
    FROM sessoes s
    JOIN sessoes_areas sa ON sa.sessao_id = s.id
    JOIN areas a ON a.id = sa.area_id
    GROUP BY s.id
)
SELECT
    e.nome AS nome_peca,
    s.data_sessao AS data_sessao,
    s.horario AS horario,
    COUNT(i.id) AS ingressos_vendidos,
    tp.total_poltronas,
    ROUND((COUNT(i.id) / tp.total_poltronas) * 100, 2) AS percentual_ocupacao
FROM eventos e
    JOIN sessoes s ON s.evento_id = e.id
    JOIN total_poltronas tp ON tp.sessao_id = s.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id
GROUP BY e.nome, s.id, s.data_sessao, s.horario, tp.total_poltronas
ORDER BY percentual_ocupacao DESC;
```

### �� Integração com Metabase

#### **Configuração**

-   Conexão com MySQL
-   Dashboards pré-configurados
-   Gráficos interativos

#### **Dashboards Disponíveis**

1. **Vendas por Peça**: Gráficos de barras e pizza
2. **Ocupação**: Percentual por sessão
3. **Faturamento**: Evolução temporal
4. **Análise Comparativa**: Relação ocupação vs faturamento

---

## Interface Gráfica

### 🎨 Design System

#### **Cores do Tema**

```java
private static final String PRIMARY_COLOR = "#3498db";    // Azul principal
private static final String SECONDARY_COLOR = "#2ecc71";  // Verde secundário
private static final String BACKGROUND_COLOR = "#f8f9fa"; // Fundo claro
private static final String TEXT_COLOR = "#2c3e50";       // Texto escuro
private static final String CARD_BACKGROUND = "white";    // Fundo de cards
```

#### **Cores das Poltronas**

```java
private static final String POLTRONA_DISPONIVEL = "#2ecc71";  // Verde
private static final String POLTRONA_OCUPADA = "#e74c3c";     // Vermelho
private static final String POLTRONA_SELECIONADA = "#3498db"; // Azul
```

### �� Telas Principais

#### **1. LoginView**

-   **Funcionalidades**: Login, cadastro, recuperação de senha
-   **Validações**: CPF/email obrigatório, senha obrigatória
-   **Feedback**: Mensagens de erro específicas

#### **2. DashboardView**

-   **Usuário Comum**: Boas-vindas, botões de ação
-   **Admin**: Estatísticas completas, gráficos, relatórios

#### **3. SessoesView**

-   **Funcionalidades**: Lista de eventos, seleção de sessão
-   **Layout**: Cards com posters dos eventos
-   **Navegação**: Filtros por tipo de sessão

#### **4. CompraIngressoView**

-   **Funcionalidades**: Seleção de área, informações de preço
-   **Atualização**: Disponibilidade em tempo real
-   **Validação**: Verificação de poltronas disponíveis

#### **5. SelecionarPoltronaView**

-   **Layout**: Grid responsivo de poltronas
-   **Interação**: Clique para selecionar/deselecionar
-   **Resumo**: Quantidade e valor total dinâmico

#### **6. ImpressaoIngressoView**

-   **Funcionalidades**: Visualização de ingressos comprados
-   **Formato**: Layout similar ao ingresso físico
-   **Filtros**: Por usuário logado

### �� Componentes Reutilizáveis

#### **EventoItemController**

-   **Propósito**: Card de evento reutilizável
-   **Dados**: Nome, poster, descrição
-   **Ação**: Navegação para sessões

#### **TopBar**

-   **Elementos**: Logo, navegação, informações do usuário
-   **Funcionalidades**: Logout, navegação entre telas

---

## Persistência de Dados

### 🗄️ Estrutura do Banco

#### **Tabelas Principais**

```sql
-- Usuários
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    endereco VARCHAR(200),
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,  -- Hash SHA-256 + Salt
    tipo_usuario ENUM('ADMIN', 'COMUM') DEFAULT 'COMUM'
);

-- Eventos
CREATE TABLE eventos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL
);

-- Sessões
CREATE TABLE sessoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    tipo_sessao ENUM('MANHA', 'TARDE', 'NOITE'),
    data_sessao TIMESTAMP,
    evento_id BIGINT,
    FOREIGN KEY (evento_id) REFERENCES eventos(id)
);

-- Áreas
CREATE TABLE areas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    capacidade_total INT NOT NULL
);

-- Relacionamento Sessões-Áreas
CREATE TABLE sessoes_areas (
    sessao_id BIGINT,
    area_id BIGINT,
    PRIMARY KEY (sessao_id, area_id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    FOREIGN KEY (area_id) REFERENCES areas(id)
);

-- Ingressos
CREATE TABLE ingressos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT,
    sessao_id BIGINT,
    area_id BIGINT,
    numero_poltrona INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    FOREIGN KEY (area_id) REFERENCES areas(id)
);
```

### 🔗 Relacionamentos

#### **Cardinalidades**

-   **Evento** → **Sessão**: 1:N (Um evento tem várias sessões)
-   **Sessão** → **Área**: N:N (Uma sessão tem várias áreas, uma área pode estar em várias sessões)
-   **Sessão** → **Ingresso**: 1:N (Uma sessão tem vários ingressos)
-   **Área** → **Ingresso**: 1:N (Uma área tem vários ingressos)
-   **Usuário** → **Ingresso**: 1:N (Um usuário pode ter vários ingressos)

#### **Constraints**

-   **CPF único**: Não pode haver dois usuários com mesmo CPF
-   **Email único**: Não pode haver dois usuários com mesmo email
-   **Poltrona única**: Não pode haver dois ingressos para mesma poltrona na mesma sessão/área
-   **Código único**: Cada ingresso tem código único

### �� Pool de Conexões

#### **DatabaseConnection (Singleton)**

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Queue<Connection> connectionPool;
    private final String url, user, password;

    public Connection getConnection() throws SQLException
    public void releaseConnection(Connection conn)
    public void closeConnection()
}
```

#### **Configuração**

-   **Driver**: `com.mysql.cj.jdbc.Driver`
-   **URL**: `jdbc:mysql://localhost:3306/teatro_db`
-   **Usuário**: `root`
-   **Senha**: `root`
-   **Pool**: Queue com conexões reutilizáveis

---

## Sistema de Logging e Observers

### �� TeatroLogger

#### **Implementação**

```java
public class TeatroLogger {
    private static TeatroLogger instance;
    private final Logger logger;

    public void info(String message)
    public void error(String message)
    public void warn(String message)
    public void debug(String message)
}
```

#### **Configuração (logback.xml)**

```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/teatro.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/teatro.%d{yyyy-MM-dd}.log</fileNamePattern>
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### ��️ Padrão Observer

#### **Implementação**

```java
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object data);
}

public interface Observer {
    void update(Object data);
}
```

#### **Observers Implementados**

1. **VendaLoggerObserver**: Registra vendas de ingressos
2. **NotificacaoSubject**: Gerencia notificações de vendas

#### **Uso no Sistema**

```java
// Quando um ingresso é vendido
notifyObservers(new NotificacaoVenda(ingresso));

// Observer registra a venda
public void update(Object data) {
    if (data instanceof NotificacaoVenda) {
        NotificacaoVenda venda = (NotificacaoVenda) data;
        logger.info("Venda realizada: " + venda.getIngresso().getCodigo());
    }
}
```

---

## Tratamento de Exceções

### �� Hierarquia de Exceções

#### **TeatroException (Base)**

```java
public class TeatroException extends RuntimeException {
    public TeatroException(String message)
    public TeatroException(String message, Throwable cause)
}
```

#### **Exceções Específicas**

1. **UsuarioNaoEncontradoException**: Usuário não encontrado
2. **SessaoNaoEncontradaException**: Sessão não encontrada
3. **AreaException**: Erro relacionado a áreas
4. **IngressoException**: Erro relacionado a ingressos
5. **PoltronaOcupadaException**: Poltrona já está ocupada

#### **PoltronaOcupadaException**

```java
public class PoltronaOcupadaException extends TeatroException {
    public PoltronaOcupadaException(int numeroPoltrona, String areaNome) {
        super(String.format("A poltrona %d da área %s já está ocupada",
              numeroPoltrona, areaNome));
    }
}
```

### 🛡️ Tratamento na Interface

#### **Try-Catch nas Views**

```java
try {
    Optional<Usuario> usuarioOpt = teatro.autenticarUsuario(identificador, senha);
    if (usuarioOpt.isPresent()) {
        // Login bem-sucedido
    } else {
        errorLabel.setText("CPF/Email ou senha incorretos.");
        errorLabel.setVisible(true);
    }
} catch (TeatroException ex) {
    // Exceções de validação
    errorLabel.setText("Dados incorretos: " + ex.getMessage());
    errorLabel.setVisible(true);
} catch (Exception ex) {
    // Exceções inesperadas
    errorLabel.setText("Erro ao fazer login. Tente novamente.");
    errorLabel.setVisible(true);
}
```

#### **Validações Preventivas**

```java
// Antes de tentar comprar
if (!teatro.verificarPoltronaDisponivel(sessao, area, numero)) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Atenção");
    alert.setHeaderText("Poltrona ocupada");
    alert.setContentText("Esta poltrona acabou de ser ocupada. Por favor, selecione outra.");
    alert.showAndWait();
    return;
}
```

---

## Testes Implementados

### �� Testes Unitários

#### **PasswordHasherTest**

```java
@Test
void testHashPassword() {
    String hash = PasswordHasher.hashPassword(senhaTeste);
    assertNotNull(hash);
    assertTrue(hash.contains(":"));
    assertNotEquals(senhaTeste, hash);
}

@Test
void testVerifyPassword() {
    String hash = PasswordHasher.hashPassword(senhaTeste);
    assertTrue(PasswordHasher.verifyPassword(senhaTeste, hash));
    assertFalse(PasswordHasher.verifyPassword("senhaErrada", hash));
}
```

#### **UsuarioFactoryTest**

```java
@Test
void testCriarUsuarioAdmin() {
    Usuario admin = UsuarioAdminFactory.criarUsuario("Admin", "123.456.789-00", "admin@teatro.com");
    assertEquals("ADMIN", admin.getTipoUsuario());
}

@Test
void testCriarUsuarioComum() {
    Usuario comum = UsuarioComumFactory.criarUsuario("João", "123.456.789-00", "joao@email.com");
    assertEquals("COMUM", comum.getTipoUsuario());
}
```

#### **PoltronaStateTest**

```java
@Test
void testDisponivelState() {
    Poltrona poltrona = new Poltrona(1);
    poltrona.setState(new DisponivelState());

    assertEquals("Disponível", poltrona.getNomeEstado());
    poltrona.ocupar();
    assertEquals("Ocupada", poltrona.getNomeEstado());
}
```

#### **EstatisticasServiceTest**

```java
@Test
void testEstruturaEstatisticas() {
    Map<String, Object> estatisticas = estatisticasService.buscarEstatisticas();

    assertTrue(estatisticas.containsKey("pecaMaisVendida"));
    assertTrue(estatisticas.containsKey("sessaoMaiorOcupacao"));
    assertTrue(estatisticas.containsKey("pecaMaisLucrativa"));
}
```

### 📊 Cobertura de Testes

#### **Funcionalidades Testadas**

-   ✅ Hash e verificação de senhas
-   ✅ Factory de usuários
-   ✅ Estados de poltronas
-   ✅ Estrutura de estatísticas
-   ✅ Observers de venda

#### **Funcionalidades que Precisam de Testes**

-   ❌ DAOs (acesso a banco)
-   ❌ Services (lógica de negócio)
-   ❌ Views (interface gráfica)
-   ❌ Validações de entrada

---

## Configurações e Dependências

### 📦 Maven (pom.xml)

#### **Dependências Principais**

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.7</version>
    </dependency>

    <!-- JUnit para testes -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### **Plugins**

```xml
<plugins>
    <!-- JavaFX Maven Plugin -->
    <plugin>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-maven-plugin</artifactId>
        <version>0.0.8</version>
        <configuration>
            <mainClass>com.teatro.Main</mainClass>
        </configuration>
    </plugin>

    <!-- Compiler Plugin -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.11.0</version>
        <configuration>
            <source>17</source>
            <target>17</target>
        </configuration>
    </plugin>
</plugins>
```

### ⚙️ Configurações de Banco

#### **database.properties**

```properties
db.url=jdbc:mysql://localhost:3306/teatro_db
db.user=root
db.password=root
db.driver=com.mysql.cj.jdbc.Driver
```

#### **database.sql**

-   Script de criação das tabelas
-   Inserção de dados iniciais
-   Criação das views de estatísticas

### 🚀 Execução

#### **Comandos Maven**

```bash
# Compilar
mvn clean compile

# Executar
mvn javafx:run

# Testes
mvn test

# Package
mvn clean package
```

#### **Script de Execução (executar.sh)**

```bash
#!/bin/bash
mvn clean compile
mvn javafx:run
```

---

## 🔍 Análise de Pontos Fortes e Melhorias

### ✅ Pontos Fortes

1. **Arquitetura Bem Estruturada**

    - Separação clara de responsabilidades (MVC)
    - Padrões de projeto bem implementados
    - Código modular e reutilizável

2. **Segurança Robusta**

    - Hash de senhas com salt
    - Validações de entrada
    - Migração automática de senhas

3. **Interface Intuitiva**

    - Design moderno e responsivo
    - Feedback visual claro
    - Navegação intuitiva

4. **Gerenciamento de Poltronas**

    - Lógica de ocupação bem definida
    - Verificação em tempo real
    - Padrão State para estados

5. **Estatísticas Avançadas**
    - Views SQL otimizadas
    - Dashboard completo para admin
    - Integração com Metabase

### 🔧 Melhorias Sugeridas

1. **Testes**

    - Aumentar cobertura de testes
    - Testes de integração
    - Testes de interface

2. **Performance**

    - Cache para consultas frequentes
    - Otimização de queries
    - Paginação de resultados

3. **Funcionalidades**

    - Reserva temporária de poltronas
    - Sistema de pagamento real
    - Notificações por email

4. **Segurança**

    - Rate limiting
    - Auditoria de ações
    - Política de senhas forte

5. **Usabilidade**
    - Tema escuro
    - Responsividade mobile
    - Acessibilidade

---

## 📋 Conclusão

O Sistema de Teatro é uma aplicação robusta e bem estruturada que demonstra:

-   **Conhecimento sólido de Java e JavaFX**
-   **Aplicação correta de padrões de projeto**
-   **Preocupação com segurança e validações**
-   **Interface gráfica moderna e intuitiva**
-   **Persistência de dados eficiente**
-   **Sistema de logging e observação**

O projeto está pronto para ser migrado para web, servindo como base sólida para aprender tecnologias como Spring Boot, React/Vue.js e desenvolvimento de APIs REST. Todas as funcionalidades core estão implementadas e podem ser facilmente adaptadas para uma arquitetura web moderna.

````

Este arquivo técnico detalhado fornece uma análise completa de todas as funcionalidades do projeto, incluindo a lógica específica de ocupação de poltronas que você mencionou. O documento explica como o sistema garante que uma poltrona ocupada em um evento/data/horário/área específica apareça como indisponível para outros usuários, mas fique disponível se qualquer um dos parâmetros mudar.

Você pode salvar este conteúdo como `ANALISE_TECNICA_COMPLETA.md` para ter uma referência completa de todas as funcionalidades do projeto!

```markdown
# Análise Técnica Completa - Sistema de Teatro JavaFX

## 📋 Índice
1. [Visão Geral do Sistema](#visão-geral-do-sistema)
2. [Arquitetura e Padrões de Projeto](#arquitetura-e-padrões-de-projeto)
3. [Modelo de Dados e Entidades](#modelo-de-dados-e-entidades)
4. [Funcionalidades Core do Sistema](#funcionalidades-core-do-sistema)
5. [Sistema de Autenticação e Segurança](#sistema-de-autenticação-e-segurança)
6. [Gerenciamento de Poltronas](#gerenciamento-de-poltronas)
7. [Sistema de Compra de Ingressos](#sistema-de-compra-de-ingressos)
8. [Dashboard e Estatísticas](#dashboard-e-estatísticas)
9. [Interface Gráfica](#interface-gráfica)
10. [Persistência de Dados](#persistência-de-dados)
11. [Sistema de Logging e Observers](#sistema-de-logging-e-observers)
12. [Tratamento de Exceções](#tratamento-de-exceções)
13. [Testes Implementados](#testes-implementados)
14. [Configurações e Dependências](#configurações-e-dependências)

---

## Visão Geral do Sistema

O Sistema de Teatro é uma aplicação desktop desenvolvida em **Java + JavaFX** com persistência em **MySQL**. O sistema gerencia eventos teatrais, sessões, áreas do teatro, poltronas e a venda de ingressos, oferecendo funcionalidades completas para administradores e usuários comuns.

### 🎯 Objetivos do Sistema
- Gerenciar eventos teatrais e suas sessões
- Controlar a ocupação de poltronas em tempo real
- Permitir compra de ingressos com seleção visual de poltronas
- Gerar estatísticas de vendas e ocupação
- Fornecer interface intuitiva para diferentes tipos de usuário

---

## Arquitetura e Padrões de Projeto

### ��️ Arquitetura Geral
O sistema segue a arquitetura **MVC (Model-View-Controller)** com separação clara de responsabilidades:

````

```plaintext

### 📦 Estrutura de Pacotes
```

````plaintext

### �� Padrões de Projeto Implementados

#### 1. **Singleton**
- **Classe**: `DatabaseConnection`, `Teatro`, `TeatroLogger`
- **Propósito**: Garantir uma única instância de conexão com banco e serviços principais

#### 2. **DAO (Data Access Object)**
- **Classes**: `UsuarioDAO`, `EventoDAO`, `SessaoDAO`, `AreaDAO`, `IngressoDAO`
- **Propósito**: Abstrair a persistência de dados, facilitando mudanças no banco

#### 3. **Factory Method**
- **Classes**: `UsuarioFactory`, `UsuarioAdminFactory`, `UsuarioComumFactory`
- **Propósito**: Criar diferentes tipos de usuário (ADMIN/COMUM) de forma flexível

#### 4. **State Pattern**
- **Classes**: `PoltronaState`, `DisponivelState`, `OcupadaState`, `SelecionadaState`
- **Propósito**: Gerenciar os estados das poltronas (disponível, ocupada, selecionada)

#### 5. **Observer Pattern**
- **Classes**: `Subject`, `Observer`, `NotificacaoSubject`, `VendaLoggerObserver`
- **Propósito**: Notificar sobre vendas de ingressos e gerar logs

#### 6. **Builder Pattern**
- **Classes**: `AbstractBuilder`, `IngressoBuilder`, `UsuarioBuilder`
- **Propósito**: Construir objetos complexos de forma flexível

#### 7. **MVC (Model-View-Controller)**
- **Model**: Entidades no pacote `model`
- **View**: Interfaces JavaFX no pacote `view`
- **Controller**: Classe `Teatro` atuando como fachada

---

## Modelo de Dados e Entidades

### ��️ Entidades Principais

#### 1. **Usuario**
```java
public class Usuario {
    private Long id;
    private String nome;
    private String cpf;           // Único
    private String endereco;
    private String telefone;
    private String email;         // Único
    private String senha;         // Hasheada com SHA-256 + Salt
    private String tipoUsuario;   // "ADMIN" ou "COMUM"
}
````

#### 2. **Evento**

```java
public class Evento {
    private Long id;
    private String nome;          // "Hamlet", "O Fantasma da Opera", "O Auto da Compadecida"
    private List<Sessao> sessoes;
}
```

#### 3. **Sessao**

```java
public class Sessao {
    private Long id;
    private String nome;
    private TipoSessao tipoSessao;  // MANHA, TARDE, NOITE
    private Timestamp data;
    private List<Area> areas;
    private Long eventoId;
}
```

#### 4. **Area**

```java
public class Area {
    private Long id;
    private String nome;          // Plateia A, Plateia B, Camarotes, Frisas, Balcão Nobre
    private double preco;         // R$ 40,00 a R$ 250,00
    private int capacidadeTotal;  // 5 a 100 poltronas
    private List<Boolean> poltronas;  // Estado de cada poltrona
    private double faturamento;
    private Long sessaoId;
}
```

#### 5. **Poltrona**

```java
public class Poltrona {
    private int numero;
    private boolean ocupada;
    private String cpfOcupante;
    private Area area;
    private PoltronaState state;  // Padrão State
}
```

#### 6. **Ingresso**

```java
public class Ingresso {
    private Long id;
    private Long usuarioId;
    private Long sessaoId;
    private Long areaId;
    private int numeroPoltrona;
    private double valor;
    private Timestamp dataCompra;
    private String codigo;        // Código único do ingresso
}
```

### 💰 Configuração de Preços e Capacidades

| Área            | Capacidade        | Preço     |
| --------------- | ----------------- | --------- |
| Plateia A       | 25 poltronas      | R$ 40,00  |
| Plateia B       | 100 poltronas     | R$ 60,00  |
| Camarotes (1-5) | 10 poltronas cada | R$ 80,00  |
| Frisas (1-6)    | 5 poltronas cada  | R$ 120,00 |
| Balcão Nobre    | 50 poltronas      | R$ 250,00 |

---

## Funcionalidades Core do Sistema

### 🔐 Sistema de Autenticação

#### **Login Multi-identificador**

-   **Funcionalidade**: Login por CPF ou email
-   **Implementação**: `UsuarioDAO.autenticar(String identificador, String senha)`
-   **Segurança**: Senhas hasheadas com SHA-256 + Salt

#### **Recuperação de Senha**

-   **Funcionalidade**: Recuperação por CPF + Email
-   **Implementação**: `Teatro.recuperarSenha(Long id, String novaSenha)`
-   **Validação**: Verifica se CPF e email correspondem ao mesmo usuário

#### **Tipos de Usuário**

-   **ADMIN**: Acesso completo ao sistema + dashboard de estatísticas
-   **COMUM**: Acesso limitado (compra e visualização de ingressos)

### 🎭 Gerenciamento de Eventos

#### **Eventos Fixos**

-   Hamlet
-   O Fantasma da Opera
-   O Auto da Compadecida

#### **Sessões por Evento**

-   **Manhã**: 08:00, 09:30, 11:00
-   **Tarde**: 13:00, 14:30, 16:00
-   **Noite**: 18:00, 19:30, 21:00

### �� Sistema de Sessões

#### **TipoSessao Enum**

```java
public enum TipoSessao {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private String descricao;
}
```

#### **Geração de Sessões**

-   Cada evento possui múltiplas sessões
-   Sessões são criadas automaticamente com todas as áreas disponíveis
-   Data e horário são configuráveis

---

## Sistema de Autenticação e Segurança

### 🔒 Hash de Senhas

#### **PasswordHasher**

```java
public class PasswordHasher {
    // Gera salt aleatório de 16 bytes
    public static String generateSalt()

    // Hash com SHA-256 + Salt
    public static String hashPassword(String password, String salt)

    // Verifica senha contra hash armazenado
    public static boolean verifyPassword(String password, String storedHash)

    // Identifica se senha já está hasheada
    public static boolean isHashed(String storedHash)
}
```

#### **Formato de Armazenamento**

-   **Novo formato**: `salt:hash` (separados por `:`)
-   **Compatibilidade**: Suporte a senhas antigas em texto plano
-   **Migração automática**: `PasswordMigration.executarMigracaoSeNecessario()`

### ��️ Validações de Segurança

#### **Validator**

```java
public class Validator {
    public static void validarCpf(String cpf)
    public static void validarEmail(String email)
    public static void validarStringNaoVazia(String valor, String campo)
    public static void validarNumeroPositivo(int numero, String campo)
    public static void validarNaoNulo(Object objeto, String campo)
}
```

#### **Validações Implementadas**

-   **CPF**: Formato e dígitos verificadores
-   **Email**: Formato válido
-   **Senha**: Não vazia (pode ser expandida para força)
-   **Campos obrigatórios**: Nome, endereço, telefone
-   **Unicidade**: CPF e email únicos por usuário

---

## Gerenciamento de Poltronas

### �� Lógica de Ocupação

#### **Chave de Ocupação**

A ocupação de uma poltrona é determinada pela combinação única de:

-   `evento_id`
-   `data_sessao`
-   `horario`
-   `area_id`
-   `poltrona_id`

#### **Verificação de Disponibilidade**

```java
// Teatro.java
public boolean verificarPoltronaDisponivel(Sessao sessao, Area area, int numeroPoltrona) {
    return !ingressoService.poltronaOcupada(sessao.getId(), area.getId(), numeroPoltrona);
}

// IngressoService.java
public boolean poltronaOcupada(Long sessaoId, Long areaId, int numeroPoltrona) {
    return dao.poltronaOcupada(sessaoId, areaId, numeroPoltrona);
}
```

#### **Consulta SQL de Verificação**

```sql
SELECT COUNT(*) FROM ingressos
WHERE sessao_id = ? AND area_id = ? AND numero_poltrona = ?
```

### 🎨 Padrão State para Poltronas

#### **Estados Possíveis**

1. **Disponível**: Poltrona livre para compra
2. **Ocupada**: Poltrona já vendida
3. **Selecionada**: Poltrona temporariamente selecionada pelo usuário

#### **Implementação**

```java
public interface PoltronaState {
    void ocupar(Poltrona poltrona);
    void liberar(Poltrona poltrona);
    void selecionar(Poltrona poltrona);
    String getNomeEstado();
}
```

#### **Transições de Estado**

-   **Disponível → Selecionada**: Usuário clica na poltrona
-   **Selecionada → Ocupada**: Confirmação da compra
-   **Selecionada → Disponível**: Usuário deseleciona
-   **Ocupada → Disponível**: Cancelamento (não implementado)

### 🖥️ Interface Visual de Seleção

#### **SelecionarPoltronaView**

-   **Layout**: Grid de botões representando poltronas
-   **Cores**:
    -   Verde: Disponível
    -   Vermelho: Ocupada
    -   Azul: Selecionada
-   **Validação em tempo real**: Verifica disponibilidade antes de confirmar

#### **Funcionalidades**

-   **Seleção múltipla**: Usuário pode selecionar várias poltronas
-   **Resumo dinâmico**: Mostra quantidade e valor total
-   **Validação**: Verifica se poltronas ainda estão disponíveis na confirmação

---

## Sistema de Compra de Ingressos

### 🛒 Fluxo de Compra

#### **1. Seleção de Evento**

-   Lista de eventos disponíveis
-   Informações básicas (nome, poster)

#### **2. Seleção de Sessão**

-   Data e horário disponíveis
-   Filtro por tipo de sessão (Manhã/Tarde/Noite)

#### **3. Seleção de Área**

-   Lista de áreas com poltronas disponíveis
-   Preço e capacidade mostrados
-   Atualização em tempo real da disponibilidade

#### **4. Seleção de Poltronas**

-   Interface visual com grid de poltronas
-   Seleção múltipla permitida
-   Resumo com quantidade e valor total

#### **5. Confirmação e Pagamento**

-   Resumo da compra
-   Geração de código único do ingresso
-   Persistência no banco de dados

### �� Geração de Ingresso

#### **IngressoModerno**

```java
public class IngressoModerno {
    private Long id;
    private String eventoNome;
    private String horario;
    private String dataSessao;
    private String areaNome;
    private int numeroPoltrona;
    private double valor;
    private Timestamp dataCompra;
    private String codigo;
}
```

#### **Formato de Impressão**

````

```plaintext

### 🔄 Transações e Concorrência

#### **Problema de Concorrência**
- Múltiplos usuários podem tentar comprar a mesma poltrona
- Necessário verificar disponibilidade antes de confirmar

#### **Solução Implementada**
1. **Verificação inicial**: Busca poltronas disponíveis
2. **Verificação na seleção**: Cada clique verifica disponibilidade
3. **Verificação na confirmação**: Última verificação antes de salvar
4. **Rollback automático**: Se alguma poltrona ficar indisponível

#### **Código de Verificação**
```java
// Verifica se todas as poltronas selecionadas ainda estão disponíveis
for (Poltrona poltrona : new ArrayList<>(poltronasSelecionadas)) {
    if (!teatro.verificarPoltronaDisponivel(sessao, area, poltrona.getNumero())) {
        // Remove poltrona da seleção e notifica usuário
        poltronasSelecionadas.remove(poltrona);
        atualizarResumo();
        return;
    }
}
````

---

## Dashboard e Estatísticas

### 📊 Dashboard de Administrador

#### **Estatísticas Disponíveis**

1. **Vendas por Peça**

    - Peça mais vendida
    - Peça menos vendida
    - Total de ingressos por peça

2. **Ocupação por Sessão**

    - Sessão com maior ocupação
    - Sessão com menor ocupação
    - Percentual de ocupação

3. **Faturamento**
    - Peça mais lucrativa
    - Peça menos lucrativa
    - Lucro médio por peça

#### **EstatisticasService**

```java
public class EstatisticasService {
    public Map<String, Object> buscarEstatisticas() {
        // Busca todas as estatísticas usando views SQL
        estatisticas.put("pecaMaisVendida", buscarPecaMaisVendida());
        estatisticas.put("pecaMenosVendida", buscarPecaMenosVendida());
        estatisticas.put("sessaoMaiorOcupacao", buscarSessaoMaiorOcupacao());
        // ... outras estatísticas
    }
}
```

### 🗃️ Views SQL para Estatísticas

#### **estatisticas_vendas_peca**

```sql
CREATE OR REPLACE VIEW estatisticas_vendas_peca AS
SELECT
    e.nome AS nome_peca,
    COUNT(i.id) AS total_ingressos_vendidos,
    SUM(i.valor) AS faturamento_total,
    AVG(i.valor) AS valor_medio_ingresso
FROM eventos e
    LEFT JOIN sessoes s ON s.evento_id = e.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id
GROUP BY e.id, e.nome
ORDER BY total_ingressos_vendidos DESC;
```

#### **estatisticas_ocupacao_sessao**

```sql
CREATE OR REPLACE VIEW estatisticas_ocupacao_sessao AS
WITH total_poltronas AS (
    SELECT s.id AS sessao_id, SUM(a.capacidade_total) AS total_poltronas
    FROM sessoes s
    JOIN sessoes_areas sa ON sa.sessao_id = s.id
    JOIN areas a ON a.id = sa.area_id
    GROUP BY s.id
)
SELECT
    e.nome AS nome_peca,
    s.data_sessao AS data_sessao,
    s.horario AS horario,
    COUNT(i.id) AS ingressos_vendidos,
    tp.total_poltronas,
    ROUND((COUNT(i.id) / tp.total_poltronas) * 100, 2) AS percentual_ocupacao
FROM eventos e
    JOIN sessoes s ON s.evento_id = e.id
    JOIN total_poltronas tp ON tp.sessao_id = s.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id
GROUP BY e.nome, s.id, s.data_sessao, s.horario, tp.total_poltronas
ORDER BY percentual_ocupacao DESC;
```

### �� Integração com Metabase

#### **Configuração**

-   Conexão com MySQL
-   Dashboards pré-configurados
-   Gráficos interativos

#### **Dashboards Disponíveis**

1. **Vendas por Peça**: Gráficos de barras e pizza
2. **Ocupação**: Percentual por sessão
3. **Faturamento**: Evolução temporal
4. **Análise Comparativa**: Relação ocupação vs faturamento

---

## Interface Gráfica

### 🎨 Design System

#### **Cores do Tema**

```java
private static final String PRIMARY_COLOR = "#3498db";    // Azul principal
private static final String SECONDARY_COLOR = "#2ecc71";  // Verde secundário
private static final String BACKGROUND_COLOR = "#f8f9fa"; // Fundo claro
private static final String TEXT_COLOR = "#2c3e50";       // Texto escuro
private static final String CARD_BACKGROUND = "white";    // Fundo de cards
```

#### **Cores das Poltronas**

```java
private static final String POLTRONA_DISPONIVEL = "#2ecc71";  // Verde
private static final String POLTRONA_OCUPADA = "#e74c3c";     // Vermelho
private static final String POLTRONA_SELECIONADA = "#3498db"; // Azul
```

### �� Telas Principais

#### **1. LoginView**

-   **Funcionalidades**: Login, cadastro, recuperação de senha
-   **Validações**: CPF/email obrigatório, senha obrigatória
-   **Feedback**: Mensagens de erro específicas

#### **2. DashboardView**

-   **Usuário Comum**: Boas-vindas, botões de ação
-   **Admin**: Estatísticas completas, gráficos, relatórios

#### **3. SessoesView**

-   **Funcionalidades**: Lista de eventos, seleção de sessão
-   **Layout**: Cards com posters dos eventos
-   **Navegação**: Filtros por tipo de sessão

#### **4. CompraIngressoView**

-   **Funcionalidades**: Seleção de área, informações de preço
-   **Atualização**: Disponibilidade em tempo real
-   **Validação**: Verificação de poltronas disponíveis

#### **5. SelecionarPoltronaView**

-   **Layout**: Grid responsivo de poltronas
-   **Interação**: Clique para selecionar/deselecionar
-   **Resumo**: Quantidade e valor total dinâmico

#### **6. ImpressaoIngressoView**

-   **Funcionalidades**: Visualização de ingressos comprados
-   **Formato**: Layout similar ao ingresso físico
-   **Filtros**: Por usuário logado

### �� Componentes Reutilizáveis

#### **EventoItemController**

-   **Propósito**: Card de evento reutilizável
-   **Dados**: Nome, poster, descrição
-   **Ação**: Navegação para sessões

#### **TopBar**

-   **Elementos**: Logo, navegação, informações do usuário
-   **Funcionalidades**: Logout, navegação entre telas

---

## Persistência de Dados

### 🗄️ Estrutura do Banco

#### **Tabelas Principais**

```sql
-- Usuários
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    endereco VARCHAR(200),
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,  -- Hash SHA-256 + Salt
    tipo_usuario ENUM('ADMIN', 'COMUM') DEFAULT 'COMUM'
);

-- Eventos
CREATE TABLE eventos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL
);

-- Sessões
CREATE TABLE sessoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    tipo_sessao ENUM('MANHA', 'TARDE', 'NOITE'),
    data_sessao TIMESTAMP,
    evento_id BIGINT,
    FOREIGN KEY (evento_id) REFERENCES eventos(id)
);

-- Áreas
CREATE TABLE areas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    capacidade_total INT NOT NULL
);

-- Relacionamento Sessões-Áreas
CREATE TABLE sessoes_areas (
    sessao_id BIGINT,
    area_id BIGINT,
    PRIMARY KEY (sessao_id, area_id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    FOREIGN KEY (area_id) REFERENCES areas(id)
);

-- Ingressos
CREATE TABLE ingressos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT,
    sessao_id BIGINT,
    area_id BIGINT,
    numero_poltrona INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    FOREIGN KEY (area_id) REFERENCES areas(id)
);
```

### 🔗 Relacionamentos

#### **Cardinalidades**

-   **Evento** → **Sessão**: 1:N (Um evento tem várias sessões)
-   **Sessão** → **Área**: N:N (Uma sessão tem várias áreas, uma área pode estar em várias sessões)
-   **Sessão** → **Ingresso**: 1:N (Uma sessão tem vários ingressos)
-   **Área** → **Ingresso**: 1:N (Uma área tem vários ingressos)
-   **Usuário** → **Ingresso**: 1:N (Um usuário pode ter vários ingressos)

#### **Constraints**

-   **CPF único**: Não pode haver dois usuários com mesmo CPF
-   **Email único**: Não pode haver dois usuários com mesmo email
-   **Poltrona única**: Não pode haver dois ingressos para mesma poltrona na mesma sessão/área
-   **Código único**: Cada ingresso tem código único

### �� Pool de Conexões

#### **DatabaseConnection (Singleton)**

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Queue<Connection> connectionPool;
    private final String url, user, password;

    public Connection getConnection() throws SQLException
    public void releaseConnection(Connection conn)
    public void closeConnection()
}
```

#### **Configuração**

-   **Driver**: `com.mysql.cj.jdbc.Driver`
-   **URL**: `jdbc:mysql://localhost:3306/teatro_db`
-   **Usuário**: `root`
-   **Senha**: `root`
-   **Pool**: Queue com conexões reutilizáveis

---

## Sistema de Logging e Observers

### �� TeatroLogger

#### **Implementação**

```java
public class TeatroLogger {
    private static TeatroLogger instance;
    private final Logger logger;

    public void info(String message)
    public void error(String message)
    public void warn(String message)
    public void debug(String message)
}
```

#### **Configuração (logback.xml)**

```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/teatro.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/teatro.%d{yyyy-MM-dd}.log</fileNamePattern>
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### ��️ Padrão Observer

#### **Implementação**

```java
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object data);
}

public interface Observer {
    void update(Object data);
}
```

#### **Observers Implementados**

1. **VendaLoggerObserver**: Registra vendas de ingressos
2. **NotificacaoSubject**: Gerencia notificações de vendas

#### **Uso no Sistema**

```java
// Quando um ingresso é vendido
notifyObservers(new NotificacaoVenda(ingresso));

// Observer registra a venda
public void update(Object data) {
    if (data instanceof NotificacaoVenda) {
        NotificacaoVenda venda = (NotificacaoVenda) data;
        logger.info("Venda realizada: " + venda.getIngresso().getCodigo());
    }
}
```

---

## Tratamento de Exceções

### �� Hierarquia de Exceções

#### **TeatroException (Base)**

```java
public class TeatroException extends RuntimeException {
    public TeatroException(String message)
    public TeatroException(String message, Throwable cause)
}
```

#### **Exceções Específicas**

1. **UsuarioNaoEncontradoException**: Usuário não encontrado
2. **SessaoNaoEncontradaException**: Sessão não encontrada
3. **AreaException**: Erro relacionado a áreas
4. **IngressoException**: Erro relacionado a ingressos
5. **PoltronaOcupadaException**: Poltrona já está ocupada

#### **PoltronaOcupadaException**

```java
public class PoltronaOcupadaException extends TeatroException {
    public PoltronaOcupadaException(int numeroPoltrona, String areaNome) {
        super(String.format("A poltrona %d da área %s já está ocupada",
              numeroPoltrona, areaNome));
    }
}
```

### 🛡️ Tratamento na Interface

#### **Try-Catch nas Views**

```java
try {
    Optional<Usuario> usuarioOpt = teatro.autenticarUsuario(identificador, senha);
    if (usuarioOpt.isPresent()) {
        // Login bem-sucedido
    } else {
        errorLabel.setText("CPF/Email ou senha incorretos.");
        errorLabel.setVisible(true);
    }
} catch (TeatroException ex) {
    // Exceções de validação
    errorLabel.setText("Dados incorretos: " + ex.getMessage());
    errorLabel.setVisible(true);
} catch (Exception ex) {
    // Exceções inesperadas
    errorLabel.setText("Erro ao fazer login. Tente novamente.");
    errorLabel.setVisible(true);
}
```

#### **Validações Preventivas**

```java
// Antes de tentar comprar
if (!teatro.verificarPoltronaDisponivel(sessao, area, numero)) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Atenção");
    alert.setHeaderText("Poltrona ocupada");
    alert.setContentText("Esta poltrona acabou de ser ocupada. Por favor, selecione outra.");
    alert.showAndWait();
    return;
}
```

---

## Testes Implementados

### �� Testes Unitários

#### **PasswordHasherTest**

```java
@Test
void testHashPassword() {
    String hash = PasswordHasher.hashPassword(senhaTeste);
    assertNotNull(hash);
    assertTrue(hash.contains(":"));
    assertNotEquals(senhaTeste, hash);
}

@Test
void testVerifyPassword() {
    String hash = PasswordHasher.hashPassword(senhaTeste);
    assertTrue(PasswordHasher.verifyPassword(senhaTeste, hash));
    assertFalse(PasswordHasher.verifyPassword("senhaErrada", hash));
}
```

#### **UsuarioFactoryTest**

```java
@Test
void testCriarUsuarioAdmin() {
    Usuario admin = UsuarioAdminFactory.criarUsuario("Admin", "123.456.789-00", "admin@teatro.com");
    assertEquals("ADMIN", admin.getTipoUsuario());
}

@Test
void testCriarUsuarioComum() {
    Usuario comum = UsuarioComumFactory.criarUsuario("João", "123.456.789-00", "joao@email.com");
    assertEquals("COMUM", comum.getTipoUsuario());
}
```

#### **PoltronaStateTest**

```java
@Test
void testDisponivelState() {
    Poltrona poltrona = new Poltrona(1);
    poltrona.setState(new DisponivelState());

    assertEquals("Disponível", poltrona.getNomeEstado());
    poltrona.ocupar();
    assertEquals("Ocupada", poltrona.getNomeEstado());
}
```

#### **EstatisticasServiceTest**

```java
@Test
void testEstruturaEstatisticas() {
    Map<String, Object> estatisticas = estatisticasService.buscarEstatisticas();

    assertTrue(estatisticas.containsKey("pecaMaisVendida"));
    assertTrue(estatisticas.containsKey("sessaoMaiorOcupacao"));
    assertTrue(estatisticas.containsKey("pecaMaisLucrativa"));
}
```

### 📊 Cobertura de Testes

#### **Funcionalidades Testadas**

-   ✅ Hash e verificação de senhas
-   ✅ Factory de usuários
-   ✅ Estados de poltronas
-   ✅ Estrutura de estatísticas
-   ✅ Observers de venda

#### **Funcionalidades que Precisam de Testes**

-   ❌ DAOs (acesso a banco)
-   ❌ Services (lógica de negócio)
-   ❌ Views (interface gráfica)
-   ❌ Validações de entrada

---

## Configurações e Dependências

### 📦 Maven (pom.xml)

#### **Dependências Principais**

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.7</version>
    </dependency>

    <!-- JUnit para testes -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### **Plugins**

```xml
<plugins>
    <!-- JavaFX Maven Plugin -->
    <plugin>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-maven-plugin</artifactId>
        <version>0.0.8</version>
        <configuration>
            <mainClass>com.teatro.Main</mainClass>
        </configuration>
    </plugin>

    <!-- Compiler Plugin -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.11.0</version>
        <configuration>
            <source>17</source>
            <target>17</target>
        </configuration>
    </plugin>
</plugins>
```

### ⚙️ Configurações de Banco

#### **database.properties**

```properties
db.url=jdbc:mysql://localhost:3306/teatro_db
db.user=root
db.password=root
db.driver=com.mysql.cj.jdbc.Driver
```

#### **database.sql**

-   Script de criação das tabelas
-   Inserção de dados iniciais
-   Criação das views de estatísticas

### 🚀 Execução

#### **Comandos Maven**

```bash
# Compilar
mvn clean compile

# Executar
mvn javafx:run

# Testes
mvn test

# Package
mvn clean package
```

#### **Script de Execução (executar.sh)**

```bash
#!/bin/bash
mvn clean compile
mvn javafx:run
```

---

## 🔍 Análise de Pontos Fortes e Melhorias

### ✅ Pontos Fortes

1. **Arquitetura Bem Estruturada**

    - Separação clara de responsabilidades (MVC)
    - Padrões de projeto bem implementados
    - Código modular e reutilizável

2. **Segurança Robusta**

    - Hash de senhas com salt
    - Validações de entrada
    - Migração automática de senhas

3. **Interface Intuitiva**

    - Design moderno e responsivo
    - Feedback visual claro
    - Navegação intuitiva

4. **Gerenciamento de Poltronas**

    - Lógica de ocupação bem definida
    - Verificação em tempo real
    - Padrão State para estados

5. **Estatísticas Avançadas**
    - Views SQL otimizadas
    - Dashboard completo para admin
    - Integração com Metabase

### 🔧 Melhorias Sugeridas

1. **Testes**

    - Aumentar cobertura de testes
    - Testes de integração
    - Testes de interface

2. **Performance**

    - Cache para consultas frequentes
    - Otimização de queries
    - Paginação de resultados

3. **Funcionalidades**

    - Reserva temporária de poltronas
    - Sistema de pagamento real
    - Notificações por email

4. **Segurança**

    - Rate limiting
    - Auditoria de ações
    - Política de senhas forte

5. **Usabilidade**
    - Tema escuro
    - Responsividade mobile
    - Acessibilidade

---

## 📋 Conclusão

O Sistema de Teatro é uma aplicação robusta e bem estruturada que demonstra:

-   **Conhecimento sólido de Java e JavaFX**
-   **Aplicação correta de padrões de projeto**
-   **Preocupação com segurança e validações**
-   **Interface gráfica moderna e intuitiva**
-   **Persistência de dados eficiente**
-   **Sistema de logging e observação**

O projeto está pronto para ser migrado para web, servindo como base sólida para aprender tecnologias como Spring Boot, React/Vue.js e desenvolvimento de APIs REST. Todas as funcionalidades core estão implementadas e podem ser facilmente adaptadas para uma arquitetura web moderna.

```

```
