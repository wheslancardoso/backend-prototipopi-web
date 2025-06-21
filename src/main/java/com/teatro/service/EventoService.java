package com.teatro.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.EventoJaExisteException;
import com.teatro.exception.EventoNaoEncontradoException;
import com.teatro.model.Evento;
import com.teatro.repository.EventoRepository;

/**
 * Service para operações de negócio relacionadas a eventos
 * 
 * Responsabilidades: - Gerenciamento de eventos teatrais - Validações de dados - Controle de
 * eventos ativos/inativos
 */
@Service
@Transactional
public class EventoService {

  @Autowired
  private EventoRepository eventoRepository;

  /**
   * Cadastra um novo evento
   * 
   * @param evento Dados do evento
   * @return Evento cadastrado
   * @throws EventoJaExisteException se evento já existe com o mesmo nome
   */
  public Evento cadastrarEvento(Evento evento) {
    // Validações de negócio
    validarDadosEvento(evento);

    // Verifica se já existe evento com o mesmo nome
    if (eventoRepository.existsByNome(evento.getNome())) {
      throw new EventoJaExisteException("Evento já cadastrado com o nome: " + evento.getNome());
    }

    // Define evento como ativo
    evento.setAtivo(true);

    return eventoRepository.save(evento);
  }

  /**
   * Busca evento por ID
   * 
   * @param id ID do evento
   * @return Evento encontrado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  @Transactional(readOnly = true)
  public Evento buscarPorId(Long id) {
    return eventoRepository.findById(id)
        .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado com ID: " + id));
  }

  /**
   * Lista todos os eventos ativos
   * 
   * @return Lista de eventos ativos
   */
  @Transactional(readOnly = true)
  public List<Evento> listarEventosAtivos() {
    return eventoRepository.findByAtivoTrue();
  }

  /**
   * Lista todos os eventos
   * 
   * @return Lista de todos os eventos
   */
  @Transactional(readOnly = true)
  public List<Evento> listarTodosEventos() {
    return eventoRepository.findAll();
  }

  /**
   * Busca eventos por nome (busca parcial)
   * 
   * @param nome Nome ou parte do nome do evento
   * @return Lista de eventos encontrados
   */
  @Transactional(readOnly = true)
  public List<Evento> buscarPorNome(String nome) {
    return eventoRepository.findByNomeContainingIgnoreCase(nome);
  }

  /**
   * Atualiza dados de um evento
   * 
   * @param id ID do evento
   * @param evento Dados atualizados
   * @return Evento atualizado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public Evento atualizarEvento(Long id, Evento evento) {
    Evento eventoExistente = buscarPorId(id);

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

    return eventoRepository.save(eventoExistente);
  }

  /**
   * Ativa/desativa um evento
   * 
   * @param id ID do evento
   * @param ativo Status desejado
   * @return Evento atualizado
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public Evento alterarStatusEvento(Long id, boolean ativo) {
    Evento evento = buscarPorId(id);
    evento.setAtivo(ativo);
    return eventoRepository.save(evento);
  }

  /**
   * Remove um evento (soft delete)
   * 
   * @param id ID do evento
   * @throws EventoNaoEncontradoException se evento não existe
   */
  public void removerEvento(Long id) {
    Evento evento = buscarPorId(id);

    // Verifica se o evento tem sessões cadastradas
    if (!evento.getSessoes().isEmpty()) {
      throw new IllegalStateException(
          "Não é possível remover um evento que possui sessões cadastradas");
    }

    evento.setAtivo(false);
    eventoRepository.save(evento);
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
   * Lista eventos com sessões futuras
   * 
   * @return Lista de eventos com sessões futuras
   */
  @Transactional(readOnly = true)
  public List<Evento> listarEventosComSessoesFuturas() {
    return eventoRepository.findEventosComSessoesFuturas();
  }

  /**
   * Lista eventos sem sessões
   * 
   * @return Lista de eventos sem sessões
   */
  @Transactional(readOnly = true)
  public List<Evento> listarEventosSemSessoes() {
    return eventoRepository.findEventosSemSessoes();
  }
}
