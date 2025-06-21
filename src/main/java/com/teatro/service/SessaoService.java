package com.teatro.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.SessaoJaExisteException;
import com.teatro.exception.SessaoNaoEncontradaException;
import com.teatro.model.Evento;
import com.teatro.model.Sessao;
import com.teatro.repository.SessaoRepository;

/**
 * Service para operações de negócio relacionadas a sessões
 * 
 * Responsabilidades: - Gerenciamento de sessões de eventos - Validações de horários e datas -
 * Controle de sessões ativas/inativas
 */
@Service
@Transactional
public class SessaoService {

  @Autowired
  private SessaoRepository sessaoRepository;

  @Autowired
  private EventoService eventoService;

  /**
   * Cadastra uma nova sessão
   * 
   * @param sessao Dados da sessão
   * @return Sessão cadastrada
   * @throws SessaoJaExisteException se sessão já existe para o mesmo evento/data/horário
   */
  public Sessao cadastrarSessao(Sessao sessao) {
    // Validações de negócio
    validarDadosSessao(sessao);

    // Verifica se já existe sessão para o mesmo evento na mesma data e horário
    if (sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(sessao.getEvento().getId(),
        sessao.getDataSessao(), sessao.getHorario())) {
      throw new SessaoJaExisteException(
          "Sessão já cadastrada para este evento na data e horário especificados");
    }

    // Define sessão como ativa
    sessao.setAtivo(true);

    return sessaoRepository.save(sessao);
  }

  /**
   * Busca sessão por ID
   * 
   * @param id ID da sessão
   * @return Sessão encontrada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  @Transactional(readOnly = true)
  public Sessao buscarPorId(Long id) {
    return sessaoRepository.findById(id)
        .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + id));
  }

  /**
   * Lista todas as sessões ativas
   * 
   * @return Lista de sessões ativas
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesAtivas() {
    return sessaoRepository.findByAtivoTrue();
  }

  /**
   * Lista sessões de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de sessões do evento
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPorEvento(Long eventoId) {
    return sessaoRepository.findByEventoId(eventoId);
  }

  /**
   * Lista sessões ativas de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de sessões ativas do evento
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesAtivasPorEvento(Long eventoId) {
    return sessaoRepository.findByAtivoTrueAndEventoId(eventoId);
  }

  /**
   * Lista sessões por tipo (MANHA, TARDE, NOITE)
   * 
   * @param tipoSessao Tipo da sessão
   * @return Lista de sessões do tipo especificado
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPorTipo(Sessao.TipoSessao tipoSessao) {
    return sessaoRepository.findByTipoSessao(tipoSessao);
  }

  /**
   * Lista sessões por data
   * 
   * @param data Data da sessão
   * @return Lista de sessões na data especificada
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPorData(LocalDate data) {
    return sessaoRepository.findByAtivoTrueAndDataSessao(data);
  }

  /**
   * Lista sessões futuras (após a data atual)
   * 
   * @return Lista de sessões futuras
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesFuturas() {
    return sessaoRepository.findSessoesFuturas();
  }

  /**
   * Lista sessões disponíveis para compra
   * 
   * @return Lista de sessões disponíveis para compra
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesDisponiveisParaCompra() {
    return sessaoRepository.findSessoesDisponiveisParaCompra();
  }

  /**
   * Lista sessões disponíveis para compra de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de sessões disponíveis para compra
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesDisponiveisParaCompraPorEvento(Long eventoId) {
    return sessaoRepository.findSessoesDisponiveisParaCompraPorEvento(eventoId);
  }

  /**
   * Atualiza dados de uma sessão
   * 
   * @param id ID da sessão
   * @param sessao Dados atualizados
   * @return Sessão atualizada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public Sessao atualizarSessao(Long id, Sessao sessao) {
    Sessao sessaoExistente = buscarPorId(id);

    // Validações de negócio
    validarDadosSessao(sessao);

    // Verifica se data/horário foi alterado e se já existe conflito
    if (!sessaoExistente.getDataSessao().equals(sessao.getDataSessao())
        || !sessaoExistente.getHorario().equals(sessao.getHorario())) {

      if (sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(sessao.getEvento().getId(),
          sessao.getDataSessao(), sessao.getHorario())) {
        throw new SessaoJaExisteException(
            "Sessão já cadastrada para este evento na data e horário especificados");
      }
    }

    // Atualiza os dados
    sessaoExistente.setNome(sessao.getNome());
    sessaoExistente.setTipoSessao(sessao.getTipoSessao());
    sessaoExistente.setDataSessao(sessao.getDataSessao());
    sessaoExistente.setHorario(sessao.getHorario());

    return sessaoRepository.save(sessaoExistente);
  }

  /**
   * Ativa/desativa uma sessão
   * 
   * @param id ID da sessão
   * @param ativo Status desejado
   * @return Sessão atualizada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public Sessao alterarStatusSessao(Long id, boolean ativo) {
    Sessao sessao = buscarPorId(id);
    sessao.setAtivo(ativo);
    return sessaoRepository.save(sessao);
  }

  /**
   * Remove uma sessão (soft delete)
   * 
   * @param id ID da sessão
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public void removerSessao(Long id) {
    Sessao sessao = buscarPorId(id);

    // Verifica se a sessão tem ingressos vendidos
    if (!sessao.getIngressos().isEmpty()) {
      throw new IllegalStateException(
          "Não é possível remover uma sessão que possui ingressos vendidos");
    }

    sessao.setAtivo(false);
    sessaoRepository.save(sessao);
  }

  /**
   * Valida dados de uma sessão
   * 
   * @param sessao Sessão a ser validada
   * @throws IllegalArgumentException se dados são inválidos
   */
  private void validarDadosSessao(Sessao sessao) {
    if (sessao.getEvento() == null || sessao.getEvento().getId() == null) {
      throw new IllegalArgumentException("Evento é obrigatório");
    }

    // Verifica se o evento existe e está ativo
    Evento evento = eventoService.buscarPorId(sessao.getEvento().getId());
    if (!evento.isAtivo()) {
      throw new IllegalArgumentException("Evento deve estar ativo");
    }

    if (sessao.getDataSessao() == null) {
      throw new IllegalArgumentException("Data da sessão é obrigatória");
    }

    if (sessao.getHorario() == null) {
      throw new IllegalArgumentException("Horário da sessão é obrigatório");
    }

    // Verifica se a data não é no passado
    if (sessao.getDataSessao().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Data da sessão não pode ser no passado");
    }

    // Verifica se o horário é válido (entre 8h e 23h)
    int hora = sessao.getHorario().getHour();
    if (hora < 8 || hora > 23) {
      throw new IllegalArgumentException("Horário deve estar entre 8h e 23h");
    }
  }

