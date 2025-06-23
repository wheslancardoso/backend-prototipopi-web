package com.teatro.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.dto.EventoDTO;
import com.teatro.exception.EventoJaExisteException;
import com.teatro.exception.EventoNaoEncontradoException;
import com.teatro.model.Evento;
import com.teatro.repository.EventoRepository;

/**
 * Service para operações de negócio relacionadas a eventos
 * 
 * Responsabilidades: - Gerenciamento de eventos teatrais - Validações de dados - Controle de
 * eventos ativos/inativos - Estatísticas de eventos
 */
@Service
@Transactional
public class EventoService {

  @Autowired
  private EventoRepository eventoRepository;

  /**
   * Cadastra um novo evento
   * 
   * @param eventoDTO Dados do evento
   * @return EventoDTO do evento cadastrado
   * @throws EventoJaExisteException se evento já existe com o mesmo nome
   */
  public EventoDTO cadastrarEvento(EventoDTO eventoDTO) {
    Evento evento = eventoDTO.toEntity();

    // Validações de negócio
    validarDadosEvento(evento);

    // Verifica se já existe evento com o mesmo nome
    if (eventoRepository.existsByNome(evento.getNome())) {
      throw new EventoJaExisteException("Evento já cadastrado com o nome: " + evento.getNome());
    }

    // Define evento como ativo
    evento.setAtivo(true);

    Evento eventoSalvo = eventoRepository.save(evento);
    return new EventoDTO(eventoSalvo);
  }

  /**
   * Busca evento por ID
   * 
   * @param id ID do evento
   * @return EventoDTO do evento encontrado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  @Transactional(readOnly = true)
  public EventoDTO buscarPorId(Long id) {
    Evento evento = eventoRepository.findById(id)
        .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado com ID: " + id));
    return new EventoDTO(evento);
  }

  /**
   * Lista todos os eventos ativos
   * 
   * @return Lista de EventoDTO dos eventos ativos
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> listarEventosAtivos() {
    return eventoRepository.findByAtivoTrue().stream().map(EventoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Lista todos os eventos
   * 
   * @return Lista de EventoDTO de todos os eventos
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> listarTodosEventos() {
    return eventoRepository.findAll().stream().map(EventoDTO::new).collect(Collectors.toList());
  }

  /**
   * Busca eventos por nome (busca parcial)
   * 
   * @param nome Nome ou parte do nome do evento
   * @return Lista de EventoDTO dos eventos encontrados
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> buscarPorNome(String nome) {
    return eventoRepository.findByNomeContainingIgnoreCase(nome).stream().map(EventoDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Atualiza dados de um evento
   * 
   * @param id ID do evento
   * @param eventoDTO Dados atualizados
   * @return EventoDTO do evento atualizado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public EventoDTO atualizarEvento(Long id, EventoDTO eventoDTO) {
    Evento eventoExistente = eventoRepository.findById(id)
        .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado com ID: " + id));

    Evento evento = eventoDTO.toEntity();

    // Validações de negócio
    validarDadosEvento(evento);

    // Verifica se nome foi alterado e se já existe conflito
    if (!eventoExistente.getNome().equals(evento.getNome())
        && eventoRepository.existsByNome(evento.getNome())) {
      throw new EventoJaExisteException("Evento já cadastrado com o nome: " + evento.getNome());
    }

    // Atualiza os dados
    eventoExistente.setNome(evento.getNome());
    eventoExistente.setDescricao(evento.getDescricao());
    eventoExistente.setDuracaoMinutos(evento.getDuracaoMinutos());
    eventoExistente.setClassificacaoIndicativa(evento.getClassificacaoIndicativa());
    eventoExistente.setUrlPoster(evento.getUrlPoster());

    Evento eventoAtualizado = eventoRepository.save(eventoExistente);
    return new EventoDTO(eventoAtualizado);
  }

  /**
   * Ativa/desativa um evento
   * 
   * @param id ID do evento
   * @param ativo Status desejado
   * @return EventoDTO do evento atualizado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public EventoDTO alterarStatusEvento(Long id, boolean ativo) {
    Evento evento = eventoRepository.findById(id)
        .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado com ID: " + id));
    evento.setAtivo(ativo);
    Evento eventoAtualizado = eventoRepository.save(evento);
    return new EventoDTO(eventoAtualizado);
  }

  /**
   * Remove um evento (soft delete)
   * 
   * @param id ID do evento
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public void removerEvento(Long id) {
    Evento evento = eventoRepository.findById(id)
        .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado com ID: " + id));

    // Verifica se o evento tem sessões cadastradas
    if (!evento.getSessoes().isEmpty()) {
      throw new IllegalStateException(
          "Não é possível remover um evento que possui sessões cadastradas");
    }

    evento.setAtivo(false);
    eventoRepository.save(evento);
  }

  /**
   * Busca eventos com sessões futuras
   * 
   * @return Lista de EventoDTO dos eventos com sessões futuras
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> listarEventosComSessoesFuturas() {
    return eventoRepository.findByAtivoTrue()
            .stream()
            .filter(evento -> !evento.getSessoes().isEmpty())
            .map(EventoDTO::new)
            .collect(Collectors.toList());
  }

  /**
   * Busca eventos sem sessões cadastradas
   * 
   * @return Lista de EventoDTO dos eventos sem sessões
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> listarEventosSemSessoes() {
    return eventoRepository.findByAtivoTrue()
            .stream()
            .filter(evento -> evento.getSessoes().isEmpty())
            .map(EventoDTO::new)
            .collect(Collectors.toList());
  }

  /**
   * Busca eventos por classificação indicativa
   * 
   * @param classificacao Classificação indicativa
   * @return Lista de EventoDTO dos eventos encontrados
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> buscarPorClassificacao(String classificacao) {
    return eventoRepository.findByAtivoTrue()
            .stream()
            .filter(evento -> classificacao.equals(evento.getClassificacaoIndicativa()))
            .map(EventoDTO::new)
            .collect(Collectors.toList());
  }

  /**
   * Busca eventos por duração (em minutos)
   * 
   * @param duracaoMinima Duração mínima em minutos
   * @param duracaoMaxima Duração máxima em minutos
   * @return Lista de EventoDTO dos eventos encontrados
   */
  @Transactional(readOnly = true)
  public List<EventoDTO> buscarPorDuracao(Integer duracaoMinima, Integer duracaoMaxima) {
    return eventoRepository.findByAtivoTrue()
            .stream()
            .filter(evento -> evento.getDuracaoMinutos() != null && 
                    evento.getDuracaoMinutos() >= duracaoMinima && 
                    evento.getDuracaoMinutos() <= duracaoMaxima)
            .map(EventoDTO::new)
            .collect(Collectors.toList());
  }

