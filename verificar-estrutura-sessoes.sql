-- Verificar estrutura da tabela sessoes
DESCRIBE sessoes;

-- Verificar dados atuais
SELECT 'DADOS ATUAIS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id FROM sessoes;

-- Verificar constraints
SHOW CREATE TABLE sessoes; 