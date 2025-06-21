package com.teatro.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teatro.model.Sessao;
import com.teatro.model.Sessao.TipoSessao;

/**
 * Repository para operações de persistência da entidade Sessao
 * 
 * Fornece métodos para:
 * - Busca de sessões por data e horário
 * - Lógica de horários dinâmicos
 * - Sessões disponíveis para compra
 */
@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    /**
     * Busca sessões ativas
     */
    List<Sessao> findByAtivoTrue();

    /**
     * Busca sessões por evento
     */
    List<Sessao> findByEventoId(Long eventoId);

    /**
     * Busca sessões ativas por evento
     */
    List<Sessao> findByAtivoTrueAndEventoId(Long eventoId);

    /**
     * Busca sessões por tipo
     */
    List<Sessao> findByTipoSessao(TipoSessao tipoSessao);

    /**
     * Busca sessões por data
     */
    List<Sessao> findByDataSessao(LocalDate dataSessao);

    /**
     * Busca sessões ativas por data
     */
    List<Sessao> findByAtivoTrueAndDataSessao(LocalDate dataSessao);

    /**
     * Busca sessões por evento e data
     */
    List<Sessao> findByEventoIdAndDataSessao(Long eventoId, LocalDate dataSessao);

    /**
     * Busca sessões ativas por evento e data
     */
    List<Sessao> findByAtivoTrueAndEventoIdAndDataSessao(Long eventoId, LocalDate dataSessao);

    /**
     * Busca sessões por evento, data e tipo
     */
    List<Sessao> findByEventoIdAndDataSessaoAndTipoSessao(Long eventoId, LocalDate dataSessao, TipoSessao tipoSessao);

    /**
     * Busca sessões futuras (data >= hoje)
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND s.dataSessao >= CURRENT_DATE " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesFuturas();

    /**
     * Busca sessões futuras por evento
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND s.evento.id = :eventoId " +
           "AND s.dataSessao >= CURRENT_DATE " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesFuturasPorEvento(@Param("eventoId") Long eventoId);

    /**
     * Busca sessões disponíveis para compra (não passadas)
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND (s.dataSessao > CURRENT_DATE " +
           "OR (s.dataSessao = CURRENT_DATE AND s.horario > CURRENT_TIME)) " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesDisponiveisParaCompra();

    /**
     * Busca sessões disponíveis para compra por evento
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND s.evento.id = :eventoId " +
           "AND (s.dataSessao > CURRENT_DATE " +
           "OR (s.dataSessao = CURRENT_DATE AND s.horario > CURRENT_TIME)) " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesDisponiveisParaCompraPorEvento(@Param("eventoId") Long eventoId);

    /**
     * Busca sessões por período de data
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND s.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesPorPeriodo(@Param("dataInicio") LocalDate dataInicio, 
                                      @Param("dataFim") LocalDate dataFim);

    /**
     * Busca sessões por evento e período
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND s.evento.id = :eventoId " +
           "AND s.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY s.dataSessao, s.horario")
    List<Sessao> findSessoesPorEventoEPeriodo(@Param("eventoId") Long eventoId,
                                             @Param("dataInicio") LocalDate dataInicio, 
                                             @Param("dataFim") LocalDate dataFim);

    /**
     * Verifica se existe sessão com mesma data, horário e evento
     */
    boolean existsByEventoIdAndDataSessaoAndHorario(Long eventoId, LocalDate dataSessao, LocalTime horario);

    /**
     * Busca sessões com maior ocupação
     */
    @Query("SELECT s, COUNT(i) as totalIngressos " +
           "FROM Sessao s " +
           "LEFT JOIN s.ingressos i " +
           "WHERE s.ativo = true " +
           "GROUP BY s " +
           "ORDER BY totalIngressos DESC")
    List<Object[]> findSessoesComMaiorOcupacao();

    /**
     * Busca sessões passadas
     */
    @Query("SELECT s FROM Sessao s " +
           "WHERE s.ativo = true " +
           "AND (s.dataSessao < CURRENT_DATE " +
           "OR (s.dataSessao = CURRENT_DATE AND s.horario < CURRENT_TIME)) " +
           "ORDER BY s.dataSessao DESC, s.horario DESC")
    List<Sessao> findSessoesPassadas();
} 