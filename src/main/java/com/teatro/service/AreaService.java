package com.teatro.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.AreaJaExisteException;
import com.teatro.exception.AreaNaoEncontradaException;
import com.teatro.model.Area;
import com.teatro.repository.AreaRepository;

@Service
@Transactional
public class AreaService {

  @Autowired
  private AreaRepository areaRepository;

  public Area cadastrarArea(Area area) {
    if (areaRepository.existsByNome(area.getNome())) {
      throw new AreaJaExisteException("Área já cadastrada: " + area.getNome());
    }
    return areaRepository.save(area);
  }

  public List<Area> listarTodasAreas() {
    return areaRepository.findAll();
  }

  public Area buscarPorId(Long id) {
    return areaRepository.findById(id)
        .orElseThrow(() -> new AreaNaoEncontradaException("Área não encontrada com ID: " + id));
  }

  public Area atualizarArea(Long id, Area area) {
    Area existente = buscarPorId(id);
    if (!existente.getNome().equals(area.getNome())
        && areaRepository.existsByNome(area.getNome())) {
      throw new AreaJaExisteException("Área já cadastrada: " + area.getNome());
    }
    existente.setNome(area.getNome());
    existente.setPreco(area.getPreco());
    existente.setCapacidadeTotal(area.getCapacidadeTotal());
    existente.setDescricao(area.getDescricao());
    return areaRepository.save(existente);
  }

  public void removerArea(Long id) {
    Area area = buscarPorId(id);
    areaRepository.delete(area);
  }
}
