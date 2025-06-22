-- Script para adicionar o campo status à tabela ingressos
-- Execute este script para corrigir o erro de "Data truncated for column 'status'"

USE teatro_db;

-- Adicionar o campo status à tabela ingressos
ALTER TABLE ingressos 
ADD COLUMN status ENUM('VALIDO', 'UTILIZADO', 'CANCELADO', 'REEMBOLSADO') 
NOT NULL DEFAULT 'VALIDO' 
AFTER codigo;

-- Adicionar campos de auditoria se não existirem
ALTER TABLE ingressos 
ADD COLUMN data_atualizacao TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN observacoes VARCHAR(500) NULL;

-- Verificar a estrutura atualizada
DESCRIBE ingressos;

-- Mostrar alguns registros para verificar
SELECT id, codigo, status, data_compra FROM ingressos LIMIT 5; 