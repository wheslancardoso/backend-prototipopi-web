package com.teatro.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teatro.model.Area;

/**
 * Repository para operações de persistência da entidade Area
 * 
 * Fornece métodos para: - Busca de áreas ativas - Cálculo de disponibilidade de poltronas -
 * Estatísticas de faturamento
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

        /**
         * Busca áreas ativas
         */
        List<Area> findByAtivoTrue();

        /**
         * Busca área por nome
         */
        Optional<Area> findByNome(String nome);

        /**
         * Busca áreas por nome (contendo)
         */
        List<Area> findByNomeContainingIgnoreCase(String nome);

        /**
         * Busca áreas ativas por nome (contendo)
         */
        List<Area> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

        /**
         * Busca áreas por preço (maior ou igual)
         */
        List<Area> findByPrecoGreaterThanEqual(BigDecimal preco);

        /**
         * Busca áreas por preço (menor ou igual)
         */
        List<Area> findByPrecoLessThanEqual(BigDecimal preco);

        /**
         * Busca áreas por faixa de preço
         */
        List<Area> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);

        /**
         * Busca áreas por capacidade (maior ou igual)
         */
        List<Area> findByCapacidadeTotalGreaterThanEqual(Integer capacidade);

        /**
         * Busca áreas por capacidade (menor ou igual)
         */
        List<Area> findByCapacidadeTotalLessThanEqual(Integer capacidade);

        /**
         * Busca áreas por faixa de capacidade
         */
        List<Area> findByCapacidadeTotalBetween(Integer capacidadeMin, Integer capacidadeMax);

        /**
         * Conta áreas ativas
         */
        long countByAtivoTrue();

        /**
         * Verifica se existe área com nome
         */
        boolean existsByNome(String nome);

        /**
         * Busca áreas que possuem poltronas disponíveis para uma sessão
         */
        @Query("SELECT DISTINCT a FROM Area a " + "JOIN a.sessoes s " + "WHERE a.ativo = true "
                        + "AND s.id = :sessaoId " + "AND s.ativa = true "
                        + "AND a.capacidadeTotal > ("
                        + "SELECT COALESCE(COUNT(i), 0) FROM Ingresso i "
                        + "WHERE i.sessao.id = :sessaoId " + "AND i.area.id = a.id "
                        + "AND i.status = 'VALIDO')")
        List<Area> findAreasComPoltronasDisponiveis(@Param("sessaoId") Long sessaoId);

        /**
         * Busca áreas com maior ocupação para uma sessão
         */
        @Query("SELECT a, "
                        + "ROUND((COUNT(i) * 100.0 / a.capacidadeTotal), 2) as percentualOcupacao "
                        + "FROM Area a " + "JOIN a.sessoes s "
                        + "LEFT JOIN Ingresso i ON i.area.id = a.id AND i.sessao.id = :sessaoId AND i.status = 'VALIDO' "
                        + "WHERE a.ativo = true " + "AND s.id = :sessaoId " + "GROUP BY a "
                        + "ORDER BY percentualOcupacao DESC")
        List<Object[]> findAreasComMaiorOcupacao(@Param("sessaoId") Long sessaoId);

        /**
         * Calcula faturamento total de uma área para uma sessão
         */
        @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Ingresso i " + "WHERE i.area.id = :areaId "
                        + "AND i.sessao.id = :sessaoId " + "AND i.status = 'VALIDO'")
        BigDecimal calcularFaturamentoPorAreaESessao(@Param("areaId") Long areaId,
                        @Param("sessaoId") Long sessaoId);

        /**
         * Calcula faturamento total de uma área para um período
         */
        @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Ingresso i " + "WHERE i.area.id = :areaId "
                        + "AND i.sessao.dataSessao BETWEEN :dataInicio AND :dataFim "
                        + "AND i.status = 'VALIDO'")
        BigDecimal calcularFaturamentoPorAreaEPeriodo(@Param("areaId") Long areaId,
                        @Param("dataInicio") String dataInicio, @Param("dataFim") String dataFim);

        /**
         * Busca áreas mais lucrativas
         */
        @Query("SELECT a, COALESCE(SUM(i.valor), 0) as faturamentoTotal " + "FROM Area a "
                        + "LEFT JOIN Ingresso i ON i.area.id = a.id AND i.status = 'VALIDO' "
                        + "WHERE a.ativo = true " + "GROUP BY a "
                        + "ORDER BY faturamentoTotal DESC")
        List<Object[]> findAreasMaisLucrativas();

        /**
         * Busca áreas que não possuem sessões
         */
        @Query("SELECT a FROM Area a " + "WHERE a.ativo = true " + "AND a.sessoes IS EMPTY")
        List<Area> findAreasSemSessoes();

        /**
         * Busca áreas por sessão
         */
        @Query("SELECT a FROM Area a " + "JOIN a.sessoes s " + "WHERE s.id = :sessaoId "
                        + "AND a.ativo = true " + "ORDER BY a.nome")
        List<Area> findAreasPorSessao(@Param("sessaoId") Long sessaoId);
}
