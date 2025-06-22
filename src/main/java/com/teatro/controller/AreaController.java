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
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.AreaDTO;
import com.teatro.exception.AreaJaExisteException;
import com.teatro.exception.AreaNaoEncontradaException;
import com.teatro.model.Area;
import com.teatro.service.AreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a áreas do teatro
 */
@RestController
@RequestMapping("/areas")
@CrossOrigin(origins = "*")
@Tag(name = "Áreas", description = "Endpoints para gerenciamento de áreas do teatro")
public class AreaController {

  @Autowired
  private AreaService areaService;

  @PostMapping
  public ResponseEntity<AreaDTO> cadastrar(@Valid @RequestBody AreaDTO areaDTO) {
    try {
      Area area = converterParaModel(areaDTO);
      Area areaSalva = areaService.cadastrarArea(area);
      return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(areaSalva));
    } catch (AreaJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<AreaDTO>> listarAreas() {
    List<Area> areas = areaService.listarTodasAreas();
    List<AreaDTO> areasDTO =
        areas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(areasDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AreaDTO> buscarPorId(@PathVariable Long id) {
    try {
      Area area = areaService.buscarPorId(id);
      return ResponseEntity.ok(converterParaDTO(area));
    } catch (AreaNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<AreaDTO> atualizar(@PathVariable Long id,
      @Valid @RequestBody AreaDTO areaDTO) {
    try {
      Area area = converterParaModel(areaDTO);
      Area areaAtualizada = areaService.atualizarArea(id, area);
      return ResponseEntity.ok(converterParaDTO(areaAtualizada));
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

  private AreaDTO converterParaDTO(Area area) {
    return new AreaDTO(area.getId(), area.getNome(),
        area.getPreco() != null ? area.getPreco().doubleValue() : null, area.getCapacidadeTotal(),
        null, // sessaoId não é campo direto
        null // faturamento não é campo direto
    );
  }

  private Area converterParaModel(AreaDTO dto) {
    Area area = new Area();
    area.setId(dto.getId());
    area.setNome(dto.getNome());
    if (dto.getPreco() != null)
      area.setPreco(java.math.BigDecimal.valueOf(dto.getPreco()));
    area.setCapacidadeTotal(dto.getCapacidadeTotal());
    area.setDescricao(null); // Ajuste se necessário
    return area;
  }
}
