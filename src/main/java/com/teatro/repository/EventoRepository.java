package com.teatro.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teatro.model.Evento;

/**
 * Repository para operações de persistência da entidade Evento
 * 
 * Fornece métodos para:
 * - Busca de eventos ativos
 * - Busca por nome
 * - Estatísticas de eventos
 */
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Busca eventos ativos
     */
    List<Evento> findByAtivoTrue();

    /**
     * Busca evento por nome
     */
    Optional<Evento> findByNome(String nome);

    /**
     * Busca eventos por nome (contendo)
     */
    List<Evento> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca eventos ativos por nome (contendo)
     */
    List<Evento> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

    /**
     * Conta eventos ativos
     */
    long countByAtivoTrue();

    /**
     * Busca eventos que possuem sessões futuras
     */
    @Query("SELECT DISTINCT e FROM Evento e " +
           "JOIN e.sessoes s " +
           "WHERE e.ativo = true " +
           "AND s.ativo = true " +
           "AND s.dataSessao >= CURRENT_DATE " +
           "ORDER BY e.nome")
    List<Evento> findEventosComSessoesFuturas();

    /**
     * Busca eventos por período de data
     */
    @Query("SELECT DISTINCT e FROM Evento e " +
           "JOIN e.sessoes s " +
           "WHERE e.ativo = true " +
           "AND s.ativo = true " +
           "AND s.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY e.nome")
    List<Evento> findEventosPorPeriodo(@Param("dataInicio") String dataInicio, 
                                      @Param("dataFim") String dataFim);

    /**
     * Busca eventos mais populares (com mais ingressos vendidos)
     */
    @Query("SELECT e, COUNT(i) as totalIngressos " +
           "FROM Evento e " +
           "LEFT JOIN e.sessoes s " +
           "LEFT JOIN s.ingressos i " +
           "WHERE e.ativo = true " +
           "GROUP BY e " +
           "ORDER BY totalIngressos DESC")
    List<Object[]> findEventosMaisPopulares();

    /**
     * Verifica se existe evento com nome
     */
    boolean existsByNome(String nome);

    /**
     * Busca eventos que não possuem sessões
     */
    @Query("SELECT e FROM Evento e " +
           "WHERE e.ativo = true " +
           "AND e.sessoes IS EMPTY")
    List<Evento> findEventosSemSessoes();
} 