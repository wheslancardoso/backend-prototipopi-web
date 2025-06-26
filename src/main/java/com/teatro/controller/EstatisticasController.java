package com.teatro.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.service.EstatisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller para operações relacionadas a estatísticas e dashboard
 *
 * Endpoints: - GET /api/estatisticas/gerais - Estatísticas gerais do sistema - GET
 * /api/estatisticas/eventos - Estatísticas de eventos - GET /api/estatisticas/eventos/{id} -
 * Estatísticas de um evento específico - GET /api/estatisticas/sessoes - Estatísticas de sessões -
 * GET /api/estatisticas/vendas - Estatísticas de vendas - GET /api/estatisticas/ocupacao -
 * Estatísticas de ocupação - GET /api/estatisticas/faturamento - Estatísticas de faturamento - GET
 * /api/estatisticas/fidelidade - Estatísticas de fidelidade - GET /api/estatisticas/periodo -
 * Estatísticas por período
 */
@RestController
@RequestMapping("/estatisticas")
@CrossOrigin(origins = "*")
@Tag(name = "Estatísticas", description = "Endpoints para consulta de estatísticas do sistema")
public class EstatisticasController {

  @Autowired
  private EstatisticasService estatisticasService;

  /**
   * Estatísticas gerais do sistema
   *
   * @return Estatísticas gerais
   */
  @GetMapping("/gerais")
  @Operation(summary = "Estatísticas gerais",
      description = "Retorna estatísticas gerais do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso",
          content = @Content(schema = @Schema(implementation = Map.class)))})
  public ResponseEntity<Map<String, Object>> estatisticasGerais() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasGerais();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de eventos
   *
   * @return Estatísticas de eventos
   */
  @GetMapping("/eventos")
  @Operation(summary = "Estatísticas de eventos",
      description = "Retorna estatísticas detalhadas de eventos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasEventos() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasEventos();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de um evento específico
   *
   * @param id ID do evento
   * @return Estatísticas do evento
   */
  @GetMapping("/eventos/{id}")
  @Operation(summary = "Estatísticas de evento específico",
      description = "Retorna estatísticas detalhadas de um evento específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado")})
  public ResponseEntity<Map<String, Object>> estatisticasEvento(
      @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
    try {
      Map<String, Object> estatisticas = estatisticasService.obterEstatisticasEvento(id);
      return ResponseEntity.ok(estatisticas);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Estatísticas de sessões
   *
   * @return Estatísticas de sessões
   */
  @GetMapping("/sessoes")
  @Operation(summary = "Estatísticas de sessões", description = "Retorna estatísticas de sessões")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasSessoes() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasSessoes();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de vendas
   *
   * @return Estatísticas de vendas
   */
  @GetMapping("/vendas")
  @Operation(summary = "Estatísticas de vendas",
      description = "Retorna estatísticas de vendas de ingressos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasVendas() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasVendas();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de ocupação
   *
   * @return Estatísticas de ocupação
   */
  @GetMapping("/ocupacao")
  @Operation(summary = "Estatísticas de ocupação",
      description = "Retorna estatísticas de ocupação de poltronas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasOcupacao() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasOcupacao();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de faturamento
   *
   * @return Estatísticas de faturamento
   */
  @GetMapping("/faturamento")
  @Operation(summary = "Estatísticas de faturamento",
      description = "Retorna estatísticas de faturamento")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasFaturamento() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasFaturamento();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas de fidelidade
   *
   * @return Estatísticas de fidelidade
   */
  @GetMapping("/fidelidade")
  @Operation(summary = "Estatísticas de fidelidade",
      description = "Retorna estatísticas do programa de fidelidade")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasFidelidade() {
    Map<String, Object> estatisticas = estatisticasService.obterEstatisticasFidelidade();
    return ResponseEntity.ok(estatisticas);
  }

  /**
   * Estatísticas por período
   *
   * @param dataInicio Data de início (YYYY-MM-DD)
   * @param dataFim Data de fim (YYYY-MM-DD)
   * @return Estatísticas do período
   */
  @GetMapping("/periodo")
  @Operation(summary = "Estatísticas por período",
      description = "Retorna estatísticas de um período específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
      @ApiResponse(responseCode = "400", description = "Período inválido")})
  public ResponseEntity<Map<String, Object>> estatisticasPorPeriodo(
      @Parameter(description = "Data de início (YYYY-MM-DD)",
          required = true) @RequestParam String dataInicio,
      @Parameter(description = "Data de fim (YYYY-MM-DD)",
          required = true) @RequestParam String dataFim) {
    try {
      Map<String, Object> estatisticas =
          estatisticasService.obterEstatisticasPorPeriodo(dataInicio, dataFim);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Dashboard completo para administradores
   *
   * @return Dashboard completo
   */
  @GetMapping("/dashboard")
  @Operation(summary = "Dashboard administrativo",
      description = "Retorna dashboard completo para administradores")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Dashboard retornado com sucesso")})
  public ResponseEntity<Map<String, Object>> dashboard() {
    Map<String, Object> dashboard = estatisticasService.obterDashboard();
    return ResponseEntity.ok(dashboard);
  }
}
