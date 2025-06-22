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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a ingressos
 * 
 * Endpoints:
 * - POST /api/ingressos - Comprar ingresso
 * - GET /api/ingressos - Listar ingressos
 * - GET /api/ingressos/{id} - Buscar ingresso por ID
 * - GET /api/ingressos/usuario/{usuarioId} - Listar ingressos por usuário
 * - GET /api/ingressos/sessao/{sessaoId} - Listar ingressos por sessão
 * - GET /api/ingressos/area/{areaId} - Listar ingressos por área
 * - DELETE /api/ingressos/{id} - Cancelar ingresso
 * - GET /api/ingressos/validar/{codigo} - Validar ingresso por código
 * - GET /api/ingressos/estatisticas - Estatísticas de vendas (admin)
 * - POST /api/ingressos/verificar-disponibilidade - Verificar disponibilidade de poltronas
 */
@RestController
@RequestMapping("/ingressos")
@CrossOrigin(origins = "*")
@Tag(name = "Ingressos", description = "Endpoints para gerenciamento de ingressos e compras")
public class IngressoController {

  @Autowired
  private IngressoService ingressoService;

  /**
   * Compra um novo ingresso
   * 
   * @param ingressoDTO Dados do ingresso
   * @return Ingresso comprado
   */
  @PostMapping
  @Operation(
      summary = "Comprar ingresso",
      description = "Realiza a compra de um ingresso para uma sessão específica"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Ingresso comprado com sucesso",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class))),
      @ApiResponse(responseCode = "409", description = "Poltrona já ocupada"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
      @ApiResponse(responseCode = "404", description = "Sessão, área ou usuário não encontrado")
  })
  public ResponseEntity<IngressoDTO> comprarIngresso(@Valid @RequestBody IngressoDTO ingressoDTO) {
    try {
      Ingresso ingressoComprado = ingressoService.comprarIngresso(
          ingressoDTO.getUsuarioId(),
          ingressoDTO.getSessaoId(),
          ingressoDTO.getAreaId(),
          ingressoDTO.getNumeroPoltrona(),
          ingressoDTO.getValor()
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(ingressoComprado));
    } catch (PoltronaOcupadaException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Lista todos os ingressos
   * 
   * @return Lista de ingressos
   */
  @GetMapping
  @Operation(
      summary = "Listar ingressos",
      description = "Retorna lista de todos os ingressos no sistema"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de ingressos retornada com sucesso",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class)))
  })
  public ResponseEntity<List<IngressoDTO>> listarIngressos() {
    List<Ingresso> ingressos = ingressoService.listarTodosIngressos();
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Busca ingresso por ID
   * 
   * @param id ID do ingresso
   * @return Ingresso encontrado
   */
  @GetMapping("/{id}")
  @Operation(
      summary = "Buscar ingresso por ID",
      description = "Retorna dados de um ingresso específico pelo ID"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ingresso encontrado",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
  })
  public ResponseEntity<IngressoDTO> buscarPorId(
          @Parameter(description = "ID do ingresso", required = true) 
          @PathVariable Long id) {
    try {
      Ingresso ingresso = ingressoService.buscarPorId(id);
      return ResponseEntity.ok(converterParaDTO(ingresso));
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Lista ingressos por usuário
   * 
   * @param usuarioId ID do usuário
   * @return Lista de ingressos do usuário
   */
  @GetMapping("/usuario/{usuarioId}")
  @Operation(
      summary = "Listar ingressos por usuário",
      description = "Retorna todos os ingressos de um usuário específico"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ingressos do usuário retornados com sucesso",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class)))
  })
  public ResponseEntity<List<IngressoDTO>> listarPorUsuario(
          @Parameter(description = "ID do usuário", required = true) 
          @PathVariable Long usuarioId) {
    List<Ingresso> ingressos = ingressoService.listarPorUsuario(usuarioId);
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Lista ingressos por sessão
   * 
   * @param sessaoId ID da sessão
   * @return Lista de ingressos da sessão
   */
  @GetMapping("/sessao/{sessaoId}")
  @Operation(
      summary = "Listar ingressos por sessão",
      description = "Retorna todos os ingressos vendidos para uma sessão específica"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ingressos da sessão retornados com sucesso",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class)))
  })
  public ResponseEntity<List<IngressoDTO>> listarPorSessao(
          @Parameter(description = "ID da sessão", required = true) 
          @PathVariable Long sessaoId) {
    List<Ingresso> ingressos = ingressoService.listarPorSessao(sessaoId);
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Lista ingressos por área
   * 
   * @param areaId ID da área
   * @return Lista de ingressos da área
   */
  @GetMapping("/area/{areaId}")
  @Operation(
      summary = "Listar ingressos por área",
      description = "Retorna todos os ingressos vendidos para uma área específica"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ingressos da área retornados com sucesso",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class)))
  })
  public ResponseEntity<List<IngressoDTO>> listarPorArea(
          @Parameter(description = "ID da área", required = true) 
          @PathVariable Long areaId) {
    List<Ingresso> ingressos = ingressoService.listarPorArea(areaId);
    List<IngressoDTO> ingressosDTO =
        ingressos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(ingressosDTO);
  }

  /**
   * Cancela um ingresso
   * 
   * @param id ID do ingresso
   * @return Status da operação
   */
  @DeleteMapping("/{id}")
  @Operation(
      summary = "Cancelar ingresso",
      description = "Cancela um ingresso (soft delete)"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Ingresso cancelado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
  })
  public ResponseEntity<Void> cancelarIngresso(
          @Parameter(description = "ID do ingresso", required = true) 
          @PathVariable Long id) {
    try {
      ingressoService.cancelarIngresso(id);
      return ResponseEntity.noContent().build();
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Valida ingresso por código
   * 
   * @param codigo Código do ingresso
   * @return Ingresso validado
   */
  @GetMapping("/validar/{codigo}")
  @Operation(
      summary = "Validar ingresso por código",
      description = "Valida um ingresso usando seu código único"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ingresso válido",
          content = @Content(schema = @Schema(implementation = IngressoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Ingresso não encontrado ou inválido")
  })
  public ResponseEntity<IngressoDTO> validarPorCodigo(
          @Parameter(description = "Código único do ingresso", required = true) 
          @PathVariable String codigo) {
    try {
      Ingresso ingresso = ingressoService.validarPorCodigo(codigo);
      return ResponseEntity.ok(converterParaDTO(ingresso));
    } catch (IngressoNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Verifica disponibilidade de poltronas
   * 
   * @param sessaoId ID da sessão
   * @param areaId ID da área
   * @return Lista de poltronas disponíveis
   */
  @PostMapping("/verificar-disponibilidade")
  @Operation(
      summary = "Verificar disponibilidade de poltronas",
      description = "Verifica quais poltronas estão disponíveis em uma sessão/área"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Disponibilidade verificada com sucesso")
  })
  public ResponseEntity<List<Integer>> verificarDisponibilidade(
          @Parameter(description = "ID da sessão", required = true) 
          @RequestParam Long sessaoId,
          @Parameter(description = "ID da área", required = true) 
          @RequestParam Long areaId) {
    List<Integer> poltronasDisponiveis = ingressoService.verificarPoltronasDisponiveis(sessaoId, areaId);
    return ResponseEntity.ok(poltronasDisponiveis);
  }

  /**
   * Estatísticas de vendas (apenas admin)
   * 
   * @return Estatísticas de vendas
   */
  @GetMapping("/estatisticas")
  @Operation(
      summary = "Estatísticas de vendas",
      description = "Retorna estatísticas de vendas de ingressos (apenas admin)"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
  })
  public ResponseEntity<Object> obterEstatisticas() {
    // TODO: Implementar estatísticas
    return ResponseEntity.ok().build();
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
