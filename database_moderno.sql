-- ========================================
-- SISTEMA DE TEATRO WEB - SCRIPT COMPLETO
-- ========================================
-- Versão: 2.0
-- Data: 2024
-- Descrição: Script completo para recriação do banco de dados
-- Inclui: Estrutura básica + funcionalidades avançadas + dados iniciais

-- 1. Criação do banco
DROP DATABASE IF EXISTS teatro_db;
CREATE DATABASE teatro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE teatro_db;

-- ========================================
-- TABELAS CORE (FUNCIONALIDADES BÁSICAS)
-- ========================================

-- 2. Usuários
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(200),
    telefone VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('ADMIN', 'COMUM') DEFAULT 'COMUM',
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Eventos
CREATE TABLE eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    poster VARCHAR(255),
    duracao_minutos INT DEFAULT 120,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Sessões (com horários fixos)
CREATE TABLE sessoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    tipo_sessao ENUM('MANHA', 'TARDE', 'NOITE') NOT NULL,
    data_sessao DATE NOT NULL,
    horario TIME NOT NULL,
    evento_id BIGINT NOT NULL,
    ativa BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE
);

-- 5. Áreas do Teatro
CREATE TABLE areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    capacidade_total INT NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 6. Relacionamento Sessões-Áreas
CREATE TABLE sessoes_areas (
    sessao_id BIGINT,
    area_id BIGINT,
    PRIMARY KEY (sessao_id, area_id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE
);

-- 7. Ingressos (com lógica de ocupação)
CREATE TABLE ingressos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    sessao_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    numero_poltrona INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('RESERVADO', 'PAGO', 'CANCELADO', 'UTILIZADO') DEFAULT 'RESERVADO',
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE
);

-- ========================================
-- FUNCIONALIDADES AVANÇADAS
-- ========================================

-- 8. Sistema de Pagamentos
CREATE TABLE pagamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ingresso_id BIGINT NOT NULL,
    metodo_pagamento ENUM('CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'BOLETO') NOT NULL,
    status ENUM('PENDENTE', 'APROVADO', 'CANCELADO', 'REEMBOLSADO') DEFAULT 'PENDENTE',
    valor DECIMAL(10,2) NOT NULL,
    codigo_transacao VARCHAR(100) UNIQUE,
    dados_pagamento JSON,
    data_pagamento TIMESTAMP NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ingresso_id) REFERENCES ingressos(id) ON DELETE CASCADE
);

