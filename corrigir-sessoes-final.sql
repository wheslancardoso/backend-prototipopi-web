-- Script final para corrigir sessões
-- Primeiro remove as sessões existentes e depois insere as corretas

-- 1. Verificar sessões atuais
SELECT 'SESSOES ATUAIS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id FROM sessoes;

-- 2. Remover sessões existentes do Hamlet
DELETE FROM sessoes WHERE evento_id = 1;

-- 3. Inserir sessões corretas
INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, ativo, evento_id) VALUES 
('Hamlet - Manhã', 'MANHA', '2025-06-22', '08:00:00', true, 1),
('Hamlet - Tarde', 'TARDE', '2025-06-22', '14:30:00', true, 1),
('Hamlet - Noite', 'NOITE', '2025-06-22', '19:30:00', true, 1);

-- 4. Verificar sessões após correção
SELECT 'SESSOES CORRIGIDAS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id FROM sessoes WHERE evento_id = 1;

-- 5. Verificar se as sessões estão ativas
SELECT 'SESSOES ATIVAS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id 
FROM sessoes 
WHERE evento_id = 1 AND ativo = true; 