-- Script SQL Completo para Sistema de Teatro Web
-- Inclui estrutura básica + funcionalidades avançadas
-- O sistema criará sessões dinamicamente quando necessário

-- 1. Criação do banco
CREATE DATABASE IF NOT EXISTS teatro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE teatro_db;

-- ========================================
-- TABELAS BÁSICAS (CORE)
-- ========================================

-- 2. Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(200),
    telefone VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('ADMIN', 'COMUM') DEFAULT 'COMUM',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabela de Eventos
CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    poster VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabela de Sessões
CREATE TABLE IF NOT EXISTS sessoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    tipo_sessao ENUM('MANHA', 'TARDE', 'NOITE') NOT NULL,
    data_sessao DATE NOT NULL,
    horario TIME NOT NULL,
    evento_id BIGINT NOT NULL,
    ativa BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE
);

-- 5. Tabela de Áreas
CREATE TABLE IF NOT EXISTS areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    capacidade_total INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Tabela de Relacionamento Sessões-Áreas
CREATE TABLE IF NOT EXISTS sessoes_areas (
    sessao_id BIGINT,
    area_id BIGINT,
    PRIMARY KEY (sessao_id, area_id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE
);

-- 7. Tabela de Ingressos
CREATE TABLE IF NOT EXISTS ingressos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    sessao_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    numero_poltrona INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('RESERVADO', 'PAGO', 'CANCELADO') DEFAULT 'RESERVADO',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE
);

-- ========================================
-- FUNCIONALIDADES AVANÇADAS
-- ========================================

-- 8. Sistema de Pagamentos
CREATE TABLE IF NOT EXISTS pagamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ingresso_id BIGINT NOT NULL,
    metodo_pagamento ENUM('CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'BOLETO') NOT NULL,
    status ENUM('PENDENTE', 'APROVADO', 'CANCELADO', 'REEMBOLSADO') DEFAULT 'PENDENTE',
    valor DECIMAL(10,2) NOT NULL,
    codigo_transacao VARCHAR(100) UNIQUE,
    data_pagamento TIMESTAMP NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ingresso_id) REFERENCES ingressos(id) ON DELETE CASCADE
);

-- 9. Sistema de Reservas Temporárias
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sessao_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    numero_poltrona INT NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expira_em TIMESTAMP NOT NULL,
    status ENUM('ATIVA', 'EXPIRADA', 'CONVERTIDA', 'CANCELADA') DEFAULT 'ATIVA',
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 10. Sistema de Fidelidade
CREATE TABLE IF NOT EXISTS pontos_fidelidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    pontos INT NOT NULL DEFAULT 0,
    tipo_operacao ENUM('GANHO', 'RESGATE', 'EXPIRACAO') NOT NULL,
    origem VARCHAR(100),
    descricao TEXT,
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 11. Sistema de Notificações
CREATE TABLE IF NOT EXISTS notificacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo ENUM('EMAIL', 'SMS', 'PUSH', 'SISTEMA') NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 12. Sistema de Auditoria/Logs
CREATE TABLE IF NOT EXISTS logs_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NULL,
    acao VARCHAR(100) NOT NULL,
    entidade VARCHAR(50) NOT NULL,
    entidade_id BIGINT NULL,
    dados_anteriores JSON NULL,
    dados_novos JSON NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- 13. Sistema de Cupons/Descontos
CREATE TABLE IF NOT EXISTS cupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    tipo ENUM('PERCENTUAL', 'VALOR_FIXO') NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    valor_minimo DECIMAL(10,2) DEFAULT 0,
    maximo_usos INT DEFAULT 1,
    usos_atuais INT DEFAULT 0,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 14. Histórico de Uso de Cupons
CREATE TABLE IF NOT EXISTS cupons_utilizados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cupom_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    ingresso_id BIGINT NOT NULL,
    valor_desconto DECIMAL(10,2) NOT NULL,
    data_uso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cupom_id) REFERENCES cupons(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (ingresso_id) REFERENCES ingressos(id) ON DELETE CASCADE
);

-- 15. Sistema de Avaliações
CREATE TABLE IF NOT EXISTS avaliacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    sessao_id BIGINT NOT NULL,
    nota INT NOT NULL CHECK (nota >= 1 AND nota <= 5),
    comentario TEXT,
    data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE
);

-- ========================================
-- ÍNDICES PARA PERFORMANCE
-- ========================================