-- 9. Sistema de Reservas Temporárias
CREATE TABLE reservas (
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
CREATE TABLE pontos_fidelidade (
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
CREATE TABLE notificacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo ENUM('EMAIL', 'SMS', 'PUSH', 'SISTEMA') NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    dados_extras JSON,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 12. Sistema de Auditoria/Logs
CREATE TABLE logs_auditoria (
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
CREATE TABLE cupons (
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
CREATE TABLE cupons_utilizados (
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
CREATE TABLE avaliacoes (
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

-- Índices básicos para consultas frequentes
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_cpf ON usuarios(cpf);
CREATE INDEX idx_usuarios_tipo ON usuarios(tipo_usuario);

CREATE INDEX idx_sessoes_evento_data ON sessoes(evento_id, data_sessao);
CREATE INDEX idx_sessoes_data_horario ON sessoes(data_sessao, horario);
CREATE INDEX idx_sessoes_tipo ON sessoes(tipo_sessao);

CREATE INDEX idx_ingressos_sessao_area ON ingressos(sessao_id, area_id, numero_poltrona);
CREATE INDEX idx_ingressos_usuario ON ingressos(usuario_id);
CREATE INDEX idx_ingressos_codigo ON ingressos(codigo);
CREATE INDEX idx_ingressos_status ON ingressos(status);

CREATE INDEX idx_pagamentos_ingresso ON pagamentos(ingresso_id);
CREATE INDEX idx_pagamentos_status ON pagamentos(status);
CREATE INDEX idx_pagamentos_transacao ON pagamentos(codigo_transacao);

CREATE INDEX idx_reservas_expira_em ON reservas(expira_em);
CREATE INDEX idx_reservas_sessao_area ON reservas(sessao_id, area_id, numero_poltrona);
CREATE INDEX idx_reservas_status ON reservas(status);

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

-- Inserir usuário admin padrão (senha: admin123)
INSERT INTO usuarios (nome, cpf, email, senha, tipo_usuario) VALUES
('Administrador', '000.000.000-00', 'admin@teatro.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN');

-- Inserir eventos padrão
INSERT INTO eventos (nome, descricao, duracao_minutos) VALUES
('Hamlet', 'A tragédia do príncipe dinamarquês que busca vingança pela morte do pai.', 180),
('O Fantasma da Ópera', 'Uma história de amor e obsessão nos bastidores da Ópera de Paris.', 150),
('O Auto da Compadecida', 'A comédia popular de Ariano Suassuna sobre João Grilo e Chicó.', 120);

-- Inserir áreas do teatro com preços e capacidades
INSERT INTO areas (nome, preco, capacidade_total, descricao) VALUES
('Plateia A', 40.00, 25, 'Primeiras fileiras da plateia, visão privilegiada'),
('Plateia B', 60.00, 100, 'Plateia central, excelente acústica'),
('Camarote 1', 80.00, 10, 'Camarote privativo com vista lateral'),
('Camarote 2', 80.00, 10, 'Camarote privativo com vista lateral'),
('Camarote 3', 80.00, 10, 'Camarote privativo com vista lateral'),
('Camarote 4', 80.00, 10, 'Camarote privativo com vista lateral'),
('Camarote 5', 80.00, 10, 'Camarote privativo com vista lateral'),
('Frisa 1', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Frisa 2', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Frisa 3', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Frisa 4', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Frisa 5', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Frisa 6', 120.00, 5, 'Frisa superior com vista panorâmica'),
('Balcão Nobre', 250.00, 50, 'Área VIP com serviço de concierge');

-- ========================================
-- VIEWS PARA ESTATÍSTICAS
-- ========================================

-- View: Estatísticas de vendas por peça
CREATE OR REPLACE VIEW estatisticas_vendas_peca AS
SELECT
    e.id AS evento_id,
    e.nome AS nome_peca,
    COUNT(i.id) AS total_ingressos_vendidos,
    SUM(i.valor) AS faturamento_total,
    AVG(i.valor) AS valor_medio_ingresso,
    COUNT(CASE WHEN i.status = 'PAGO' THEN 1 END) AS ingressos_pagos,
    COUNT(CASE WHEN i.status = 'RESERVADO' THEN 1 END) AS ingressos_reservados
FROM eventos e
    LEFT JOIN sessoes s ON s.evento_id = e.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id
WHERE e.ativo = TRUE
GROUP BY e.id, e.nome
ORDER BY total_ingressos_vendidos DESC;

-- View: Estatísticas de ocupação por sessão
CREATE OR REPLACE VIEW estatisticas_ocupacao_sessao AS
WITH total_poltronas AS (
    SELECT 
        s.id AS sessao_id, 
        SUM(a.capacidade_total) AS total_poltronas
    FROM sessoes s
    JOIN sessoes_areas sa ON sa.sessao_id = s.id
    JOIN areas a ON a.id = sa.area_id
    WHERE s.ativa = TRUE AND a.ativo = TRUE
    GROUP BY s.id
)
SELECT
    e.nome AS nome_peca,
    s.data_sessao AS data_sessao,
    s.horario AS horario,
    s.tipo_sessao,
    COUNT(i.id) AS ingressos_vendidos,
    tp.total_poltronas,
    ROUND((COUNT(i.id) / tp.total_poltronas) * 100, 2) AS percentual_ocupacao
FROM eventos e
    JOIN sessoes s ON s.evento_id = e.id
    JOIN total_poltronas tp ON tp.sessao_id = s.id
    LEFT JOIN ingressos i ON i.sessao_id = s.id AND i.status IN ('PAGO', 'RESERVADO')
WHERE s.ativa = TRUE AND e.ativo = TRUE
GROUP BY e.nome, s.id, s.data_sessao, s.horario, s.tipo_sessao, tp.total_poltronas
ORDER BY percentual_ocupacao DESC;

-- View: Estatísticas de pagamentos
CREATE OR REPLACE VIEW estatisticas_pagamentos AS
SELECT
    metodo_pagamento,
    status,
    COUNT(*) AS total_transacoes,
    SUM(valor) AS valor_total,
    AVG(valor) AS valor_medio
FROM pagamentos
GROUP BY metodo_pagamento, status
ORDER BY metodo_pagamento, status;

-- View: Estatísticas de fidelidade
CREATE OR REPLACE VIEW estatisticas_fidelidade AS
SELECT
    u.nome AS nome_usuario,
    u.email,
    SUM(CASE WHEN pf.tipo_operacao = 'GANHO' THEN pf.pontos ELSE 0 END) AS pontos_ganhos,
    SUM(CASE WHEN pf.tipo_operacao = 'RESGATE' THEN pf.pontos ELSE 0 END) AS pontos_resgatados,
    SUM(CASE WHEN pf.tipo_operacao = 'GANHO' THEN pf.pontos ELSE -pf.pontos END) AS saldo_atual
FROM usuarios u
    LEFT JOIN pontos_fidelidade pf ON pf.usuario_id = u.id
WHERE u.tipo_usuario = 'COMUM'
GROUP BY u.id, u.nome, u.email
ORDER BY saldo_atual DESC;

-- ========================================
-- PROCEDURES ÚTEIS
-- ========================================

-- Procedure: Limpar reservas expiradas
DELIMITER //
CREATE PROCEDURE limpar_reservas_expiradas()
BEGIN
    UPDATE reservas 
    SET status = 'EXPIRADA' 
    WHERE expira_em < NOW() AND status = 'ATIVA';
    
    SELECT ROW_COUNT() AS reservas_expiradas;
END //
DELIMITER ;

-- Procedure: Gerar sessões para um evento
DELIMITER //
CREATE PROCEDURE gerar_sessoes_evento(
    IN p_evento_id BIGINT,
    IN p_data_inicio DATE,
    IN p_data_fim DATE
)
BEGIN
    DECLARE v_data DATE;
    DECLARE v_horario TIME;
    DECLARE v_tipo_sessao ENUM('MANHA', 'TARDE', 'NOITE');
    DECLARE v_nome VARCHAR(100);
    
    SET v_data = p_data_inicio;
    
    WHILE v_data <= p_data_fim DO
        -- Manhã: 08:00, 09:30, 11:00
        SET v_horario = '08:00:00';
        SET v_tipo_sessao = 'MANHA';
        SET v_nome = CONCAT('Sessão Manhã - ', DATE_FORMAT(v_data, '%d/%m/%Y'));
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '09:30:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '11:00:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        -- Tarde: 13:00, 14:30, 16:00
        SET v_horario = '13:00:00';
        SET v_tipo_sessao = 'TARDE';
        SET v_nome = CONCAT('Sessão Tarde - ', DATE_FORMAT(v_data, '%d/%m/%Y'));
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '14:30:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '16:00:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        -- Noite: 18:00, 19:30, 21:00
        SET v_horario = '18:00:00';
        SET v_tipo_sessao = 'NOITE';
        SET v_nome = CONCAT('Sessão Noite - ', DATE_FORMAT(v_data, '%d/%m/%Y'));
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '19:30:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_horario = '21:00:00';
        INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, evento_id) 
        VALUES (v_nome, v_tipo_sessao, v_data, v_horario, p_evento_id);
        
        SET v_data = DATE_ADD(v_data, INTERVAL 1 DAY);
    END WHILE;
    
    -- Associar todas as áreas a todas as sessões criadas
    INSERT INTO sessoes_areas (sessao_id, area_id)
    SELECT s.id, a.id
    FROM sessoes s
    CROSS JOIN areas a
    WHERE s.evento_id = p_evento_id 
    AND s.data_sessao BETWEEN p_data_inicio AND p_data_fim
    AND a.ativo = TRUE;
    
    SELECT COUNT(*) AS sessoes_criadas FROM sessoes 
    WHERE evento_id = p_evento_id 
    AND data_sessao BETWEEN p_data_inicio AND p_data_fim;
END //
DELIMITER ;

-- ========================================
-- TRIGGERS PARA INTEGRIDADE
-- ========================================

-- Trigger: Atualizar data_atualizacao automaticamente
DELIMITER //
CREATE TRIGGER usuarios_update_trigger
BEFORE UPDATE ON usuarios
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//

CREATE TRIGGER eventos_update_trigger
BEFORE UPDATE ON eventos
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//

CREATE TRIGGER sessoes_update_trigger
BEFORE UPDATE ON sessoes
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//

CREATE TRIGGER areas_update_trigger
BEFORE UPDATE ON areas
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//

CREATE TRIGGER ingressos_update_trigger
BEFORE UPDATE ON ingressos
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//

CREATE TRIGGER pagamentos_update_trigger
BEFORE UPDATE ON pagamentos
FOR EACH ROW
SET NEW.data_atualizacao = CURRENT_TIMESTAMP;
//
DELIMITER ;

-- ========================================
-- MENSAGEM FINAL
-- ========================================

SELECT 'Banco de dados teatro_db criado com sucesso!' AS mensagem;
SELECT 'Usuário admin criado: admin@teatro.com / admin123' AS credenciais;
SELECT 'Execute: CALL gerar_sessoes_evento(1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY));' AS proximo_passo; 