  /**
   * Valida dados de um evento
   * 
   * @param evento Evento a ser validado
   * @throws IllegalArgumentException se dados são inválidos
   */
  private void validarDadosEvento(Evento evento) {
    if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome do evento é obrigatório");
    }

    if (evento.getDescricao() == null || evento.getDescricao().trim().isEmpty()) {
      throw new IllegalArgumentException("Descrição do evento é obrigatória");
    }

    if (evento.getDuracaoMinutos() != null && evento.getDuracaoMinutos() <= 0) {
      throw new IllegalArgumentException("Duração do evento deve ser maior que zero");
    }

    if (evento.getDuracaoMinutos() != null && evento.getDuracaoMinutos() > 300) {
      throw new IllegalArgumentException("Duração do evento não pode ser maior que 300 minutos");
    }
  }

  /**
   * Conta eventos ativos
   * 
   * @return Número de eventos ativos
   */
  @Transactional(readOnly = true)
  public long contarEventosAtivos() {
    return eventoRepository.countByAtivoTrue();
  }

  /**
   * Verifica se um evento está ativo
   * 
   * @param id ID do evento
   * @return true se o evento está ativo
   */
  @Transactional(readOnly = true)
  public boolean isEventoAtivo(Long id) {
    return eventoRepository.existsByIdAndAtivoTrue(id);
  }

  /**
   * Busca evento por nome exato
   * 
   * @param nome Nome exato do evento
   * @return EventoDTO do evento encontrado ou null
   */
  @Transactional(readOnly = true)
  public EventoDTO buscarPorNomeExato(String nome) {
    return eventoRepository.findByAtivoTrue()
            .stream()
            .filter(evento -> nome.equals(evento.getNome()))
            .findFirst()
            .map(EventoDTO::new)
            .orElse(null);
  }
}
