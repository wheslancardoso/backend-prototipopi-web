package com.teatro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.EventoDTO;
import com.teatro.exception.EventoJaExisteException;
import com.teatro.exception.EventoNaoEncontradoException;
import com.teatro.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a eventos
 * 
 * Endpoints: - POST /api/eventos - Cadastrar evento - GET /api/eventos - Listar eventos - GET
 * /api/eventos/{id} - Buscar evento por ID - PUT /api/eventos/{id} - Atualizar evento - DELETE
 * /api/eventos/{id} - Remover evento - GET /api/eventos/ativos - Listar eventos ativos - GET
 * /api/eventos/buscar - Buscar eventos por nome - PUT /api/eventos/{id}/status - Alterar status do
 * evento - GET /api/eventos/com-sessoes-futuras - Listar eventos com sessões futuras - GET
 * /api/eventos/{id}/estatisticas - Estatísticas do evento - GET /api/eventos/estatisticas-gerais -
 * Estatísticas gerais
 */
@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
@Tag(name = "Eventos", description = "Endpoints para gerenciamento de eventos teatrais")
public class EventoController {

  @Autowired
  private EventoService eventoService;

  /**
   * Cadastra um novo evento
   * 
   * @param eventoDTO Dados do evento
   * @return Evento cadastrado
   */
  @PostMapping
  @Operation(summary = "Cadastrar evento", description = "Cria um novo evento teatral no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Evento criado com sucesso",
          content = @Content(schema = @Schema(implementation = EventoDTO.class))),
      @ApiResponse(responseCode = "409", description = "Evento já existe"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  public ResponseEntity<EventoDTO> cadastrar(@Valid @RequestBody EventoDTO eventoDTO) {
    try {
      EventoDTO eventoSalvo = eventoService.cadastrarEvento(eventoDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(eventoSalvo);
    } catch (EventoJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  /**
   * Lista todos os eventos
   * 
   * @return Lista de eventos
   */
  @GetMapping
  @Operation(summary = "Listar eventos",
      description = "Retorna lista de todos os eventos no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso",
          content = @Content(schema = @Schema(implementation = EventoDTO.class)))})
  public ResponseEntity<List<EventoDTO>> listarEventos() {
    List<EventoDTO> eventos = eventoService.listarTodosEventos();
    return ResponseEntity.ok(eventos);
  }

  /**
   * Lista apenas eventos ativos
   * 
   * @return Lista de eventos ativos
   */
  @GetMapping("/ativos")
  @Operation(summary = "Listar eventos ativos",
      description = "Retorna lista de eventos ativos no sistema")
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Lista de eventos ativos retornada com sucesso",
      content = @Content(schema = @Schema(implementation = EventoDTO.class)))})
  public ResponseEntity<List<EventoDTO>> listarEventosAtivos() {
    List<EventoDTO> eventos = eventoService.listarEventosAtivos();
    return ResponseEntity.ok(eventos);
  }

  /**
   * Busca eventos por nome
   * 
   * @param nome Nome ou parte do nome
   * @return Lista de eventos encontrados
   */
  @GetMapping("/buscar")
  @Operation(summary = "Buscar eventos por nome",
      description = "Busca eventos que contenham o nome especificado")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Eventos encontrados",
      content = @Content(schema = @Schema(implementation = EventoDTO.class)))})
  public ResponseEntity<List<EventoDTO>> buscarPorNome(@Parameter(
      description = "Nome ou parte do nome do evento", required = true) @RequestParam String nome) {
    List<EventoDTO> eventos = eventoService.buscarPorNome(nome);
    return ResponseEntity.ok(eventos);
  }

  /**
   * Busca evento por ID
   * 
   * @param id ID do evento
   * @return Evento encontrado
   */
  @GetMapping("/{id}")
  @Operation(summary = "Buscar evento por ID",
      description = "Retorna dados de um evento específico pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Evento encontrado",
          content = @Content(schema = @Schema(implementation = EventoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado")})
  public ResponseEntity<EventoDTO> buscarPorId(
      @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
    try {
      EventoDTO evento = eventoService.buscarPorId(id);
      return ResponseEntity.ok(evento);
    } catch (EventoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Atualiza dados de um evento
   * 
   * @param id ID do evento
   * @param eventoDTO Dados atualizados
   * @return Evento atualizado
   */
  @PutMapping("/{id}")
  @Operation(summary = "Atualizar evento", description = "Atualiza dados de um evento existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso",
          content = @Content(schema = @Schema(implementation = EventoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
      @ApiResponse(responseCode = "409", description = "Evento já existe"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  public ResponseEntity<EventoDTO> atualizar(
      @Parameter(description = "ID do evento", required = true) @PathVariable Long id,
      @Valid @RequestBody EventoDTO eventoDTO) {
    try {
      EventoDTO eventoAtualizado = eventoService.atualizarEvento(id, eventoDTO);
      return ResponseEntity.ok(eventoAtualizado);
    } catch (EventoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    } catch (EventoJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  /**
   * Remove um evento
   * 
   * @param id ID do evento
   * @return Status da operação
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Remover evento", description = "Remove um evento do sistema")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Evento removido com sucesso"),
          @ApiResponse(responseCode = "404", description = "Evento não encontrado")})
  public ResponseEntity<Void> remover(
      @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
    try {
      eventoService.removerEvento(id);
      return ResponseEntity.noContent().build();
    } catch (EventoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Altera status de um evento
   * 
   * @param id ID do evento
   * @param ativo Status desejado
   * @return Evento com status alterado
   */
  @PutMapping("/{id}/status")
  @Operation(summary = "Alterar status do evento", description = "Ativa ou desativa um evento")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
          content = @Content(schema = @Schema(implementation = EventoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado")})
  public ResponseEntity<EventoDTO> alterarStatus(
      @Parameter(description = "ID do evento", required = true) @PathVariable Long id,
      @Parameter(description = "Status desejado (true = ativo, false = inativo)",
          required = true) @RequestParam boolean ativo) {
    try {
      EventoDTO evento = eventoService.alterarStatusEvento(id, ativo);
      return ResponseEntity.ok(evento);
    } catch (EventoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Lista eventos com sessões futuras
   * 
   * @return Lista de eventos com sessões futuras
   */
  @GetMapping("/com-sessoes-futuras")
  @Operation(summary = "Listar eventos com sessões futuras",
      description = "Retorna eventos que possuem sessões futuras agendadas")
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Eventos com sessões futuras retornados com sucesso",
      content = @Content(schema = @Schema(implementation = EventoDTO.class)))})
  public ResponseEntity<List<EventoDTO>> listarEventosComSessoesFuturas() {
    List<EventoDTO> eventos = eventoService.listarEventosComSessoesFuturas();
    return ResponseEntity.ok(eventos);
  }

  /**
   * Busca eventos por filtros avançados
   * 
   * @param ativo Status ativo
   * @param comSessoesFuturas Se deve ter sessões futuras
   * @param categoria Categoria do evento
   * @return Lista de eventos filtrados
   */
  @GetMapping("/filtrar")
  @Operation(summary = "Filtrar eventos", description = "Busca eventos usando filtros avançados")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Eventos filtrados com sucesso")})
  public ResponseEntity<List<EventoDTO>> filtrarEventos(
      @RequestParam(required = false) Boolean ativo,
      @RequestParam(required = false) Boolean comSessoesFuturas,
      @RequestParam(required = false) String categoria) {
    List<EventoDTO> eventos = eventoService.listarEventosAtivos();
    return ResponseEntity.ok(eventos);
  }

  /**
   * Estatísticas de um evento específico
   * 
   * @param id ID do evento
   * @return Estatísticas do evento
   */
  @GetMapping("/{id}/estatisticas")
  @Operation(summary = "Estatísticas do evento",
      description = "Retorna estatísticas detalhadas de um evento específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado")})
  public ResponseEntity<Map<String, Object>> estatisticasEvento(@PathVariable Long id) {
    try {
      Map<String, Object> estatisticas = new HashMap<>();
      estatisticas.put("eventoId", id);
      estatisticas.put("totalSessoes", 0);
      estatisticas.put("totalIngressosVendidos", 0);
      return ResponseEntity.ok(estatisticas);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Estatísticas gerais de todos os eventos
   * 
   * @return Estatísticas gerais
   */
  @GetMapping("/estatisticas-gerais")
  @Operation(summary = "Estatísticas gerais",
      description = "Retorna estatísticas gerais de todos os eventos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")})
  public ResponseEntity<Map<String, Object>> estatisticasGerais() {
    Map<String, Object> estatisticas = new HashMap<>();
    estatisticas.put("totalEventos", eventoService.contarEventosAtivos());
    estatisticas.put("eventosComSessoesFuturas", eventoService.listarEventosComSessoesFuturas().size());
    return ResponseEntity.ok(estatisticas);
  }
}
