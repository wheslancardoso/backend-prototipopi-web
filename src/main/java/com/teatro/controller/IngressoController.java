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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.IngressoDTO;
import com.teatro.exception.IngressoNaoEncontradoException;
import com.teatro.exception.PoltronaOcupadaException;
import com.teatro.model.Ingresso;
import com.teatro.service.IngressoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a ingressos
 *
 * Endpoints: - POST /api/ingressos - Comprar ingresso - GET /api/ingressos - Listar ingressos - GET
 * /api/ingressos/{id} - Buscar ingresso por ID - GET /api/ingressos/usuario/{usuarioId} - Listar
 * ingressos por usuário - GET /api/ingressos/sessao/{sessaoId} - Listar ingressos por sessão - GET
 * /api/ingressos/area/{areaId} - Listar ingressos por área - DELETE /api/ingressos/{id} - Cancelar
 * ingresso - GET /api/ingressos/validar/{codigo} - Validar ingresso por código - GET
 * /api/ingressos/estatisticas - Estatísticas de vendas (admin) - POST
 * /api/ingressos/verificar-disponibilidade - Verificar disponibilidade de poltronas
 */
@RestController
@RequestMapping("/ingressos")
@CrossOrigin(origins = "*")
@Tag(name = "Ingressos", description = "Endpoints para gerenciamento de ingressos")
public class IngressoController {

  @Autowired
  private IngressoService ingressoService;

  @PostMapping
  public ResponseEntity<IngressoDTO> comprarIngresso(@Valid @RequestBody IngressoDTO ingressoDTO) {
    try {
      Ingresso ingresso = ingressoService.comprarIngresso(ingressoDTO.toEntity());
      return ResponseEntity.status(HttpStatus.CREATED).body(new IngressoDTO(ingresso));
    } catch (PoltronaOcupadaException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<IngressoDTO>> listarIngressos() {
    List<IngressoDTO> ingressos = ingressoService.listarTodosIngressos().stream()
        .map(IngressoDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok(ingressos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IngressoDTO> buscarPorId(@PathVariable Long id) {
    try {
      Ingresso ingresso = ingressoService.buscarPorId(id);
      return ResponseEntity.ok(new IngressoDTO(ingresso));
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/usuario/{usuarioId}")
  public ResponseEntity<List<IngressoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
    List<IngressoDTO> ingressos = ingressoService.listarPorUsuario(usuarioId).stream()
        .map(IngressoDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok(ingressos);
  }

  @GetMapping("/sessao/{sessaoId}")
  public ResponseEntity<List<IngressoDTO>> listarPorSessao(@PathVariable Long sessaoId) {
    List<IngressoDTO> ingressos = ingressoService.listarPorSessao(sessaoId).stream()
        .map(IngressoDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok(ingressos);
  }

  @GetMapping("/area/{areaId}")
  public ResponseEntity<List<IngressoDTO>> listarPorArea(@PathVariable Long areaId) {
    List<IngressoDTO> ingressos = ingressoService.listarPorArea(areaId).stream()
        .map(IngressoDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok(ingressos);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelarIngresso(@PathVariable Long id) {
    try {
      ingressoService.cancelarIngresso(id);
      return ResponseEntity.noContent().build();
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/validar/{codigo}")
  public ResponseEntity<IngressoDTO> validarPorCodigo(@PathVariable String codigo) {
    try {
      Ingresso ingresso = ingressoService.validarPorCodigo(codigo);
      return ResponseEntity.ok(new IngressoDTO(ingresso));
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/verificar-disponibilidade")
  public ResponseEntity<List<Integer>> verificarDisponibilidade(@RequestParam Long sessaoId,
      @RequestParam Long areaId) {
    List<Integer> poltronasDisponiveis =
        ingressoService.verificarPoltronasDisponiveis(sessaoId, areaId);
    return ResponseEntity.ok(poltronasDisponiveis);
  }

  @GetMapping("/estatisticas")
  public ResponseEntity<Object> obterEstatisticas() {
    // TODO: Implementar estatísticas de ingressos
    return ResponseEntity.ok("Estatísticas de ingressos - em desenvolvimento");
  }
}