-- Índices básicos
CREATE INDEX idx_usuarios_cpf ON usuarios(cpf);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_sessoes_evento_data ON sessoes(evento_id, data_sessao);
CREATE INDEX idx_sessoes_data_horario ON sessoes(data_sessao, horario);
CREATE INDEX idx_ingressos_sessao_area ON ingressos(sessao_id, area_id, numero_poltrona);
CREATE INDEX idx_ingressos_usuario ON ingressos(usuario_id);
CREATE INDEX idx_ingressos_codigo ON ingressos(codigo);

-- Índices para funcionalidades avançadas
CREATE INDEX idx_pagamentos_ingresso ON pagamentos(ingresso_id);
CREATE INDEX idx_pagamentos_status ON pagamentos(status);
CREATE INDEX idx_reservas_expira_em ON reservas(expira_em);
CREATE INDEX idx_reservas_sessao_area ON reservas(sessao_id, area_id, numero_poltrona);
CREATE INDEX idx_pontos_usuario ON pontos_fidelidade(usuario_id);
CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id, lida);
CREATE INDEX idx_logs_usuario ON logs_auditoria(usuario_id);
CREATE INDEX idx_logs_acao ON logs_auditoria(acao, data_acao);
CREATE INDEX idx_cupons_codigo ON cupons(codigo);
CREATE INDEX idx_cupons_data ON cupons(data_inicio, data_fim);
CREATE INDEX idx_avaliacoes_sessao ON avaliacoes(sessao_id);

-- ========================================
-- DADOS INICIAIS
-- ========================================

-- Usuário Admin (senha: admin123 - hash BCrypt)
INSERT INTO usuarios (nome, cpf, email, senha, tipo_usuario, endereco, telefone) VALUES
('Administrador', '123.456.789-00', 'admin@teatro.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', 'Rua do Teatro, 123', '(11) 99999-9999');

-- Eventos
INSERT INTO eventos (nome, descricao, poster) VALUES
('Hamlet', 'A tragédia do príncipe dinamarquês que busca vingança pela morte do pai', 'hamlet-poster.jpg'),
('O Fantasma da Opera', 'O misterioso fantasma que habita os subterrâneos da Ópera de Paris', 'fantasma-opera-poster.jpg'),
('O Auto da Compadecida', 'A comédia popular de Ariano Suassuna sobre João Grilo e Chicó', 'compadecida-poster.jpg');

-- Áreas do Teatro
INSERT INTO areas (nome, preco, capacidade_total) VALUES
('Plateia A', 40.00, 25),
('Plateia B', 60.00, 100),
('Camarote 1', 80.00, 10),
('Camarote 2', 80.00, 10),
('Camarote 3', 80.00, 10),
('Camarote 4', 80.00, 10),
('Camarote 5', 80.00, 10),
('Frisa 1', 120.00, 5),
('Frisa 2', 120.00, 5),
('Frisa 3', 120.00, 5),
('Frisa 4', 120.00, 5),
('Frisa 5', 120.00, 5),
('Frisa 6', 120.00, 5),
('Balcão Nobre', 250.00, 50);

-- Cupons de exemplo
INSERT INTO cupons (codigo, tipo, valor, valor_minimo, maximo_usos, data_inicio, data_fim) VALUES
('PRIMEIRA10', 'PERCENTUAL', 10.00, 50.00, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY)),
('FIDELIDADE20', 'PERCENTUAL', 20.00, 100.00, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 60 DAY)),
('DESCONTO50', 'VALOR_FIXO', 50.00, 200.00, 10, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 15 DAY));

-- ========================================
-- VIEWS PARA ESTATÍSTICAS
-- ========================================

-- View: Estatísticas de Vendas por Peça
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

-- View: Estatísticas de Ocupação por Sessão
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

-- View: Estatísticas de Pagamentos
CREATE OR REPLACE VIEW estatisticas_pagamentos AS
SELECT
    metodo_pagamento,
    status,
    COUNT(*) AS total_transacoes,
    SUM(valor) AS valor_total,
    AVG(valor) AS valor_medio
FROM pagamentos
GROUP BY metodo_pagamento, status
ORDER BY valor_total DESC;

-- View: Estatísticas de Fidelidade
CREATE OR REPLACE VIEW estatisticas_fidelidade AS
SELECT
    u.nome AS nome_usuario,
    u.email,
    SUM(CASE WHEN pf.tipo_operacao = 'GANHO' THEN pf.pontos ELSE 0 END) AS pontos_ganhos,
    SUM(CASE WHEN pf.tipo_operacao = 'RESGATE' THEN pf.pontos ELSE 0 END) AS pontos_resgatados,
    SUM(CASE WHEN pf.tipo_operacao = 'GANHO' THEN pf.pontos ELSE -pf.pontos END) AS saldo_atual