  /**
   * Lista sessões por período
   * 
   * @param dataInicio Data de início
   * @param dataFim Data de fim
   * @return Lista de sessões no período
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
    return sessaoRepository.findSessoesPorPeriodo(dataInicio, dataFim);
  }

  /**
   * Lista sessões por evento e período
   * 
   * @param eventoId ID do evento
   * @param dataInicio Data de início
   * @param dataFim Data de fim
   * @return Lista de sessões no período
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPorEventoEPeriodo(Long eventoId, LocalDate dataInicio,
      LocalDate dataFim) {
    return sessaoRepository.findSessoesPorEventoEPeriodo(eventoId, dataInicio, dataFim);
  }

  /**
   * Lista sessões passadas
   * 
   * @return Lista de sessões passadas
   */
  @Transactional(readOnly = true)
  public List<Sessao> listarSessoesPassadas() {
    return sessaoRepository.findSessoesPassadas();
  }

  /**
   * Verifica se uma sessão está ativa
   * 
   * @param id ID da sessão
   * @return true se a sessão está ativa
   */
  @Transactional(readOnly = true)
  public boolean isSessaoAtiva(Long id) {
    return sessaoRepository.findById(id).map(Sessao::isAtiva).orElse(false);
  }

  /**
   * Verifica se uma sessão está disponível para compra
   * 
   * @param id ID da sessão
   * @return true se a sessão está disponível para compra
   */
  @Transactional(readOnly = true)
  public boolean isSessaoDisponivelParaCompra(Long id) {
    return sessaoRepository.findById(id).map(Sessao::isDisponivelParaCompra).orElse(false);
  }
}
