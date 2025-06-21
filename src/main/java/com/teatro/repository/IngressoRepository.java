package com.teatro.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teatro.model.Ingresso;
import com.teatro.model.Ingresso.StatusIngresso;

/**
 * Repository para operações de persistência da entidade Ingresso
 * 
 * Implementa a lógica de ocupação de poltronas com chave única:
 * - evento_id + data + horário + area_id + poltrona_id
 * 
 * Uma vez que uma poltrona é reservada para uma combinação específica,
 * ela fica ocupada para todos os usuários até que qualquer parâmetro mude.
 */
@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    /**
     * Busca ingressos por usuário
     */
    List<Ingresso> findByUsuarioId(Long usuarioId);

    /**
     * Busca ingressos válidos por usuário
     */
    List<Ingresso> findByUsuarioIdAndStatus(Long usuarioId, StatusIngresso status);

    /**
     * Busca ingressos por sessão
     */
    List<Ingresso> findBySessaoId(Long sessaoId);

    /**
     * Busca ingressos válidos por sessão
     */
    List<Ingresso> findBySessaoIdAndStatus(Long sessaoId, StatusIngresso status);

    /**
     * Busca ingressos por área
     */
    List<Ingresso> findByAreaId(Long areaId);

    /**
     * Busca ingressos por sessão e área
     */
    List<Ingresso> findBySessaoIdAndAreaId(Long sessaoId, Long areaId);

    /**
     * Busca ingressos válidos por sessão e área
     */
    List<Ingresso> findBySessaoIdAndAreaIdAndStatus(Long sessaoId, Long areaId, StatusIngresso status);

    /**
     * Busca ingresso por código
     */
    Optional<Ingresso> findByCodigo(String codigo);

    /**
     * Verifica se existe ingresso com código
     */
    boolean existsByCodigo(String codigo);

    /**
     * VERIFICAÇÃO DE OCUPAÇÃO DE POLTRONAS
     * 
     * Esta é a consulta mais importante que implementa a lógica de ocupação
     * baseada na combinação única: evento_id + data + horário + area_id + poltrona_id
     */

    /**
     * Verifica se uma poltrona está ocupada para uma sessão e área específicas
     * (implementa a lógica de ocupação única)
     */
    @Query("SELECT COUNT(i) > 0 FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.area.id = :areaId " +
           "AND i.numeroPoltrona = :numeroPoltrona " +
           "AND i.status = 'VALIDO'")
    boolean isPoltronaOcupada(@Param("sessaoId") Long sessaoId,
                             @Param("areaId") Long areaId,
                             @Param("numeroPoltrona") Integer numeroPoltrona);

    /**
     * Busca todas as poltronas ocupadas para uma sessão e área
     */
    @Query("SELECT i.numeroPoltrona FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.area.id = :areaId " +
           "AND i.status = 'VALIDO'")
    List<Integer> findPoltronasOcupadas(@Param("sessaoId") Long sessaoId,
                                       @Param("areaId") Long areaId);

    /**
     * Busca todas as poltronas disponíveis para uma sessão e área
     * (retorna números de poltronas que NÃO estão ocupadas)
     */
    @Query("SELECT p.numero FROM (SELECT 1 as numero UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 " +
           "UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 " +
           "UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 " +
           "UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 " +
           "UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 " +
           "UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30 " +
           "UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35 " +
           "UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40 " +
           "UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45 " +
           "UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50 " +
           "UNION SELECT 51 UNION SELECT 52 UNION SELECT 53 UNION SELECT 54 UNION SELECT 55 " +
           "UNION SELECT 56 UNION SELECT 57 UNION SELECT 58 UNION SELECT 59 UNION SELECT 60 " +
           "UNION SELECT 61 UNION SELECT 62 UNION SELECT 63 UNION SELECT 64 UNION SELECT 65 " +
           "UNION SELECT 66 UNION SELECT 67 UNION SELECT 68 UNION SELECT 69 UNION SELECT 70 " +
           "UNION SELECT 71 UNION SELECT 72 UNION SELECT 73 UNION SELECT 74 UNION SELECT 75 " +
           "UNION SELECT 76 UNION SELECT 77 UNION SELECT 78 UNION SELECT 79 UNION SELECT 80 " +
           "UNION SELECT 81 UNION SELECT 82 UNION SELECT 83 UNION SELECT 84 UNION SELECT 85 " +
           "UNION SELECT 86 UNION SELECT 87 UNION SELECT 88 UNION SELECT 89 UNION SELECT 90 " +
           "UNION SELECT 91 UNION SELECT 92 UNION SELECT 93 UNION SELECT 94 UNION SELECT 95 " +
           "UNION SELECT 96 UNION SELECT 97 UNION SELECT 98 UNION SELECT 99 UNION SELECT 100) p " +
           "WHERE p.numero <= (SELECT a.capacidadeTotal FROM Area a WHERE a.id = :areaId) " +
           "AND p.numero NOT IN (" +
           "SELECT i.numeroPoltrona FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.area.id = :areaId " +
           "AND i.status = 'VALIDO')")
    List<Integer> findPoltronasDisponiveis(@Param("sessaoId") Long sessaoId,
                                          @Param("areaId") Long areaId);

    /**
     * Conta ingressos vendidos por sessão
     */
    @Query("SELECT COUNT(i) FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.status = 'VALIDO'")
    long countIngressosVendidosPorSessao(@Param("sessaoId") Long sessaoId);

    /**
     * Conta ingressos vendidos por sessão e área
     */
    @Query("SELECT COUNT(i) FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.area.id = :areaId " +
           "AND i.status = 'VALIDO'")
    long countIngressosVendidosPorSessaoEArea(@Param("sessaoId") Long sessaoId,
                                             @Param("areaId") Long areaId);

    /**
     * Calcula faturamento total por sessão
     */
    @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.status = 'VALIDO'")
    BigDecimal calcularFaturamentoPorSessao(@Param("sessaoId") Long sessaoId);

    /**
     * Calcula faturamento total por sessão e área
     */
    @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Ingresso i " +
           "WHERE i.sessao.id = :sessaoId " +
           "AND i.area.id = :areaId " +
           "AND i.status = 'VALIDO'")
    BigDecimal calcularFaturamentoPorSessaoEArea(@Param("sessaoId") Long sessaoId,
                                                @Param("areaId") Long areaId);

    /**
     * Busca ingressos por período de data
     */
    @Query("SELECT i FROM Ingresso i " +
           "WHERE i.sessao.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY i.sessao.dataSessao, i.sessao.horario")
    List<Ingresso> findIngressosPorPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                          @Param("dataFim") LocalDate dataFim);

    /**
     * Busca ingressos válidos por período de data
     */
    @Query("SELECT i FROM Ingresso i " +
           "WHERE i.sessao.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "AND i.status = 'VALIDO' " +
           "ORDER BY i.sessao.dataSessao, i.sessao.horario")
    List<Ingresso> findIngressosValidosPorPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim);

    /**
     * Busca ingressos por usuário e período
     */
    @Query("SELECT i FROM Ingresso i " +
           "WHERE i.usuario.id = :usuarioId " +
           "AND i.sessao.dataSessao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY i.sessao.dataSessao, i.sessao.horario")
    List<Ingresso> findIngressosPorUsuarioEPeriodo(@Param("usuarioId") Long usuarioId,
                                                  @Param("dataInicio") LocalDate dataInicio,
                                                  @Param("dataFim") LocalDate dataFim);

    /**
     * Verifica se um usuário já comprou ingresso para uma sessão específica
     */
    @Query("SELECT COUNT(i) > 0 FROM Ingresso i " +
           "WHERE i.usuario.id = :usuarioId " +
           "AND i.sessao.id = :sessaoId " +
           "AND i.status = 'VALIDO'")
    boolean usuarioJaComprouParaSessao(@Param("usuarioId") Long usuarioId,
                                      @Param("sessaoId") Long sessaoId);
} 