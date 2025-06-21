package com.teatro.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.IngressoNaoEncontradoException;
import com.teatro.exception.PoltronaOcupadaException;
import com.teatro.model.Ingresso;
import com.teatro.repository.IngressoRepository;

@Service
@Transactional
public class IngressoService {

  @Autowired
  private IngressoRepository ingressoRepository;

  public Ingresso comprarIngresso(Ingresso ingresso) {
    // Lógica de verificação de poltrona ocupada (correta)
    boolean ocupado = ingressoRepository.isPoltronaOcupada(ingresso.getSessao().getId(),
        ingresso.getArea().getId(), ingresso.getNumeroPoltrona());
    if (ocupado) {
      throw new PoltronaOcupadaException("Poltrona já ocupada!");
    }
    return ingressoRepository.save(ingresso);
  }

  public List<Ingresso> listarTodosIngressos() {
    return ingressoRepository.findAll();
  }

  public List<Ingresso> listarPorUsuario(Long usuarioId) {
    return ingressoRepository.findByUsuarioId(usuarioId);
  }

  public Ingresso buscarPorId(Long id) {
    return ingressoRepository.findById(id).orElseThrow(
        () -> new IngressoNaoEncontradoException("Ingresso não encontrado com ID: " + id));
  }
}
