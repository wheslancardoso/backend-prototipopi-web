-- Corrigir sessões existentes

-- 1. Verificar sessões atuais
SELECT 'SESSOES ATUAIS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo FROM sessoes;

-- 2. Atualizar horários corretos
UPDATE sessoes SET 
    horario = '08:00:00',
    ativo = true
WHERE id = 1 AND nome = 'Hamlet - Manhã';

UPDATE sessoes SET 
    horario = '14:30:00',
    ativo = true
WHERE id = 2 AND nome = 'Hamlet - Tarde';

UPDATE sessoes SET 
    horario = '19:30:00',
    ativo = true
WHERE id = 3 AND nome = 'Hamlet - Noite';

-- 3. Verificar sessões após correção
SELECT 'SESSOES CORRIGIDAS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo FROM sessoes;

-- 4. Verificar se as sessões estão ativas
SELECT 'SESSOES ATIVAS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo 
FROM sessoes 
WHERE ativo = true; 