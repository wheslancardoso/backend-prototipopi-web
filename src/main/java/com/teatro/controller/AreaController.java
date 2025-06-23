package com.teatro.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.AreaDTO;
import com.teatro.exception.AreaJaExisteException;
import com.teatro.exception.AreaNaoEncontradaException;
import com.teatro.service.AreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a áreas do teatro
 *
 * Endpoints: - POST /api/areas - Cadastrar área - GET /api/areas - Listar áreas - GET
 * /api/areas/{id} - Buscar área por ID - PUT /api/areas/{id} - Atualizar área - DELETE
 * /api/areas/{id} - Remover área - GET /api/areas/sessao/{sessaoId} - Listar áreas por sessão - GET
 * /api/areas/disponiveis/{sessaoId} - Listar áreas disponíveis para compra em uma sessão
 */
@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
@Tag(name = "Áreas", description = "Endpoints para gerenciamento de áreas do teatro")
public class AreaController {

  @Autowired
  private AreaService areaService;

  @PostMapping
  public ResponseEntity<AreaDTO> cadastrar(@Valid @RequestBody AreaDTO areaDTO) {
    try {
      AreaDTO areaSalva = areaService.cadastrarArea(areaDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(areaSalva);
    } catch (AreaJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<AreaDTO>> listarAreas() {
    List<AreaDTO> areas = areaService.listarTodasAreas();
    return ResponseEntity.ok(areas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AreaDTO> buscarPorId(@PathVariable Long id) {
    try {
      AreaDTO area = areaService.buscarPorId(id);
      return ResponseEntity.ok(area);
    } catch (AreaNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<AreaDTO> atualizar(@PathVariable Long id,
      @Valid @RequestBody AreaDTO areaDTO) {
    try {
      AreaDTO areaAtualizada = areaService.atualizarArea(id, areaDTO);
      return ResponseEntity.ok(areaAtualizada);
    } catch (AreaNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    } catch (AreaJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    try {
      areaService.removerArea(id);
      return ResponseEntity.noContent().build();
    } catch (AreaNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/sessao/{sessaoId}")
  public ResponseEntity<List<AreaDTO>> listarAreasPorSessao(@PathVariable Long sessaoId) {
    List<AreaDTO> areas = areaService.listarAreasPorSessao(sessaoId);
    return ResponseEntity.ok(areas);
  }

  @GetMapping("/disponiveis/{sessaoId}")
  public ResponseEntity<List<AreaDTO>> listarAreasDisponiveisParaCompra(
      @PathVariable Long sessaoId) {
    List<AreaDTO> areas = areaService.listarAreasDisponiveisParaCompra(sessaoId);
    return ResponseEntity.ok(areas);
  }
}
