package com.teatro.controller;

import java.util.List;
import java.util.stream.Collectors;
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
import com.teatro.model.Evento;
import com.teatro.service.EventoService;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a eventos
 * 
 * Endpoints: - POST /api/eventos - Cadastrar evento - GET /api/eventos - Listar eventos - GET
 * /api/eventos/{id} - Buscar evento por ID - PUT /api/eventos/{id} - Atualizar evento - DELETE
 * /api/eventos/{id} - Remover evento - GET /api/eventos/ativos - Listar eventos ativos - GET
 * /api/eventos/buscar - Buscar eventos por nome - PUT /api/eventos/{id}/status - Alterar status do
 * evento
 */
@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
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
  public ResponseEntity<EventoDTO> cadastrar(@Valid @RequestBody EventoDTO eventoDTO) {
    try {
      Evento evento = converterParaModel(eventoDTO);
      Evento eventoSalvo = eventoService.cadastrarEvento(evento);
      return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(eventoSalvo));
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
  public ResponseEntity<List<EventoDTO>> listarEventos() {
    List<Evento> eventos = eventoService.listarTodosEventos();
    List<EventoDTO> eventosDTO =
        eventos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(eventosDTO);
  }

  /**
   * Lista apenas eventos ativos
   * 
   * @return Lista de eventos ativos
   */
  @GetMapping("/ativos")
  public ResponseEntity<List<EventoDTO>> listarEventosAtivos() {
    List<Evento> eventos = eventoService.listarEventosAtivos();
    List<EventoDTO> eventosDTO =
        eventos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(eventosDTO);
  }

  /**
   * Busca eventos por nome
   * 
   * @param nome Nome ou parte do nome
   * @return Lista de eventos encontrados
   */
  @GetMapping("/buscar")
  public ResponseEntity<List<EventoDTO>> buscarPorNome(@RequestParam String nome) {
    List<Evento> eventos = eventoService.buscarPorNome(nome);
    List<EventoDTO> eventosDTO =
        eventos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(eventosDTO);
  }

  /**
   * Busca evento por ID
   * 
   * @param id ID do evento
   * @return Evento encontrado
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
    try {
      Evento evento = eventoService.buscarPorId(id);
      return ResponseEntity.ok(converterParaDTO(evento));
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
  public ResponseEntity<EventoDTO> atualizar(@PathVariable Long id,
      @Valid @RequestBody EventoDTO eventoDTO) {
    try {
      Evento evento = converterParaModel(eventoDTO);
      Evento eventoAtualizado = eventoService.atualizarEvento(id, evento);
      return ResponseEntity.ok(converterParaDTO(eventoAtualizado));
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
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    try {
      eventoService.removerEvento(id);
      return ResponseEntity.noContent().build();
    } catch (EventoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Altera status de um evento (ativo/inativo)
   * 
   * @param id ID do evento
   * @param ativo Status desejado
   * @return Evento atualizado
   */
  @PutMapping("/{id}/status")
  public ResponseEntity<EventoDTO> alterarStatus(@PathVariable Long id,
      @RequestParam boolean ativo) {
    try {
      Evento evento = eventoService.alterarStatusEvento(id, ativo);
      return ResponseEntity.ok(converterParaDTO(evento));
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
  public ResponseEntity<List<EventoDTO>> listarEventosComSessoesFuturas() {
    List<Evento> eventos = eventoService.listarEventosComSessoesFuturas();
    List<EventoDTO> eventosDTO =
        eventos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(eventosDTO);
  }

  /**
   * Converte Evento para EventoDTO
   */
  private EventoDTO converterParaDTO(Evento evento) {
    return new EventoDTO(evento.getId(), evento.getNome(), evento.getDescricao(),
        evento.getDuracaoMinutos(), evento.getClassificacaoIndicativa(), evento.getUrlPoster(),
        evento.isAtivo());
  }

  /**
   * Converte EventoDTO para Evento
   */
  private Evento converterParaModel(EventoDTO eventoDTO) {
    Evento evento = new Evento();
    evento.setId(eventoDTO.getId());
    evento.setNome(eventoDTO.getNome());
    evento.setDescricao(eventoDTO.getDescricao());
    evento.setDuracaoMinutos(eventoDTO.getDuracaoMinutos());
    evento.setClassificacaoIndicativa(eventoDTO.getClassificacaoIndicativa());
    evento.setUrlPoster(eventoDTO.getUrlPoster());
    evento.setAtivo(eventoDTO.getAtivo());
    return evento;
  }
}
