package com.teatro.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.dto.SessaoDTO;
import com.teatro.exception.SessaoJaExisteException;
import com.teatro.exception.SessaoNaoEncontradaException;
import com.teatro.model.Evento;
import com.teatro.model.Sessao;
import com.teatro.model.Sessao.TipoSessao;
import com.teatro.repository.SessaoRepository;

/**
 * Service para operações de negócio relacionadas a sessões
 * 
 * Responsabilidades: - Gerenciamento de sessões de eventos - Validações de horários e datas -
 * Controle de sessões ativas/inativas - Geração de horários dinâmicos
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
   * @param sessaoDTO Dados da sessão
   * @return SessaoDTO da sessão cadastrada
   * @throws SessaoJaExisteException se sessão já existe para o mesmo evento/data/horário
   */
  public SessaoDTO cadastrarSessao(SessaoDTO sessaoDTO) {
    Sessao sessao = sessaoDTO.toEntity();

    // Validações de negócio
    validarDadosSessao(sessao);

    // Verifica se já existe sessão para o mesmo evento na mesma data e horário
    if (sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(sessao.getEvento().getId(),
        sessao.getDataSessao(), sessao.getHorario())) {
      throw new SessaoJaExisteException(
          "Sessão já cadastrada para este evento na data e horário especificados");
    }

    // Define sessão como ativa
    sessao.setAtiva(true);

    Sessao sessaoSalva = sessaoRepository.save(sessao);
    return new SessaoDTO(sessaoSalva);
  }

  /**
   * Gera horários dinâmicos para um evento em uma data específica
   * 
   * @param eventoId ID do evento
   * @param data Data para gerar os horários
   * @return Lista de SessaoDTO com os horários gerados
   */
  public List<SessaoDTO> gerarHorariosDinamicos(Long eventoId, LocalDate data) {
    Evento evento = eventoService.buscarPorId(eventoId).toEntity();

    // Horários fixos por tipo de sessão
    LocalTime[] horariosManha = {LocalTime.of(8, 0), LocalTime.of(9, 30), LocalTime.of(11, 0)};
    LocalTime[] horariosTarde = {LocalTime.of(13, 0), LocalTime.of(14, 30), LocalTime.of(16, 0)};
    LocalTime[] horariosNoite = {LocalTime.of(18, 0), LocalTime.of(19, 30), LocalTime.of(21, 0)};

    List<SessaoDTO> sessoesGeradas = new java.util.ArrayList<>();

    // Gera sessões para cada tipo
    for (LocalTime horario : horariosManha) {
      if (!sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(eventoId, data, horario)) {
        Sessao sessao = new Sessao();
        sessao.setEvento(evento);
        sessao.setNome("Sessão " + TipoSessao.MANHA.getDescricao() + " - " + horario);
        sessao.setTipoSessao(TipoSessao.MANHA);
        sessao.setDataSessao(data);
        sessao.setHorario(horario);
        sessao.setAtiva(true);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        sessoesGeradas.add(new SessaoDTO(sessaoSalva));
      }
    }

    for (LocalTime horario : horariosTarde) {
      if (!sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(eventoId, data, horario)) {
        Sessao sessao = new Sessao();
        sessao.setEvento(evento);
        sessao.setNome("Sessão " + TipoSessao.TARDE.getDescricao() + " - " + horario);
        sessao.setTipoSessao(TipoSessao.TARDE);
        sessao.setDataSessao(data);
        sessao.setHorario(horario);
        sessao.setAtiva(true);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        sessoesGeradas.add(new SessaoDTO(sessaoSalva));
      }
    }

    for (LocalTime horario : horariosNoite) {
      if (!sessaoRepository.existsByEventoIdAndDataSessaoAndHorario(eventoId, data, horario)) {
        Sessao sessao = new Sessao();
        sessao.setEvento(evento);
        sessao.setNome("Sessão " + TipoSessao.NOITE.getDescricao() + " - " + horario);
        sessao.setTipoSessao(TipoSessao.NOITE);
        sessao.setDataSessao(data);
        sessao.setHorario(horario);
        sessao.setAtiva(true);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        sessoesGeradas.add(new SessaoDTO(sessaoSalva));
      }
    }

    return sessoesGeradas;
  }

  /**
   * Busca sessão por ID
   * 
   * @param id ID da sessão
   * @return SessaoDTO da sessão encontrada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  @Transactional(readOnly = true)
  public SessaoDTO buscarPorId(Long id) {
    Sessao sessao = sessaoRepository.findById(id)
        .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + id));
    return new SessaoDTO(sessao);
  }

  /**
   * Lista todas as sessões ativas
   * 
   * @return Lista de SessaoDTO das sessões ativas
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesAtivas() {
    return sessaoRepository.findByAtivaTrue().stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de SessaoDTO das sessões do evento
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorEvento(Long eventoId) {
    return sessaoRepository.findByEventoId(eventoId).stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões ativas de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de SessaoDTO das sessões ativas do evento
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesAtivasPorEvento(Long eventoId) {
    return sessaoRepository.findByAtivaTrueAndEventoId(eventoId).stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões por tipo (MANHA, TARDE, NOITE)
   * 
   * @param tipoSessao Tipo da sessão
   * @return Lista de SessaoDTO das sessões do tipo especificado
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorTipo(TipoSessao tipoSessao) {
    return sessaoRepository.findByTipoSessao(tipoSessao).stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões por data
   * 
   * @param data Data da sessão
   * @return Lista de SessaoDTO das sessões na data especificada
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorData(LocalDate data) {
    return sessaoRepository.findByAtivaTrueAndDataSessao(data).stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões futuras (após a data atual)
   * 
   * @return Lista de SessaoDTO das sessões futuras
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesFuturas() {
    return sessaoRepository.findSessoesFuturas().stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões disponíveis para compra
   * 
   * @return Lista de SessaoDTO das sessões disponíveis para compra
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesDisponiveisParaCompra() {
    return sessaoRepository.findSessoesDisponiveisParaCompra().stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões disponíveis para compra de um evento específico
   * 
   * @param eventoId ID do evento
   * @return Lista de SessaoDTO das sessões disponíveis para compra
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesDisponiveisParaCompraPorEvento(Long eventoId) {
    return sessaoRepository.findSessoesDisponiveisParaCompraPorEvento(eventoId).stream()
        .map(SessaoDTO::new).collect(Collectors.toList());
  }

  /**
   * Atualiza dados de uma sessão
   * 
   * @param id ID da sessão
   * @param sessaoDTO Dados atualizados
   * @return SessaoDTO da sessão atualizada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public SessaoDTO atualizarSessao(Long id, SessaoDTO sessaoDTO) {
    Sessao sessaoExistente = sessaoRepository.findById(id)
        .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + id));

    Sessao sessao = sessaoDTO.toEntity();

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

    Sessao sessaoAtualizada = sessaoRepository.save(sessaoExistente);
    return new SessaoDTO(sessaoAtualizada);
  }

  /**
   * Ativa/desativa uma sessão
   * 
   * @param id ID da sessão
   * @param ativa Status desejado
   * @return SessaoDTO da sessão atualizada
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public SessaoDTO alterarStatusSessao(Long id, boolean ativa) {
    Sessao sessao = sessaoRepository.findById(id)
        .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + id));
    sessao.setAtiva(ativa);
    Sessao sessaoAtualizada = sessaoRepository.save(sessao);
    return new SessaoDTO(sessaoAtualizada);
  }

  /**
   * Remove uma sessão (soft delete)
   * 
   * @param id ID da sessão
   * @throws SessaoNaoEncontradaException se sessão não existe
   */
  public void removerSessao(Long id) {
    Sessao sessao = sessaoRepository.findById(id)
        .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + id));

    // Verifica se a sessão tem ingressos vendidos
    if (sessao.getIngressos() != null && !sessao.getIngressos().isEmpty()) {
      throw new IllegalStateException(
          "Não é possível remover uma sessão que possui ingressos vendidos");
    }

    sessao.setAtiva(false);
    sessaoRepository.save(sessao);
  }

  /**
   * Lista sessões por período
   * 
   * @param dataInicio Data de início
   * @param dataFim Data de fim
   * @return Lista de SessaoDTO das sessões no período
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
    return sessaoRepository.findSessoesPorPeriodo(dataInicio, dataFim).stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista sessões por evento e período
   * 
   * @param eventoId ID do evento
   * @param dataInicio Data de início
   * @param dataFim Data de fim
   * @return Lista de SessaoDTO das sessões no período
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorEventoEPeriodo(Long eventoId, LocalDate dataInicio,
      LocalDate dataFim) {
    return sessaoRepository.findSessoesPorEventoEPeriodo(eventoId, dataInicio, dataFim).stream()
        .map(SessaoDTO::new).collect(Collectors.toList());
  }

  /**
   * Lista sessões passadas
   * 
   * @return Lista de SessaoDTO das sessões passadas
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPassadas() {
    return sessaoRepository.findSessoesPassadas().stream().map(SessaoDTO::new)
        .collect(Collectors.toList());
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
    Sessao sessao = sessaoRepository.findById(id).orElse(null);
    if (sessao == null || !sessao.isAtiva())
      return false;

    LocalDate hoje = LocalDate.now();
    LocalTime agora = LocalTime.now();

    // Sessão deve ser futura
    if (sessao.getDataSessao().isBefore(hoje))
      return false;
    if (sessao.getDataSessao().equals(hoje) && sessao.getHorario().isBefore(agora))
      return false;

    // Sessão deve ter pelo menos uma área disponível
    return sessao.getAreas() != null && !sessao.getAreas().isEmpty();
  }

  /**
   * Lista sessões por tipo (string)
   * 
   * @param tipoSessao Tipo da sessão como string
   * @return Lista de SessaoDTO das sessões do tipo
   */
  @Transactional(readOnly = true)
  public List<SessaoDTO> listarSessoesPorTipo(String tipoSessao) {
    try {
      TipoSessao tipo = TipoSessao.valueOf(tipoSessao.toUpperCase());
      return listarSessoesPorTipo(tipo);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Tipo de sessão inválido: " + tipoSessao);
    }
  }

  /**
   * Lista horários disponíveis para uma data específica
   * 
   * @param data Data para buscar horários disponíveis
   * @return Lista de horários disponíveis como strings
   */
  @Transactional(readOnly = true)
  public List<String> listarHorariosDisponiveisPorData(LocalDate data) {
    // Retorna os 9 horários fixos disponíveis para qualquer data
    return List.of("08:00", "09:30", "11:00", // Manhã
        "13:00", "14:30", "16:00", // Tarde
        "18:00", "19:30", "21:00" // Noite
    );
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

    if (sessao.getDataSessao() == null) {
      throw new IllegalArgumentException("Data da sessão é obrigatória");
    }

    if (sessao.getHorario() == null) {
      throw new IllegalArgumentException("Horário da sessão é obrigatório");
    }

    if (sessao.getTipoSessao() == null) {
      throw new IllegalArgumentException("Tipo da sessão é obrigatório");
    }

    // Verifica se a data não é passada
    if (sessao.getDataSessao().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Data da sessão não pode ser no passado");
    }

    // Verifica se a data é hoje, o horário deve ser futuro
    if (sessao.getDataSessao().equals(LocalDate.now())
        && sessao.getHorario().isBefore(LocalTime.now())) {
      throw new IllegalArgumentException("Horário da sessão não pode ser no passado");
    }
  }
}
