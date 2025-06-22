package com.teatro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.IngressoNaoEncontradoException;
import com.teatro.exception.PoltronaOcupadaException;
import com.teatro.model.Area;
import com.teatro.model.Ingresso;
import com.teatro.model.Sessao;
import com.teatro.model.Usuario;
import com.teatro.repository.AreaRepository;
import com.teatro.repository.IngressoRepository;
import com.teatro.repository.SessaoRepository;
import com.teatro.repository.UsuarioRepository;

@Service
@Transactional
public class IngressoService {

  @Autowired
  private IngressoRepository ingressoRepository;

  @Autowired
  private AreaRepository areaRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private SessaoRepository sessaoRepository;

  /**
   * Compra ingresso com IDs e faz as associações necessárias
   */
  public Ingresso comprarIngresso(Long usuarioId, Long sessaoId, Long areaId,
      Integer numeroPoltrona, java.math.BigDecimal valor) {
    // Buscar entidades
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Sessao sessao = sessaoRepository.findById(sessaoId)
        .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

    Area area = areaRepository.findById(areaId)
        .orElseThrow(() -> new RuntimeException("Área não encontrada"));

    // Verificar se a sessão está ativa
    if (!sessao.isAtiva()) {
      throw new RuntimeException("Sessão não está ativa");
    }

    // Verificar se a sessão não passou
    if (sessao.isPassada()) {
      throw new RuntimeException("Sessão já aconteceu");
    }

    // Verificar se a poltrona está disponível
    boolean ocupado = ingressoRepository.isPoltronaOcupada(sessaoId, areaId, numeroPoltrona);
    if (ocupado) {
      throw new PoltronaOcupadaException("Poltrona " + numeroPoltrona + " já está ocupada!");
    }

    // Criar ingresso
    Ingresso ingresso = new Ingresso();
    ingresso.setUsuario(usuario);
    ingresso.setSessao(sessao);
    ingresso.setArea(area);
    ingresso.setNumeroPoltrona(numeroPoltrona);
    ingresso.setValor(valor);
    ingresso.setDataCompra(LocalDateTime.now());
    ingresso.setCodigo("ING-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    ingresso.setStatus(Ingresso.StatusIngresso.VALIDO);

    return ingressoRepository.save(ingresso);
  }

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

  /**
   * Lista ingressos por sessão
   */
  public List<Ingresso> listarPorSessao(Long sessaoId) {
    return ingressoRepository.findBySessaoId(sessaoId);
  }

  /**
   * Lista ingressos por área
   */
  public List<Ingresso> listarPorArea(Long areaId) {
    return ingressoRepository.findByAreaId(areaId);
  }

  /**
   * Cancela um ingresso (soft delete)
   */
  public void cancelarIngresso(Long id) {
    Ingresso ingresso = buscarPorId(id);
    // TODO: Implementar soft delete ou marcar como cancelado
    ingressoRepository.delete(ingresso);
  }

  /**
   * Valida ingresso por código
   */
  public Ingresso validarPorCodigo(String codigo) {
    return ingressoRepository.findByCodigo(codigo).orElseThrow(
        () -> new IngressoNaoEncontradoException("Ingresso não encontrado com código: " + codigo));
  }

  /**
   * Verifica poltronas disponíveis em uma sessão/área
   */
  public List<Integer> verificarPoltronasDisponiveis(Long sessaoId, Long areaId) {
    // Busca a área para obter a capacidade total
    var area = areaRepository.findById(areaId)
        .orElseThrow(() -> new RuntimeException("Área não encontrada"));

    // Busca poltronas ocupadas
    List<Integer> poltronasOcupadas = ingressoRepository.findPoltronasOcupadas(sessaoId, areaId);

    // Gera lista de poltronas disponíveis
    List<Integer> poltronasDisponiveis = new ArrayList<>();
    for (int i = 1; i <= area.getCapacidadeTotal(); i++) {
      if (!poltronasOcupadas.contains(i)) {
        poltronasDisponiveis.add(i);
      }
    }

    return poltronasDisponiveis;
  }
}
