package com.teatro.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.SessaoDTO;
import com.teatro.exception.SessaoJaExisteException;
import com.teatro.exception.SessaoNaoEncontradaException;
import com.teatro.model.Sessao;
import com.teatro.service.SessaoService;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a sessões
 * 
 * Endpoints:
 * - POST /api/sessoes - Cadastrar sessão
 * - GET /api/sessoes - Listar sessões
 * - GET /api/sessoes/{id} - Buscar sessão por ID
 * - PUT /api/sessoes/{id} - Atualizar sessão
 * - DELETE /api/sessoes/{id} - Remover sessão
 * - GET /api/sessoes/ativas - Listar sessões ativas
 * - GET /api/sessoes/evento/{eventoId} - Listar sessões por evento
 * - GET /api/sessoes/futuras - Listar sessões futuras
 * - GET /api/sessoes/disponiveis - Listar sessões disponíveis para compra
 * - PUT /api/sessoes/{id}/status - Alterar status da sessão
 */
@RestController
@RequestMapping("/api/sessoes")
@CrossOrigin(origins = "*")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    /**
     * Cadastra uma nova sessão
     * 
     * @param sessaoDTO Dados da sessão
     * @return Sessão cadastrada
     */
    @PostMapping
    public ResponseEntity<SessaoDTO> cadastrar(@Valid @RequestBody SessaoDTO sessaoDTO) {
        try {
            Sessao sessao = converterParaModel(sessaoDTO);
            Sessao sessaoSalva = sessaoService.cadastrarSessao(sessao);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(sessaoSalva));
        } catch (SessaoJaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Lista todas as sessões
     * 
     * @return Lista de sessões
     */
    @GetMapping
    public ResponseEntity<List<SessaoDTO>> listarSessoes() {
        List<Sessao> sessoes = sessaoService.listarSessoesAtivas();
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista apenas sessões ativas
     * 
     * @return Lista de sessões ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<SessaoDTO>> listarSessoesAtivas() {
        List<Sessao> sessoes = sessaoService.listarSessoesAtivas();
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões de um evento específico
     * 
     * @param eventoId ID do evento
     * @return Lista de sessões do evento
     */
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorEvento(@PathVariable Long eventoId) {
        List<Sessao> sessoes = sessaoService.listarSessoesPorEvento(eventoId);
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões ativas de um evento específico
     * 
     * @param eventoId ID do evento
     * @return Lista de sessões ativas do evento
     */
    @GetMapping("/evento/{eventoId}/ativas")
    public ResponseEntity<List<SessaoDTO>> listarSessoesAtivasPorEvento(@PathVariable Long eventoId) {
        List<Sessao> sessoes = sessaoService.listarSessoesAtivasPorEvento(eventoId);
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões por tipo
     * 
     * @param tipoSessao Tipo da sessão (MANHA, TARDE, NOITE)
     * @return Lista de sessões do tipo
     */
    @GetMapping("/tipo/{tipoSessao}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorTipo(@PathVariable String tipoSessao) {
        try {
            Sessao.TipoSessao tipo = Sessao.TipoSessao.valueOf(tipoSessao.toUpperCase());
            List<Sessao> sessoes = sessaoService.listarSessoesPorTipo(tipo);
            List<SessaoDTO> sessoesDTO = sessoes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(sessoesDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lista sessões por data
     * 
     * @param data Data da sessão
     * @return Lista de sessões na data
     */
    @GetMapping("/data")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<Sessao> sessoes = sessaoService.listarSessoesPorData(data);
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões futuras
     * 
     * @return Lista de sessões futuras
     */
    @GetMapping("/futuras")
    public ResponseEntity<List<SessaoDTO>> listarSessoesFuturas() {
        List<Sessao> sessoes = sessaoService.listarSessoesFuturas();
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões disponíveis para compra
     * 
     * @return Lista de sessões disponíveis para compra
     */
    @GetMapping("/disponiveis")
    public ResponseEntity<List<SessaoDTO>> listarSessoesDisponiveisParaCompra() {
        List<Sessao> sessoes = sessaoService.listarSessoesDisponiveisParaCompra();
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Lista sessões disponíveis para compra de um evento específico
     * 
     * @param eventoId ID do evento
     * @return Lista de sessões disponíveis para compra
     */
    @GetMapping("/disponiveis/evento/{eventoId}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesDisponiveisParaCompraPorEvento(@PathVariable Long eventoId) {
        List<Sessao> sessoes = sessaoService.listarSessoesDisponiveisParaCompraPorEvento(eventoId);
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Busca sessão por ID
     * 
     * @param id ID da sessão
     * @return Sessão encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessaoDTO> buscarPorId(@PathVariable Long id) {
        try {
            Sessao sessao = sessaoService.buscarPorId(id);
            return ResponseEntity.ok(converterParaDTO(sessao));
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Atualiza dados de uma sessão
     * 
     * @param id ID da sessão
     * @param sessaoDTO Dados atualizados
     * @return Sessão atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessaoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SessaoDTO sessaoDTO) {
        try {
            Sessao sessao = converterParaModel(sessaoDTO);
            Sessao sessaoAtualizada = sessaoService.atualizarSessao(id, sessao);
            return ResponseEntity.ok(converterParaDTO(sessaoAtualizada));
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        } catch (SessaoJaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Remove uma sessão
     * 
     * @param id ID da sessão
     * @return Status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            sessaoService.removerSessao(id);
            return ResponseEntity.noContent().build();
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Altera status de uma sessão (ativo/inativo)
     * 
     * @param id ID da sessão
     * @param ativo Status desejado
     * @return Sessão atualizada
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<SessaoDTO> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        try {
            Sessao sessao = sessaoService.alterarStatusSessao(id, ativo);
            return ResponseEntity.ok(converterParaDTO(sessao));
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista sessões por período
     * 
     * @param dataInicio Data de início
     * @param dataFim Data de fim
     * @return Lista de sessões no período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<Sessao> sessoes = sessaoService.listarSessoesPorPeriodo(dataInicio, dataFim);
        List<SessaoDTO> sessoesDTO = sessoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessoesDTO);
    }

    /**
     * Verifica se uma sessão está ativa
     * 
     * @param id ID da sessão
     * @return true se a sessão está ativa
     */
    @GetMapping("/{id}/ativa")
    public ResponseEntity<Boolean> isSessaoAtiva(@PathVariable Long id) {
        boolean ativa = sessaoService.isSessaoAtiva(id);
        return ResponseEntity.ok(ativa);
    }

    /**
     * Verifica se uma sessão está disponível para compra
     * 
     * @param id ID da sessão
     * @return true se a sessão está disponível para compra
     */
    @GetMapping("/{id}/disponivel")
    public ResponseEntity<Boolean> isSessaoDisponivelParaCompra(@PathVariable Long id) {
        boolean disponivel = sessaoService.isSessaoDisponivelParaCompra(id);
        return ResponseEntity.ok(disponivel);
    }

    /**
     * Converte Sessao para SessaoDTO
     */
    private SessaoDTO converterParaDTO(Sessao sessao) {
        SessaoDTO dto = new SessaoDTO();
        dto.setId(sessao.getId());
        dto.setNome(sessao.getNome());
        dto.setTipoSessao(sessao.getTipoSessao().name());
        dto.setDataSessao(sessao.getDataSessao());
        dto.setHorario(sessao.getHorario());
        dto.setAtivo(sessao.isAtiva());
        dto.setEventoId(sessao.getEvento() != null ? sessao.getEvento().getId() : null);
        dto.setEventoNome(sessao.getEvento() != null ? sessao.getEvento().getNome() : null);
        return dto;
    }

    /**
     * Converte SessaoDTO para Sessao
     */
    private Sessao converterParaModel(SessaoDTO sessaoDTO) {
        Sessao sessao = new Sessao();
        sessao.setId(sessaoDTO.getId());
        sessao.setNome(sessaoDTO.getNome());
        sessao.setTipoSessao(Sessao.TipoSessao.valueOf(sessaoDTO.getTipoSessao()));
        sessao.setDataSessao(sessaoDTO.getDataSessao());
        sessao.setHorario(sessaoDTO.getHorario());
        sessao.setAtivo(sessaoDTO.getAtivo());
        
        // TODO: Carregar evento se eventoId for fornecido
        // sessao.setEvento(eventoService.buscarPorId(sessaoDTO.getEventoId()));
        
        return sessao;
    }
} 