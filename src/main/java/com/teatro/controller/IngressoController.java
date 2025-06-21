package com.teatro.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.IngressoDTO;
import com.teatro.exception.IngressoNaoEncontradoException;
import com.teatro.exception.PoltronaOcupadaException;
import com.teatro.model.Ingresso;
import com.teatro.service.IngressoService;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a ingressos
 */
@RestController
@RequestMapping("/api/ingressos")
@CrossOrigin(origins = "*")
public class IngressoController {

  @Autowired
  private IngressoService ingressoService;

  /**
   * Compra de ingresso
   */
  @PostMapping
  public ResponseEntity<IngressoDTO> comprar(@Valid @RequestBody IngressoDTO ingressoDTO) {
    try {
      Ingresso ingresso = converterParaModel(ingressoDTO);
      Ingresso ingressoSalvo = ingressoService.comprarIngresso(ingresso);
      return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(ingressoSalvo));
    } catch (PoltronaOcupadaException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  /**
   * Lista todos os ingressos
   */
  @GetMapping
  public ResponseEntity<List<IngressoDTO>> listarIngressos() {
    List<Ingresso> ingressos = ingressoService.listarTodosIngressos();
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Lista ingressos por usuário
   */
  @GetMapping("/usuario/{usuarioId}")
  public ResponseEntity<List<IngressoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
    List<Ingresso> ingressos = ingressoService.listarPorUsuario(usuarioId);
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Busca ingresso por ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<IngressoDTO> buscarPorId(@PathVariable Long id) {
    try {
      Ingresso ingresso = ingressoService.buscarPorId(id);
      return ResponseEntity.ok(converterParaDTO(ingresso));
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Converte Ingresso para IngressoDTO
   */
  private IngressoDTO converterParaDTO(Ingresso ingresso) {
    IngressoDTO dto = new IngressoDTO();
    dto.setId(ingresso.getId());
    dto.setUsuarioId(ingresso.getUsuario() != null ? ingresso.getUsuario().getId() : null);
    dto.setSessaoId(ingresso.getSessao() != null ? ingresso.getSessao().getId() : null);
    dto.setAreaId(ingresso.getArea() != null ? ingresso.getArea().getId() : null);
    dto.setNumeroPoltrona(ingresso.getNumeroPoltrona());
    dto.setValor(ingresso.getValor());
    dto.setDataCompra(ingresso.getDataCompra());
    dto.setCodigo(ingresso.getCodigo());
    dto.setEventoNome(ingresso.getSessao() != null && ingresso.getSessao().getEvento() != null
        ? ingresso.getSessao().getEvento().getNome()
        : null);
    dto.setAreaNome(ingresso.getArea() != null ? ingresso.getArea().getNome() : null);
    dto.setUsuarioNome(ingresso.getUsuario() != null ? ingresso.getUsuario().getNome() : null);
    return dto;
  }

  /**
   * Converte IngressoDTO para Ingresso
   */
  private Ingresso converterParaModel(IngressoDTO dto) {
    Ingresso ingresso = new Ingresso();
    ingresso.setId(dto.getId());
    ingresso.setNumeroPoltrona(dto.getNumeroPoltrona());
    ingresso.setValor(dto.getValor());
    ingresso.setCodigo(dto.getCodigo());
    // Associações (usuario, sessao, area) devem ser resolvidas no service
    return ingresso;
  }
}
