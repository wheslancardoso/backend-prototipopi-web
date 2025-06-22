-- Verificar e inserir dados necessários para teste de compra de ingresso

-- 1. Verificar eventos
SELECT 'EVENTOS:' as tipo, id, nome FROM eventos;

-- 2. Verificar áreas
SELECT 'AREAS:' as tipo, id, nome, preco, capacidade_total FROM areas;

-- 3. Verificar sessões
SELECT 'SESSOES:' as tipo, id, nome, tipo_sessao, data_sessao, horario, evento_id FROM sessoes;

-- 4. Verificar usuários
SELECT 'USUARIOS:' as tipo, id, nome, email, tipo_usuario FROM usuarios;

-- 5. Verificar ingressos existentes
SELECT 'INGRESSOS:' as tipo, id, usuario_id, sessao_id, area_id, numero_poltrona, valor FROM ingressos;

-- Inserir dados se não existirem
INSERT IGNORE INTO eventos (id, nome) VALUES 
(1, 'Hamlet'),
(2, 'O Fantasma da Opera'),
(3, 'O Auto da Compadecida');

INSERT IGNORE INTO areas (id, nome, preco, capacidade_total) VALUES 
(1, 'Plateia A', 40.00, 25),
(2, 'Plateia B', 60.00, 100),
(3, 'Camarotes', 80.00, 10),
(4, 'Frisas', 120.00, 5),
(5, 'Balcão Nobre', 250.00, 50);

-- Inserir sessões para Hamlet no dia 22/06/2025
-- Corrigido: campos separados para data_sessao (DATE) e horario (TIME), e campo ativo
INSERT IGNORE INTO sessoes (id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id) VALUES 
(1, 'Hamlet - Manhã', 'MANHA', '2025-06-22', '08:00:00', true, 1),
(2, 'Hamlet - Tarde', 'TARDE', '2025-06-22', '14:30:00', true, 1),
(3, 'Hamlet - Noite', 'NOITE', '2025-06-22', '19:30:00', true, 1);

-- Associar áreas às sessões
INSERT IGNORE INTO sessoes_areas (sessao_id, area_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),  -- Todas as áreas para sessão 1
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),  -- Todas as áreas para sessão 2
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5);  -- Todas as áreas para sessão 3

-- Verificar dados após inserção
SELECT 'DADOS FINAIS:' as info;
SELECT 'EVENTOS:' as tipo, id, nome FROM eventos;
SELECT 'AREAS:' as tipo, id, nome, preco FROM areas;
SELECT 'SESSOES:' as tipo, id, nome, tipo_sessao, data_sessao, horario, ativo FROM sessoes;
SELECT 'SESSOES_AREAS:' as tipo, sessao_id, area_id FROM sessoes_areas; 