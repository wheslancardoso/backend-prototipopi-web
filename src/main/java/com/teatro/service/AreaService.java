package com.teatro.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.dto.AreaDTO;
import com.teatro.exception.AreaJaExisteException;
import com.teatro.exception.AreaNaoEncontradaException;
import com.teatro.model.Area;
import com.teatro.repository.AreaRepository;

@Service
@Transactional
public class AreaService {

  @Autowired
  private AreaRepository areaRepository;

  public AreaDTO cadastrarArea(AreaDTO areaDTO) {
    if (areaRepository.existsByNome(areaDTO.getNome())) {
      throw new AreaJaExisteException("Área já cadastrada: " + areaDTO.getNome());
    }

    Area area = areaDTO.toEntity();
    Area areaSalva = areaRepository.save(area);
    return new AreaDTO(areaSalva);
  }

  public List<AreaDTO> listarTodasAreas() {
    return areaRepository.findAll().stream().map(AreaDTO::new).collect(Collectors.toList());
  }

  public AreaDTO buscarPorId(Long id) {
    Area area = areaRepository.findById(id)
        .orElseThrow(() -> new AreaNaoEncontradaException("Área não encontrada com ID: " + id));
    return new AreaDTO(area);
  }

  public AreaDTO atualizarArea(Long id, AreaDTO areaDTO) {
    Area existente = buscarPorId(id).toEntity();
    if (!existente.getNome().equals(areaDTO.getNome())
        && areaRepository.existsByNome(areaDTO.getNome())) {
      throw new AreaJaExisteException("Área já cadastrada: " + areaDTO.getNome());
    }

    existente.setNome(areaDTO.getNome());
    existente.setPreco(areaDTO.getPreco());
    existente.setCapacidadeTotal(areaDTO.getCapacidadeTotal());
    existente.setDescricao(areaDTO.getDescricao());

    Area areaAtualizada = areaRepository.save(existente);
    return new AreaDTO(areaAtualizada);
  }

  public void removerArea(Long id) {
    Area area = buscarPorId(id).toEntity();
    areaRepository.delete(area);
  }

  public List<AreaDTO> listarAreasPorSessao(Long sessaoId) {
    // TODO: Implementar lógica para buscar áreas de uma sessão específica
    return areaRepository.findAll().stream().map(AreaDTO::new).collect(Collectors.toList());
  }

  public List<AreaDTO> listarAreasDisponiveisParaCompra(Long sessaoId) {
    // TODO: Implementar lógica para buscar áreas disponíveis para compra
    return areaRepository.findAll().stream().map(AreaDTO::new).collect(Collectors.toList());
  }
}