FROM usuarios u
    LEFT JOIN pontos_fidelidade pf ON pf.usuario_id = u.id
GROUP BY u.id, u.nome, u.email
ORDER BY saldo_atual DESC;

-- ========================================
-- TRIGGERS PARA AUTOMAÇÃO
-- ========================================

-- Trigger: Atualizar pontos de fidelidade quando ingresso é pago
DELIMITER //
CREATE TRIGGER tr_ingresso_pago_fidelidade
AFTER UPDATE ON ingressos
FOR EACH ROW
BEGIN
    IF NEW.status = 'PAGO' AND OLD.status != 'PAGO' THEN
        -- Adiciona pontos baseado no valor do ingresso (1 ponto por R$ 10)
        INSERT INTO pontos_fidelidade (usuario_id, pontos, tipo_operacao, origem, descricao)
        VALUES (NEW.usuario_id, FLOOR(NEW.valor / 10), 'GANHO', 'COMPRA_INGRESSO', 
                CONCAT('Compra ingresso ', NEW.codigo, ' - R$ ', NEW.valor));
    END IF;
END //
DELIMITER ;

-- Trigger: Log de auditoria para mudanças em ingressos
DELIMITER //
CREATE TRIGGER tr_ingressos_auditoria
AFTER UPDATE ON ingressos
FOR EACH ROW
BEGIN
    IF NEW.status != OLD.status THEN
        INSERT INTO logs_auditoria (usuario_id, acao, entidade, entidade_id, dados_anteriores, dados_novos)
        VALUES (NEW.usuario_id, 'ALTERACAO_STATUS', 'INGRESSO', NEW.id,
                JSON_OBJECT('status_anterior', OLD.status),
                JSON_OBJECT('status_novo', NEW.status));
    END IF;
END //
DELIMITER ;

-- ========================================
-- COMENTÁRIOS SOBRE O SISTEMA COMPLETO
-- ========================================

/*
SISTEMA COMPLETO DE TEATRO WEB:

1. FUNCIONALIDADES CORE (Já implementadas):
   - Autenticação e usuários
   - Gerenciamento de eventos
   - Sistema de sessões dinâmico
   - Áreas e poltronas
   - Compra de ingressos

2. FUNCIONALIDADES AVANÇADAS (Estrutura pronta):
   - Sistema de pagamentos (cartão, PIX, boleto)
   - Reservas temporárias com timeout
   - Sistema de pontos/fidelidade
   - Notificações por email/SMS
   - Auditoria completa de ações
   - Cupons e descontos
   - Sistema de avaliações

3. HORÁRIOS FIXOS POR DIA:
   - MANHÃ: 08:00, 09:30, 11:00
   - TARDE: 13:00, 14:30, 16:00
   - NOITE: 18:00, 19:30, 21:00

4. VANTAGENS:
   - Estrutura 100% completa
   - Pronto para implementar todas as funcionalidades
   - Performance otimizada com índices
   - Automação com triggers
   - Estatísticas avançadas

5. COMO USAR:
   - Execute este script para criar estrutura completa
   - O sistema criará sessões dinamicamente
   - Implemente funcionalidades avançadas conforme necessário
   - Todas as tabelas estão prontas para uso
*/

-- ========================================
-- MENSAGEM DE SUCESSO
-- ========================================

SELECT '✅ Banco de dados COMPLETO criado com sucesso!' AS status;
SELECT '🎭 Eventos: Hamlet, O Fantasma da Opera, O Auto da Compadecida' AS eventos;
SELECT '🎫 Áreas: 14 áreas configuradas com diferentes preços' AS areas;
SELECT '👤 Admin: admin@teatro.com / admin123' AS admin;
SELECT '💳 Pagamentos: Cartão, PIX, Boleto' AS pagamentos;
SELECT '🎁 Cupons: 3 cupons de exemplo criados' AS cupons;
SELECT '📊 Estatísticas: 4 views para relatórios' AS estatisticas;
SELECT '🔧 Automação: Triggers para fidelidade e auditoria' AS automacao;
SELECT '🚀 Sistema 100% pronto para todas as funcionalidades!' AS sistema; 