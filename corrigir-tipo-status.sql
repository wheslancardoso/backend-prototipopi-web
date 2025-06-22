-- Script para corrigir o tipo do campo status
-- O campo já existe, mas precisa ser alterado para ENUM

USE teatro_db;

-- Verificar a estrutura atual
DESCRIBE ingressos;

-- Alterar o tipo do campo status para ENUM
ALTER TABLE ingressos 
MODIFY COLUMN status ENUM('VALIDO', 'UTILIZADO', 'CANCELADO', 'REEMBOLSADO') 
NOT NULL DEFAULT 'VALIDO';

-- Verificar se os campos de auditoria existem, se não, adicionar
ALTER TABLE ingressos 
ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS observacoes VARCHAR(500) NULL;

-- Verificar a estrutura final
DESCRIBE ingressos;

-- Mostrar alguns registros para verificar
SELECT id, codigo, status, data_compra FROM ingressos LIMIT 5; 